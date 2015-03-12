package mx.edu.cicese.alejandro.voicehelper;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import mx.edu.cicese.alejandro.audio.Detector.ConsistentLoudNoiseDetector;
import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.audio.record.OneDetectorManyObservers;
import mx.edu.cicese.alejandro.rules.Mistep;
import mx.edu.cicese.alejandro.rules.RulesEngine;
import mx.edu.cicese.alejandro.voicehelper.views.AmplitudeCardCardView;

public class MainActivity extends Activity {

    private static final String TAG = "VoiceTracker";
    private final int delayTime = 5000;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    private CardScrollView mCardScroller;
    private RecordAudioTask recordAudioTask;
    private Context context;
    private RulesEngine rulesEngine;
    private Handler myHandler = new Handler();
    private boolean mistepDetected = false;
    private Runnable timeout = new Runnable() {
        public void run() {
            Log.d("VoiceHelper", "Timeout");
            releaseScreen();
            amplitudeCardView.clearFooterMessages();
            mistepDetected = false;
        }
    };
    private AmplitudeCardCardView amplitudeCardView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        init();

        EventBus.getDefault().register(this);
        startTask(createAudioLogger(), "Voice Tracker");
        setContentView(mCardScroller);
    }

    protected void init() {
        context = this.getApplicationContext();
        rulesEngine = new RulesEngine(this);

        powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "Wake Lock");

        amplitudeCardView = new AmplitudeCardCardView(this.context);

        mCardScroller = new CardScrollView(this);
        CustomCardScrollAdapter mAdapter = new CustomCardScrollAdapter();
        mAdapter.addView(amplitudeCardView);
        mCardScroller.setAdapter(mAdapter);
        // Handle the TAP event.
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Plays disallowed sound to indicate that TAP actions are not supported.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(Sounds.DISALLOWED);
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

    /**
     * Builds a Glass styled "Hello World!" view using the {@link CardBuilder} class.
     */
    private View buildView(int value) {
        View view;
        switch (value) {
            case 1:
                view = new AmplitudeCardCardView(this.context);

                break;
            case 2:
                view = new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE)
                        .setEmbeddedLayout(R.layout.activity_fluency)
                        .setFootnote("Check your fluency")
                        .setTimestamp("CICESE")
                        .getView();
                break;
            default:
                view = new CardBuilder(this, CardBuilder.Layout.EMBED_INSIDE)
                        .setEmbeddedLayout(R.layout.activity_main)
                        .setFootnote("Foods you tracked")
                        .setTimestamp("CICESE")
                        .getView();
                break;

        }
        return view;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        stopAll();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void startTask(AudioClipListener detector, String name) {
        stopAll();

        recordAudioTask = new RecordAudioTask(MainActivity.this, name);
        List<AudioClipListener> observers = new ArrayList<>();
        observers.add(new ConsistentLoudNoiseDetector(2, 200, 200));
        //observers.add(new AudioClipLogWrapper(this));
        //observers.add(new AudioClipLogFixedWrapper(this));
        OneDetectorManyObservers wrapped =
                new OneDetectorManyObservers(detector, observers);
        recordAudioTask.execute(wrapped);
    }

    private void stopAll() {
        Log.d(TAG, "stop record audio");
        shutDownTaskIfNecessary(recordAudioTask);
    }

    private void shutDownTaskIfNecessary(final AsyncTask task) {
        if ((task != null) && (!task.isCancelled())) {
            if ((task.getStatus().equals(AsyncTask.Status.RUNNING))
                    || (task.getStatus()
                    .equals(AsyncTask.Status.PENDING))) {
                Log.d(TAG, "CANCEL " + task.getClass().getSimpleName());
                task.cancel(true);
            } else {
                Log.d(TAG, "task not running");
            }
        }
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
            wakeLock.acquire();
        }

    }

    public void releaseScreen() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    public void onEventMainThread(ConsistentLoudNoiseDetector event) {
        if (mCardScroller.getSelectedItemPosition() == 0) {
            amplitudeCardView.updateScale((int) event.getCurrentVolume());
            if (event.isTooLoud() && !mistepDetected) {
                mistepDetected = true;
                amplitudeCardView.incidentDetect(rulesEngine.addMistep(Mistep.Kind.VOICE));
                turnOnScreen();
                myHandler.postDelayed(timeout, delayTime);
            }
        }
    }

}
