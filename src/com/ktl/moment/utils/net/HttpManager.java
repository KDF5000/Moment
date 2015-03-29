/**
 * Http������
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
		client.setTimeout(11000);//���ó�ʱ����Ϊ10s
	}
	/**
	 * get����
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
	 * post����
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
	 * ȡ������
	 * @param context
	 */
	public static void cancelRequest(Context context){
		client.cancelRequests(context, true);
	}
	/**
	 * ȡ�����е�����
	 */
	public static void cancelAllRequest(){
		client.cancelAllRequests(true);
	}
	
			
}
