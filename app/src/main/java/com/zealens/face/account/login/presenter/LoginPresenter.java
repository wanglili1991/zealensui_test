package com.zealens.face.account.login.presenter;

import com.zealens.face.presenter.BasePresenter;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public interface LoginPresenter extends BasePresenter {

    void loadLoginPage();

    void loadLoginPage(boolean reload);

    void switchAlreadyLoginVisibility(int visibility);

    void removeLoginPage();
}
