package com.zealens.face.domain.umpire;

/**
 * Created on 2017/3/27
 * in BlaBla by Kyle
 */

public interface ScoreParseCallback {
    void shouldStartNextBout();

    void shouldStartNextGame();

    void shouldEndGame();
}
