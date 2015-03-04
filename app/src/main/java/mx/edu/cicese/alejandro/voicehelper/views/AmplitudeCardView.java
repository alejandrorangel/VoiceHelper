package mx.edu.cicese.alejandro.voicehelper.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import mx.edu.cicese.alejandro.voicehelper.R;

/**
 * Created by Alejandro on 2/28/15.
 */


public class AmplitudeCardView extends FrameLayout {
    private ProgressBar progressBar;
    private TextSwitcher textSwitcher;
    private TextView incidentTextview;
    private ObjectAnimator animation;

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
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        this.textSwitcher = (TextSwitcher) view.findViewById(R.id.footer_textswitcher);
        this.textSwitcher.setFactory(mFactory);
        this.incidentTextview = (TextView) view.findViewById(R.id.incident_textview);
        addView(view);
    }

    public void setProgressBarValue(int value){
            animation = ObjectAnimator.ofInt(progressBar, "progress", value);
            animation.setDuration(200); // 0.5 second
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
    }

    public void setCurrentText(String text){
        this.textSwitcher.setCurrentText(text);
    }
    public void setText(String text){
        this.textSwitcher.setText(text);
    }

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {
            TextView t = new TextView(getContext());
            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            t.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
            return t;
        }
    };
}

