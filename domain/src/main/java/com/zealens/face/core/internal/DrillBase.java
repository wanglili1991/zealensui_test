package com.zealens.face.core.internal;

/**
 * Created by Kyle on 12/04/2017
 */

public interface DrillBase {
    /**
     * 开始训练后，会触发正式开始（表达底层已经准备好）
     */
    void onBegin();

    /**
     * 训练过程中，出现状态变化，则触发此接口
     *
     * @param drillBoxScore 训练数据
     */
    void onDataChange(TennisBase.DrillBoxScore drillBoxScore);

    /**
     * 速度发生变化时，则触发此接口
     *
     * @param speed 当前球速
     */
    void onSpeedChange(int speed);


    void onEnd();
}
