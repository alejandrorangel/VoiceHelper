package mx.edu.cicese.alejandro.voicehelper;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import mx.edu.cicese.alejandro.audio.Detector.ConsistentLoudNoiseDetector;
import mx.edu.cicese.alejandro.audio.Detector.PitchDetector;
import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.audio.record.OneDetectorManyObservers;
import mx.edu.cicese.alejandro.rules.Mistep;
import mx.edu.cicese.alejandro.rules.RulesEngine;
import mx.edu.cicese.alejandro.voicehelper.views.AmplitudeCardView;
import mx.edu.cicese.alejandro.voicehelper.views.CustomCardScrollAdapter;
import mx.edu.cicese.alejandro.voicehelper.views.FluencyCardView;
import mx.edu.cicese.alejandro.voicehelper.views.PitchCardView;

public class MainActivity extends Activity {

    private static final String TAG = "VoiceTracker";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        init();
        startTask(createAudioLogger(), "Voice Tracker");
        setContentView(mCardScroller);
        EventBus.getDefault().register(this);
    }

    protected void init() {
        context = this.getApplicationContext();
        rulesEngine = new RulesEngine(this);

        powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "Wake Lock");

        amplitudeCardView = new AmplitudeCardView(this.context);
        fluencyCardView = new FluencyCardView(this.context);
        pitchCardView = new PitchCardView(this.context);
        mCardScroller = new CardScrollView(this);
        CustomCardScrollAdapter mAdapter = new CustomCardScrollAdapter();
        mAdapter.addView(amplitudeCardView);
        mAdapter.addView(fluencyCardView);
        mAdapter.addView(pitchCardView);
        mCardScroller.setAdapter(mAdapter);
        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays disallowed sound to indicate that TAP actions are not supported.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
                getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        _audioDetectorRunnable.interrupt();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void startTask(AudioClipListener detector, String name) {
        List<AudioClipListener> observers = new ArrayList<>();
        observers.add(new ConsistentLoudNoiseDetector(2, 200, 200));
        observers.add(new PitchDetector());
        OneDetectorManyObservers wrapped =
                new OneDetectorManyObservers(detector, observers);
        _audioDetectorRunnable = new Thread(new RecordAudioRunnable(this, wrapped));
        _audioDetectorRunnable.start();
    }


    /*
    TODO Esto se puede cambiar
     */
    private AudioClipListener createAudioLogger() {
        AudioClipListener audioLogger = new AudioClipListener() {
            @Override
            public boolean heard(short[] audioData, int sampleRate) {
                if (audioData == null || audioData.length == 0) {
                    return true;
                }

                // returning false means the recording won't be stopped
                // users have to manually stop it via the stop button
                return false;
            }
        };

        return audioLogger;
    }

    public void turnOnScreen() {
        if (powerManager.isScreenOn() == false) {
            wakeLock.acquire(3000);
        }

    }

    public void onEventMainThread(ConsistentLoudNoiseDetector event) {
        if (mCardScroller.getSelectedItemPosition() == 0) {
            amplitudeCardView.updateScale((int) event.getCurrentVolume());
            if (event.isTooLoud() && !mistepDetected) {
                mistepDetected = true;
                amplitudeCardView.mistepDetect(rulesEngine.addMistep(Mistep.Kind.VOICE));
                turnOnScreen();
            }
        }
    }

    public void onEventMainThread(PitchDetector event) {
        Log.d(TAG, "PitchDetector " + event.getPitchValue());
        if (mCardScroller.getSelectedItemPosition() == 2) {
            pitchCardView.updateScale(event.getPitchValue());
        }
    }


    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    private CardScrollView mCardScroller;
    private Context context;
    private RulesEngine rulesEngine;
    private boolean mistepDetected = false;

    private AmplitudeCardView amplitudeCardView;
    private PitchCardView pitchCardView;
    private FluencyCardView fluencyCardView;

    Thread _audioDetectorRunnable;
}
