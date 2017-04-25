package com.zealens.face.activity.base;

import android.view.View;

import com.zealens.face.account.login.VerifyCallback;
import com.zealens.face.account.login.presenter.LoginPresenter;
import com.zealens.face.account.login.presenter.LoginPresenterImpl;
import com.zealens.face.data.common.DataConst;
import com.zealens.face.data.user.UserCacheManager;
import com.zealens.face.user.UserInfo;

public abstract class LoginBaseActivity extends PortraitBaseActivity implements VerifyCallback {
    protected LoginPresenter mLoginPresenter;

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mLoginPresenter = new LoginPresenterImpl(getCoreContext(), mActivityContent, this);
    }

    @Override
    protected LoginPresenter assembleLoginPresenter() {
        return mLoginPresenter;
    }

    @Override
    public void cancel(Object o) {
        mActivity.runOnUiThread(() -> mLoginPresenter.removeLoginPage());
    }

    @Override
    public void bingo(Object o) {
        mActivity.runOnUiThread(() -> {
            if (o instanceof UserInfo) {
                UserInfo ui = (UserInfo) o;
                int addResult = mUserCacheManager.add(ui);
                if (addResult >= 0) {
                    mLoginPresenter.removeLoginPage();
                    executeTaskIfNotNull(assembleAddOkTask(ui));
                    mLoginPresenter.switchAlreadyLoginVisibility(View.GONE);
                    if (addResult == DataConst.MAX_PLAYER_COUNT) {
                        executeTaskIfNotNull(assembleLoginCountReachMaxTask(ui));
                    }
                } else if (addResult == UserCacheManager.ALREADY_EXIST) {
                    mLoginPresenter.switchAlreadyLoginVisibility(View.VISIBLE);
                    mLoginPresenter.loadLoginPage(true);
                }
                /* UserCacheManager.NO_MORE_SPACE is ignored, because under this condition
               , the login button should never be visible to user*/

                executeTaskIfNotNull(assembleLoginOkEndTask(ui));
            }
        });
    }

    protected Runnable assembleAddOkTask(final UserInfo userInfo) {
        return null;
    }

    protected Runnable assembleLoginCountReachMaxTask(final UserInfo userInfo) {
        return null;
    }

    protected Runnable assembleLoginOkEndTask(final UserInfo userInfo) {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.dispose();
    }
}
