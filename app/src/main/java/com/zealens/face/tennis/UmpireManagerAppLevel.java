package com.zealens.face.tennis;

import android.os.Environment;

import com.zealens.face.base.Rule;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.internal.IPCameraPresenter;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.core.internal.TennisBout;
import com.zealens.face.core.internal.TennisDrill;
import com.zealens.face.domain.coach.FakeIPCameraImpl;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.umpire.UmpireManager;

import java.util.Map;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by KyleCe on 30/03/2017
 */

public class UmpireManagerAppLevel extends UmpireManager {
    private CoreContext mCoreContext;

    public UmpireManagerAppLevel(CoreContext coreContext) {
        super(new IPCameraPresenter[]{new FakeIPCameraImpl(Environment.getExternalStorageDirectory())
                , new FakeIPCameraImpl(Environment.getExternalStorageDirectory())});
        mCoreContext = coreContext;
        mPathPrefix = Environment.getExternalStorageDirectory();
    }

    @Override
    public void assignComponentsByPlayModel(@TennisBase.PlayModel int model) {
        assignComponentsByPlayModel(model, TennisBase.DrillDifficulty.UNKNOWN);
    }

    @Override
    public void assignComponentsByPlayModel(@TennisBase.PlayModel int model
            , @TennisBase.DrillDifficulty int difficulty) {
        assignComponentsByPlayModel(model, difficulty, Rule.BreakMode.FREE_STYLE);
    }

    @Override
    public void assignComponentsByPlayModel(@TennisBase.PlayModel int model
            , @TennisBase.DrillDifficulty int difficulty, @Rule.BreakMode int breakMode) {
        switch (model) {
            case TennisBase.PlayModel.MATCH_SINGLE:
            case TennisBase.PlayModel.MATCH_DOUBLE:
                TennisBout bout = new TennisBout();
                TennisBase.InitParam param = UmpireManagerHelper.generateInitBout(
                        mCoreContext.getApplicationContext(), true);
                assignComponent(bout, param);
                break;
            default:
                assertNotEquals(TennisBase.DrillDifficulty.UNKNOWN, difficulty);
                TennisDrill drill = new TennisDrill();
                TennisBase.DrillInitParam drillParams;
                TennisBase.InitParam initParam = UmpireManagerHelper.generateInitBout(
                        mCoreContext.getApplicationContext());
                drillParams = new TennisBase.DrillInitParam(initParam, difficulty, breakMode);
                drillParams.playModel = model;
                assignComponent(drill, drillParams);
                break;
            case TennisBase.PlayModel.PLAYMODEL_MASK:
            case TennisBase.PlayModel.DRILL_MODEL_MASK:
                throw new IllegalArgumentException("Unsupported model");
        }
    }

    @Override
    protected void videoFileAndNoteFileProcess(int tag, Score score, long lastingTime, String oldName
            , Map<String, String> map, int side, @Rule.Team int team) {
        mCoreContext.postTask(() ->
                super.videoFileAndNoteFileProcess(tag, score, lastingTime, oldName, map, side, team));
    }
}
