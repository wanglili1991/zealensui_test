package com.zealens.face.activity.base;

import android.view.View;

import com.zealens.face.account.headlayer.presenter.HeadAccountLayerFacade;
import com.zealens.face.account.headlayer.presenter.HeadAccountLayerPresenter;
import com.zealens.face.base.Rule;
import com.zealens.face.common.SimpleCallbackWithArg;
import com.zealens.face.user.UserInfo;

public abstract class HeadAccountLayerBaseActivity extends LoginBaseActivity implements SimpleCallbackWithArg {
    private HeadAccountLayerPresenter mHeadAccountLayerPresenter;

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mHeadAccountLayerPresenter = new HeadAccountLayerFacade(getCoreContext(), mActivityContent, this, mLoginPresenter);
        mHeadAccountLayerPresenter.loadHeadLayer();
    }

    @Override
    public void onRefresh(Object obj) {
        if (obj instanceof UserInfo) {
            if (parseMode() == Rule.ChannelMode.IGNORE) return;
            if (mPortraitViewPresenter != null)
                mPortraitViewPresenter.injectUserInfoToView((UserInfo) obj);
        }
    }

    @Override
    protected Runnable assembleAddOkTask(final UserInfo userInfo) {
        return () -> mHeadAccountLayerPresenter.fetchUserIcon(userInfo);
    }

    @Override
    protected Runnable assembleLoginCountReachMaxTask(final UserInfo userInfo) {
        return () -> mHeadAccountLayerPresenter.switchLoginVisibility(View.GONE);
    }

    @Override
    protected Runnable assembleLoginOkEndTask(final UserInfo userInfo) {
        return () -> onRefresh(userInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHeadAccountLayerPresenter.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHeadAccountLayerPresenter.dispose();
    }

}
