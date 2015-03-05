package mx.edu.cicese.alejandro.rules;

/**
 * Created by Alejandro on 2/23/15.
 */
public class Mistep {
    Kind kindOf;
    long timepstamp;
    String triggerMessege;
    String RulesMessege;
    int numberOfIncident;

    public Mistep(Kind kindOf) {
        this.kindOf = kindOf;
    }

    public Mistep(Kind kindOf, long timepstamp) {
        this.kindOf = kindOf;
        this.timepstamp = timepstamp;
    }

    public int getNumberOfIncident() {
        return numberOfIncident;
    }

    public void setNumberOfIncident(int numberOfIncident) {
        this.numberOfIncident = numberOfIncident;
    }

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

    public void setTimepstamp(long timepstamp) {
        this.timepstamp = timepstamp;
    }

    public Kind getKindOf() {
        return this.kindOf;
    }

    public void setKindOf(Kind kindOf) {
        this.kindOf = kindOf;
    }

    public enum Kind {
        VOICE, FLUENCY, EMOTION
    }


}
