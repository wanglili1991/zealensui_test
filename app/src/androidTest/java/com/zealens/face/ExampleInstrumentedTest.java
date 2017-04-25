package com.zealens.face;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.zealens.face.common.ResourceConst;
import com.zealens.face.util.MediaHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
//@RunWith(MockitoJUnitRunner.class)
public class ExampleInstrumentedTest {
    private Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void useAppContext() throws Exception {

        assertEquals("com.zealens.zealensui", appContext.getPackageName());
    }

    @Test
    public void testReturnValueDependentOnMethodParameter() {
        Comparable c = Mockito.mock(Comparable.class);
        Mockito.when(c.compareTo("Mockito")).thenReturn(1);
        assertEquals(1, c.compareTo("Mockito"));
    }

    @Test
    public void listenAudio() throws Exception {
        MediaPlayer mp = new MediaPlayer();
        MediaPlayer spy = Mockito.spy(mp);
        MediaHelper.playAudioUnderAssets(appContext, spy
                , ResourceConst.AUDIO_DIR + File.separator + ResourceConst.Audio.ACE);
    }
}
