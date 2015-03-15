package mx.edu.cicese.alejandro.rules;

/**
 * Created by Alejandro on 2/23/15.
 */
public class Mistep {
    Kind kindOf;
    long detectionTime;
    String triggerMessege;
    String RulesMessege;
    int numberOfIncident;

    public Mistep(Kind kindOf) {
        this.kindOf = kindOf;
    }

    public Mistep(Kind kindOf, long detectionTime) {
        this.kindOf = kindOf;
        this.detectionTime = detectionTime;
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

    public void setDetectionTime(long detectionTime) {
        this.detectionTime = detectionTime;
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
