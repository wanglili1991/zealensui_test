package com.zealens.face.domain.coach;

import com.zealens.face.core.internal.IPCameraPresenter;
import com.zealens.face.util.FileUtil;

import java.io.File;

/**
 * Created by Kyle on 18/04/2017
 */

public class FakeIPCameraImpl implements IPCameraPresenter {
    File mSource;
    File mPrefix;
    public static final String PATH = "/tmp/A.mp4";

    public FakeIPCameraImpl() {
        init();
    }

    public FakeIPCameraImpl(File mPrefix) {
        this.mPrefix = mPrefix;
        init();
    }

    private void init() {
        mSource = new File((mPrefix == null ? "" : mPrefix.toString()) + PATH);
    }

    @Override
    public boolean init(String tmpPath, String ip, short port, String userName, String password) {
        return mSource.exists();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean startRecord(String savePath) {
        File file = new File(savePath);
        FileUtil.copyFileUsingApacheApi(mSource, file);
        return true;
    }

    @Override
    public boolean stopRecord() {
        return true;
    }
}
