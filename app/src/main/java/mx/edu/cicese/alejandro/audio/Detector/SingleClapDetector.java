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
package mx.edu.cicese.alejandro.audio.Detector;

import android.util.Log;

import mx.edu.cicese.alejandro.audio.record.AmplitudeClipListener;

/**
 * @author Greg Milette &#60;<a
 *         href="mailto:gregorym@gmail.com">gregorym@gmail.com</a>&#62;
 */
public class SingleClapDetector implements AmplitudeClipListener {
    /**
     * requires a little of noise by the user to trigger, background noise may
     * trigger it
     */
    public static final int AMPLITUDE_DIFF_LOW = 10000;
    public static final int AMPLITUDE_DIFF_MED = 18000;
    private static final int DEFAULT_AMPLITUDE_DIFF = AMPLITUDE_DIFF_MED;
    /**
     * requires a lot of noise by the user to trigger. background noise isn't
     * likely to be this loud
     */
    public static final int AMPLITUDE_DIFF_HIGH = 25000;
    private static final String TAG = "SingleClapDetector";
    /**
     * required loudness to determine it is a clap
     */
    private int amplitudeThreshold;

    public SingleClapDetector() {
        this(DEFAULT_AMPLITUDE_DIFF);
    }

    public SingleClapDetector(int amplitudeThreshold) {
        this.amplitudeThreshold = amplitudeThreshold;
    }

    @Override
    public boolean heard(int maxAmplitude) {
        boolean clapDetected = false;

        if (maxAmplitude >= amplitudeThreshold) {
            Log.d(TAG, "heard a clap");
            clapDetected = true;
        }

        return clapDetected;
    }
}
