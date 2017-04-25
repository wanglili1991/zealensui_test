package com.zealens.face.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.util.ViewUtil;

/**
 * Created on 2016/11/22
 * in BlaBla by Kyle
 */

public class PortraitView extends FrameLayout {
    @IntDef({Direction.left, Direction.top, Direction.right, Direction.bottom})
    public @interface Direction {
        int left = 0;
        int top = 1;
        int right = 2;
        int bottom = 3;
    }

    @Direction
    private int mDescriptionLocation = Direction.bottom;
    private boolean mShowExchangeIcon;
    private ImageView mPortrait;
    private TextView mNickName;
    private TextView mDescription;
    private View mExchangeView;
    private boolean mUiCustomed;

    public PortraitView(Context context) {
        super(context, null);
        init(context, null, 0);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PortraitView, defStyleAttr, 0);
            mDescriptionLocation = ta.getInteger(R.styleable.PortraitView_description_direction, Direction.bottom);
            mShowExchangeIcon = ta.getBoolean(R.styleable.PortraitView_exchange_icon_on, false);
            ta.recycle();
        }
        @LayoutRes
        int layoutId;
        switch (mDescriptionLocation) {
            case Direction.left:
                layoutId = R.layout.portrait_layout_description_left;
                break;
            case Direction.top:
                layoutId = R.layout.portrait_layout_description_top;
                break;
            case Direction.right:
                layoutId = R.layout.portrait_layout_description_right;
                break;
            default:
            case Direction.bottom:
                layoutId = R.layout.portrait_layout_description_bottom;
                break;
        }
        View base = inflate(context, layoutId, this);
        mPortrait = (ImageView) base.findViewById(R.id.portrait);
        mNickName = (TextView) base.findViewById(R.id.nick_name);
        mDescription = (TextView) base.findViewById(R.id.status);
        mExchangeView = base.findViewById(R.id.exchange_player);
        ViewUtil.setClickListener(v -> this.performClick(), mPortrait, mExchangeView);
        if (mShowExchangeIcon) mExchangeView.setVisibility(View.VISIBLE);
    }

    public void assignPortraitClickListener(OnClickListener portraitClickListener) {
        setOnClickListener(portraitClickListener);
    }

    public void assignNickName(String desc) {
        mNickName.setText(desc);
        mUiCustomed = true;
    }

    public void assignNickName(@StringRes int res) {
        mNickName.setText(res);
        mUiCustomed = true;
    }

    public void assignDescription(String desc) {
        mDescription.setText(desc);
        mUiCustomed = true;
    }

    public void assignDescription(@StringRes int res) {
        mDescription.setText(res);
        mUiCustomed = true;
    }

    public ImageView getPortrait() {
        return mPortrait;
    }

    public void switchExchangeIconVisibility(int visibility) {
        mExchangeView.setVisibility(visibility);
        mUiCustomed = true;
    }

    public void clearUiData() {
        mPortrait.setImageResource(android.R.color.transparent);
        mPortrait.setBackgroundResource(R.drawable.button_effect_add_player);
        mNickName.setText(getResources().getString(R.string.log_in));
        mDescription.setText(getResources().getString(R.string.blank));
        mUiCustomed = false;
    }

    public boolean isUiCustomed() {
        return mUiCustomed;
    }
}
