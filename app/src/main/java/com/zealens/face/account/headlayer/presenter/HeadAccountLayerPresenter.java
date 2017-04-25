package com.zealens.face.account.headlayer.presenter;

import com.zealens.face.presenter.BasePresenter;
import com.zealens.face.user.UserInfo;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public interface HeadAccountLayerPresenter extends BasePresenter {

    void loadHeadLayer();

    void onResume();

    void removeHeadLayer();

    void fetchUserIcon(UserInfo ui);

    void switchLoginVisibility(int visible);
}
