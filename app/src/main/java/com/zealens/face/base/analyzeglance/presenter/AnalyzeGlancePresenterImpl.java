package com.zealens.face.base.analyzeglance.presenter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zealens.face.R;
import com.zealens.face.adapter.AnalyzeRecyclerViewAdapter;
import com.zealens.face.base.Rule;
import com.zealens.face.base.analyzeglance.IndicatorItemDecorator;
import com.zealens.face.base.modechoose.UnScrollableLinearLayoutManager;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.presenter.BasePresenterImpl;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class AnalyzeGlancePresenterImpl extends BasePresenterImpl implements AnalyzeGlancePresenter {
    @IntDef({Mode.singleMatch, Mode.doubleMatch, Mode.training})
    public @interface Mode {
        int singleMatch = 0;
        int doubleMatch = 1;
        int training = 2;
    }

    private CoreContext mCoreContext;
    private ViewGroup mRootVG;
    @Rule.ChannelMode
    private int mChanelMode;
    private RecyclerView mRecyclerView;
    private TennisBase.BaseBoxScore[] mMatchBoxScores;

    public AnalyzeGlancePresenterImpl(CoreContext coreContext, ViewGroup rootVG, @Mode int mode
            , TennisBase.BaseBoxScore[] scores) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mChanelMode = mode;
        mMatchBoxScores = scores;
    }

    @Override
    public void initialize() {
        super.initialize();
        mRecyclerView = (RecyclerView) mRootVG.findViewById(R.id.analyze_recycler_view);
        Context context = mCoreContext.getApplicationContext();
        mRecyclerView.setLayoutManager(new UnScrollableLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        AnalyzeRecyclerViewAdapter adapter = new AnalyzeRecyclerViewAdapter(mCoreContext.getApplicationContext()
                , mMatchBoxScores, mChanelMode);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new IndicatorItemDecorator());
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
