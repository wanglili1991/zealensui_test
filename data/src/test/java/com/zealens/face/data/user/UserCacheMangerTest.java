package com.zealens.face.data.user;

import com.zealens.face.user.UserInfo;
import com.zealens.face.util.CollectionUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.zealens.face.util.LogcatUtil.sop;

/**
 * Created on 2017/3/20
 * in BlaBla by Kyle
 */
public class UserCacheMangerTest {

    private UserCacheManager mManager;
    UserInfo mInfo0;
    UserInfo mInfo1;
    UserInfo mInfo2;
    UserInfo mInfo3;
    UserInfo mInfo4;
    UserInfo mInfo5;

    @Before
    public void setUp() throws Exception {
        mManager = new UserCacheManager();
        mManager.initialize();

        mInfo0 = new UserInfo("1", "2", "6");
        mInfo1 = new UserInfo("1", "1", "1");
        mInfo2 = new UserInfo("2", "2", "2");
        mInfo3 = new UserInfo("3", "3", "3");
        mInfo4 = new UserInfo("4", "4", "4");
        mInfo5 = new UserInfo("5", "5", "5");
    }

    @After
    public void tearDown() throws Exception {
        mManager.dispose();
    }

    @Test
    public void assignChosenPlayer() throws Exception {
        mManager.add(UserIndex.USER_0, mInfo0);
        mManager.add(UserIndex.USER_1, mInfo1);
        mManager.add(UserIndex.USER_2, mInfo2);
        mManager.add(UserIndex.USER_3, mInfo3);
        mManager.assignChosenPlayer(ChosenUserArea.SIDE_A_1ST, mInfo0);
//        mManager.assignChosenPlayer(ChosenUserArea.SIDE_A_2ND, mInfo1);
        mManager.assignChosenPlayer(ChosenUserArea.SIDE_B_1ST, mInfo2);
//        mManager.assignChosenPlayer(ChosenUserArea.SIDE_B_2ND, mInfo3);
        UserInfo uiSIDE_A_1ST = mManager.parseChosenPlayer(ChosenUserArea.SIDE_A_1ST);
        UserInfo uiSIDE_A_2ND = mManager.parseChosenPlayer(ChosenUserArea.SIDE_A_2ND);
        UserInfo uiSIDE_B_1ST = mManager.parseChosenPlayer(ChosenUserArea.SIDE_B_1ST);
//        UserInfo uiSIDE_B_2ND = mManager.parseChosenPlayer(ChosenUserArea.SIDE_B_2ND);
        Assert.assertTrue(mManager.parseChosenPlayer(ChosenUserArea.SIDE_A_1ST).equals(uiSIDE_A_1ST));
        Assert.assertTrue(mManager.parseChosenPlayer(ChosenUserArea.SIDE_B_1ST).equals(uiSIDE_B_1ST));
        Assert.assertFalse(mManager.parseChosenPlayer(ChosenUserArea.SIDE_B_1ST).equals(uiSIDE_A_2ND));
//        Assert.assertTrue(mManager.parseChosenPlayer(ChosenUserArea.SIDE_B_2ND).equals(uiSIDE_B_2ND));
    }

    @Test
    public void assignChosenPlayer1() throws Exception {

    }

    @Test
    public void parseChosenPlayer() throws Exception {

    }

    @Test
    public void hasUserLogin() throws Exception {
        Assert.assertFalse(mManager.atLeastOneUserLogin());
        mManager.add(mInfo0);
        Assert.assertTrue(mManager.atLeastOneUserLogin());
        Assert.assertTrue(mManager.isUserLogin(mInfo0));
        Assert.assertFalse(mManager.isUserLogin(mInfo1));
    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void getAll() throws Exception {

    }

    @Test
    public void count() throws Exception {
        sop(mManager.getUserLoginCount());
        mManager.add(mInfo0);
        sop(mManager.getUserLoginCount());
        mManager.add(mInfo1);
        sop(mManager.getUserLoginCount());
        mManager.add(mInfo2);
        sop(mManager.getUserLoginCount());
    }

    @Test
    public void add() throws Exception {
        int index = mManager.add(mInfo0);
        Assert.assertEquals(index, 0);

        index = mManager.add(mInfo0);
        Assert.assertEquals(index, UserCacheManager.ALREADY_EXIST);

        index = mManager.add(mInfo1);
        Assert.assertEquals(index, 1);

        int index1 = CollectionUtil.findIndexUseLoop(mManager.getAll(), mInfo3);
        Assert.assertEquals(index1, -1);
    }

    @Test
    public void add1() throws Exception {
        int index = mManager.add(UserIndex.USER_1, mInfo0);
        Assert.assertEquals(index, UserIndex.USER_1.index);

        index = mManager.add(UserIndex.USER_1, mInfo0);
        Assert.assertEquals(index, UserCacheManager.ALREADY_EXIST);

        mManager.add(UserIndex.USER_1, mInfo1);
        Assert.assertEquals(mManager.get(UserIndex.USER_1), mInfo1);
    }

    @Test
    public void resetAll() throws Exception {
        mManager.add(mInfo0);
        mManager.add(mInfo1);
        int index1 = CollectionUtil.findIndexUseLoop(mManager.getAll(), mInfo1);
        Assert.assertEquals(index1, 1);
        mManager.resetAll();
        index1 = CollectionUtil.findIndexUseLoop(mManager.getAll(), mInfo1);
        Assert.assertEquals(index1, -1);
    }

    @Test
    public void initialize() throws Exception {
    }

    @Test
    public void dispose() throws Exception {
        mManager.dispose();
    }

    @Test
    public void order() throws Exception {

    }

    @Test
    public void freeMemory() throws Exception {

    }
}