package mx.edu.cicese.alejandro.audio.Detector;

import android.util.Log;

import java.util.LinkedList;

import de.greenrobot.event.EventBus;
import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.audio.util.AudioUtil;

/**
 * Created by Alejandro on 3/4/15.
 */
public class ConsistentLoudNoiseDetector implements AudioClipListener {
    private LinkedList<Double> volumeHistory;
    private double averageVolume;
    private double currentVolume;
    private double volumeThreshold;
    private double rangeThreshold;
    private double silenceThreshold;

    private double lowPassAlpha = 0.5;
    private double STARTING_AVERAGE = 400.0;
    private double INCREASE_FACTOR = 4.5;

    public ConsistentLoudNoiseDetector(int historySize, int rangeThreshold,
                                       int silenceThreshold) {
        averageVolume = STARTING_AVERAGE;
        volumeHistory = new LinkedList<Double>();
        // pre-fill so modification is easy
        for (int i = 0; i < historySize; i++) {
            volumeHistory.add(Double.MAX_VALUE);
        }

        this.rangeThreshold = rangeThreshold;
        this.silenceThreshold = silenceThreshold;
    }

    @Override
    public boolean heard(short[] audioData, int sampleRate) {

        currentVolume = AudioUtil.rootMeanSquared(audioData);
        volumeHistory.addFirst(currentVolume);
        // since history is always full, just remove the last
        volumeHistory.removeLast();

        volumeThreshold = averageVolume * INCREASE_FACTOR;

        Log.d("VoiceHelper", "actual: " + currentVolume + " promedio: " + averageVolume
           + " threshold: " + volumeThreshold);

        if (currentVolume < volumeThreshold)
            averageVolume = lowPass(currentVolume, averageVolume);

        EventBus.getDefault().post(this);
        return false;
    }

    private double calculateRange() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Double value : volumeHistory) {
            if (value >= max) {
                max = value;
            }

            if (value < min) {
                min = value;
            }
        }
        return max - min;
    }

    public double getCurrentVolume() {
        return currentVolume;
    }


    private double lowPass(double current, double last) {
        return last * (1.0 - lowPassAlpha) + current * lowPassAlpha;
    }

    public boolean isTooLoud() {
        boolean response = false;
        if ((currentVolume > volumeThreshold) && isConsistent())
            response = true;

        return response;
    }

    public boolean isConsistent() {
        boolean response = false;
        if (calculateRange() < rangeThreshold) {
            response = true;
        }

        //Log.d("VoiceHelper", "Valor rango:" + calculateRange() + " rangeThreshold");

        return response;
    }

    @Override
    public String toString() {
        return String.valueOf(getCurrentVolume());
    }
}
