package mx.edu.cicese.alejandro.voicehelper.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import mx.edu.cicese.alejandro.rules.Mistep;
import mx.edu.cicese.alejandro.voicehelper.R;

/**
 * Created by Alejandro on 3/11/15.
 */
public class FluencyCardView extends FrameLayout implements MistepCardView {
    private ProgressBar progressBar;
    private TextView triggerTextView;
    private TextView rulesTextView;
    private TextView mistepCounter;
    private ViewFlipper mViewFlipper;
    private ObjectAnimator animation;

    public FluencyCardView(Context context) {
        super(context);
        initView();
    }

    public void initView() {
        View view = inflate(getContext(), R.layout.fluencycard_layout, null);

        mistepCounter = (TextView) view.findViewById(R.id.incident_textview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        rulesTextView = (TextView) view.findViewById(R.id.footer_rules);
        triggerTextView = (TextView) view.findViewById(R.id.footer_trigger);

        mViewFlipper = (ViewFlipper) view.findViewById(R.id.footer);
        mViewFlipper.setFlipInterval(R.integer.footer_flip_interval);

        addView(view);
    }

    public void clearFooterMessages() {
        triggerTextView.setText(null);
        rulesTextView.setText(null);
        mViewFlipper.stopFlipping();
    }

    @Override
    public void updateScale(int value) {
        animation = ObjectAnimator.ofInt(progressBar, "progress", value);
        animation.setDuration(R.integer.update_frequency);
        animation.setInterpolator(new DecelerateInterpolator());

        animation.start();
    }

    @Override
    public void updateMistepCounter(int value) {
        this.mistepCounter.setText(String.valueOf(value));
    }

    @Override
    public void updateRulesMessage(String rule) {
        this.rulesTextView.setText(rule);
    }

    @Override
    public void updateTriggerMessage(String trigger) {
        this.triggerTextView.setText(trigger);
    }

    @Override
    public void updateViewWithMistep(Mistep mistep) {
        updateMistepCounter(mistep.getNumberOfIncident());
        updateTriggerMessage(mistep.getTriggerMessege());
        updateRulesMessage(mistep.getRulesMessege());

        this.mViewFlipper.startFlipping();
    }
}
