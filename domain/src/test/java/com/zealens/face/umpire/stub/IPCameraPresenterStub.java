package com.zealens.face.umpire.stub;

import com.zealens.face.core.internal.IPCameraPresenter;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2017/3/28
 * in BlaBla by Kyle
 */

public class IPCameraPresenterStub implements IPCameraPresenter {

    @Override
    public boolean init(String tmpPath, String ip, short port, String userName, String password) {
        return true;
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean startRecord(String savePath) {
        Assert.assertNotNull(savePath);
        File file = new File(savePath);
        if (!file.exists()) {
            try {
                boolean r = file.createNewFile();
//                sop(r);
//                sop("file name:" + FileUtil.getFileNameFromAbsolutePath(file.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean stopRecord() {
        return false;
    }
}
