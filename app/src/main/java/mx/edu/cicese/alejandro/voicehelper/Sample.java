package mx.edu.cicese.alejandro.voicehelper;

/**
 * Created by Alejandro on 3/26/15.
 */
public class Sample {
    short[] audioDataShort;
    byte[] audioDataByte;
    int sampleRate;

    public short[] getAudioDataShort() {
        return audioDataShort;
    }

    public void setAudioDataShort(short[] audioDataShort) {
        this.audioDataShort = audioDataShort;
    }

    public byte[] getAudioDataByte() {
        return audioDataByte;
    }

    public void setAudioDataByte(byte[] audioDataByte) {
        this.audioDataByte = audioDataByte;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }
}
