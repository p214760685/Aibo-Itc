package com.comtom.aibo.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.comtom.aibo.module.base.HomeActivity;
import com.haozi.dev.smartframe.utils.tool.UtilLog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/*************************************
 * 类名称：MyActivityManager
 * 类描述：提供退出应用的方法
 * 实现Application.ActivityLifecycleCallbacks接口
 **********************************/
public class MyActivityManager implements Application.ActivityLifecycleCallbacks {
    private ArrayList<WeakReference<Activity>> sActivity = new ArrayList();
    private static MyActivityManager instance;
    private MyActivityManager() {
    }

    private static class MY_ACTIVITY_MANAGER_INSTANCE {
        private static final MyActivityManager MY_ACTIVITY_MANAGER = new MyActivityManager();
    }

    public static MyActivityManager getInstance() {
        return MY_ACTIVITY_MANAGER_INSTANCE.MY_ACTIVITY_MANAGER;
    }


    public static MyActivityManager init(Application application){
        if (instance == null) {
            instance = new MyActivityManager();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    private void add(Activity activity) {
        if (activity != null) {
            WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
            sActivity.add(activityWeakReference);
        }
    }

    private void remove(Activity activity) {
        for (int i = 0, size = sActivity.size(); i < size; i++) {
            WeakReference<Activity> activityWeakReference = sActivity.get(i);
            Activity innerActivity = activityWeakReference.get();
            if (innerActivity == activity) {
                sActivity.remove(activityWeakReference);
                break;
            }
        }
    }

    /**
     * 获取MainActivity
     *
     * @return
     */
    public HomeActivity getMainActivity() {
        for (int i = 0, size = sActivity.size(); i < size; i++) {
            WeakReference<Activity> activityWeakReference = sActivity.get(i);
            Activity activity = activityWeakReference.get();
            if (activity instanceof HomeActivity) {
                return (HomeActivity) activity;
            }
        }
        return null;
    }



    /**
     * finish掉所有Activity
     */
    public void exitApp() {
        for (int i = 0, size = sActivity.size(); i < size; i++) {
            WeakReference<Activity> activityWeakReference = sActivity.get(i);
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                activity.finish();
            }
        }
        if (sActivity != null && sActivity.size() > 0) {
            sActivity.clear();
        }
        //释放内存，退出程序
//        System.exit(0);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //activity  create时，添加activity
        add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //activity  start时，可以做一些操作，例如记录此activity回到前台的时间等

    }

    @Override
    public void onActivityResumed(final Activity activity) {
        //activity  resume时，可以做一些操作，例如让一些后台任务重新开启，或者app切换到前台的时间等
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //activity  pause时，可以做一些操作，例如暂停一些后台任务
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //activity  stop时，可以做一些操作，例如记录app切换到后台的时间等
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        //保存状态
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //activity  destroy时，移除activity
        remove(activity);
    }
}
