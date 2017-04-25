package com.zealens.face.base.scorevideopanel;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.zealens.face.R;
import com.zealens.face.adapter.VideoPanelRecyclerViewAdapter;
import com.zealens.face.base.Rule;
import com.zealens.face.core.CoreContext;
import com.zealens.face.domain.umpire.UmpireOperator;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.video.presenter.VideoPlayerPresenter;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class ControlPanelPresenterImpl extends BasePresenterImpl implements VideoPanelPresenter {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;
    private VideoPlayerPresenter mVideoPlayerPresenter;

    private RecyclerView mVideoPanelRV;
    private UmpireOperator mUmpireOperator;
    @Rule.ChannelMode
    private int mChannelMode;
    private boolean isInTrainingChannel;

    public ControlPanelPresenterImpl(CoreContext coreContext, ViewGroup rootVG
            , VideoPlayerPresenter verifyCallback, @Rule.ChannelMode int channelMode) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mVideoPlayerPresenter = verifyCallback;
        mChannelMode = channelMode;
        isInTrainingChannel = mChannelMode == Rule.ChannelMode.TRAINING;
        mUmpireOperator = (UmpireOperator) coreContext.getApplicationService(UmpireManagerAppLevel.class);
    }

    @Override
    public void initialize() {
        super.initialize();
        boolean splitSection = !isInTrainingChannel;
        mVideoPanelRV = (RecyclerView) mRootVG.findViewById(R.id.video_recycler_view);
        mVideoPanelRV.setLayoutManager(new LinearLayoutManager(mCoreContext.getApplicationContext()));
        VideoPanelRecyclerViewAdapter adapter = new VideoPanelRecyclerViewAdapter(mCoreContext.getApplicationContext()
                , mVideoPlayerPresenter, mUmpireOperator.getScore(), mUmpireOperator.getAllVideo(Rule.Team.TAN), splitSection);
        mVideoPanelRV.setAdapter(adapter);

        RadioGroup players = (RadioGroup) mRootVG.findViewById(R.id.player_radio_group);
        players.setOnCheckedChangeListener((radioGroupF, id) -> {
                    int team;
                    switch (id) {
                        default:
                            team = Rule.Team.TAN;
                            break;
                        case R.id.player_two:
                            team = Rule.Team.RED;
                            break;
                    }
                    adapter.assignVideos(mUmpireOperator.getAllVideo(team));
                    adapter.notifyDataSetChanged();
                    mVideoPlayerPresenter.refreshWithFirstItem(team);
                }
        );
        players.check(R.id.player_one);
        if (isInTrainingChannel) {
            mRootVG.findViewById(R.id.player_one).setBackgroundColor(Color.TRANSPARENT);
            mRootVG.findViewById(R.id.player_two).setVisibility(View.GONE);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        mVideoPanelRV = null;
    }
}
