package com.zealens.face.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.base.Rule;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class ScoreBoardView extends FrameLayout {

    @IntDef({Mode.with_portrait, Mode.without_portrait})
    public @interface Mode {
        int with_portrait = 0;
        int without_portrait = 1;
    }

    /**
     * @see Rule.TrainState for array index
     */
    private final int[] STATE_RES_ARR = {
            R.drawable.score_board_state_end,
            R.drawable.score_board_state_in,
            R.drawable.score_board_state_out,
    };

    @Mode
    private int mMode;
    private PortraitView mPortraitView;
    private ImageView mStateView;
    private TextView mSuccessTimes;
    private TextView mRealTimeSpeed;

    public ScoreBoardView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ScoreBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ScoreBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.score_board_layout, this);
        mPortraitView = (PortraitView) findViewById(R.id.portrait_view);
        mStateView = (ImageView) findViewById(R.id.status_container);
        mSuccessTimes = (TextView) findViewById(R.id.success_time);
        mRealTimeSpeed = (TextView) findViewById(R.id.speed_real_time);

        if (attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScoreBoardView, defStyleAttr, 0);
            mMode = ta.getInteger(R.styleable.ScoreBoardView_mode, Mode.with_portrait);
            ta.recycle();
        }

        if (mMode == Mode.without_portrait) {
            LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
            int sumWithoutPortrait = context.getResources().getInteger(R.integer.sb_weight_sum_without_portrait);
            parent.setWeightSum(sumWithoutPortrait);
            View portraitContainer = findViewById(R.id.portrait_container);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) portraitContainer.getLayoutParams();
            lp.weight = context.getResources().getInteger(R.integer.sb_weight_portrait_zero);
            portraitContainer.setLayoutParams(lp);
        }
    }

    public PortraitView getPortraitView() {
        return mPortraitView;
    }

    public void setStateImage(@Rule.TrainState int state) {
        mStateView.setImageResource(STATE_RES_ARR[state]);
    }

    public void setSuccessTimesText(int times) {
        mSuccessTimes.setText(String.valueOf(times));
    }

    public void setRealTimeSpeedText(int speed) {
        mRealTimeSpeed.setText(String.valueOf(speed));
    }
}
