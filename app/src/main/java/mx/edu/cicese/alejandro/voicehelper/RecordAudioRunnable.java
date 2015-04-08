package mx.edu.cicese.alejandro.voicehelper;

import android.content.Context;

import mx.edu.cicese.alejandro.audio.record.AudioClipListener;

/**
 * Created by Alejandro on 3/19/15.
 */
public class RecordAudioRunnable implements Runnable {
    private Context context;
    private String taskName;
    private AudioClipListener listener;

    public RecordAudioRunnable(Context context, String taskName, AudioClipListener listener) {
        this.context = context;
        this.taskName = taskName;
        this.listener = listener;
    }

    @Override
    public void run() {


    }
}
