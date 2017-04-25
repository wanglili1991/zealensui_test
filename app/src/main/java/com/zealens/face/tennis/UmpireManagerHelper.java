package com.zealens.face.tennis;

import android.content.Context;
import android.support.annotation.Nullable;

import com.zealens.face.common.ResourceConst;
import com.zealens.face.core.internal.TennisBase;
import com.zealens.face.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by KyleCe on 30/03/2017 in GNU.
 */

public class UmpireManagerHelper {

    public static TennisBase.InitParam generateInitBout(Context context) {
        return generateInitBout(context, false);
    }

    public static TennisBase.InitParam generateInitBout(Context context, boolean useTempDirectory) {
        String path = context.getFilesDir().getPath();
        String configPath = copyFilesAssetsFromPath(context, path, ResourceConst.CONFIG_DIR);
        if (configPath == null) return null;

        String tmpPath = copyFilesAssetsFromPath(context, path, ResourceConst.TEMP_DIR);
        if (tmpPath == null) return null;

        File configTmpPath = new File("");
        if (useTempDirectory) {
            configTmpPath = new File(context.getExternalCacheDir().toString()
                    + File.separator + ResourceConst.CONFIG_DIR);
            if (!configTmpPath.exists()) configTmpPath.mkdirs();
            FileUtil.copyDirectoryUsingApacheApi(new File(configPath), configTmpPath);
        }

        TennisBase.InitParam initParam = new TennisBase.InitParam();
        String appPath = context.getApplicationInfo().nativeLibraryDir;
        initParam.appPath = appPath + File.separator;
        initParam.cfgPath = (useTempDirectory ? configTmpPath.toString() : configPath) + File.separator;
        initParam.tmpPath = tmpPath + File.separator;
        return initParam;
    }

    @Nullable
    private static String copyFilesAssetsFromPath(Context context, String path, String dir) {
        String configPath = path + File.separator + dir;
        if (!copyFilesAssets(context, dir, configPath)) {
            return null;
        }
        return configPath;
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     * @return 成功返回 true， 失败返回 false
     */
    public static boolean copyFilesAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    copyFilesAssets(context, oldPath + File.separator + fileName, newPath + File.separator + fileName);
                }
            } else {
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[8096];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
