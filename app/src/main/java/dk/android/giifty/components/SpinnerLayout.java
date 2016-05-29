package dk.android.giifty.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import dk.android.giifty.R;


public class SpinnerLayout extends FrameLayout {

    private ProgressBar progressBar;

    public SpinnerLayout(Context context) {
        super(context);
        initLayout(context);
    }

    public SpinnerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public SpinnerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary_transperant_50));
        progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        this.setVisibility(GONE);
    }

    public void showProgressBar() {
        this.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        this.setVisibility(View.GONE);
    }

}
