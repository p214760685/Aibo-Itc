package com.comtom.aibo.step;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;


public class killSelfService extends Service {
    /**关闭应用后多久重新启动*/

    private static long stopDelayed=2000;

    private Handler handler;

    private String PackageName;

    public killSelfService() {
        handler=new Handler();

    }

    @Override

    public int onStartCommand(final Intent intent, int flags, int startId) {
        stopDelayed=intent.getLongExtra("Delayed",2000);

        PackageName=intent.getStringExtra("PackageName");

        handler.postDelayed(()->{
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(PackageName);

            startActivity(LaunchIntent);

            killSelfService.this.stopSelf();

        },stopDelayed);

        return super.onStartCommand(intent, flags, startId);

    }

    @Override

    public IBinder onBind(Intent intent) {
        return null;

    }

}
