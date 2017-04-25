package com.zealens.face.domain.umpire;

import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;

/**
 * Created by Kyle on 12/04/2017
 */

public interface CorrectComponents {
    /**
     * @return score after operation
     * @see Score
     */
    Score reverseLastScoreJudge();

    /**
     * @see #reverseLastScoreJudge() comment
     */
    Score cancelLastScore();

    /**
     * @return of visual team {tan,red}
     * @see Video
     * @see com.zealens.face.base.Rule.Team
     */
    Video[] getVideoOfLastScore();
}
