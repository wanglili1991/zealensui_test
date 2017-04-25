package com.zealens.face.activity;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadAccountLayerBaseActivity;
import com.zealens.face.activity.match.MatchModeChooseActivity;
import com.zealens.face.activity.train.TrainModeChooseActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.common.DebugConst;

public class HomeActivity extends HeadAccountLayerBaseActivity {
    @Override
    protected void initDataIgnoringUi() {
        super.initDataIgnoringUi();
        if (DebugConst.GUIDE_TO_DEBUG_ACTIVITY) {
            startActivity(DebugConst.DEBUGGING_CLASS);
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        findViewById(R.id.single_match).setOnClickListener((v) -> triggerChannelDistinguishActivity(
                MatchModeChooseActivity.class, Rule.ChannelMode.SINGLE_MATCH));
        findViewById(R.id.double_match).setOnClickListener((v) -> triggerChannelDistinguishActivity(
                MatchModeChooseActivity.class, Rule.ChannelMode.DOUBLE_MATCH));
        findViewById(R.id.train).setOnClickListener((v) -> startActivity(TrainModeChooseActivity.class));
    }

    @Override
    protected boolean showBackPressButton() {
        return false;
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
