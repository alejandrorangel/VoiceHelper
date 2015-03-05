package mx.edu.cicese.alejandro.voicehelper;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by Alejandro on 3/4/15.
 */
public class VoiceHelper extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
