package com.zealens.face.umpire;

import com.zealens.face.base.Rule;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.DomainConst;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.umpire.UmpireDelegate;
import com.zealens.face.domain.umpire.UmpireManager;
import com.zealens.face.umpire.fake.FakeUmpireCallback;
import com.zealens.face.umpire.stub.IPCameraPresenterStub;
import com.zealens.face.umpire.stub.TennisBoutPresenterStub;
import com.zealens.face.util.ApiFromAndroid;
import com.zealens.face.util.CollectionUtil;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static com.zealens.face.util.LogcatUtil.sop;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.spy;

/**
 * Created on 2017/3/26
 * in BlaBla by Kyle
 */

public class UmpireTest {
    UmpireManager mUmpireManagerSpy;
    TennisBoutPresenterStub mTennisBoutSpy;
    private boolean[] TEAM_TAN_WIN_ARR = new boolean[]{false, false, true, true, true, true};
    private boolean[] TEAM_RED_WIN_ARR = new boolean[]{true, true, false, false, false, false};

    @Before
    public void setupUmpire() {
        TennisBoutPresenterStub tennisBoutStub = new TennisBoutPresenterStub();
        mTennisBoutSpy = spy(tennisBoutStub);
        IPCameraPresenterStub stub = new IPCameraPresenterStub();
        IPCameraPresenterStub stubB = new IPCameraPresenterStub();
        IPCameraPresenterStub mIPCameraPresenter = spy(stub);
        IPCameraPresenterStub mIPCameraPresenterB = spy(stubB);
        IPCameraPresenterStub[] ipCameras = new IPCameraPresenterStub[]{mIPCameraPresenter, mIPCameraPresenterB};
        UmpireManager umpireManager = new UmpireManager(ipCameras);
        umpireManager.assignComponent(mTennisBoutSpy, new TennisBase.InitParam());
        mUmpireManagerSpy = spy(umpireManager);
        setupUmpireSpy();
    }

    public void setupUmpireSpy() {
        mUmpireManagerSpy.setMatchMode(Rule.MatchMode.TIE_BREAK_T6);
        mUmpireManagerSpy.assignUmpireCallback(new FakeUmpireCallback());
        mUmpireManagerSpy.initialize();
        sop("init spy");
    }

    @Test
    public void scoreInString() throws Exception {
        boutScore();
        String scoreStr = mUmpireManagerSpy.getScore().getScoreInString();
        sop(scoreStr);
        assertTrue(scoreStr.contains(DomainConst.SCORE_TAG_BOUT));
    }

    @Test
    public void boutScore() {
        int[][] total = mUmpireManagerSpy.getScore().total;
        mTennisBoutSpy.appendBout(true);
        assertEquals(concatABScore(total, Rule.ScoreType.BOUT), "1:0");
        mTennisBoutSpy.appendBout(new boolean[]{true, true, false, false, false, true});
        assertEquals(concatABScore(total, Rule.ScoreType.BOUT), "4:3");
        mTennisBoutSpy.appendBout(false);
        mTennisBoutSpy.appendBout(true);
        mTennisBoutSpy.appendBout(false);
        assertEquals(concatABScore(total, Rule.ScoreType.BOUT), "5:5");
        mTennisBoutSpy.appendBout(true);
        mTennisBoutSpy.appendBout(false);
        mTennisBoutSpy.appendBout(true);
        mTennisBoutSpy.appendBout(false);
        mTennisBoutSpy.appendBout(true);
        mTennisBoutSpy.appendBout(false);
        assertEquals(concatABScore(total, Rule.ScoreType.BOUT), "8:8");
        mTennisBoutSpy.appendBout(true);
        mTennisBoutSpy.appendBout(true);
        assertEquals(concatABScore(total, Rule.ScoreType.BOUT), "0:0");
        assertEquals(concatABScore(total, Rule.ScoreType.GAME), "1:0");
    }

    @Test
    public void gameScoreModeTieBreak6() {
        int[][] total = mUmpireManagerSpy.getScore().total;
        testTie(6);
        assertEquals(concatABScore(total, Rule.ScoreType.GAME), String.valueOf(6) + ":" + String.valueOf(6));
        letWinGameSeveralTimes(mTennisBoutSpy);
        assertEquals(concatABScore(total, Rule.ScoreType.GAME), "0:0");
        assertEquals(concatABScore(total, Rule.ScoreType.SET), "1:0");
//        letWinGameSeveralTimes(Rule.Side.b, mTennisBoutSpy);
//        assertEquals(concatABScore(total, Rule.ScoreType.GAME), "0:0");
//        assertEquals(concatABScore(total, Rule.ScoreType.SET), "0:1");
    }

    @Test
    public void gameScoreModeTieBreak4() {
        mUmpireManagerSpy.reset();
        int[][] total = mUmpireManagerSpy.getScore().total;
        mUmpireManagerSpy.setMatchMode(Rule.MatchMode.TTE_BREAK_T4);
        testTie(4);
        assertEquals(concatABScore(total, Rule.ScoreType.GAME), String.valueOf(4) + ":" + String.valueOf(4));
        letWinGameSeveralTimes(mTennisBoutSpy);
        assertEquals(concatABScore(total, Rule.ScoreType.GAME), "0:0");
        assertEquals(concatABScore(total, Rule.ScoreType.SET), "1:0");
    }

    @Test
    public void reverseLastScore() throws Exception {
        clearDir();
        wonTimes();
        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE).size(), 6);
        sop(getScoreString() + " before reverse");
        mUmpireManagerSpy.reverseLastScoreJudge();
        sop(getScoreString() + " after reverse");
    }

    @Test
    public void cancelLastScore() throws Exception {
        clearDir();
        wonTimes();
        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE).size(), 6);
        sop(getScoreString() + " before cancel");
        mUmpireManagerSpy.cancelLastScore();
        sop(getScoreString() + " after cancel");

        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE_AND_VIDEO).size(), 15);
        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE).size(), 5);
    }

    @Test
    public void wonTimes() throws Exception {
        mTennisBoutSpy.appendBout(false);
        sopScore();
        mTennisBoutSpy.appendBout(false);
        sopScore();
        mTennisBoutSpy.appendBout(true);
        sopScore();
        mTennisBoutSpy.appendBout(true);
        sopScore();
        mTennisBoutSpy.appendBout(true);
        sopScore();
        mTennisBoutSpy.appendBout(true);
        sopScore();
    }

    private void sopScore() {
        sop(getScoreString());
    }

    private String getScoreString() {
        Score score = mUmpireManagerSpy.getScore();
        return (score.getScoreInString());
    }

    @Test
    public void deleteFileInPath() throws Exception {
        String keyString = DomainConst.SCORE_TYPE_VIDEO_END;
        List<File> files = getFileList(UmpireDelegate.ExtensionType.NOTE_AND_VIDEO);
        for (File file : files) {
            String fileName = file.getName();
            if (!ApiFromAndroid.TextUtil_isEmpty(fileName) && fileName.contains(keyString))
                file.delete();
        }
    }

    @Test
    public void clearDir() {
        List<File> files = getFileList(UmpireDelegate.ExtensionType.NOTE_AND_VIDEO);
        for (File file : files) {
            file.delete();
        }
    }

    private List<File> getFileList(@UmpireDelegate.ExtensionType int type) {
        File dirFile = new File(DomainConst.X_NIX_VIDEO_PATH);
        return UmpireDelegate.getFileListUnderDirectory(dirFile, type);
    }

    @Test
    public void gameScoreModeTieBreak() {
        mUmpireManagerSpy.reset();
        int[][] total = mUmpireManagerSpy.getScore().total;
        mUmpireManagerSpy.setMatchMode(Rule.MatchMode.TIE_BREAK);
        letWinGameSeveralTimes(mTennisBoutSpy);
        assertEquals(concatABScore(total, Rule.ScoreType.GAME), "0:0");
        assertEquals(concatABScore(total, Rule.ScoreType.SET), "1:0");
    }

    private void testTie(int limit) {
        letWinGameSeveralTimes(mTennisBoutSpy, limit - 1);
        letWinGameSeveralTimes(Rule.Team.RED, mTennisBoutSpy, limit - 1);
        letWinGameSeveralTimes(Rule.Team.RED, mTennisBoutSpy);
        letWinGameSeveralTimes(mTennisBoutSpy);
    }

    private void letWinGameSeveralTimes(TennisBoutPresenterStub stub) {
        letWinGameSeveralTimes(Rule.Team.TAN, stub, 1);
    }

    private void letWinGameSeveralTimes(TennisBoutPresenterStub stub, int times) {
        letWinGameSeveralTimes(Rule.Team.TAN, stub, times);
    }

    private void letWinGameSeveralTimes(@Rule.Team int side, TennisBoutPresenterStub stub) {
        letWinGameSeveralTimes(side, stub, 1);
    }

    private void letWinGameSeveralTimes(@Rule.Team int side, TennisBoutPresenterStub stub, int times) {
        boolean[] arr = side == Rule.Team.TAN ? TEAM_TAN_WIN_ARR : TEAM_RED_WIN_ARR;
        for (int i = 0; i < times; i++) {
            stub.appendBout(arr);
        }
    }

    private String concatABScore(int[][] score, @Rule.ScoreType int type) {
        int[] abArr = score[type];
        return abArr[Rule.Team.TAN] + ":" + abArr[Rule.Team.RED];
    }

    @Test
    public void boxScore() throws Exception {
        letWinGameSeveralTimes(mTennisBoutSpy);
        TennisBase.MatchBoxScore scoreTan = mUmpireManagerSpy.getBoxScores()[Rule.Team.TAN];
        TennisBase.MatchBoxScore scoreRed = mUmpireManagerSpy.getBoxScores()[Rule.Team.RED];
        assertEquals(scoreTan.speedMax, TennisBoutPresenterStub.MAX_SPEED);
        assertEquals(scoreTan.ace, getTrueCount(TEAM_TAN_WIN_ARR));
        assertEquals(scoreRed.ace, getTrueCount(TEAM_RED_WIN_ARR));
        mUmpireManagerSpy.exchangeSide();
        letWinGameSeveralTimes(mTennisBoutSpy);
        assertEquals(scoreTan.ace, TEAM_TAN_WIN_ARR.length);
        assertEquals(scoreRed.ace, TEAM_RED_WIN_ARR.length);
    }

    private int getTrueCount(boolean[] arr) {
        int count = 0;
        for (boolean b : arr) {
            if (b) count++;
        }
        return count;
    }

    @Test
    public void techAnalyze() throws Exception {
        int count = 10000;
        mTennisBoutSpy.appendReceivePoints(count);

        assertEquals(CollectionUtil.usedCellCount(mUmpireManagerSpy.getReceiveHits()[Rule.Team.TAN]), count);
        assertEquals(CollectionUtil.usedCellCount(mUmpireManagerSpy.getServeFallPoints()[Rule.Team.TAN]), count);
    }

    @Test
    public void pathOnUnix() throws Exception {
        File file = new File(DomainConst.X_NIX_VIDEO_PATH);
        if (file.exists()) {
            file.mkdir();
        }
    }

    @Test
    public void exchangeSide() throws Exception {
        mTennisBoutSpy.appendBout(false);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.B], 1);
        mUmpireManagerSpy.exchangeSide();
        mTennisBoutSpy.appendBout(true);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.B], 2);
        mUmpireManagerSpy.exchangeSide();
        mTennisBoutSpy.appendBout(false);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.B], 3);
        mUmpireManagerSpy.exchangeSide();
        mTennisBoutSpy.appendBout(true);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManagerSpy.getScore().bout[Rule.Side.B], 0);
        assertEquals(mUmpireManagerSpy.getScore().game[Rule.Side.A], 0);
        assertEquals(mUmpireManagerSpy.getScore().game[Rule.Side.B], 1);
    }

    @Test
    public void lastingTime() throws Exception {
        long time = System.currentTimeMillis();
        mUmpireManagerSpy.start();
        mUmpireManagerSpy.stop();
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).run();
        assumeTrue(mUmpireManagerSpy.getLastingTime() > 100);
        assertTrue(time - mUmpireManagerSpy.getWholeGameStartTime() <= 0);
    }
}
