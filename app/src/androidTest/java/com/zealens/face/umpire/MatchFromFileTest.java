package com.zealens.face.umpire;

import android.content.Context;
import android.test.ApplicationTestCase;

import com.zealens.face.RealApplication;
import com.zealens.face.common.ResourceConst;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.umpire.UmpireManager;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.umpire.algorithm.MatchFileAlgorithm;
import com.zealens.face.util.FileUtil;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import java.io.File;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MatchFromFileTest extends ApplicationTestCase<RealApplication> {
    private UmpireManager mSpy;
    private Context mContext;
    private File mConfigPath;
    private int mIndex;
    UmpireManager mUmpireManager;
    @Rule
    public final TestRule globalTimeout = Timeout.millis(10000L);

    public MatchFromFileTest() {
        super(RealApplication.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        mContext = getInstrumentation().getTargetContext();
        initSpy();
    }

    private void initSpy() {
        RealApplication app = getApplication();
        mUmpireManager = (UmpireManager) app.getCoreContext().getApplicationService(UmpireManagerAppLevel.class);
        mUmpireManager.initialize();
    }

    private void replaceConfigFileEventId(@MatchFileAlgorithm.EventIdIndex int index) throws java.io.IOException {
        mConfigPath = new File(mContext.getExternalCacheDir().toString()
                + File.separator + ResourceConst.CONFIG_DIR);
        File file = new File(mConfigPath.toString() + File.separator + ResourceConst.CONFIG_FILE_NAME);
        FileUtil.replaceCertainLineInFileLine(file, ResourceConst.CONFIG_PARSING_FILE_LINE_TAG
                , ResourceConst.CONFIG_PARSING_FILE_LINE_TAG + MatchFileAlgorithm.EVENT_ID_ARR[index]);
    }

    @SuppressWarnings("MagicConstant")
    @Test
    public void testStartBout() throws Exception {
        mIndex = MatchFileAlgorithm.EventIdIndex.D000;
        replaceConfigFileEventId(mIndex);

        mUmpireManager.assignComponentsByPlayModel(TennisBase.PlayModel.MATCH_SINGLE);
        mSpy = spy(mUmpireManager);

        mSpy.start();
//        verify(mSpy, timeout(1000).times(1)).onServeFailed();
//        verify(mSpy, timeout(1000).atLeastOnce()).onAddScore(anyInt(), any(), any(), any());
//        verify(mSpy, timeout(1000).atLeast(2)).onServeSuccessful();

        verify(mSpy, timeout(10000).atLeastOnce()).onBall(any());
        verify(mSpy, timeout(10000).atLeastOnce()).onServe(any());

        while (mSpy.runOnce()) {
        }
        Score score = mSpy.getScore();
        assertThat(score, MatchFileAlgorithm.matches(mIndex));
    }

    @AfterClass
    public void tearDown() throws Exception {
        if (mConfigPath != null)
            FileUtils.deleteDirectory(mConfigPath);
    }
}
