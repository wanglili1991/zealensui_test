package com.zealens.face.activity.base;

import com.zealens.face.account.login.presenter.LoginPresenter;
import com.zealens.face.account.portrait.PortraitViewPresenter;
import com.zealens.face.account.portrait.PortraitViewPresenterImpl;
import com.zealens.face.util.CollectionUtil;

public abstract class PortraitBaseActivity extends ChannelModeBaseActivity {
    protected PortraitViewPresenter mPortraitViewPresenter;

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
    }

    @Override
    protected void assembleDataAfterUiAffairs() {
        super.assembleDataAfterUiAffairs();
        int[] ids = assemblePortraitViewIds();
        if (CollectionUtil.notEmptyArr(ids)) {
            mPortraitViewPresenter = new PortraitViewPresenterImpl(getCoreContext(), mActivityContent
                    , assembleLoginPresenter(), ids, portraitClickable(), showExchangeIconWhenLoaded());
            mPortraitViewPresenter.initialize();
        }
    }

    protected LoginPresenter assembleLoginPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPortraitViewPresenter != null) mPortraitViewPresenter.dispose();
    }
}
