package com.zealens.face.core.internal;

/**
 * 网球的一个回合
 */
public class TennisBout implements MatchPresenter {
    static {
        System.loadLibrary("tennisbout");
    }

     /**
     * 初始化
     * @param initParam 初始化参数
     * @param matchCallback 回调函数
     * @return 成功返回 true；失败返回 false
     */
    public native boolean init(TennisBase.InitParam initParam, BoutCallback matchCallback);

    /**
     * 释放底层模块
     * @return 无返回值
     */
    @Override
    public native void dispose();

    /**
     * 设置球权
     * @param ballPower 球权
     * @return 成功返回 true；失败返回 false
     */
    public native boolean ballPower(@TennisBase.BallPower int ballPower);

    /**
     * 启动所有摄像头，捕捉视频并分析
     * @return 成功返回 true；失败返回 false
     */
    public native boolean start();

    /**
     * 通知底层暂时停止数据处理
     * @return 成功返回 true；失败返回 false
     */
    public native boolean pause();

    /**
     * 通知重新开始处理数据
     * @return 成功返回 true；失败返回 false
     */
    public native boolean resume();

    /**
     * 终止底层服务
     * @return 成功返回 true；失败返回 false
     */
    public native boolean stop();

    /**
     * 获取底层数据
     * @return 成功返回 true；失败返回 false
     */
    public native boolean runOnce();

    /**
     * 开始一个新的回合
     * @return 成功返回 true；失败返回 false
     */
    public native boolean restart(@TennisBase.CourtArea int serveArea);
}
