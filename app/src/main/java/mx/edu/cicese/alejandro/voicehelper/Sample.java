package mx.edu.cicese.alejandro.voicehelper;

import java.util.Date;

/**
 * Created by Alejandro on 3/26/15.
 */
public class Sample {

    public Sample(short[] audioDataShort, int sampleRate) {
        this.name = "negro";
        this.audioDataShort = audioDataShort;
        this.sampleRate = sampleRate;
        this.timestamp = new Date().toString();
    }

    public short[] getAudioDataShort() {

        return audioDataShort;
    }

    public void setAudioDataShort(short[] audioDataShort) {

        this.audioDataShort = audioDataShort;
    }

    public int getSampleRate() {

        return sampleRate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSampleRate(int sampleRate) {

        this.sampleRate = sampleRate;
    }

    short[] audioDataShort;
    int sampleRate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    String timestamp;
}
