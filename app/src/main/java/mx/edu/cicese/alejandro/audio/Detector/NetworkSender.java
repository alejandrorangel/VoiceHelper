package mx.edu.cicese.alejandro.audio.Detector;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mx.edu.cicese.alejandro.audio.record.AudioClipListener;
import mx.edu.cicese.alejandro.voicehelper.Sample;

/**
 * Created by UCI on 4/14/15.
 */
public class NetworkSender implements AudioClipListener{
    @Override
    public boolean heard(short[] audioData, int sampleRate) {

        if(tempSample == null){
            short[] bigContainter = new short [audioData.length * 20];
            System.arraycopy(audioData,0,bigContainter,0,audioData.length);
            tempSample = new Sample(bigContainter,sampleRate);
        }
        else{
            short[] bigContainter =  tempSample.getAudioDataShort();
            System.arraycopy(audioData,0,bigContainter,(audioData.length*counter),audioData.length);
            tempSample.setAudioDataShort(bigContainter);
        }
        counter++;

        if(counter ==20){
            sendPostRequest(tempSample);
            counter =0;
            tempSample = null;
        }

        return false;
    }

    private void sendPostRequest(Sample sample) {

        class SendPostReqAsyncTask extends AsyncTask<Sample, Void, String> {

            @Override
            protected String doInBackground(Sample... params) {

                //TODO cambiar esto de lugar
                // The connection URL
                // String url = "https://ajax.googleapis.com/ajax/"+"services/search/web?v=1.0&q={query}";
                String url = "http://192.168.43.143:5984/waes/{query}";
                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate();

                // Add the String message converter
                restTemplate.setMessageConverters(getMessageConverters());

                restTemplate.put(url, params[0], UUID.randomUUID().toString());

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(sample);
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        return converters;
    }
    int counter = 0;
    Sample tempSample = null;
}
