package com.ktl.moment.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

@SuppressLint("DefaultLocale")
public class BasicInfoUtil {
	
	public static BasicInfoUtil basicInfoUtil = null;
	private Context mContext;
	
	public BasicInfoUtil(Context context){
		this.mContext = context;
	}
	/**
	 * 获取一个网网络工具实例 
	 *   如果context对应activity销毁了，会不会此处context为null？ 如果设置为applicationcontext呢？安全的方法是每次创建一个实例
	 * @param context
	 * @return
	 */
	public static BasicInfoUtil getInstance(Context context){
		if(basicInfoUtil==null){
			basicInfoUtil = new BasicInfoUtil(context);
		}
		return basicInfoUtil;
	}
	/**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected() {
    	 ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo ni = cm.getActiveNetworkInfo();
         return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    
    public int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }        
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(extraInfo!="" && !extraInfo.equals("") ){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
}
