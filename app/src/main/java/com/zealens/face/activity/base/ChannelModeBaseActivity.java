package com.zealens.face.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.zealens.face.base.Rule;
import com.zealens.face.common.KeyConst;
import com.zealens.face.common.ResourceConst;
import com.zealens.face.data.user.ChosenUserArea;
import com.zealens.face.data.user.UserCacheManager;
import com.zealens.face.util.ViewUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class ChannelModeBaseActivity extends BaseActivity {

    @Rule.ChannelMode
    protected int mChannelMode;
    protected UserCacheManager mUserCacheManager;

    @SuppressWarnings("MagicConstant")
    @Override
    protected void parseNonNullBundle(Bundle bundle) {
        mChannelMode = bundle.getInt(KeyConst.CHANNEL_MODE, Rule.ChannelMode.IGNORE);
    }

    @Override
    protected void initDataIgnoringUi() {
        super.initDataIgnoringUi();
        if(mChannelMode == Rule.ChannelMode.IGNORE)
            mChannelMode = parseMode();
        mUserCacheManager = (UserCacheManager) getCoreContext().getApplicationService(UserCacheManager.class);
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        if (mChannelMode == Rule.ChannelMode.SINGLE_MATCH || mChannelMode == Rule.ChannelMode.TRAINING) {
            ViewUtil.hide(findViewById(ResourceConst.PLAYER_IDS[ChosenUserArea.SIDE_A_2ND.index])
                    , findViewById(ResourceConst.PLAYER_IDS[ChosenUserArea.SIDE_B_2ND.index]));
        }
    }

    @Rule.ChannelMode
    protected int parseMode() {
        return Rule.ChannelMode.IGNORE;
    }

    protected int[] assemblePortraitViewIds() {
        @Rule.ChannelMode
        int mode = parseMode();
        if (mode == Rule.ChannelMode.IGNORE) return null;
        return mode == Rule.ChannelMode.DOUBLE_MATCH ? ResourceConst.PLAYER_IDS
                : new int[]{ResourceConst.PLAYER_IDS[0], ResourceConst.PLAYER_IDS[1]};
    }

    protected boolean portraitClickable() {
        return false;
    }

    protected boolean showExchangeIconWhenLoaded() {
        return false;
    }

    public void startActivity(Class clz, Bundle bundle, int flags, boolean clearOtherFlags) {
        if (bundle == null) bundle = new Bundle();
        bundle.putInt(KeyConst.CHANNEL_MODE, mChannelMode);
        Intent intent = new Intent(mContext, clz).putExtras(bundle);
        if (flags != ResourceConst.IGNORE_FLAG) {
            if (clearOtherFlags) {
                intent.setFlags(flags);
            } else {
                intent.addFlags(flags);
            }
        }
        startActivity(intent);
    }

    public void triggerChannelDistinguishActivity(Class clz, @Rule.ChannelMode int mode) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(KeyConst.CHANNEL_MODE, mode);
        startActivityWithData(clz, map);
    }
}
