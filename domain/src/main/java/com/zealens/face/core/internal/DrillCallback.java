package com.zealens.face.core.internal;

/**
 * Created by yuflh on 2017/4/5.
 */

public interface DrillCallback extends DrillBase {

    /**
     * 训练结束后，会触发正式结束（表达底层正式停止）
     */
    void onEnd();

    /**
     * 下一回合训练已经准备好，可以开始喂球或者发球
     */
    void onDrillNext();

    /**
     * internal error
     *
     * @param code         error code
     * @param errorMessage
     */
    void onError(int code, String errorMessage);
}
