package com.zealens.face.base.fakedialog;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zealens.face.R;
import com.zealens.face.common.ClickCallback;
import com.zealens.face.core.CoreContext;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.util.ViewUtil;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class DialogPresenterImpl extends BasePresenterImpl implements DialogPresenter {
    protected CoreContext mCoreContext;
    protected ViewGroup mRootVG;
    protected ClickCallback mClickCallback;

    protected View mDialogLayout;

    public DialogPresenterImpl(CoreContext coreContext, ViewGroup rootVG, @IntRange(from = 1) @LayoutRes int layoutRes, ClickCallback verifyCallback) {
        this(coreContext, rootVG,
                LayoutInflater.from(coreContext.getApplicationContext()).inflate(layoutRes, null, false), verifyCallback);
    }

    public DialogPresenterImpl(CoreContext coreContext, ViewGroup rootVG, View layoutView, ClickCallback verifyCallback) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mDialogLayout = layoutView;
        mClickCallback = verifyCallback;
    }

    @Override
    public void loadDialogPage() {
        mRootVG.addView(mDialogLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ViewUtil.setClickListener((v -> removeDialogPage()), mDialogLayout.findViewById(R.id.close));
        ViewUtil.setClickListener((v -> {
            removeDialogPage();
            mClickCallback.onYes();
        }), mDialogLayout.findViewById(R.id.yes));
        ViewUtil.setClickListener((v -> {
            removeDialogPage();
            mClickCallback.onNo();
        }), mDialogLayout.findViewById(R.id.no));
    }

    @Override
    public void removeDialogPage() {
        mRootVG.removeView(mDialogLayout);
    }

    public void setClickCallback(ClickCallback mClickCallback) {
        this.mClickCallback = mClickCallback;
    }

    @Override
    public void dispose() {
        super.dispose();
        removeDialogPage();
        mDialogLayout = null;
        mClickCallback = null;
    }
}
