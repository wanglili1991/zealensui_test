package com.zealens.face;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.HandlerThread;
import android.os.Process;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.zealens.face.core.CoreApplication;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.analyze.AnalyticsManager;
import com.zealens.face.core.config.PreferencesManager;
import com.zealens.face.core.connection.NetworkManager;
import com.zealens.face.data.user.UserCacheManager;
import com.zealens.face.tennis.UmpireManagerAppLevel;
import com.zealens.face.util.FontMaster;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RealApplication extends CoreApplication {
    private static final String TAG = RealApplication.class.getSimpleName();
    private static final int DEFAULT_THREAD_POOL_SIZE = 4;

    private static final int PROCESS_MAIN = 0;
    private static final int PROCESS_SERVICE = 1;

    private static final int PROCESS_DEAMON_MIN = 10;
    private static final int PROCESS_DEAMON_1 = 11;
    private static final int PROCESS_DEAMON_2 = 12;
    private static final int PROCESS_DEAMON_3 = 13;
    private static final int PROCESS_DEAMON_MAX = 20;

    private final AtomicInteger mThreadId = new AtomicInteger(1);

    private int mProcessType;

    @Override
    protected final ExecutorService onCreateWorkerThreadPool() {
        return createThreadPool(Thread.NORM_PRIORITY);
    }

    @Override
    protected final ExecutorService onCreateBackgroundThreadPool() {
        return createThreadPool(Thread.MIN_PRIORITY);
    }

    @Override
    protected ScheduledThreadPoolExecutor onCreateScheduledThreadPool() {
        return new ScheduledThreadPoolExecutor(2) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                logException(r, t);
            }
        };
    }

    @Override
    protected final HandlerThread onCreateWorkerThread(final String poolName) {
        return new HandlerThread(RealApplication.class.getName());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        mProcessType = getProcessType(base);
    }

    @Override
    public void initialize() {
        if (!BuildConfig.PUBLISH_FLAG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
//                    .penaltyDialog()
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
        }

        if (isProcessDeamon())
            return;


        if (isProcessMain()) {
            FontMaster.init(getApplicationContext());
        }

        super.initialize();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Initialize a variety of services here
     */
    @Override
    protected void onInitializeApplicationService(final CoreContext coreContext) {
        if (isProcessDeamon())
            return;

        coreContext.addApplicationService(new PreferencesManager(coreContext));
        coreContext.addApplicationService(new NetworkManager(coreContext));
        coreContext.addApplicationService(new AnalyticsManager(coreContext));
        coreContext.addApplicationService(new UserCacheManager());
        coreContext.addApplicationService(new UmpireManagerAppLevel(coreContext));
    }

    private boolean isProcessDeamon() {
        return mProcessType > PROCESS_DEAMON_MIN && mProcessType < PROCESS_DEAMON_MAX;
    }

    private boolean isProcessMain() {
        return mProcessType == PROCESS_MAIN;
    }

    private int getProcessType(Context context) {
        SparseArray<String> processMap = new SparseArray<>(5);

        try {
            String processName = context.getPackageManager().getApplicationInfo(getPackageName(), 0).processName.trim();
            processMap.put(PROCESS_MAIN, processName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        String processName = "";
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo.pid == pid) {
                processName = runningAppProcessInfo.processName.trim();
                break;
            }
        }

        for (int index = 0; index < processMap.size(); index++) {
            if (processMap.valueAt(index).compareTo(processName) == 0) {
                return processMap.keyAt(index);
            }
        }

        return -1;
    }

    private ExecutorService createThreadPool(int priority) {
        int maximumPoolSize = DEFAULT_THREAD_POOL_SIZE + 1;
        final int processors = Runtime.getRuntime().availableProcessors();
        if (processors > 1) {
            maximumPoolSize = processors * 2 + 1;
        }

        return new ThreadPoolExecutor(2, maximumPoolSize, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
            private final ThreadGroup mThreadGroup = System.getSecurityManager() != null ? System.getSecurityManager().getThreadGroup() : Thread.currentThread().getThreadGroup();
            private final AtomicInteger mAtomic = new AtomicInteger(1);
            private final String mThreadName = "optimize-master-pool-" + mThreadId.getAndIncrement() + "-thread-";

            @Override
            public Thread newThread(@NonNull Runnable task) {
                Thread thread = new Thread(mThreadGroup, task, mThreadName + mAtomic.getAndIncrement(), 0);
                if (thread.isDaemon()) {
                    thread.setDaemon(false);
                }

                thread.setPriority(priority);
                return thread;
            }
        }) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                logException(r, t);
            }
        };
    }
}
