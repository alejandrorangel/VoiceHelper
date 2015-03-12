package mx.edu.cicese.alejandro.voicehelper.views;

import mx.edu.cicese.alejandro.rules.Mistep;

/**
 * Created by Alejandro on 3/11/15.
 * TODO
 * Temporaly name until I think in a better name
 */
public interface MistepCardView {

    public void updateViewWithMistep(Mistep mistep);

    public void updateScale(int value);

    public void updateMistepCounter(int value);

    public void updateRulesMessage(String rule);

    public void updateTriggerMessage(String trigger);

}
