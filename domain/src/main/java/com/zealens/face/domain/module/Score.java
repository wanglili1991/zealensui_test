package com.zealens.face.domain.module;

import com.zealens.face.base.Rule;
import com.zealens.face.domain.DomainConst;

import org.junit.Assert;

import java.util.Arrays;

/**
 * Created on 2017/3/25
 * in BlaBla by Kyle
 */

public class Score {
    /**
     * score matrix:
     * {
     * ----------side a,side b--------
     * ---bout  {    ba,bb    },
     * ---game  {    ga,gb    },
     * ----set  {    sa,sb    },
     * }
     *
     * @return score array
     * @see com.zealens.face.base.Rule.ScoreType use this for first index side
     * @see com.zealens.face.base.Rule.Team use this for second index side
     */
    public int[][] total;

    /**
     * @see com.zealens.face.base.Rule.Team use this for index side
     */
    public int[] bout;
    public int[] game;
    public int[] set;
    public static final int BITS = 6;

    public Score() {
        this(new int[][]{{0, 0}, {0, 0}, {0, 0}});
    }

    public Score(int[][] src) {
        init(src);
    }

    public Score(Score score) {
        this.total = new int[][]{
                {score.bout[Rule.Team.TAN], score.bout[Rule.Team.RED]},
                {score.game[Rule.Team.TAN], score.game[Rule.Team.RED]},
                {score.set[Rule.Team.TAN], score.set[Rule.Team.RED]},
        };
        init(total);
    }

    private void init(int[][] src) {
        if (src != null)
            total = src;
        else
            total = new int[][]{{0, 0}, {0, 0}, {0, 0}};
        bout = total[Rule.ScoreType.BOUT];
        game = total[Rule.ScoreType.GAME];
        set = total[Rule.ScoreType.SET];
    }

    public void reset() {
        init(null);
    }

    public String getScoreInString() {
        String[] tags = {DomainConst.SCORE_TAG_BOUT, DomainConst.SCORE_TAG_GAME, DomainConst.SCORE_TAG_SET};
        return getScoreInString(DomainConst.SCORE_DIVIDER, tags);
    }

    public String getScoreInString(CharSequence divider, String[] tags) {
        Assert.assertEquals(tags.length, Rule.ScoreType.class.getFields().length);
        return tags[0] + tags[1] + tags[2] + divider
                + getScoreInt(divider);
    }

    public String getScoreInt(CharSequence divider) {
        return "" + bout[Rule.Team.TAN] + divider + bout[Rule.Team.RED] + divider
                + game[Rule.Team.TAN] + divider + game[Rule.Team.RED] + divider
                + set[Rule.Team.TAN] + divider + set[Rule.Team.RED];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Score score = (Score) o;

        if (!Arrays.deepEquals(total, score.total)) return false;
        if (!Arrays.equals(bout, score.bout)) return false;
        if (!Arrays.equals(game, score.game)) return false;
        return Arrays.equals(set, score.set);
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(total);
        result = 31 * result + Arrays.hashCode(bout);
        result = 31 * result + Arrays.hashCode(game);
        result = 31 * result + Arrays.hashCode(set);
        return result;
    }

    @Override
    public String toString() {
        return bout[Rule.Team.TAN] + DomainConst.SCORE_DIVIDER + bout[Rule.Team.RED] + DomainConst.SCORE_SPLIT
                + game[Rule.Team.TAN] + DomainConst.SCORE_DIVIDER + game[Rule.Team.RED] + DomainConst.SCORE_SPLIT
                + set[Rule.Team.TAN] + DomainConst.SCORE_DIVIDER + set[Rule.Team.RED] + DomainConst.SCORE_SPLIT;
    }
}
