package mx.edu.cicese.alejandro.voicehelper;

import android.app.Activity;
import android.util.Log;

import mx.edu.cicese.alejandro.audio.interp.LoudNoiseDetector;
import mx.edu.cicese.alejandro.audio.processing.ZeroCrossing;
import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.audio.util.AudioUtil;


/**
 * Created by Alejandro on 1/28/15.
 */
public class AudioClipLogFixedWrapper implements AudioClipListener {
    private static final String TAG = "Voice Tracker";

    private Activity context;

    private double previousFrequency = -1;

    public AudioClipLogFixedWrapper(Activity context) {
        this.context = context;
    }

    @Override
    public boolean heard(short[] audioData, int sampleRate) {
        final double zero = ZeroCrossing.calculate(sampleRate, audioData);
        final double volume = AudioUtil.rootMeanSquared(audioData);

        final boolean isLoudEnough = volume > LoudNoiseDetector.DEFAULT_LOUDNESS_THRESHOLD;
        //range threshold of 100
        final boolean isDifferentFromLast = Math.abs(zero - previousFrequency) > 100;

        final StringBuilder message = new StringBuilder();
        message.append("volume: ").append((int) volume);
        if (!isLoudEnough) {
            message.append(" (silence) ");
        }
        message.append(" freqency: ").append((int) zero);
        if (isDifferentFromLast) {
            message.append(" (diff)");
        }

        previousFrequency = zero;
        Log.d(TAG, String.valueOf(message));

        return false;
    }
}
