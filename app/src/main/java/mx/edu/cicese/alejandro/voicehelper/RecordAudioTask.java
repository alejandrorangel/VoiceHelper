/*
 * Copyright 2012 Greg Milette and Adam Stroud
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.edu.cicese.alejandro.voicehelper;

import android.content.Context;
import android.media.AudioFormat;
import android.os.AsyncTask;
import android.util.Log;

import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.audio.record.AudioClipRecorder;


public class RecordAudioTask extends AsyncTask<AudioClipListener, Void, Boolean> {
    private static final String TAG = "RecordAudioTask";

    private Context context;
    private String taskName;

    public RecordAudioTask(Context context, String taskName) {
        this.context = context;
        this.taskName = taskName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(AudioClipListener... listeners) {
        if (listeners.length == 0) {
            return (Boolean) false;
        }

        AudioClipListener listener = listeners[0];

        AudioClipRecorder recorder = new AudioClipRecorder(listener, this);

        boolean heard = false;
        for (int i = 0; i < 10; i++) {
            try {
                heard =
                        recorder.startRecordingForTime(500,
                                AudioClipRecorder.RECORDER_SAMPLERATE_CD,
                                AudioFormat.ENCODING_PCM_16BIT);
                break;
            } catch (IllegalStateException ie) {
                // failed to setup, sleep and try again
                // if still can't set it up, just fail
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }

        //collect the audio
        return (Boolean) heard;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(TAG, "After execute got result: " + result);
        //update the UI with what happened
        //add to log
        //redraw perhaps
        if (result) {
            Log.d(TAG, getTaskName() + " detected " + AudioTaskUtil.getNow());
            //AudioTaskUtil.appendToStartOfLog(log, getTaskName() + " detected " + AudioTaskUtil.getNow());
        } else {
            Log.d(TAG, "stopped");
            //AudioTaskUtil.appendToStartOfLog(log, "stopped");
        }

        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled() {
        Log.d(TAG, "OnCancelled");
        //the recorder should have shut down, this method
        //needs to just clean up resources
        Log.d(TAG, "cancelled " + getTaskName());
        super.onCancelled();
    }


    public String getTaskName() {
        return taskName;
    }
}
