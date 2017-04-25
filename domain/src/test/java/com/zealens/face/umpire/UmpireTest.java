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
    UmpireManager mUmpireManager;
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
        mUmpireManager = new UmpireManager(ipCameras);
        mUmpireManager.initialize();
        mUmpireManager.assignComponent(mTennisBoutSpy, new TennisBase.InitParam());
        mUmpireManager.disableAutoReverseSide();
        setupUmpireSpy();
    }

    public void setupUmpireSpy() {
        mUmpireManager.setMatchMode(Rule.MatchMode.TIE_BREAK_T6);
        mUmpireManager.assignUmpireCallback(new FakeUmpireCallback());
        sop("init spy");
    }

    @Test
    public void scoreInString() throws Exception {
        boutScore();
        String scoreStr = mUmpireManager.getScore().getScoreInString();
        sop(scoreStr);
        assertTrue(scoreStr.contains(DomainConst.SCORE_TAG_BOUT));
    }

    @Test
    public void boutScore() {
        int[][] total = mUmpireManager.getScore().total;
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
        int[][] total = mUmpireManager.getScore().total;
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
        mUmpireManager.reset();
        int[][] total = mUmpireManager.getScore().total;
        mUmpireManager.setMatchMode(Rule.MatchMode.TTE_BREAK_T4);
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
        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE).size(), 5);
        sop(getScoreString() + " before reverse");
        mUmpireManager.reverseLastScoreJudge();
        sop(getScoreString() + " after reverse");
    }

    @Test
    public void cancelLastScore() throws Exception {
        clearDir();
        wonTimes();
        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE).size(), 5);
        sop(getScoreString() + " before cancel");
        mUmpireManager.cancelLastScore();
        sop(getScoreString() + " after cancel");

        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE_AND_VIDEO).size(), 14);
        assertEquals(getFileList(UmpireDelegate.ExtensionType.NOTE).size(), 4);
    }

    private void wonTimes() throws Exception {
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
        Score score = mUmpireManager.getScore();
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

    private void clearDir() {
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
        mUmpireManager.reset();
        int[][] total = mUmpireManager.getScore().total;
        mUmpireManager.setMatchMode(Rule.MatchMode.TIE_BREAK);
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
        TennisBase.MatchBoxScore scoreTan = mUmpireManager.getBoxScores()[Rule.Team.TAN];
        TennisBase.MatchBoxScore scoreRed = mUmpireManager.getBoxScores()[Rule.Team.RED];
        assertEquals(scoreTan.speedMax, TennisBoutPresenterStub.MAX_SPEED);
        assertEquals(scoreTan.ace, getTrueCount(TEAM_TAN_WIN_ARR));
        assertEquals(scoreRed.ace, getTrueCount(TEAM_RED_WIN_ARR));
        mUmpireManager.exchangeSide();
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

        assertEquals(CollectionUtil.usedCellCount(mUmpireManager.getReceiveHits()[Rule.Team.TAN]), count);
        assertEquals(CollectionUtil.usedCellCount(mUmpireManager.getServeFallPoints()[Rule.Team.TAN]), count);
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
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.B], 1);
        mUmpireManager.exchangeSide();
        mTennisBoutSpy.appendBout(true);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.B], 2);
        mUmpireManager.exchangeSide();
        mTennisBoutSpy.appendBout(false);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.B], 3);
        mUmpireManager.exchangeSide();
        mTennisBoutSpy.appendBout(true);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.A], 0);
        assertEquals(mUmpireManager.getScore().bout[Rule.Side.B], 0);
        assertEquals(mUmpireManager.getScore().game[Rule.Side.A], 0);
        assertEquals(mUmpireManager.getScore().game[Rule.Side.B], 1);
    }

    @Test
    public void lastingTime() throws Exception {
        long time = System.currentTimeMillis();
        mUmpireManager.start();
        mUmpireManager.stop();
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).run();
        assumeTrue(mUmpireManager.getLastingTime() > 100);
        assertTrue(time - mUmpireManager.getWholeGameStartTime() <= 0);
    }
}
