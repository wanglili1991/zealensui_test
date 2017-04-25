package com.zealens.face.activity.common;

import android.widget.RadioGroup;

import com.zealens.face.R;
import com.zealens.face.activity.base.ChannelModeBaseActivity;
import com.zealens.face.base.Rule;
import com.zealens.face.domain.module.BasePoint;
import com.zealens.face.domain.module.Point2D;
import com.zealens.face.domain.module.Point3D;
import com.zealens.face.domain.umpire.FunctionToolBox;
import com.zealens.face.domain.umpire.UmpireOperator;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.util.ViewUtil;
import com.zealens.face.view.BallCastPointAnalyzeView;

import java.util.ArrayList;
import java.util.List;

public class SkillAnalyzeActivity extends ChannelModeBaseActivity {
    private Point2D[][] mReceiveHits;
    private Point3D[][] mServeFallHits;
    private BallCastPointAnalyzeView mReceiveAnalyzeView;
    private BallCastPointAnalyzeView mServeAnalyzeView;

    @Override
    protected void initDataIgnoringUi() {
        super.initDataIgnoringUi();
        FunctionToolBox toolBox = (UmpireOperator) getCoreContext().getApplicationService(UmpireManagerAppLevel.class);
        mReceiveHits = toolBox.getReceiveHits();
        mServeFallHits = toolBox.getServeFallPoints();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_skill_analyze;
    }

    @Override
    protected void viewAffairs() {
        super.viewAffairs();
        mReceiveAnalyzeView = (BallCastPointAnalyzeView) findViewById(R.id.cast_scene_with_ruler);
        mServeAnalyzeView = (BallCastPointAnalyzeView) findViewById(R.id.cast_scene_without_ruler);

        RadioGroup players = (RadioGroup) findViewById(R.id.player_radio_group);
        players.setOnCheckedChangeListener((radioGroupF, i) -> {
                    int team = Rule.Team.TAN;
                    switch (radioGroupF.getCheckedRadioButtonId()) {
                        default:
                            break;
                        case R.id.player_two:
                            team = Rule.Team.RED;
                            break;
                    }
                    paintReceiveAndServe(team);
                }
        );
        paintReceiveAndServe(Rule.Team.TAN);
        if (mChannelMode != Rule.ChannelMode.SINGLE_MATCH && mChannelMode != Rule.ChannelMode.DOUBLE_MATCH)
            ViewUtil.invisible(findViewById(R.id.receive_points_illustrate));
    }

    private void paintReceiveAndServe(int team) {
        BasePoint[] receivePoints = mReceiveHits[team];
        List<BasePoint> receiveList = new ArrayList<>();
        for (BasePoint p : receivePoints) {
            if (p == null) continue;
            receiveList.add(p);
        }
        mReceiveAnalyzeView.paintReceiveDots(receiveList);

        Point3D[] points = mServeFallHits[team];
        List<Point3D> serveList = new ArrayList<>();
        for (Point3D p : points) {
            if (p == null) continue;
            serveList.add(p);
        }
        mServeAnalyzeView.paintServeDots(serveList);
    }
}
