package mx.edu.cicese.alejandro.voicehelper.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import mx.edu.cicese.alejandro.rules.Mistep;
import mx.edu.cicese.alejandro.voicehelper.R;

/**
 * Created by Alejandro on 2/28/15.
 *
 */


public class AmplitudeCardView extends FrameLayout {
    private ProgressBar progressBar;
    private TextView textTrigger;
    private TextView textRules;
    private TextView mistepTextview;
    private ViewFlipper mViewFlipper;
    private ObjectAnimator animation;
    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {
            TextView t = new TextView(getContext());
            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            t.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
            return t;
        }
    };

    public AmplitudeCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public AmplitudeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AmplitudeCardView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.amplitudecard_layout, null);
        this.mistepTextview = (TextView) view.findViewById(R.id.incident_textview);
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        this.textRules = (TextView) view.findViewById(R.id.footer_rules);
        this.textTrigger = (TextView) view.findViewById(R.id.footer_trigger);
        this.mViewFlipper = (ViewFlipper) view.findViewById(R.id.footer);
        this.mViewFlipper.setFlipInterval(1500);
        addView(view);
    }

    public void setProgressBarValue(int value) {
        animation = ObjectAnimator.ofInt(progressBar, "progress", value);
        animation.setDuration(200); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    public void setTriggerText(String text) {
        this.textTrigger.setText(text);
    }

    public void setRulesText(String text) {
        this.textRules.setText(text);
    }

    public void setIncidentText(int number) {
        this.mistepTextview.setText(String.valueOf(number));
    }

    public void incidentDetect(Mistep mistep) {
        this.mistepTextview.setText(String.valueOf(mistep.getNumberOfIncident()));
        this.setTriggerText(mistep.getTriggerMessege());
        this.setRulesText(mistep.getRulesMessege());
        this.mViewFlipper.startFlipping();
    }
}

