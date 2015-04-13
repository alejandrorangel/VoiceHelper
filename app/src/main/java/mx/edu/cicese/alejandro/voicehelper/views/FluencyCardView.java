package mx.edu.cicese.alejandro.voicehelper.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import mx.edu.cicese.alejandro.rules.Mistep;
import mx.edu.cicese.alejandro.voicehelper.R;

/**
 * Created by Alejandro on 3/11/15.
 */
public class FluencyCardView extends FrameLayout implements MistepCardView {
    private SeekBar progressBar;
    private TextView triggerTextView;
    private TextView rulesTextView;
    private TextView mistepCounter;
    private AnimationDrawable bunnyAnimation;
    private ViewFlipper mViewFlipper;
    private ObjectAnimator animation;

    public FluencyCardView(Context context) {
        super(context);
        initView();
    }

    public void initView() {

        View view = inflate(getContext(), R.layout.fluencycard_layout, null);

        mistepCounter = (TextView) view.findViewById(R.id.incident_textview);
        progressBar = (SeekBar) view.findViewById(R.id.progressBar);
        rulesTextView = (TextView) view.findViewById(R.id.footer_rules);
        triggerTextView = (TextView) view.findViewById(R.id.footer_trigger);


        mViewFlipper = (ViewFlipper) view.findViewById(R.id.footer);
        mViewFlipper.setFlipInterval(R.integer.footer_flip_interval);
        ImageView bunnyImage = new ImageView(getContext());
        bunnyImage.setBackgroundResource(R.drawable.bunny_animation);
        bunnyAnimation = (AnimationDrawable) bunnyImage.getBackground();
        progressBar.setThumb(bunnyAnimation);
        bunnyAnimation.start();
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
