/**
 * activity管理
 */
package com.ktl.moment.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

public class AppManager {
	public static List<Activity> activityCollector;
	public static AppManager instance;
	
	/**
	 * 获取实例
	 * @return
	 */
	public static AppManager getInstance(){
		if(instance==null){
			instance = new AppManager();
		}
		if(activityCollector==null){
			activityCollector = new ArrayList<Activity>();
		}
		return instance;
	}
	/**
	 * 添加一个活动
	 * @param activity
	 */
	public void addActivity(Activity activity){
		if(activityCollector!=null){
			activityCollector.add(activity);
		}
	}
	/**
	 * 移除指定的activity
	 * @param activity
	 */
	public void removeActivity(Activity activity){
		if(activityCollector!=null){
			activityCollector.remove(activity);
			activity.finish();
		}
	}
	/**
	 * 移除所有的activity
	 */
	public void finishAll(){
		if(activityCollector!=null){
			for(Activity activity : activityCollector){
				activity.finish();
			}
		}
	}
	 /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
	public void AppExit(Context context) {
        try {
            finishAll();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
