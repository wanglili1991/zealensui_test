package com.zealens.face;

import com.zealens.face.util.FileUtil;

import org.junit.Test;

import static com.zealens.face.util.LogcatUtil.sop;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Example {
    @Test
    public void nameFormPath() throws Exception {
        sop(FileUtil.getFileNameFromAbsolutePath("/dev/tre/3/323/fjdso.jpg"));
    }
}