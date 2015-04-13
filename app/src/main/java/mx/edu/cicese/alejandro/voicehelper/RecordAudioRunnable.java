package mx.edu.cicese.alejandro.voicehelper;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import mx.edu.cicese.alejandro.audio.record.AudioClipListener;

/**
 * Created by Alejandro on 3/19/15.
 */
public class RecordAudioRunnable implements Runnable {

    private static String LOG_TAG = "PitchDetector";
    private final static int RATE = 8000;
    private final static int CHANNEL_MODE = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private final static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private final static int BUFFER_SIZE_IN_MS = 250;
    private final static int BUFFER_SIZE = 4096;

    public RecordAudioRunnable(Context context, AudioClipListener clipListener) {
        this.clipListener_ =  clipListener;
        this.context_ = context;
    }

    @Override
    public void run() {

        Log.e(LOG_TAG, "starting to detect pitch");

        readBufferSize_ =  determineCalculatedBufferSize();

        android.os.Process
                .setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        recorder_ = new AudioRecord(MediaRecorder.AudioSource.MIC, RATE, CHANNEL_MODE,
                ENCODING, readBufferSize_);
        if (recorder_.getState() != AudioRecord.STATE_INITIALIZED) {
            return;
        }

        final short[] readBuffer = new short[256];

        recorder_.startRecording();
        while (!Thread.interrupted()) {
            recorder_.read(readBuffer, 0, 256);
           // recorder_.read(readBuffer, 0, readBufferSize_);
            heard_ = clipListener_.heard(readBuffer, RATE);
        }
        recorder_.stop();


    }

    private int determineMinimumBufferSize() {
        int minBufferSize =
                AudioRecord.getMinBufferSize(RATE,
                        CHANNEL_MODE, ENCODING);
        return minBufferSize;
    }

    /**
     * Calculate audio buffer size such that it holds numSamplesInBuffer and is
     * bigger than the minimum size<br>
     *
     */
    private int determineCalculatedBufferSize() {

        float percentOfASecond = (float) BUFFER_SIZE_IN_MS / 1000.0f;
        int numSamplesRequired = (int) ((float) RATE * percentOfASecond);
        int minBufferSize = determineMinimumBufferSize();

        int bufferSize;
        // each sample takes two bytes, need a bigger buffer
        if (ENCODING == AudioFormat.ENCODING_PCM_16BIT) {
            bufferSize = numSamplesRequired * 2;
        } else {
            bufferSize = numSamplesRequired;
        }

        if (bufferSize < minBufferSize) {
            Log.w(LOG_TAG, "Increasing buffer to hold enough samples "
                    + minBufferSize + " was: " + bufferSize);
            bufferSize = minBufferSize;
        }

        return bufferSize;
    }

    private AudioClipListener clipListener_;
    private AudioRecord recorder_;
    private Context context_;
    private int readBufferSize_;
    private boolean heard_;
}
