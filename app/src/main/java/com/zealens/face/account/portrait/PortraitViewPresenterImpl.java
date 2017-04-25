package com.zealens.face.account.portrait;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zealens.face.R;
import com.zealens.face.account.login.presenter.LoginPresenter;
import com.zealens.face.adapter.RecyclerViewAdapter;
import com.zealens.face.core.CoreContext;
import com.zealens.face.data.common.DataConst;
import com.zealens.face.data.user.ChosenUserArea;
import com.zealens.face.data.user.UserCacheManager;
import com.zealens.face.data.user.UserIndex;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.user.UserInfo;
import com.zealens.face.util.CollectionUtil;
import com.zealens.face.util.DisplayUtil;
import com.zealens.face.util.ViewUtil;
import com.zealens.face.view.PortraitView;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/3/10
 * in BlaBla by Kyle
 */

public class PortraitViewPresenterImpl extends BasePresenterImpl implements PortraitViewPresenter {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;

    private Context mContext;
    private UserCacheManager mUserCacheManager;
    private int[] mPortraitViewIds;
    private PortraitView[] mPortraitViews;
    private final int PORTRAIT_COUNT;
    private View mExchangeLayout;
    private RecyclerView mChangeRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private LoginPresenter mLoginPresenter;
    private boolean mPortraitClickable;
    private boolean mShowExchangeIconWhenLoaded;

    public PortraitViewPresenterImpl(CoreContext coreContext, ViewGroup rootVG, @Nullable LoginPresenter loginPresenter
            , @IdRes int[] idArr, boolean portraitClickable, boolean showExchange) {
        Assert.assertTrue(CollectionUtil.notEmptyArr(idArr));
        Assert.assertTrue(idArr.length <= DataConst.MAX_CHOSEN_PLAYER_COUNT);
        mContext = coreContext.getApplicationContext();
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mPortraitViewIds = idArr;
        PORTRAIT_COUNT = mPortraitViewIds.length;

        mRootVG = rootVG;
        mLoginPresenter = loginPresenter;
        mPortraitClickable = portraitClickable;
        mShowExchangeIconWhenLoaded = showExchange;
    }

    @UiThread
    @Override
    public void initialize() {
        super.initialize();
        mUserCacheManager = (UserCacheManager) mCoreContext.getApplicationService(UserCacheManager.class);
        mPortraitViews = new PortraitView[PORTRAIT_COUNT];
        @IdRes int id;
        for (int i = 0; i < PORTRAIT_COUNT; i++) {
            id = mPortraitViewIds[i];
            mPortraitViews[i] = (PortraitView) mRootVG.findViewById(id);
            if (mUserCacheManager.get(UserIndex.find(i)) != null)
                mUserCacheManager.assignChosenPlayer(ChosenUserArea.find(i), UserIndex.find(i));
        }
        injectUserInfoToView();
        if (!mPortraitClickable) {
            ViewUtil.disClick(mPortraitViews);
        } else {
            assignClickTask(mPortraitViews);
        }
    }

    @Override
    @UiThread
    public void injectUserInfoToView() {
        for (int i = 0; i < PORTRAIT_COUNT; i++) {
            injectUserInfoToView(mPortraitViews[i], mUserCacheManager.parseChosenPlayer(ChosenUserArea.find(i)));
        }
    }

    @UiThread
    @Override
    public void injectUserInfoToView(UserInfo info) {
        if (PORTRAIT_COUNT == DataConst.MIN_CHOSEN_PLAYER_COUNT) {
            if (!injectToAreaOk(ChosenUserArea.SIDE_A_1ST.index, info))
                injectToAreaOk(ChosenUserArea.SIDE_B_1ST.index, info);
        } else if (PORTRAIT_COUNT == DataConst.MAX_CHOSEN_PLAYER_COUNT) {
            if (!injectToAreaOk(ChosenUserArea.SIDE_A_1ST.index, info))
                if (!injectToAreaOk(ChosenUserArea.SIDE_B_1ST.index, info))
                    if (!injectToAreaOk(ChosenUserArea.SIDE_A_2ND.index, info))
                        injectToAreaOk(ChosenUserArea.SIDE_B_2ND.index, info);
        }
    }

    private boolean injectToAreaOk(int i, UserInfo info) {
        UserInfo ui = mUserCacheManager.parseChosenPlayer(ChosenUserArea.find(i));
        if (ui != null) return false;
        mUserCacheManager.assignChosenPlayer(ChosenUserArea.find(i), info);
        injectUserInfoToView(mPortraitViews[i], info);
        return true;
    }

    private void injectUserInfoToView(PortraitView pv, UserInfo ui) {
        if (pv == null || ui == null) return;
        Glide.with(mContext).load(ui.headImageUrl).into(pv.getPortrait());
        pv.assignNickName(ui.nickName);
        if (mShowExchangeIconWhenLoaded) pv.switchExchangeIconVisibility(View.VISIBLE);
    }

    @Override
    public void loadExchangePage(View triggerView) {
        if (mExchangeLayout == null)
            mExchangeLayout = LayoutInflater.from(mContext).inflate(R.layout.change_player_layout, null, false);
        if (mChangeRecyclerView == null) {
            mChangeRecyclerView = (RecyclerView) mExchangeLayout.findViewById(R.id.player_recycler_view);
        }
        int[] viewLocationInWindow = new int[2];
        triggerView.getLocationInWindow(viewLocationInWindow);
        int screenWith = DisplayUtil.getScreenWidth(mContext);
        boolean reverseOrder = viewLocationInWindow[0] > screenWith >> 1;
        LinearLayoutManager llm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, reverseOrder);
        mChangeRecyclerView.setLayoutManager(llm);
        List<UserInfo> list = new ArrayList<>(5);
        list.add(new UserInfo("A", "fdjljfsfs", "213"));
        list.add(new UserInfo("B", "fdjljfsfs", "2133"));
        list.add(new UserInfo("C", "fdjljfsfs", "2113"));
        mRecyclerViewAdapter = new RecyclerViewAdapter(list, reverseOrder);
        mChangeRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRootVG.addView(mExchangeLayout);
        mExchangeLayout.findViewById(R.id.parent).setOnClickListener((v -> removeLoginPage()));

        int guideLayoutMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp20);
        int triggerViewWidth = triggerView.getWidth();
        int triggerViewHeight = triggerView.getHeight();
        int screenHeight = DisplayUtil.getScreenHeight(mContext, true);
        Point T = new Point(viewLocationInWindow[0] + (triggerViewWidth >> 1), viewLocationInWindow[1] + (triggerViewHeight >> 1));
        Point O = new Point(screenWith >> 1, screenHeight >> 1);
        DisplayUtil.addTaskWithPreDrawListener(mRootVG, () -> {
            int layoutWith = mChangeRecyclerView.getWidth();
            int dx = triggerViewWidth / 2 + guideLayoutMargin + layoutWith / 2;
            mChangeRecyclerView.animate().translationX(T.x - O.x + (reverseOrder ? -1 : 1) * dx).translationY(T.y - O.y).setDuration(0).start();
            // TODO: 2017/3/21 replace the reversed .9 bg file +by KyleCe 2017-3-21 18:53:17
            mChangeRecyclerView.setBackgroundResource(reverseOrder ? R.drawable.change_player_bg : R.drawable.change_player_bg);
        });
    }

    private void assignClickTask(PortraitView... views) {
        for (PortraitView pv : views)
            pv.assignPortraitClickListener((v) -> {
                if (v instanceof PortraitView) {
                    PortraitView p = (PortraitView) v;
                    if (p.isUiCustomed()) loadExchangePage(v);
                    else {
                        if (mLoginPresenter != null)
                            mLoginPresenter.loadLoginPage();
                    }
                }
            });
    }

    @Override
    public void removeLoginPage() {
        mRootVG.removeView(mExchangeLayout);
    }

    @Override
    public void dispose() {
        super.dispose();
        removeLoginPage();
        mContext = null;
        mExchangeLayout = null;
        mChangeRecyclerView = null;
        mRecyclerViewAdapter = null;
    }
}
