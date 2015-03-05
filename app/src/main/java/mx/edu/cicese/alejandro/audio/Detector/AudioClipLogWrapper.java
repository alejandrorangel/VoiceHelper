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

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import de.greenrobot.event.EventBus;
import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.audio.util.AudioUtil;


public class AudioClipLogWrapper implements AudioClipListener {
    private Context context;
    private double averageVolume;
    private double currentVolume;
    private double volumeThreshold;
    private double lowPassAlpha = 0.5;
    private double STARTING_AVERAGE = 200.0;
    private double INCREASE_FACTOR = 2.0;


    public AudioClipLogWrapper(Context context) {
        this.context = context;
        averageVolume = STARTING_AVERAGE;
    }

    @Override
    public boolean heard(short[] audioData, int sampleRate) {


        currentVolume = AudioUtil.rootMeanSquared(audioData);

        volumeThreshold = averageVolume * INCREASE_FACTOR;

        Log.d("VoiceHelper", "actual: " + currentVolume + " promedio: " + averageVolume
                + " threshold: " + volumeThreshold);


        if (currentVolume < volumeThreshold)
            averageVolume = lowPass(currentVolume, averageVolume);

        EventBus.getDefault().post(this);
        return false;
    }

    public double getAverageVolume() {
        return averageVolume;
    }

    public double getCurrentVolume() {
        return currentVolume;
    }


    private double lowPass(double current, double last) {
        return last * (1.0 - lowPassAlpha) + current * lowPassAlpha;
    }

    public boolean isTooLoud() {
        boolean returnValue = false;
        if (currentVolume > volumeThreshold)
            returnValue = true;

        return returnValue;
    }

    public void turnOnScreen() {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "Wake Lock");
        if (powerManager.isScreenOn() == false) {
            wakeLock.acquire();
        }
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    public String toString() {
        return String.valueOf(getCurrentVolume());
    }
}
