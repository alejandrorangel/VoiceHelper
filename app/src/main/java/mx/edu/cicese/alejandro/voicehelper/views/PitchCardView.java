package mx.edu.cicese.alejandro.voicehelper.views;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.LinkedList;

import mx.edu.cicese.alejandro.rules.Mistep;
import mx.edu.cicese.alejandro.voicehelper.R;

/**
 * Created by UCI on 4/13/15.
 */
public class PitchCardView extends FrameLayout implements MistepCardView {
    private final int DELAYTIME = 2000;

    private Runnable timeout = new Runnable() {
        public void run() {
            Log.d("VoiceHelper", "Timeout");
            //releaseScreen();
            clearFooterMessages();
        }
    };



    public PitchCardView(Context context){
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.pitchcard_layout, null);

        _mistepCounter = (TextView) view.findViewById(R.id.incident_textview);
        _rulesTextView = (TextView) view.findViewById(R.id.footer_rules);
        _triggerTextView = (TextView) view.findViewById(R.id.footer_trigger);
        _textView = (TextView) view.findViewById(R.id.text);
        _secondTextView = (TextView) view.findViewById(R.id.text2);
        _mViewFlipper = (ViewFlipper) view.findViewById(R.id.footer);
        _mViewFlipper.setFlipInterval(R.integer.footer_flip_interval);

        addView(view);
    }


    @Override
    public void updateScale(int value) {

    }

    public void updateScale(Double value){

        String num = String.format("%.2f",value);
        this._textView.setText("");
    }

    public void updateSecondView(String text){
        this._secondTextView.setText(text);
        myHandler.postDelayed(timeout, DELAYTIME);
    }


    @Override
    public void updateMistepCounter(int value) {
        this._mistepCounter.setText(String.valueOf(value));
    }

    @Override
    public void updateRulesMessage(String rule) {
        this._rulesTextView.setText(rule);
    }

    @Override
    public void updateTriggerMessage(String trigger) {
        this._triggerTextView.setText(trigger);
    }

    @Override
    public void updateViewWithMistep(Mistep mistep) {
        updateMistepCounter(mistep.getNumberOfIncident());
        updateTriggerMessage(mistep.getTriggerMessege());
        updateRulesMessage(mistep.getRulesMessege());

        this._mViewFlipper.startFlipping();
    }

    public void clearFooterMessages() {
        this._secondTextView.setText("normal");
    }

    private Handler myHandler = new Handler();
    private TextView _triggerTextView;
    private TextView _rulesTextView;
    private TextView _mistepCounter;
    private TextView _textView;
    private TextView _secondTextView;
    private ViewFlipper _mViewFlipper;
}
