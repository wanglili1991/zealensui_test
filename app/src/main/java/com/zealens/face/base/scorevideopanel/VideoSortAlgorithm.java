package com.zealens.face.base.scorevideopanel;

import com.zealens.face.base.Rule;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;
import com.zealens.face.domain.umpire.UmpireDelegate;
import com.zealens.face.util.CollectionUtil;

import org.jetbrains.annotations.NonNls;

import java.util.Arrays;

/**
 * Created by Kyle on 22/04/2017
 */

public class VideoSortAlgorithm {

    public static Video[] sortVideosAndInsertDivider(@NonNls Score finalScore, @NonNls Video[] videos
            , boolean splitSection) {
        if (!splitSection || videos.length == 0) return videos;

        Video tmp;
        for (Video v : videos) {
            UmpireDelegate.parseVideoInfoByPath(v);
            if (Rule.isTagSpecial(v.tag)) {
                tmp = new Video(v);
                tmp.tag = Rule.VideoTag.GENERAL;
                videos = CollectionUtil.assembleCapacityExpandedArray(videos, 1);
                int len = videos.length;
                videos[len - 1] = tmp;
            }
        }

        Arrays.sort(videos, (o1, o2) -> {
            if (o1.tag < o2.tag) return -1;
            else if (o1.tag > o2.tag) return 1;
            else {
                if (o1.score == null || o2.score == null) return 0;
                int setCompare = parseArraySumCompare(o1.score.set, o2.score.set);
                if (setCompare != 0) return setCompare;
                int gameCompare = parseArraySumCompare(o1.score.game, o2.score.game);
                if (gameCompare != 0) return gameCompare;
                int boutCompare = parseArraySumCompare(o1.score.bout, o2.score.bout);
                if (boutCompare != 0) return boutCompare;
                return 0;
            }
        });

        int gameSum = CollectionUtil.accumulateIntegerArray(finalScore.game);
        if (CollectionUtil.accumulateIntegerArray(finalScore.bout) != 0) gameSum += 1;
        boolean hasJewelVideo = Rule.isTagSpecial(videos[0].tag);
        int increment = gameSum + (hasJewelVideo ? 1 : 0);
        int oldLen = videos.length;
        videos = CollectionUtil.assembleCapacityExpandedArray(videos, increment);
        int len = videos.length;
        Video jewelDivider = new Video(Rule.VideoDivider.JEWEL);
        Video gameDivider = new Video(Rule.VideoDivider.GAME);

        Video carrier;
        int i = len - 1;
        int j = oldLen - 1;
        videos[0] = hasJewelVideo ? jewelDivider : gameDivider;
        while (i >= 0) {
            carrier = videos[j];
            if (j != 0) {
                if (videos[j].score == null || videos[j - 1].score == null) {
                    break;
                }
                int gameCompare = parseArraySumCompare(videos[j].score.game, videos[j - 1].score.game);
                boolean firstBout = CollectionUtil.accumulateIntegerArray(videos[j].score.bout) == 1;

                videos[i--] = carrier;
                boolean nonSpecial = !Rule.isTagSpecial(videos[j].tag) && !Rule.isTagSpecial(videos[j - 1].tag);
                boolean generalSpecialGap = !Rule.isTagSpecial(videos[j].tag) && Rule.isTagSpecial(videos[j - 1].tag);
                boolean shouldInsertGameDivider = firstBout && ((gameCompare == 0 && nonSpecial) || generalSpecialGap);
                if (shouldInsertGameDivider) {
                    videos[i--] = gameDivider;
                }
                j--;
            } else {
                videos[i] = videos[0];
                videos[0] = hasJewelVideo ? jewelDivider : gameDivider;
                break;
            }
        }
        return videos;
    }

    private static int parseArraySumCompare(int[] a1, int[] a2) {
        int sum1 = CollectionUtil.accumulateIntegerArray(a1);
        int sum2 = CollectionUtil.accumulateIntegerArray(a2);
        return compareNum(sum1, sum2);
    }

    private static int compareNum(int a, int b) {
        if (a > b) return 1;
        if (a < b) return -1;
        return 0;
    }
}
