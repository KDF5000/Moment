/**
 * Http管理类
 * @author KDF5000
 * @date 2015-03-29
 */
package com.ktl.moment.utils.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpManager {
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	static{
		client.setTimeout(11000);//设置超时连接为10s
	}
	/**
	 * get请求
	 * @param context
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void get(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(context, url, params, responseHandler);
	}
	/**
	 * post请求
	 * @param context
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(Context context, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler){
		client.post(context, url, params, responseHandler);
	}
	/**
	 * 取消请求
	 * @param context
	 */
	public static void cancelRequest(Context context){
		client.cancelRequests(context, true);
	}
	/**
	 * 取消所有的请求
	 */
	public static void cancelAllRequest(){
		client.cancelAllRequests(true);
	}
	
			
}
