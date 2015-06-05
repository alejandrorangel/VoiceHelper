package mx.edu.cicese.alejandro.audio.Detector;

import android.util.Log;

import java.util.LinkedList;

import de.greenrobot.event.EventBus;
import mx.edu.cicese.alejandro.audio.processing.PitchAnalyzer;
import mx.edu.cicese.alejandro.audio.record.AudioClipListener;

/**
 * Created by UCI on 4/9/15.
 */
public class PitchDetector implements AudioClipListener {
    private static final String LOG_TAG = "PitchDetector";
    private final static int historySize = 13;


    @Override
    public boolean heard(short[] audioData, int sampleRate) {
        int numSamples = audioData.length;

        if(history==null){
            history = new LinkedList<Double>();
            for (int i = 0; i < historySize; i++) {
                history.add(Double.MAX_VALUE);
            }
        }
        // numSamples required to be a power of 2
        if (numSamples > 0 && Integer.bitCount(numSamples) == 1) {
            fftSampleSize = numSamples;
            unitFrequency = (double) sampleRate / numSamples;

            // set boundary
            lowerBoundary = (int) (highPass / unitFrequency);
            upperBoundary = (int) (lowPass / unitFrequency);

            short[] amplitudes = audioData;

            // analyze sound
            int totalAbsValue = 0;
            float averageAbsValue = 0.0f;

            for (int i = 0; i < amplitudes.length; i++) {
                totalAbsValue += Math.abs(amplitudes[i]);
            }
            averageAbsValue = totalAbsValue / amplitudes.length;

            // no input
            if (averageAbsValue < 15) {
                return false;
            }


            // set signals for fft
            WindowFunction window = new WindowFunction();
            window.setWindowType("Hamming");
            double[] win = window.generate(fftSampleSize);

            double[] signals = new double[fftSampleSize];
            double[] signalsSinVentana = new double[fftSampleSize];
            double meanSignal = 0;
            double[] coefLP = new double[11];
            double[] coefHP = new double[31];


            for (int n = 0; n < fftSampleSize; n++) {
                meanSignal += (amplitudes[n] / 32768.0);
                signalsSinVentana[n] = (amplitudes[n] / 32768.0);
            }

            for (int n = 0; n < fftSampleSize; n++) {
                signalsSinVentana[n] = amplitudes[n];
                signals[n] = (amplitudes[n]) * win[n];
            }
            //pitch
            PitchAnalyzer pitch = new PitchAnalyzer(signalsSinVentana, sampleRate, fftSampleSize);
            pitchValue = pitch.featureExtraction();

            history.removeFirst();
            history.addLast(pitchValue);

            EventBus.getDefault().post(this);

            Log.d(LOG_TAG, "pitch value: " + pitchValue);

        }
        return false;
    }

    public double getPitchValue(){
        return  pitchValue;
    }

    public double calculateRange() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Double value : history) {
            if (value >= max) {
                max = value;
            }

            if (value < min) {
                min = value;
            }
        }
        return max - min;
    }

    public double calculateAverage(){
            double average = 0.0;
            for(double temp: history){
                average += temp;
            }
            return average/history.size();
    }

    double pitchValue;
    private LinkedList<Double> history;
    protected int fftSampleSize;
    protected double unitFrequency;
    protected int highPass, lowPass;
    protected int lowerBoundary, upperBoundary;
}
