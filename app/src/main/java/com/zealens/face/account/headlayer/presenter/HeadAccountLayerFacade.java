package com.zealens.face.account.headlayer.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zealens.face.R;
import com.zealens.face.account.login.presenter.LoginPresenter;
import com.zealens.face.activity.HomeActivity;
import com.zealens.face.activity.base.BaseActivity;
import com.zealens.face.base.fakedialog.DialogPresenter;
import com.zealens.face.base.fakedialog.DialogPresenterImpl;
import com.zealens.face.common.ClickCallback;
import com.zealens.face.core.CoreContext;
import com.zealens.face.data.user.UserCacheManager;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.user.UserInfo;
import com.zealens.face.util.ViewUtil;
import com.zealens.face.view.RoundImageView;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class HeadAccountLayerFacade extends BasePresenterImpl implements HeadAccountLayerPresenter, ClickCallback {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;

    private View mHeadAccountLayer;
    private LoginPresenter mLoginPresenter;
    private LinearLayout mPortraitContainer;
    private DialogPresenter mLogoutPresenter;
    private Activity mActivity;
    private UserCacheManager mUserCacheManger;
    private View mLogin;
    private View mLogout;

    public HeadAccountLayerFacade(CoreContext coreContext, ViewGroup rootVG, Activity activity, @NonNull LoginPresenter dialogPresenter) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mActivity = activity;
        mLoginPresenter = dialogPresenter;
    }

    @Override
    public void loadHeadLayer() {
        mUserCacheManger = (UserCacheManager) mCoreContext.getApplicationService(UserCacheManager.class);

        if (mHeadAccountLayer == null)
            mHeadAccountLayer = LayoutInflater.from(mCoreContext.getApplicationContext()).inflate(R.layout.head_account_layer_layout, null, false);

        mLogin = mHeadAccountLayer.findViewById(R.id.log_in);
        mLogin.setOnClickListener((v) -> loginClicked());

        mLogout = mHeadAccountLayer.findViewById(R.id.log_out);
        mLogout.setOnClickListener((v) -> {
            if (mPortraitContainer.getChildCount() != 0)
                mLogoutPresenter.loadDialogPage();
        });
        mPortraitContainer = (LinearLayout) mHeadAccountLayer.findViewById(R.id.portrait_container);
        mLogoutPresenter = new DialogPresenterImpl(mCoreContext, mRootVG, R.layout.logout_layout, this);

        ContentFrameLayout.LayoutParams lp = new ContentFrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = ContentFrameLayout.LayoutParams.MATCH_PARENT;
        lp.height = mCoreContext.getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.head_account_layer_height);
        lp.gravity = Gravity.TOP;
        mRootVG.addView(mHeadAccountLayer, lp);
    }

    @Override
    public void onResume() {
        UserInfo[] userInfos = mUserCacheManger.getAll();
        int userCount = mUserCacheManger.getUserLoginCount();
        mPortraitContainer.removeAllViews();
        if (userCount == 0) {
            ViewUtil.hide(mLogout);
            return;
        }
        for (UserInfo ui : userInfos) if (ui != null) fetchUserIcon(ui);
    }

    private void loginClicked() {
        mLoginPresenter.loadLoginPage();
    }

    @Override
    public void removeHeadLayer() {
        mRootVG.removeView(mHeadAccountLayer);
    }

    @Override
    public void dispose() {
        super.dispose();
        removeHeadLayer();
        mHeadAccountLayer = null;
        mPortraitContainer = null;
        mLogoutPresenter.dispose();
        mLogoutPresenter = null;
        mActivity = null;
    }

    @Override
    public void fetchUserIcon(UserInfo ui) {
        String url = ui.headImageUrl;
        url = url.replace("/0", "/96");
        RoundImageView iv = new RoundImageView(mCoreContext.getApplicationContext());
        int size = mCoreContext.getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.dp80);
        int margin = mCoreContext.getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.dp10);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        lp.gravity = Gravity.CENTER_VERTICAL;
        lp.setMargins(margin, margin, margin, margin);
        mPortraitContainer.addView(iv, lp);
        Glide.with(mActivity).load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                        ViewUtil.show(mLogout);
                        return false;
                    }
                })
                .into(iv);
    }

    @Override
    public void switchLoginVisibility(int visible) {
        mLogin.setVisibility(visible);
    }

    @Override
    public void onYes() {
        mPortraitContainer.removeAllViews();
        mUserCacheManger.resetAll();
        if (!(mActivity instanceof HomeActivity)) {
            ActivityCompat.finishAffinity(mActivity);
            ((BaseActivity) mActivity).startActivity(HomeActivity.class);
        }
    }

    @Override
    public void onNo() {

    }

    @Override
    public void onClose() {

    }
}
