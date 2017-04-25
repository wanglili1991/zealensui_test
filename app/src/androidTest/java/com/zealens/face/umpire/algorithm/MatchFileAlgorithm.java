package com.zealens.face.umpire.algorithm;

import com.zealens.face.domain.module.Score;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Created by Kyle on 05/04/2017
 */

public class MatchFileAlgorithm {
    @MagicConstant(intValues = {EventIdIndex.D888, EventIdIndex.D000, EventIdIndex.D001, EventIdIndex.D801,})
    public @interface EventIdIndex {
        int D888 = 0;
        int D000 = 1;
        int D001 = 2;
        int D801 = 3;
    }

    public static final String[] EVENT_ID_ARR = {
            "D888",
            "D000",
            "D001",
            "D801",
    };

    private static final Score[] SCORE_ARR = {
            new Score(new int[][]{{0, 0}, {0, 0}, {0, 0}}),
            new Score(new int[][]{{0, 0}, {1, 0}, {0, 0}}),
            new Score(new int[][]{{0, 0}, {0, 0}, {0, 0}}),
            new Score(new int[][]{{1, 0}, {0, 0}, {0, 0}}),
    };

    public static Matcher<Score> matches(@EventIdIndex int fileIndex) {
        return new BaseMatcher<Score>() {
            @Override
            public boolean matches(Object o) {
                if (!(o instanceof Score)) return false;
                Score s = (Score) o;
                return SCORE_ARR[fileIndex].equals(s);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(SCORE_ARR[fileIndex].getScoreInString());
            }
        };
    }
}
