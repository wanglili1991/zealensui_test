package com.zealens.face.activity.match;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.zealens.face.R;
import com.zealens.face.activity.base.HeadAccountLayerBaseActivity;
import com.zealens.face.adapter.ViewPagerAdapter;
import com.zealens.face.base.Rule;
import com.zealens.face.common.KeyConst;
import com.zealens.face.view.PortraitView;

public class MatchModeChooseActivity extends HeadAccountLayerBaseActivity {
    private ViewPager mImageViewPager;
    private ViewPagerAdapter mPagerAdapter;
    private PortraitView[] mPortraitView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_match_mode_choose;
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void initDataIgnoringUi() {
        super.initDataIgnoringUi();
        mPortraitView = new PortraitView[4];
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        findViewById(R.id.start).setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(KeyConst.CHANNEL_MODE, mChannelMode);
            startActivity(MatchScoreBoardActivity.class, bundle);
        });

        mImageViewPager = (ViewPager) findViewById(R.id.mode_chooser);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mImageViewPager, true);
        String[] modeArr = getResources().getStringArray(R.array.GameMode);
        mPagerAdapter = new ViewPagerAdapter(modeArr);
        mImageViewPager.setAdapter(mPagerAdapter);

        findViewById(R.id.mode_chooser_control_left).setOnClickListener((v -> {
            int current = mImageViewPager.getCurrentItem();
            int target = 0;
            if (current == 0) target = modeArr.length - 1;
            else target = current - 1;
            mImageViewPager.setCurrentItem(target, true);
        }));

        findViewById(R.id.mode_chooser_control_right).setOnClickListener((v -> {
            int current = mImageViewPager.getCurrentItem();
            int target = 0;
            if (current == modeArr.length - 1) target = 0;
            else target = current + 1;
            mImageViewPager.setCurrentItem(target, true);
        }));
    }

    @Override
    protected boolean portraitClickable() {
        return true;
    }

    @Override
    protected boolean showExchangeIconWhenLoaded() {
        return true;
    }

    @Rule.ChannelMode
    protected int parseMode() {
        return mChannelMode;
    }
}
