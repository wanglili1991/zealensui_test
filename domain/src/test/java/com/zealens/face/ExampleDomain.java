package com.zealens.face;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zealens.face.base.Rule;
import com.zealens.face.domain.DomainConst;
import com.zealens.face.domain.module.Score;
import com.zealens.face.domain.module.Video;
import com.zealens.face.domain.umpire.UmpireDelegate;
import com.zealens.face.util.CollectionUtil;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleDomain {

    @Test
    public void testReturnValueDependentOnMethodParameter() {
        Comparable c = Mockito.mock(Comparable.class);
        Mockito.when(c.compareTo("Mockito")).thenReturn(1);
        assertEquals(1, c.compareTo("Mockito"));
    }

    @Test
    public void arrayMatrix() {
        int[][] score = new int[][]{{0, 1}, {2, 0}, {3, 3}};
        assertEquals(score[0][1], 1);
        assertEquals(score[1][0], 2);
        assertEquals(score[2][1], 3);

        Score s = new Score();
        assertEquals(s.bout[Rule.Side.B], 0);
        s = new Score(score);
        s.bout[Rule.Side.A] = 12;
        assertEquals(s.bout[Rule.Side.B], 1);
        assertEquals(s.game[Rule.Side.A], 2);
    }

    @Test
    public void expandArray() throws Exception {
        Object[][] what = new Object[2][2];
        what[0] = CollectionUtil.assembleCapacityExpandedArray(what[0], 100);
        assertEquals(what[0].length, 102);
    }

    @Test
    public void fileRenameOnWindowsWithApache() throws Exception {
        if (!System.getProperty("os.name").contains("Windows")) return;

        String path = "D:\\projects\\zealensui\\temp";
        String fileName = "test.txt";
        String prefix = "prefix_";
        String suffix = "_suffix";

        int dotIndex = fileName.lastIndexOf(".");
        String edited = prefix + fileName.substring(0, dotIndex) + suffix + fileName.substring(dotIndex);
        if (new File(path + "\\" + fileName).exists() && new File(path + "\\" + edited).exists()) {
            FileUtils.moveFile(
                    FileUtils.getFile(path + "\\" + fileName),
                    FileUtils.getFile(path + "\\" + edited));
            assertFalse(new File(path + "\\" + fileName).exists());
            assertTrue(new File(path + "\\" + edited).exists());
        }
    }

    @Test
    public void gson() throws Exception {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting().create();
        Map<Long, String> original = new HashMap<>();
        original.put(123L, "a");
        original.put(1232L, "b");
        System.out.println(gson.toJson(original));

    }

    @Test
    public void path() throws Exception {
       String path =  DomainConst.X_NIX_VIDEO_PATH ;
       File p = new File(path);
       if(!p.exists())
           p.mkdir();
    }

    @Test
    public void videoNameParsing() throws Exception {
        String scoreWithDivider = "0-1-0-0-0-0";
        String path = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-" + scoreWithDivider +
                ";start==1492756669536;lasting==3;A.mp4";
        Video video = UmpireDelegate.parseVideoInfoByPath(path);
        assertEquals(scoreWithDivider, video.score.getScoreInt(DomainConst.SCORE_DIVIDER));
        assertTrue(video.timeLast >= 0);
        assertEquals(Rule.VideoTag.GENERAL, video.tag);
    }

    @Test
    public void dividerInsert() throws Exception {
        String[] data = new String[]{};
        /*
        *0 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-2-2-0-2-0-0;start==1492941747651;lasting==673;A.mp4"
1 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-3-3-0-2-0-0;start==1492941748421;lasting==70;A.mp4"
2 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-4-3-0-2-0-0;start==1492941748491;lasting==77;A.mp4"
3 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-0-1-1-2-0-0;start==1492941748708;lasting==64;A.mp4"
4 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-2-2-1-3-0-0;start==1492941749356;lasting==74;A.mp4"
5 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-2-2-0-0-0-0;start==1492941746692;lasting==97;A.mp4"
6 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-0-1-0-1-0-0;start==1492941746949;lasting==79;A.mp4"
7 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-0-3-0-1-0-0;start==1492941747201;lasting==48;A.mp4"
8 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-1-1-0-2-0-0;start==1492941747414;lasting==95;B.mp4"
9 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-2-3-0-2-0-0;start==1492941748358;lasting==61;B.mp4"
10 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-0-0-1-2-0-0;start==1492941748568;lasting==139;A.mp4"
11 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-1-0-1-3-0-0;start==1492941749074;lasting==118;A.mp4"
12 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-1-0-0-0-0;start==1492941746377;lasting==77;A.mp4"
13 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-1-0-0-0-0;start==1492941746377;lasting==77;B.mp4"
14 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-2-0-0-0-0;start==1492941746455;lasting==147;A.mp4"
15 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-2-0-0-0-0;start==1492941746455;lasting==147;B.mp4"
16 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-2-0-0-0-0;start==1492941746604;lasting==88;A.mp4"
17 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-2-0-0-0-0;start==1492941746604;lasting==88;B.mp4"
18 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-2-0-0-0-0;start==1492941746692;lasting==97;B.mp4"
19 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-2-2-0-0-0-0;start==1492941746692;lasting==97;A.mp4"
20 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-3-0-0-0-0;start==1492941746791;lasting==71;A.mp4"
21 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-3-0-0-0-0;start==1492941746791;lasting==71;B.mp4"
22 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-0-1-0-0;start==1492941746863;lasting==76;A.mp4"
23 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-0-1-0-0;start==1492941746863;lasting==76;B.mp4"
24 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-1-0-1-0-0;start==1492941746949;lasting==79;B.mp4"
25 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-0-1-0-1-0-0;start==1492941746949;lasting==79;A.mp4"
26 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-2-0-1-0-0;start==1492941747039;lasting==161;A.mp4"
27 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-2-0-1-0-0;start==1492941747039;lasting==161;B.mp4"
28 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-3-0-1-0-0;start==1492941747201;lasting==48;B.mp4"
29 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-0-3-0-1-0-0;start==1492941747201;lasting==48;A.mp4"
30 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-0-2-0-0;start==1492941747249;lasting==107;A.mp4"
31 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-0-2-0-0;start==1492941747249;lasting==107;B.mp4"
32 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-0-0-2-0-0;start==1492941747357;lasting==56;B.mp4"
33 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-1-0-0-2-0-0;start==1492941747357;lasting==56;A.mp4"
34 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-1-0-2-0-0;start==1492941747414;lasting==95;A.mp4"
35 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-1-1-0-2-0-0;start==1492941747414;lasting==95;B.mp4"
36 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-2-0-2-0-0;start==1492941747515;lasting==132;A.mp4"
37 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-2-0-2-0-0;start==1492941747515;lasting==132;B.mp4"
38 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-2-0-2-0-0;start==1492941747651;lasting==673;B.mp4"
39 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-2-2-0-2-0-0;start==1492941747651;lasting==673;A.mp4"
40 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-3-0-2-0-0;start==1492941748358;lasting==61;A.mp4"
41 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-2-3-0-2-0-0;start==1492941748358;lasting==61;B.mp4"
42 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-3-3-0-2-0-0;start==1492941748421;lasting==70;B.mp4"
43 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-3-3-0-2-0-0;start==1492941748421;lasting==70;A.mp4"
44 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-4-3-0-2-0-0;start==1492941748491;lasting==77;B.mp4"
45 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-4-3-0-2-0-0;start==1492941748491;lasting==77;A.mp4"
46 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-1-2-0-0;start==1492941748568;lasting==139;B.mp4"
47 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-0-0-1-2-0-0;start==1492941748568;lasting==139;A.mp4"
48 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-1-1-2-0-0;start==1492941748708;lasting==64;B.mp4"
49 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-0-1-1-2-0-0;start==1492941748708;lasting==64;A.mp4"
50 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-2-1-2-0-0;start==1492941748773;lasting==98;A.mp4"
51 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-2-1-2-0-0;start==1492941748773;lasting==98;B.mp4"
52 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-3-1-2-0-0;start==1492941748872;lasting==103;A.mp4"
53 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-3-1-2-0-0;start==1492941748872;lasting==103;B.mp4"
54 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-1-3-0-0;start==1492941748976;lasting==97;A.mp4"
55 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-1-3-0-0;start==1492941748976;lasting==97;B.mp4"
56 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-0-1-3-0-0;start==1492941749074;lasting==118;B.mp4"
57 = "/storage/emulated/0/tmp/video/tag==2;end==bout=game=set=-1-0-1-3-0-0;start==1492941749074;lasting==118;A.mp4"
58 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-1-1-3-0-0;start==1492941749193;lasting==75;A.mp4"
59 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-1-1-3-0-0;start==1492941749193;lasting==75;B.mp4"
60 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-2-1-3-0-0;start==1492941749269;lasting==87;A.mp4"
61 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-1-2-1-3-0-0;start==1492941749269;lasting==87;B.mp4"
62 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-2-1-3-0-0;start==1492941749356;lasting==74;B.mp4"
63 = "/storage/emulated/0/tmp/video/tag==1;end==bout=game=set=-2-2-1-3-0-0;start==1492941749356;lasting==74;A.mp4"
64 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-3-1-3-0-0;start==1492941749434;lasting==86;A.mp4"
65 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-2-3-1-3-0-0;start==1492941749434;lasting==86;B.mp4"
66 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-1-4-0-0;start==1492941749525;lasting==106;A.mp4"
67 = "/storage/emulated/0/tmp/video/tag==8;end==bout=game=set=-0-0-1-4-0-0;start==1492941749525;lasting==106;B.mp4"*
        *
        * * */
    }
}