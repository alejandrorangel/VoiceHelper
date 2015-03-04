package mx.edu.cicese.alejandro.rules;

/**
 * Created by Alejandro on 2/23/15.
 */
public class Incident {
    public enum Kind{
        VOICE, FLUENCY, EMOTION
    }

    Kind kindOf;
    long timepstamp;

    public String getRulesMessege() {
        return RulesMessege;
    }

    public void setRulesMessege(String rulesMessege) {
        RulesMessege = rulesMessege;
    }

    public String getTriggerMessege() {
        return triggerMessege;
    }

    public void setTriggerMessege(String triggerMessege) {
        this.triggerMessege = triggerMessege;
    }

    String triggerMessege;
    String RulesMessege;

    public Incident(Kind kindOf){
        this.kindOf =  kindOf;
    }

    public Incident(Kind kindOf, long timepstamp){
        this.kindOf =  kindOf;
        this.timepstamp =  timepstamp;
    }


    public void setTimepstamp(long timepstamp){
        this.timepstamp =  timepstamp;
    }

    public void setKindOf(Kind kindOf){
        this.kindOf =  kindOf;
    }

    public Kind getKindOf(){
        return this.kindOf;
    }


}
