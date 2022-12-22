package com.comtom.aibo.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.comtom.aibo.greendao.gen.DaoMaster;
import com.comtom.aibo.greendao.gen.DaoSession;
import com.comtom.aibo.httpset.RequestSetting;
import com.haozi.dev.smartframe.rxhttp.core.RxHttp;

public class AirApp extends Application{

	private static AirApp mInstance;
	WakeLock wakeLock;
	private DaoSession daoSession;//数据库操作块
	
	@SuppressLint("InvalidWakeLockTag")
	@Override
	public void onCreate() {
		super.onCreate();
		RxHttp.init(this);//初始化请求插件
		RxHttp.initRequest(new RequestSetting(this));
		initDreenDao();//初始化数据库
		MyActivityManager.init(this);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
		CrashHandler.getInstance().init(getApplicationContext());
	}

	public AirApp() {
		mInstance = this;
	}

	public static AirApp instance() {
		if (mInstance == null) {
			mInstance = new AirApp();
		}
		return mInstance;
	}

	/**
	 * greendao数据库初始化
	 */
	private void initDreenDao() {
		DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "comtom.db");
		SQLiteDatabase db = devOpenHelper.getWritableDatabase();
		DaoMaster daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
	}


	/**
	 * 获取 DaoSession
	 *
	 * @return
	 */
	public DaoSession getDaoSession() {
		return daoSession;
	}
}
