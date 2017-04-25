package com.zealens.face.presenter.train.zone;

import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.activity.base.BaseActivity;
import com.zealens.face.activity.base.ChannelModeBaseActivity;
import com.zealens.face.activity.train.TrainScoreBoardActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.common.KeyConst;
import com.zealens.face.core.CoreContext;
import com.zealens.face.presenter.BasePresenterImpl;
import com.zealens.face.util.ViewUtil;
import com.zealens.face.view.GroundView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public class ZoneShowPresenterImpl extends BasePresenterImpl implements ZoneShowPresenter {
    private CoreContext mCoreContext;
    private ViewGroup mRootVG;

    private GroundView mPlaygroundView;

    private final int[][] RECEIVE_RESOURCES_ARR = {
            {GroundView.Zoo.side_b_whole,},
            {GroundView.Zoo.side_b_backyard,},
            {GroundView.Zoo.side_b_backyard_half,},
    };

    private final int[][] FOREHAND_DOWN_THE_LINE_RESOURCES_ARR = {
            {GroundView.Zoo.player_backyard_half_yellow, GroundView.Zoo.player_backyard_half_blue_bottom
                    , GroundView.Zoo.side_b_whole_top, GroundView.Zoo.side_b_whole_bottom},
            {GroundView.Zoo.player_backyard_half_yellow, GroundView.Zoo.player_backyard_half_blue_bottom
                    , GroundView.Zoo.side_b_backyard_top, GroundView.Zoo.side_b_backyard_bottom},
            {GroundView.Zoo.player_backyard_half_yellow, GroundView.Zoo.player_backyard_half_blue_bottom
                    , GroundView.Zoo.side_b_backyard_top_half_yellow, GroundView.Zoo.side_b_backyard_bottom_half_blue},
    };

    private final int[][] FOREHAND_CROSS_THE_COURT_RESOURCES_ARR = {
            {GroundView.Zoo.player_backyard_half_blue, GroundView.Zoo.player_backyard_half_yellow_bottom
                    , GroundView.Zoo.side_b_whole_top, GroundView.Zoo.side_b_whole_bottom},
            {GroundView.Zoo.player_backyard_half_blue, GroundView.Zoo.player_backyard_half_yellow_bottom
                    , GroundView.Zoo.side_b_backyard_top, GroundView.Zoo.side_b_backyard_bottom},
            {GroundView.Zoo.player_backyard_half_blue, GroundView.Zoo.player_backyard_half_yellow_bottom
                    , GroundView.Zoo.side_b_backyard_top_half_yellow, GroundView.Zoo.side_b_backyard_bottom_half_blue},
    };

    /**
     * int [a][b][c]
     *
     * @see Rule.TrainMode for index a
     * @see Rule.Level for index b
     * @see com.zealens.face.view.GroundView.Zoo array for c
     */
    @GroundView.Zoo
    private final int[][][] TARGET_TO_SHOW_RES = {{
            {GroundView.Zoo.player_blue_bottom_line, GroundView.Zoo.player_yellow_bottom_line
                    , GroundView.Zoo.side_b_service_area_top, GroundView.Zoo.side_b_service_area_bottom,},
            {GroundView.Zoo.player_blue_bottom_line, GroundView.Zoo.player_yellow_bottom_line
                    , GroundView.Zoo.side_b_service_area_top_inner, GroundView.Zoo.side_b_service_area_bottom_inner,},
            {GroundView.Zoo.player_blue_bottom_line, GroundView.Zoo.player_yellow_bottom_line
                    , GroundView.Zoo.side_b_service_area_top_outer, GroundView.Zoo.side_b_service_area_bottom_outer,},},/*service */
            RECEIVE_RESOURCES_ARR,/*alternate_service */
            RECEIVE_RESOURCES_ARR,/*forehand */
            FOREHAND_DOWN_THE_LINE_RESOURCES_ARR,/*forehand_down_the_line */
            FOREHAND_CROSS_THE_COURT_RESOURCES_ARR,/*forehand_cross_court */
            RECEIVE_RESOURCES_ARR,/*backhand */
            FOREHAND_DOWN_THE_LINE_RESOURCES_ARR,/*backhand_down_the_line */
            FOREHAND_CROSS_THE_COURT_RESOURCES_ARR,/*backhand_cross_court */
            RECEIVE_RESOURCES_ARR,/*volley */
            RECEIVE_RESOURCES_ARR,/*free_style */
            RECEIVE_RESOURCES_ARR,/*multiple_balls */
    };

    @Rule.TrainMode
    private int mTrainMode;
    private boolean mLevelChosenAlready;
    private RadioGroup mLevelSelector;
    private RadioGroup mBreakModeSelector;
    @Rule.Level
    private int mChosenLevel;
    @Rule.BreakMode
    private int mBreakStyle;
    private BaseActivity mBaseActivity;

    public ZoneShowPresenterImpl(CoreContext coreContext, ViewGroup rootVG, @Rule.TrainMode int mode, BaseActivity activity) {
        mCoreContext = coreContext;
        mRootVG = rootVG;
        mTrainMode = mode;
        mBaseActivity = activity;
    }

    @Override
    public void initialize() {
        super.initialize();

        mPlaygroundView = (GroundView) mRootVG.findViewById(R.id.playground_view);

        mLevelSelector = (RadioGroup) mRootVG.findViewById(R.id.level_radio_group);
        mLevelSelector.setOnCheckedChangeListener((radioGroupF, i) -> {
                    switch (radioGroupF.getCheckedRadioButtonId()) {
                        case R.id.level_easy:
                        default:
                            mChosenLevel = Rule.Level.EASY;
                            break;
                        case R.id.level_middle:
                            mChosenLevel = Rule.Level.MEDIUM;
                            break;
                        case R.id.level_hard:
                            mChosenLevel = Rule.Level.HARD;
                    }
            modeDisplay(mTrainMode, mChosenLevel);
                }
        );
        mLevelSelector.check(R.id.level_easy);

        int len = Rule.Level.class.getFields().length;
        TextView[] textViews = new TextView[len];
        textViews[Rule.Level.EASY] = (TextView) mRootVG.findViewById(R.id.level_easy);
        textViews[Rule.Level.MEDIUM] = (TextView) mRootVG.findViewById(R.id.level_middle);
        textViews[Rule.Level.HARD] = (TextView) mRootVG.findViewById(R.id.level_hard);
        String[] strArr = mCoreContext.getBaseContext().getResources().getStringArray(R.array.TrainModeLevel);
        String[] strArrForService = mCoreContext.getBaseContext().getResources().getStringArray(R.array.TrainModeLevelForService);
        for (int i = 0; i < len; i++)
            textViews[i].setText(mTrainMode == Rule.TrainMode.SERVICE ? strArrForService[i] : strArr[i]);

        mBreakModeSelector = (RadioGroup) mRootVG.findViewById(R.id.break_mode_chooser);
        mBreakModeSelector.check(R.id.four_minute);
        mBreakModeSelector.setOnCheckedChangeListener((radioGroupF, i) -> {
                    switch (radioGroupF.getCheckedRadioButtonId()) {
                        case R.id.four_minute:
                        default:
                            mBreakStyle = Rule.BreakMode.FOUR_MINUTES;
                            break;
                        case R.id.hits:
                            mBreakStyle = Rule.BreakMode.TWENTY_HITS;
                            break;
                        case R.id.free_style:
                            mBreakStyle = Rule.BreakMode.FREE_STYLE;
                            break;
                    }
                }
        );
    }

    @Override
    public Runnable assembleControlUnitResponse() {
        return () -> {
            if (!mLevelChosenAlready) {
                mLevelChosenAlready = true;
                ViewUtil.hide(mLevelSelector, mPlaygroundView);
                ViewUtil.show(mBreakModeSelector);
            } else {
                if (mBaseActivity instanceof ChannelModeBaseActivity) {
                    Map<String, Object> map = new HashMap<>(2);
                    map.put(KeyConst.CHANNEL_MODE, Rule.ChannelMode.TRAINING);
                    /*keep array order in Rule.TrainParamsIndex*/
                    map.put(KeyConst.TRAIN_ARRAY, new int[]{mTrainMode, mChosenLevel, mBreakStyle});
                    mBaseActivity.startActivityWithData(TrainScoreBoardActivity.class, map);
                }
            }
        };
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void modeDisplay(@Rule.TrainMode int mode, @Rule.Level int level) {
        mPlaygroundView.modeDisplay(TARGET_TO_SHOW_RES[mode][level]);
    }

    @Override
    public void dispose() {
        super.dispose();
        mPlaygroundView = null;
        mLevelSelector = null;
        mBreakModeSelector = null;
    }
}
