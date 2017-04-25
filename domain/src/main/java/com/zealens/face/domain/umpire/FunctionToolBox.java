package com.zealens.face.domain.umpire;

import com.zealens.face.base.Rule;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.module.Point2D;
import com.zealens.face.domain.module.Point3D;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;

/**
 * Created by KyleCe on 01/04/2017.
 */

public interface FunctionToolBox extends CorrectComponents {

    /**
     * @return of visual team {tan,red}
     * @see TennisBase.BaseBoxScore
     * @see com.zealens.face.base.Rule.Team
     */
    TennisBase.BaseBoxScore[] getAnalyzeData();

    Point2D[][] getReceiveHits();

    Point3D[][] getServeFallPoints();

    long getWholeGameStartTime();

    long getLastingTime();

    Score getScore();

    Video[] getVideoOfAce(@Rule.Team int team);

    Video[] getVideoOfWinner(@Rule.Team int team);

    Video[] getVideoOfManyHits(@Rule.Team int team);

    Video[] getVideoOfGeneral(@Rule.Team int team);

    Video[] getAllVideo(@Rule.Team int team);
}
