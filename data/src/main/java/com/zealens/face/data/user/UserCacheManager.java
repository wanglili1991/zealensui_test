package com.zealens.face.data.user;

import com.zealens.face.core.BaseCoreManager;
import com.zealens.face.data.common.DataConst;
import com.zealens.face.user.UserInfo;
import com.zealens.face.util.CollectionUtil;

import org.junit.Assert;

public class UserCacheManager extends BaseCoreManager implements UserSystemInter {
    public static final int NO_MORE_SPACE = -2;
    public static final int ALREADY_EXIST = -3;

    private UserInfo[] mUserInfoArr;
    private int[] mChosenPlayerIndex;

    /**
     * @param chosenUserArea
     * @param userInfo
     * @throws IllegalStateException if userInfo do not contained in cache
     */
    @Override
    public void assignChosenPlayer(ChosenUserArea chosenUserArea, UserInfo userInfo) {
        int index = CollectionUtil.findIndexUseLoop(mUserInfoArr, userInfo);
        if (index < 0)
            throw new IllegalStateException("cannot assign user that not exist in cache");
        assignChosenPlayer(chosenUserArea, UserIndex.find(index));
    }

    @Override
    public void assignChosenPlayer(ChosenUserArea chosenUserArea, UserIndex index) {
        mChosenPlayerIndex[chosenUserArea.index] = index.index;
    }

    @Override
    public UserInfo parseChosenPlayer(ChosenUserArea chosenUserArea) {
        int userIndex = mChosenPlayerIndex[chosenUserArea.index];
        return userIndex == DataConst.CHOSE_PLAYER_NOT_SET ? null : mUserInfoArr[userIndex];
    }

    @Override
    public boolean atLeastOneUserLogin() {
        return CollectionUtil.usedCellCountWithRearrange(mUserInfoArr, null) > 0;
    }

    @Override
    public boolean isUserLogin(UserInfo userInfo) {
        int tokenIndex = CollectionUtil.findIndexUseLoop(mUserInfoArr, userInfo);
        return tokenIndex >= 0;
    }

    @Override
    public UserInfo get(UserIndex index) {
        return mUserInfoArr[index.index];
    }

    @Override
    public int getUserLoginCount() {
        return CollectionUtil.usedCellCount(mUserInfoArr);
    }

    @Override
    public UserInfo[] getAll() {
        return mUserInfoArr.clone();
    }

    @Override
    public int add(UserInfo userInfo) {
        int tokenIndex = CollectionUtil.findIndexUseLoop(mUserInfoArr, userInfo);
        if (tokenIndex >= 0)
            return ALREADY_EXIST;
        int usedCellCount = CollectionUtil.usedCellCountWithRearrange(mUserInfoArr);
        if (usedCellCount == DataConst.MAX_PLAYER_COUNT)
            return NO_MORE_SPACE;
        mUserInfoArr[usedCellCount] = userInfo;
        return usedCellCount;
    }

    @Override
    public int add(UserIndex index, UserInfo userInfo) {
        int tokenIndex = CollectionUtil.findIndexUseLoop(mUserInfoArr, userInfo);
        if (tokenIndex >= 0)
            return ALREADY_EXIST;
        mUserInfoArr[index.index] = userInfo;
        return index.index;
    }

    @Override
    public void resetAll() {
        CollectionUtil.clear(mUserInfoArr);
        for (int i = 0; i < DataConst.MAX_CHOSEN_PLAYER_COUNT; i++) {
            mChosenPlayerIndex[i] = DataConst.CHOSE_PLAYER_NOT_SET;
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        Assert.assertEquals(UserIndex.values().length, DataConst.MAX_PLAYER_COUNT);
        mUserInfoArr = new UserInfo[DataConst.MAX_PLAYER_COUNT];
        mChosenPlayerIndex = new int[DataConst.MAX_CHOSEN_PLAYER_COUNT];
        resetAll();
    }

    @Override
    public void dispose() {
        super.dispose();
        CollectionUtil.clear(mUserInfoArr);
    }

    @Override
    public int order() {
        return ORDER.USER_CACHE;
    }

    @Override
    public void freeMemory() {

    }
}
