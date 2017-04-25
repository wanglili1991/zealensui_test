package com.zealens.face.domain.umpire;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zealens.face.base.Rule;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.DomainConst;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;
import com.zealens.face.util.ApiFromAndroid;
import com.zealens.face.util.FileUtil;

import org.apache.commons.io.FileUtils;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created on 2017/3/25
 * in BlaBla by Kyle
 */

public class UmpireDelegate {
    private static final int BOUT_POINT_SCORE = 4;
    private static final int BOUT_SCORE_GAP_LEAST = 2;
    private static final int GAME_TIE_BREAK_ALTER = 1;
    /**
     * @see com.zealens.face.base.Rule.MatchMode for index
     */
    private static final int[] GAME_POINT_SCORE_ARR = {6, 4, 0,};
    private static final int[] GAME_SCORE_GAP_LEAST_ARR = {2, 2, 1,};
    private static final int[] SET_POINT_SCORE_ARR = {2, 2, 2,};

    private static final int HITS_LIMIT_FOR_SPECIAL_VIDEO = 20;
    private static File PREFIX;

    /**
     * @see com.zealens.face.base.Rule.Team
     */
    @MagicConstant(intValues = {LastScoreJudge.NONE, LastScoreJudge.TEAM_TAN, LastScoreJudge.TEAM_RED})
    public @interface LastScoreJudge {
        int NONE = 0;
        int TEAM_TAN = 1;
        int TEAM_RED = 2;
    }

    @MagicConstant(intValues = {ExtensionType.NOTE, ExtensionType.VIDEO, ExtensionType.NOTE_AND_VIDEO})
    public @interface ExtensionType {
        int NOTE = 0;
        int VIDEO = 1;
        int NOTE_AND_VIDEO = 2;
    }

    /**
     * @see ExtensionType for index
     */
    private static final String[][] FILE_EXTENSION_FILTER = {
            new String[]{DomainConst.VIDEO_TRACK_FILE_EXTENSIONS_REAL},
            new String[]{DomainConst.VIDEO_FILE_EXTENSIONS_REAL},
            new String[]{DomainConst.VIDEO_FILE_EXTENSIONS_REAL, DomainConst.VIDEO_TRACK_FILE_EXTENSIONS_REAL}};

    //--**-BLOCK_DIVIDER-**-----------------------score parsing--------------------------//
    @Rule.ScoreCallbackTag
    public static int parseScore(@Rule.MatchMode int mode, @NonNls Score oldScore, boolean teamTanWin) {
        Assert.assertNotNull(oldScore);
        increaseByControl(oldScore.bout, teamTanWin);
        if (!anyMemberReachPoint(oldScore.bout, BOUT_POINT_SCORE)) {
            return Rule.ScoreCallbackTag.NEXT_BOUT;
        }

        int difBoutAB = difOfArray1st2nd(oldScore.bout);
        if (okToIgnoreDifference(difBoutAB, BOUT_SCORE_GAP_LEAST)) {
            return Rule.ScoreCallbackTag.NEXT_BOUT;
        }

        increaseByControl(oldScore.game, difBoutAB > 0);
        cleanArray1st2nd(oldScore.bout);
        if (!anyMemberReachPoint(oldScore.game, GAME_POINT_SCORE_ARR[mode])) {
            return Rule.ScoreCallbackTag.NEXT_BOUT;
        }

        boolean reachTieBreakLimit = anyMemberReachPoint(oldScore.game, GAME_POINT_SCORE_ARR[mode] + GAME_TIE_BREAK_ALTER);
        int difGameAB = difOfArray1st2nd(oldScore.game);
        if (!reachTieBreakLimit && okToIgnoreDifference(difGameAB, GAME_SCORE_GAP_LEAST_ARR[mode])) {
            return Rule.ScoreCallbackTag.NEXT_GAME;
        }

        increaseByControl(oldScore.set, difGameAB > 0);
        cleanArray1st2nd(oldScore.game);
        if (!anyMemberReachPoint(oldScore.set, GAME_POINT_SCORE_ARR[mode])) {
            return Rule.ScoreCallbackTag.NEXT_BOUT;
        }

        int difSetAB = difOfArray1st2nd(oldScore.game);
        if (okToIgnoreDifference(difSetAB, SET_POINT_SCORE_ARR[mode])) {
            return Rule.ScoreCallbackTag.END_GAME;
        }
        return Rule.ScoreCallbackTag.DUMMY;
    }

    private static void increaseByControl(int[] target, boolean control) {
        target[control ? Rule.Team.TAN : Rule.Team.RED]++;
    }

    private static boolean anyMemberReachPoint(int[] arr, int standard) {
        Assert.assertTrue(arr.length > 1);
        return arr[0] >= standard || arr[1] >= standard;
    }

    private static int difOfArray1st2nd(@NonNls int[] arr) {
        Assert.assertTrue(arr.length > 1);
        return arr[0] - arr[1];
    }

    private static void cleanArray1st2nd(@NonNls int[] arr) {
        Assert.assertTrue(arr.length > 1);
        arr[0] = 0;
        arr[1] = 0;
    }

    private static boolean okToIgnoreDifference(int difAB, int limit) {
        return Math.abs(difAB) < limit;
    }

    //--**-BLOCK_DIVIDER-**-----------------------box score--------------------------//
    @Rule.VideoTag
    public static int appendBoxScore(TennisBase.MatchBoxScore target, @NonNls TennisBase.MatchBoxScore increment
            , ScoreParseCallback callback) {
        Assert.assertNotNull(target);
        Assert.assertNotNull(increment);

        @Rule.VideoTag
        int videoTag = Rule.VideoTag.GENERAL;
        target.score += increment.score;

        target.ace += increment.ace;
        if (increment.ace > 0) videoTag = Rule.VideoTag.ACE;

        target.doubleFaultsNum += increment.doubleFaultsNum;
        target.n1stServeTotalNum += increment.n1stServeTotalNum;
        target.n1stServeSNum += increment.n1stServeSNum;
        target.n1stSocreSNum += increment.n1stSocreSNum;
        target.n2ndServeTotalNum += increment.n2ndServeTotalNum;
        target.n2ndServeSNum += increment.n2ndServeSNum;
        target.n2ndSocreSNum += increment.n2ndSocreSNum;

        target.winners += increment.winners;
        if (increment.winners > 0) videoTag = Rule.VideoTag.WINNER;

        target.speedMax = Math.max(target.speedMax, increment.speedMax);
        target.speedSum = Math.max(target.speedSum, increment.speedSum);

        target.hitTotalNum += increment.hitTotalNum;
        if (increment.hitTotalNum > HITS_LIMIT_FOR_SPECIAL_VIDEO)
            videoTag = Rule.VideoTag.ABOVE_20_HITS;

        target.hitNumInside += increment.hitNumInside;
        target.hitNumBaseline += increment.hitNumBaseline;
        target.hitNumOutside += increment.hitNumOutside;
        target.area11stInsideNum += increment.area11stInsideNum;
        target.area11stOutsideNum += increment.area11stOutsideNum;
        target.area21stInsideNum += increment.area21stInsideNum;
        target.area21stOutsideNum += increment.area21stOutsideNum;
        target.area12stInsideNum += increment.area12stInsideNum;
        target.area12stOutsideNum += increment.area12stOutsideNum;
        target.area22ndInsideNum += increment.area22ndInsideNum;
        target.area22stOutsideNum += increment.area22stOutsideNum;

        return videoTag;
    }

    //--**-BLOCK_DIVIDER-**-----------------------event cache--------------------------//
    public static void putEventIntoCache(Map<String, String> map, @Rule.VideoEvent String event) {
        putEventIntoCache(map, event, null);
    }

    public static void putEventIntoCache(Map<String, String> map, @Rule.VideoEvent String event, @Nullable Object content) {
        String value = event + (content == null || "".equals(content.toString()) ? "" : DomainConst.MATCH_EVENT_CONCAT + content);
        map.put(String.valueOf(System.currentTimeMillis()) + event, value);
    }

    public static void putMatchVideoEventIntoFile(String oldVideoPath, Map<String, String> map, @Rule.Side int side) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(map);
        String sideStrAndExt = DomainConst.SCORE_SPLIT + getSideString(side) + DomainConst.VIDEO_FILE_EXTENSIONS;
        String noteFileName = oldVideoPath.replace(sideStrAndExt, DomainConst.VIDEO_TRACK_FILE_EXTENSIONS);
        FileUtil.appendContentToFile(noteFileName, jsonString);
    }

    //--**-BLOCK_DIVIDER-**-----------------------video cache--------------------------//

    /**
     * @param prefix path prefix
     * @throws IllegalStateException when no permission
     */
    public static void createVideoCacheDirectory(File prefix) {
        if (prefix != null) PREFIX = prefix;
        String savePath = (prefix != null ? prefix.toString() : "") + DomainConst.X_NIX_VIDEO_PATH;
        File pathFile = new File(savePath);
        if (!pathFile.exists()) {
            boolean mkdir = pathFile.mkdir();
            if (!mkdir) throw new IllegalStateException("check SDCard permission");
        }
    }

    public static String getVideoCachePath(boolean withDirectorySplit) {
        return (PREFIX != null ? PREFIX.toString() : "")
                + DomainConst.X_NIX_VIDEO_PATH + (withDirectorySplit ? DomainConst.FILE_SEPARATOR : "");
    }

    public static String generateAbsoluteFilePath(Score startScore, String timeStamp, @Rule.Side int side, File prefix) {
        return generateAbsoluteFilePath(null, startScore, timeStamp, side, prefix);
    }

    public static String generateAbsoluteFilePath(Score endScore, Score startScore, String timeStamp, @Rule.Side int side, File prefix) {
        String sideStr = getSideString(side);
        return getVideoCachePath(true)
                + getStartEndScorePrefix(endScore, startScore) + timeStamp
                + DomainConst.SCORE_SPLIT + sideStr
                + DomainConst.VIDEO_FILE_EXTENSIONS;
    }

    private static String getStartEndScorePrefix(Score endScore, Score startScore) {
        return (endScore != null ? getScoreStrWithSplit4StartOrEnd(endScore) : "")
                + getScoreStrWithSplit4StartOrEnd(startScore, true);
    }

    private static String getScoreStrWithSplit4StartOrEnd(Score score) {
        return getScoreStrWithSplit4StartOrEnd(score, false);
    }

    private static String getScoreStrWithSplit4StartOrEnd(Score score, boolean startScoreNotEndScore) {
        return startScoreNotEndScore ? DomainConst.SCORE_TYPE_VIDEO_START : DomainConst.SCORE_TYPE_VIDEO_END
                + score.getScoreInString() + DomainConst.SCORE_SPLIT;
    }

    private static String getSideString(@Rule.Side int side) {
        return side == Rule.Side.A ? Rule.SideStr.A : Rule.SideStr.B;
    }

    public static String renameRecordFile(@Rule.VideoTag int tag, Score endScore, long lastingTime, String oldName) {
        String endScoreString = getScoreStrWithSplit4StartOrEnd(endScore);
        String fileName = FileUtil.getFileNameFromAbsolutePath(oldName);
        int splitIndex = fileName.indexOf(DomainConst.SCORE_SPLIT);
        String splitPre = fileName.substring(0, splitIndex + 1);
        String splitSuf = fileName.substring(splitIndex + 1);
        String lastingTimeAppendedOldName = splitPre + DomainConst.VIDEO_LASTING_TIME
                + lastingTime + DomainConst.SCORE_SPLIT + splitSuf;
        String tagString = DomainConst.VIDEO_TAG + tag + DomainConst.SCORE_SPLIT;
        String newName = getVideoCachePath(true) + tagString + endScoreString + lastingTimeAppendedOldName;
        return FileUtil.renameFileWithApacheApi(oldName, newName);
    }

    public static void reverseVideoFilesName(Score oldEndScore, Score endScore, Score startScore) {
        String oldPrefix = getStartEndScorePrefix(oldEndScore, startScore);
        String newPrefix = getStartEndScorePrefix(endScore, startScore);
        String path = getVideoCachePath(false);
        File pathFile = new File(path);
        List<File> files = getFileListUnderDirectory(pathFile, ExtensionType.VIDEO);
        String fileName;
        String newName;
        for (File file : files) {
            fileName = file.toString();
            if (fileName.contains(oldPrefix)) {
                newName = fileName.replace(oldPrefix, newPrefix);
                FileUtil.renameFileWithApacheApi(fileName, newName);
            }
        }
    }

    public static boolean removeVideoAndNoteFiles(Score oldScore, @ExtensionType int extensionType) {
        String endScoreSlice = DomainConst.SCORE_TYPE_VIDEO_END + oldScore.getScoreInString();
        String path = getVideoCachePath(false);
        File pathFile = new File(path);
        if (!pathFile.exists()) return false;

        deleteFilesContainString(endScoreSlice, pathFile, extensionType);
        return true;
    }

    private static void deleteFilesContainString(String keyString, File fileDir, @ExtensionType int extensionType) {
        List<File> files = getFileListUnderDirectory(fileDir, extensionType);
        for (File file : files) {
            String fileName = file.getName();
            if (!ApiFromAndroid.TextUtil_isEmpty(fileName) && fileName.contains(keyString))
                file.delete();
        }
    }

    public static List<File> getFileListUnderDirectory(File fileDir, @ExtensionType int extensionType) {
        return (List<File>) FileUtils.listFiles(fileDir, FILE_EXTENSION_FILTER[extensionType], false);
    }

    /**
     * @param currentScore
     * @param historyScore
     * @return
     * @throws IllegalStateException
     */
    public static boolean teamTanWonBout(Score currentScore, Score historyScore) {
        int boutChange = teamTanChange(currentScore.bout, historyScore.bout);
        if (boutChange != LastScoreJudge.NONE)
            return boutChange == LastScoreJudge.TEAM_TAN;
        int gameChange = teamTanChange(currentScore.game, historyScore.game);
        if (gameChange != LastScoreJudge.NONE)
            return gameChange == LastScoreJudge.TEAM_TAN;
        int setChange = teamTanChange(currentScore.set, historyScore.set);
        if (setChange != LastScoreJudge.NONE)
            return setChange == LastScoreJudge.TEAM_TAN;
        throw new IllegalStateException("bout score incorrect");
    }

    @LastScoreJudge
    private static int teamTanChange(@NonNls int[] currentArr, @NonNls int[] historyArr) {
        int difTan = currentArr[Rule.Team.TAN] - historyArr[Rule.Team.TAN];
        if (difTan > 0) return LastScoreJudge.TEAM_TAN;
        int difRed = currentArr[Rule.Team.RED] - historyArr[Rule.Team.RED];
        if (difRed > 0) return LastScoreJudge.TEAM_RED;
        return LastScoreJudge.NONE;
    }

    public static Video parseVideoInfoByPath(@NonNls String path) {
        Video video = new Video();
        video.path = path;
        parseVideoInfoByPath(video);
        return video;
    }

    @SuppressWarnings("MagicConstant")
    public static void parseVideoInfoByPath(@NonNls Video video) {
        String path = video.path;
        String name = path.substring(path.lastIndexOf(File.separator) + 1);
        String[] contents = name.split(DomainConst.SCORE_SPLIT);

        for (String s : contents) {
            if (s.startsWith(DomainConst.VIDEO_LASTING_TIME)) {
                String lastingStr = s.substring(DomainConst.VIDEO_LASTING_TIME.length());
                video.timeLast = Long.parseLong(lastingStr);
            } else if (s.startsWith(DomainConst.VIDEO_TAG)) {
                String tag = s.substring(DomainConst.VIDEO_TAG.length());
                video.tag = Integer.valueOf(tag);
            } else if (s.startsWith(DomainConst.SCORE_TYPE_VIDEO_END)) {
                String[] scores = s.split(DomainConst.SCORE_DIVIDER);
                int len = scores.length;
                int[][] scoreDigits = new int[][]{{0, 0}, {0, 0}, {0, 0}};
                for (int i = 0, j = 0; i < len && j < Score.BITS; i++) {
                    if (!scores[i].matches("\\d+")) continue;
                    int k = j / 2;
                    scoreDigits[k][j++ & 0x1] = Integer.valueOf(scores[i]);
                }
                video.score = new Score(scoreDigits);
            }
        }
    }
}
