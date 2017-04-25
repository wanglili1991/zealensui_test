package com.zealens.face.base.headtimer.presenter;

import android.support.annotation.IntDef;
import android.support.v7.widget.ContentFrameLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.activity.base.BaseActivity;
import com.zealens.face.base.fakedialog.DialogPresenter;
import com.zealens.face.base.fakedialog.DialogPresenterImpl;
import com.zealens.face.base.headtimer.HeadTimerHelper;
import com.zealens.face.common.ClickCallback;
import com.zealens.face.common.CompleteCallBack;
import com.zealens.face.core.CoreContext;
import com.zealens.face.presenter.BasePresenterImpl;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class HeadTimerLayerPresenterImpl extends BasePresenterImpl implements HeadTimerLayerPresenter, ClickCallback {
    @IntDef({Mode.match, Mode.train})
    public @interface Mode {
        int match = 0;
        int train = 1;
    }

    private CoreContext mCoreContext;
    private ViewGroup mRootVG;

    private View mHeadTimerLayer;
    private DialogPresenter mEndScenePresenter;
    private CompleteCallBack mCompleteCallBack;
    @Mode
    private int mMode;
    private TextView mTimerView;
    private final long TIMER_VIEW_REFRESH_INTERNAL = 1000;
    private BaseActivity mBaseActivity;
    private ScheduledFuture<?> mScheduledFuture;

    public HeadTimerLayerPresenterImpl(CoreContext coreContext, ViewGroup rootVG, @Mode int mode
            , CompleteCallBack callBack, BaseActivity baseActivity) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mCompleteCallBack = callBack;
        mMode = mode;
        mBaseActivity = baseActivity;
    }

    @Override
    public void loadHeadLayer() {
        if (mHeadTimerLayer == null)
            mHeadTimerLayer = LayoutInflater.from(mCoreContext.getApplicationContext()).inflate(R.layout.head_timer_layer_layout, null, false);

        mTimerView = (TextView) mHeadTimerLayer.findViewById(R.id.timer_text);
        boolean modeMatching = mMode == Mode.match;
        ((TextView) mHeadTimerLayer.findViewById(R.id.start_tag)).setText(modeMatching ? R.string.matching : R.string.training);
        ((TextView) mHeadTimerLayer.findViewById(R.id.action_button)).setText(modeMatching ? R.string.end_matching : R.string.end_training);

        View endScoreView = LayoutInflater.from(mCoreContext.getApplicationContext()).inflate(R.layout.end_score_dialog_layout, null, false);
        ((TextView) endScoreView.findViewById(R.id.title)).setText(modeMatching ? R.string.end_matching : R.string.end_training);
        ((TextView) endScoreView.findViewById(R.id.description)).setText(modeMatching ? R.string.end_matching_confirm_hint : R.string.end_train_confirm_hint);
        ((TextView) endScoreView.findViewById(R.id.yes_text)).setText(modeMatching ? R.string.end_matching : R.string.end_training);
        ((TextView) endScoreView.findViewById(R.id.no_text)).setText(modeMatching ? R.string.continue_matching : R.string.continue_train);
        mEndScenePresenter = new DialogPresenterImpl(mCoreContext, mRootVG, endScoreView, this);
        mHeadTimerLayer.findViewById(R.id.action_button).setOnClickListener((v) -> mEndScenePresenter.loadDialogPage());

        ContentFrameLayout.LayoutParams lp = new ContentFrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = ContentFrameLayout.LayoutParams.MATCH_PARENT;
        lp.height = mCoreContext.getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.head_account_layer_height);
        lp.gravity = Gravity.TOP;
        mRootVG.addView(mHeadTimerLayer, lp);

        long startTime = System.currentTimeMillis();
        mScheduledFuture = mCoreContext.getScheduleExecutor().scheduleAtFixedRate(() -> {
            if (mBaseActivity != null && !mBaseActivity.isFinishing())
                mBaseActivity.runOnUiThread(() -> {
                    long passedTimeValue = System.currentTimeMillis() - startTime;
                    String time = HeadTimerHelper.generateTimerString(passedTimeValue);
                    mTimerView.setText(time);
                });
        }, 0, TIMER_VIEW_REFRESH_INTERNAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void removeHeadLayer() {
        mRootVG.removeView(mHeadTimerLayer);
    }

    @Override
    public void dispose() {
        super.dispose();
        removeHeadLayer();
        mHeadTimerLayer = null;
        mEndScenePresenter = null;
        mCompleteCallBack = null;
        mTimerView = null;
        mScheduledFuture.cancel(true);
    }

    @Override
    public void onYes() {
        mCompleteCallBack.onComplete(null);
    }

    @Override
    public void onNo() {
    }

    @Override
    public void onClose() {
    }
}
