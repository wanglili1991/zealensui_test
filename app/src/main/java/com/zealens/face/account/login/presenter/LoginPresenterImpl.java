package com.zealens.face.account.login.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zealens.face.R;
import com.zealens.face.account.login.SignInClient;
import com.zealens.face.account.login.VerifyCallback;
import com.zealens.face.common.LoginUrlConst;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.connection.NetworkManager;
import com.zealens.face.presenter.BasePresenterImpl;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class LoginPresenterImpl extends BasePresenterImpl implements LoginPresenter {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;
    private VerifyCallback mVerifyCallback;

    private SignInClient mSignInClient;
    private View mLoginLayout;
    private WebView mLoginWebView;
    private View mNetworkErrorView;
    private View mAlreadyLoginView;

    public LoginPresenterImpl(CoreContext coreContext, ViewGroup rootVG, VerifyCallback verifyCallback) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mVerifyCallback = verifyCallback;
    }

    @Override
    public void loadLoginPage() {
        loadLoginPage(false);
    }

    @Override
    public void loadLoginPage(boolean reload) {
        if (mLoginLayout == null) {
            mLoginLayout = LayoutInflater.from(mCoreContext.getApplicationContext()).inflate(R.layout.login_layout, null, false);
            mLoginLayout.findViewById(R.id.close).setOnClickListener((v) -> removeLoginPage());
            mSignInClient = new SignInClient(mVerifyCallback, mCoreContext);
            mLoginWebView = (WebView) mLoginLayout.findViewById(R.id.log_in_web_view);
            mLoginWebView.setWebViewClient(mSignInClient);
            mLoginWebView.getSettings().setJavaScriptEnabled(true);
            mLoginWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mLoginWebView.setScrollbarFadingEnabled(true);
            mLoginWebView.setVerticalScrollBarEnabled(false);
            mNetworkErrorView = mLoginLayout.findViewById(R.id.network_down_hint);
            mAlreadyLoginView = mLoginLayout.findViewById(R.id.already_login);
        }

        NetworkManager networkManager = (NetworkManager) mCoreContext.getApplicationService(NetworkManager.class);
        boolean connected = networkManager.isConnected(mCoreContext.getBaseContext());
        if (mLoginLayout.getParent() == null)
            mRootVG.addView(mLoginLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mNetworkErrorView.setVisibility(connected ? View.GONE : View.VISIBLE);
        if (!reload) {
            mAlreadyLoginView.setVisibility(View.GONE);
            mLoginWebView.setVisibility(View.INVISIBLE);// show the view when load finish
        }
        if (connected) {
            mLoginWebView.loadUrl(LoginUrlConst.WECHAT_LOGIN_URL);
        }
    }

    @Override
    public void switchAlreadyLoginVisibility(int visibility) {
        mAlreadyLoginView.setVisibility(visibility);
    }

    @Override
    public void removeLoginPage() {
        mLoginWebView.loadUrl(LoginUrlConst.BLANK_URL);
        mLoginWebView.clearCache(true);
        mRootVG.removeView(mLoginLayout);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (mLoginLayout == null) return;
        removeLoginPage();
        mLoginLayout = null;
        mLoginWebView = null;
        mSignInClient = null;
        mNetworkErrorView = null;
    }
}
