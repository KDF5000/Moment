package com.ktl.moment.android;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.ktl.moment.config.AppConfig;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.android.tpush.XGPushConfig;

public class MomentApplication extends Application {

	private static MomentApplication application = null;
	private NotificationManager notificationManager = null;
	
	public synchronized static MomentApplication getApplication(){
		return application;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = this;
		initImageLoader(this);
		// 初始化sharedPreferences
		SharedPreferencesUtil.initSharedPreferences(getApplicationContext());
		//配置信鸽
		XGPushConfig.enableDebug(this, AppConfig.MODE_DEVELOP);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
		
	}
	
	public NotificationManager getNotificationManager(){
		if(notificationManager == null){
			notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		}
		return notificationManager;
	}
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		Log.i("MomentApplication", "onTerminate");
	}
}
