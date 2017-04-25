package com.zealens.face.data.user;

import com.zealens.face.user.UserInfo;

/**
 * Created on 2017/3/18
 * in BlaBla by Kyle
 */

public interface UserSystemInter {
    void assignChosenPlayer(ChosenUserArea chosenUserArea, UserInfo userInfo);

    void assignChosenPlayer(ChosenUserArea chosenUserArea, UserIndex index);

    UserInfo parseChosenPlayer(ChosenUserArea chosenUserArea);

    boolean atLeastOneUserLogin();

    boolean isUserLogin(UserInfo userInfo);

    UserInfo get(UserIndex index);

    int getUserLoginCount();

    UserInfo[] getAll();

    int add(UserInfo userInfo);

    int add(UserIndex index, UserInfo userInfo);

    void resetAll();
}
