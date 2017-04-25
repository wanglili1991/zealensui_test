package com.zealens.face.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zealens.face.R;
import com.zealens.face.common.GeneralConst;
import com.zealens.face.core.CoreApplication;
import com.zealens.face.core.CoreContext;
import com.zealens.face.core.CoreThreadPool;
import com.zealens.face.core.analyze.AnalyticsInterface;
import com.zealens.face.core.analyze.AnalyticsManager;
import com.zealens.face.core.config.PreferencesManager;
import com.zealens.face.core.config.PreferencesService;
import com.zealens.face.core.connection.NetworkManager;
import com.zealens.face.core.log.CoreLogger;
import com.zealens.face.core.log.Logger;
import com.zealens.face.presenter.BasePresenter;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class BaseActivity extends AppCompatActivity
        implements CoreThreadPool, CoreLogger, AnalyticsInterface, PreferencesService {

    private final int mFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    protected final String TAG;
    protected Context mContext;
    protected Activity mActivity;
    protected PreferencesManager mPreferencesManager;
    protected NetworkManager mNetworkManager;
    protected AnalyticsManager mAnalyticsManager;
    protected ViewGroup mActivityContent;
    protected BasePresenter mBasePresenter;
    protected View mActionButton;

    public BaseActivity() {
        TAG = getClass().getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        mActivity = this;

        mNetworkManager = (NetworkManager) getCoreContext().getApplicationService(NetworkManager.class);
        mPreferencesManager = (PreferencesManager) getCoreContext().getApplicationService(PreferencesManager.class);
        mAnalyticsManager = (AnalyticsManager) getCoreContext().getApplicationService(AnalyticsManager.class);

        parseIntent(getIntent());

        initDataIgnoringUi();

        requestFullscreenAndImmersiveSticky(getWindow().getDecorView());

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                requestFullscreenAndImmersiveSticky(decorView);
            }
        });

        int layoutResId = getLayoutResourceId();
        if (layoutResId != 0) setContentView(layoutResId);
        mActivityContent = (ViewGroup) findViewById(android.R.id.content);
        viewAffairs();

        if (showBackPressButton()) {
            View backButton = LayoutInflater.from(mContext).inflate(R.layout.back_button_layout, null, false);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM;
            int leftM = getResources().getDimensionPixelOffset(R.dimen.dp24);
            int bottomM = getResources().getDimensionPixelOffset(R.dimen.control_button_margin_bottom);
            lp.setMargins(leftM, 0, 0, bottomM);
            mActivityContent.addView(backButton, lp);
            backButton.setOnClickListener((v -> onBackPressed()));
        }

        if (showBottomControlButton()) {
            mActionButton = LayoutInflater.from(mContext).inflate(R.layout.action_button_layout, null, false);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            int bottomM = getResources().getDimensionPixelOffset(R.dimen.control_button_margin_bottom);
            lp.setMargins(0, 0, 0, bottomM);
            mActivityContent.addView(mActionButton, lp);
            TextView textView = (TextView) mActionButton.findViewById(R.id.action_button);
            textView.setText(prepareActionText());
            mActionButton.setOnClickListener((v -> assembleBottomControlButtonAction().run()));
        }

        assembleDataAfterUiAffairs();
    }

    protected void initDataIgnoringUi() {
    }

    protected void assembleDataAfterUiAffairs() {
    }

    protected boolean showBackPressButton() {
        return true;
    }

    protected boolean showBottomControlButton() {
        return false;
    }

    protected Runnable assembleBottomControlButtonAction() {
        return null;
    }

    protected String prepareActionText() {
        return "";
    }

    @LayoutRes
    protected int getLayoutResourceId() {
        return 0;
    }

    protected void viewAffairs() {
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            requestFullscreenAndImmersiveSticky(getWindow().getDecorView());
        }
    }

    protected void requestFullscreenAndImmersiveSticky(View v) {
        v.setSystemUiVisibility(mFlags);
    }

    @SuppressWarnings("ResourceType")
    protected void parseIntent(Intent intent) {
        if (intent == null) return;

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            parseNonNullBundle(bundle);
        }
    }

    protected void parseNonNullBundle(Bundle bundle) {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBasePresenter != null) mBasePresenter.dispose();
    }

    @Override
    public final ExecutorService getWorkerThreadPool() {
        return ((CoreApplication) getApplication()).getWorkerThreadPool();
    }

    @Override
    public ScheduledThreadPoolExecutor getScheduleExecutor() {
        return ((CoreApplication) getApplication()).getScheduleExecutor();
    }

    @Override
    public final HandlerThread getWorkerThread() {
        final CoreContext coreContext = getCoreContext();
        if (coreContext == null)
            return null;

        return ((CoreApplication) getApplication()).getWorkerThread();
    }

    @Override
    public final Handler getWorkerHandler() {
        return ((CoreApplication) getApplication()).getWorkerHandler();
    }

    @Override
    public void executeAsyncTask(@NonNull final AsyncTask<?, ?, ?> task) {
        ((CoreApplication) getApplication()).executeAsyncTask(task);
    }

    @Override
    public void executeAsyncTask(@NonNull final FutureTask<?> task) {
        ((CoreApplication) getApplication()).executeAsyncTask(task);
    }

    @Override
    public void executeAsyncTask(@NonNull final Callable<?> callable) {
        ((CoreApplication) getApplication()).executeAsyncTask(callable);
    }

    @Override
    public void executeAsyncTask(@NonNull final Runnable task) {
        ((CoreApplication) getApplication()).executeAsyncTask(task);
    }

    @Override
    public <T> Future<?> submitByExecutorService(Callable<T> task) {
        return ((CoreApplication) getApplication()).submitByExecutorService(task);
    }

    @Override
    public <T> Future<?> submitByExecutorService(Runnable task, T result) {
        return ((CoreApplication) getApplication()).submitByExecutorService(task, result);
    }

    @Override
    public Future<?> submitByExecutorService(Runnable task) {
        return ((CoreApplication) getApplication()).submitByExecutorService(task);
    }

    @Override
    public void executeAsyncBackgroundTask(@NonNull Runnable task) {
        ((CoreApplication) getApplication()).executeAsyncBackgroundTask(task);
    }

    @Override
    public void executeAsyncTaskOnQueue(@NonNull Runnable task) {
        ((CoreApplication) getApplication()).executeAsyncTaskOnQueue(task);
    }

    @Override
    public void executeAsyncTaskWithFixedDelay(long initialDelay, long delay, @NonNull Runnable task) {
        ((CoreApplication) getApplication()).executeAsyncTaskWithFixedDelay(initialDelay, delay, task);
    }

    @Override
    public boolean removeExecuteAsyncTask(@NonNull Runnable task) {
        return ((CoreApplication) getApplication()).removeExecuteAsyncTask(task);
    }

    @Override
    public void postTask(@NonNull final Runnable task) {
        ((CoreApplication) getApplication()).postTask(task);
    }

    @Override
    public void postTaskAtFront(@NonNull final Runnable task) {
        ((CoreApplication) getApplication()).postTaskAtFront(task);
    }

    @Override
    public void postTaskAsDelayed(@NonNull final Runnable task, final long delayMillis) {
        ((CoreApplication) getApplication()).postTaskAsDelayed(task, delayMillis);
    }

    @Override
    public void postTaskAtTime(@NonNull final Runnable task, final long uptimeMillis) {
        ((CoreApplication) getApplication()).postTaskAtTime(task, uptimeMillis);
    }

    @Override
    public void v(final String msg) {
        Logger.v(TAG, msg);
    }

    @Override
    public void i(final String msg) {
        Logger.i(TAG, msg);
    }

    @Override
    public void d(final String msg) {
        Logger.d(TAG, msg);
    }

    @Override
    public void w(final String msg) {
        Logger.w(TAG, msg);
    }

    @Override
    public void e(final String msg) {
        Logger.e(TAG, msg);
    }

    @Override
    public void logEvent(final String msg) {
    }

    @Override
    public void logException(final String msg, final Exception e) {
        mAnalyticsManager.logException(msg, e);
    }

    @Override
    public void logException(final Exception e) {
        mAnalyticsManager.logException(e);
    }

    @Override
    public void put(final String key, final Object value) {
        mPreferencesManager.put(key, value);
    }

    @Override
    public Object get(final String key, final Object defaultValue) {
        return mPreferencesManager.get(key, defaultValue);
    }

    @Override
    public void remove(final String key) {
        mPreferencesManager.remove(key);
    }

    @Override
    public void clear() {
        mPreferencesManager.clear();
    }

    @Override
    public boolean contains(final String key) {
        return mPreferencesManager.contains(key);
    }

    @Override
    public HashMap<String, ?> getAll() {
        return mPreferencesManager.getAll();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected CoreContext getCoreContext() {
        return ((CoreApplication) getApplication()).getCoreContext();
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    protected Message generateMsg(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        return msg;
    }

    public NetworkManager getNetworkManager() {
        return mNetworkManager;
    }

    public boolean noLongerAlive() {
        return noLongerAlive(mActivity);
    }

    public boolean noLongerAvailable(WeakReference<Activity> reference) {
        return reference == null || noLongerAlive(reference.get());
    }

    public boolean noLongerAlive(Activity activity) {
        return activity == null || activity.isFinishing();
    }

    public void startActivity(Class clz) {
        startActivity(clz, null);
    }

    public void startActivity(Class clz, Bundle bundle) {
        startActivity(clz, bundle, GeneralConst.IGNORE_FLAG, false);
    }

    public void startActivity(Class clz, int flags) {
        startActivity(clz, flags, false);
    }

    public void startActivity(Class clz, int flags, boolean clearOtherFlags) {
        startActivity(clz, null, flags, clearOtherFlags);
    }

    public void startActivity(Class clz, Bundle bundle, int flags, boolean clearOtherFlags) {
        Intent intent = new Intent(mContext, clz).putExtras(bundle);
        if (flags != GeneralConst.IGNORE_FLAG) {
            if (clearOtherFlags) {
                intent.setFlags(flags);
            } else {
                intent.addFlags(flags);
            }
        }
        startActivity(intent);
    }

    public void executeTaskIfNotNull(@Nullable Runnable r) {
        if (r != null) r.run();
    }

    public void startActivityWithData(Class clz, Map<String, Object> map) {
        Bundle bundle = new Bundle();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof Integer)
                    bundle.putInt(entry.getKey(), (Integer) entry.getValue());
                else if (entry.getValue() instanceof String)
                    bundle.putString(entry.getKey(), (String) entry.getValue());
                else if (entry.getValue() instanceof Boolean)
                    bundle.putBoolean(entry.getKey(), (Boolean) entry.getValue());
                else if (entry.getValue() instanceof Long)
                    bundle.putLong(entry.getKey(), (Long) entry.getValue());
                else if (entry.getValue() instanceof int[])
                    bundle.putIntArray(entry.getKey(), (int[]) entry.getValue());
                else if (entry.getValue() instanceof String[])
                    bundle.putStringArray(entry.getKey(), (String[]) entry.getValue());
            }
        }
        startActivity(new Intent(mContext, clz).putExtras(bundle));
    }
}