package com.zealens.face.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ServiceConnection;

/**
 * Created on 2017/3/17
 * in BlaBla by Kyle
 */

public class EndingUtil4Android extends EndingUtil {
    public static <B extends BroadcastReceiver> void unregisterReceiverSafely(Context ctx, B b) {
        if (b == null || ctx == null) return;

        try {
            ctx.unregisterReceiver(b);
        } catch (IllegalArgumentException lae) {
            lae.printStackTrace();
        }
    }

    public static <S extends ServiceConnection> void unbindServiceSafely(Context ctx, S s) {
        if (s == null || ctx == null) return;

        try {
            ctx.unbindService(s);
        } catch (IllegalArgumentException lae) {
            lae.printStackTrace();
        }
    }

    public static <B extends BroadcastReceiver> void abortBroadcastSafely(B b) {
        if (b == null) return;

        try {
            b.abortBroadcast();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
