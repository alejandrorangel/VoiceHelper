package mx.edu.cicese.alejandro.rules;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import mx.edu.cicese.alejandro.voicehelper.R;

/**
 * Created by Alejandro on 3/2/15.
 * TODO
 * This is a temporaly name, I have to find a better name for this class
 */
public class Rules {
    ArrayList<Incident> incidents;
    Context context;

    public Rules(Context context){
        this.context = context;
        this.incidents =  new ArrayList<Incident>();
    }

    public void addIncident(Incident.Kind kind){
        Long current = System.currentTimeMillis()/1000;

        Incident newIncident = new Incident(kind, current);

        newIncident.setTriggerMessege(selectTringgerString(kind));

        Log.d("VoiceHelper", newIncident.getTriggerMessege());

        /** parte para agregar**/
        incidents.add(newIncident);
    }

    public int checkNumberOfIncidents(Incident.Kind kind){
        int count = 0;
        for(Incident incident : incidents){
            if(incident.kindOf == kind)
                count++;
        }
        return count;
    }


    /** Revisar... con quedo muy limpio que digamos**/
    public String selectTringgerString(Incident.Kind kind){
        int count  = checkNumberOfIncidents(kind);
        Resources res = context.getResources();
        String[] strings = null;

        if (count <= 1) {
            if(kind == Incident.Kind.VOICE){
                strings = res.getStringArray(R.array.voice_trigger_first);
            }
            if(kind == Incident.Kind.FLUENCY){
                strings = res.getStringArray(R.array.fluency_trigger_first);
            }
            if(kind == Incident.Kind.EMOTION){
                strings = res.getStringArray(R.array.emotion_trigger_first);
            }
        }
        else if (/* myInterval >= 0 && */ count <= 3){
            if(kind == Incident.Kind.VOICE){
                strings = res.getStringArray(R.array.voice_trigger_second);
            }
            if(kind == Incident.Kind.FLUENCY){
                strings = res.getStringArray(R.array.fluency_trigger_second);
            }
            if(kind == Incident.Kind.EMOTION){
                strings = res.getStringArray(R.array.emotion_trigger_second);
            }
        }
        else {
            if(kind == Incident.Kind.VOICE){
                strings = res.getStringArray(R.array.voice_trigger_third);
            }
            if(kind == Incident.Kind.FLUENCY){
                strings = res.getStringArray(R.array.fluency_trigger_third);
            }
            if(kind == Incident.Kind.EMOTION){
                strings = res.getStringArray(R.array.emotion_trigger_third);
            }
        }


        return strings[new Random().nextInt(strings.length)];
    }
}
