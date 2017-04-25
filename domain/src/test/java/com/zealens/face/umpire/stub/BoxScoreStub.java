package com.zealens.face.umpire.stub;

import com.zealens.face.core.internal.TennisBase;

/**
 * Created on 2017/3/27
 * in BlaBla by Kyle
 */

public class BoxScoreStub extends TennisBase.MatchBoxScore {
    private BoxScoreStub(Builder builder) {
        score = builder.score;
        ace = builder.ace;
        doubleFaultsNum = builder.doubleFaultsNum;
        n1stServeTotalNum = builder.n1stServeTotalNum;
        n1stServeSNum = builder.n1stServeSNum;
        n1stSocreSNum = builder.n1stSocreSNum;
        n2ndServeTotalNum = builder.n2ndServeTotalNum;
        n2ndServeSNum = builder.n2ndServeSNum;
        n2ndSocreSNum = builder.n2ndSocreSNum;
        winners = builder.winners;
        speedMax = builder.speedMax;
        speedSum = builder.speedSum;
        hitTotalNum = builder.hitTotalNum;
        hitNumInside = builder.hitNumInside;
        hitNumBaseline = builder.hitNumBaseline;
        hitNumOutside = builder.hitNumOutside;
        area11stInsideNum = builder.area11stInsideNum;
        area11stOutsideNum = builder.area11stOutsideNum;
        area21stInsideNum = builder.area21stInsideNum;
        area21stOutsideNum = builder.area21stOutsideNum;
        area12stInsideNum = builder.area12stInsideNum;
        area12stOutsideNum = builder.area12stOutsideNum;
        area22ndInsideNum = builder.area22ndInsideNum;
        area22stOutsideNum = builder.area22stOutsideNum;
    }

    public static final class Builder {
        private int score;
        private int ace;
        private int doubleFaultsNum;
        private int n1stServeTotalNum;
        private int n1stServeSNum;
        private int n1stSocreSNum;
        private int n2ndServeTotalNum;
        private int n2ndServeSNum;
        private int n2ndSocreSNum;
        private int winners;
        private int speedMax;
        private int speedSum;
        private int hitTotalNum;
        private int hitNumInside;
        private int hitNumBaseline;
        private int hitNumOutside;
        private int area11stInsideNum;
        private int area11stOutsideNum;
        private int area21stInsideNum;
        private int area21stOutsideNum;
        private int area12stInsideNum;
        private int area12stOutsideNum;
        private int area22ndInsideNum;
        private int area22stOutsideNum;

        public Builder() {
        }

        public Builder withScore(int val) {
            score = val;
            return this;
        }

        public Builder withAce(int val) {
            ace = val;
            return this;
        }

        public Builder withDoubleFaultsNum(int val) {
            doubleFaultsNum = val;
            return this;
        }

        public Builder withN1stServeTotalNum(int val) {
            n1stServeTotalNum = val;
            return this;
        }

        public Builder withN1stServeSNum(int val) {
            n1stServeSNum = val;
            return this;
        }

        public Builder withN1stSocreSNum(int val) {
            n1stSocreSNum = val;
            return this;
        }

        public Builder withN2ndServeTotalNum(int val) {
            n2ndServeTotalNum = val;
            return this;
        }

        public Builder withN2ndServeSNum(int val) {
            n2ndServeSNum = val;
            return this;
        }

        public Builder withN2ndSocreSNum(int val) {
            n2ndSocreSNum = val;
            return this;
        }

        public Builder withWinners(int val) {
            winners = val;
            return this;
        }

        public Builder withSpeedMax(int val) {
            speedMax = val;
            return this;
        }

        public Builder withSpeedSum(int val) {
            speedSum = val;
            return this;
        }

        public Builder withHitTotalNum(int val) {
            hitTotalNum = val;
            return this;
        }

        public Builder withHitNumInside(int val) {
            hitNumInside = val;
            return this;
        }

        public Builder withHitNumBaseline(int val) {
            hitNumBaseline = val;
            return this;
        }

        public Builder withHitNumOutside(int val) {
            hitNumOutside = val;
            return this;
        }

        public Builder withArea11stInsideNum(int val) {
            area11stInsideNum = val;
            return this;
        }

        public Builder withArea11stOutsideNum(int val) {
            area11stOutsideNum = val;
            return this;
        }

        public Builder withArea21stInsideNum(int val) {
            area21stInsideNum = val;
            return this;
        }

        public Builder withArea21stOutsideNum(int val) {
            area21stOutsideNum = val;
            return this;
        }

        public Builder withArea12stInsideNum(int val) {
            area12stInsideNum = val;
            return this;
        }

        public Builder withArea12stOutsideNum(int val) {
            area12stOutsideNum = val;
            return this;
        }

        public Builder withArea22ndInsideNum(int val) {
            area22ndInsideNum = val;
            return this;
        }

        public Builder withArea22stOutsideNum(int val) {
            area22stOutsideNum = val;
            return this;
        }

        public BoxScoreStub build() {
            return new BoxScoreStub(this);
        }
    }
}
