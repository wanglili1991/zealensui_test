package com.zealens.face.umpire;

import com.zealens.face.core.internal.IPCameraPresenter;
import com.zealens.face.domain.coach.FakeIPCameraImpl;

import org.junit.Test;

/**
 * Created by Kyle on 18/04/2017
 */

public class IPCameraTest {

    @Test
    public void camera() throws Exception {
        IPCameraPresenter ip = new FakeIPCameraImpl();
        ip.startRecord("");
    }
}
