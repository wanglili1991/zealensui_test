package com.zealens.face.account.portrait;

import android.support.annotation.UiThread;
import android.view.View;

import com.zealens.face.presenter.BasePresenter;
import com.zealens.face.user.UserInfo;

/**
 * Created on 2017/3/20
 * in BlaBla by Kyle
 */

public interface PortraitViewPresenter extends BasePresenter {
    void loadExchangePage(View triggerView);

    void removeLoginPage();

    @UiThread
    void injectUserInfoToView();

    @UiThread
    void injectUserInfoToView(UserInfo info);

}
