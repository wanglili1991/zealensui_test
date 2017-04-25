package com.zealens.face.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;


/**
 * Created by KyleCe on 2016/8/1.
 *
 * @author: KyleCe
 */
public class EndingUtil {

    public static void closeSilently(Closeable... closeables) {
        for (Closeable c : closeables) closeSilently(c);
    }

    public static void closeSilently(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException t) {
            t.printStackTrace();
        }
    }

    public static <E extends ExecutorService> void shutDownExecutorServiceSafely(E executor) {
        if(executor == null) return;

        try {
            if (!executor.isShutdown())
                executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
