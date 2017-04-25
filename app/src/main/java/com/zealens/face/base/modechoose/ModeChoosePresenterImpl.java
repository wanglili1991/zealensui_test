package com.zealens.face.base.modechoose;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zealens.face.R;
import com.zealens.face.adapter.ModeChooseRecyclerViewAdapter;
import com.zealens.face.base.Rule;
import com.zealens.face.common.SimpleCallbackWithArg;
import com.zealens.face.core.CoreContext;
import com.zealens.face.presenter.BasePresenterImpl;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class ModeChoosePresenterImpl extends BasePresenterImpl implements ModeChoosePresenter, SimpleCallbackWithArg {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;
    private SimpleCallbackWithArg mCallbackWithArg;

    private RecyclerView mModeChoosePanelSideA;
    private RecyclerView mModeChoosePanelSideB;
    private ModeChooseRecyclerViewAdapter mAdapterA;
    private ModeChooseRecyclerViewAdapter mAdapterB;
    private boolean mSideAChosen;
    private boolean mSideBChosen;

    public ModeChoosePresenterImpl(CoreContext coreContext, ViewGroup rootVG, SimpleCallbackWithArg verifyCallback) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mCallbackWithArg = verifyCallback;
    }

    @Override
    public void initialize() {
        super.initialize();
        mModeChoosePanelSideA = (RecyclerView) mRootVG.findViewById(R.id.train_mode_choose_panel_size_a);
        mModeChoosePanelSideB = (RecyclerView) mRootVG.findViewById(R.id.train_mode_choose_panel_size_b);
        int spanCount = mCoreContext.getInteger(R.integer.mode_choose_panel_span_count);
        GridLayoutManager glmA = new UnScrollableGridLayoutManager(mCoreContext.getBaseContext()
                , spanCount, GridLayoutManager.VERTICAL, false);
        GridLayoutManager glmB = new UnScrollableGridLayoutManager(mCoreContext.getBaseContext()
                , spanCount, GridLayoutManager.VERTICAL, false);
        mAdapterA = new ModeChooseRecyclerViewAdapter(mCoreContext.getApplicationContext(), Rule.Side.A, this);
        mAdapterB = new ModeChooseRecyclerViewAdapter(mCoreContext.getApplicationContext(), Rule.Side.B, this);
        mModeChoosePanelSideA.setLayoutManager(glmA);
        mModeChoosePanelSideA.setAdapter(mAdapterA);
        mModeChoosePanelSideB.setLayoutManager(glmB);
        mModeChoosePanelSideB.setAdapter(mAdapterB);
        mModeChoosePanelSideA.setNestedScrollingEnabled(false);
        mModeChoosePanelSideB.setNestedScrollingEnabled(false);
    }

    @Override
    public void dispose() {
        super.dispose();
        mModeChoosePanelSideA = null;
        mModeChoosePanelSideB = null;
    }

    @Override
    public void onRefresh(Object obj) {
        if (obj instanceof SideEffectUnit) {
            SideEffectUnit unit = (SideEffectUnit) obj;
            (unit.side == Rule.Side.A ? mAdapterB : mAdapterA).enableItem(!unit.checked, Rule.TRAIN_MODE_ITEM_ALTERNATIVE_RULER[unit.trainMode]);
            if (unit.side == Rule.Side.A)
                mSideAChosen = unit.checked;
            else
                mSideBChosen = unit.checked;
            mCallbackWithArg.onRefresh(unit.trainMode);
            mCallbackWithArg.onRefresh(mSideAChosen || mSideBChosen);
        }
    }
}
