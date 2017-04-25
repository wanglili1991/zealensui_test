package com.zealens.face.presenter.train.zone;

import com.zealens.face.base.Rule;
import com.zealens.face.presenter.BasePresenter;

/**
 * Created on 2017/3/7
 * in BlaBla by Kyle
 */

public interface ZoneShowPresenter extends BasePresenter {

    Runnable assembleControlUnitResponse();

    void modeDisplay(@Rule.TrainMode int mode, @Rule.Level int level);
}
