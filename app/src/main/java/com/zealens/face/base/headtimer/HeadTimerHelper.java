package com.zealens.face.base.headtimer;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kyle on 11/04/2017
 */

public class HeadTimerHelper {
    public static String generateTimerString(long m) {
        long hour = TimeUnit.MILLISECONDS.toHours(m);
        long minute = TimeUnit.MILLISECONDS.toMinutes(m) - TimeUnit.HOURS.toMinutes(hour);
        long second = TimeUnit.MILLISECONDS.toSeconds(m)
                - TimeUnit.MINUTES.toSeconds(minute)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.HOURS.toMinutes(hour));
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
