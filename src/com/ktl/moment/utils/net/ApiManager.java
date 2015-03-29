/**
 * API get,post封装
 * @author KDF5000 
 * @date 2015-03-29
 */
package com.ktl.moment.utils.net;

import org.apache.http.Header;

import android.content.Context;

import com.ktl.moment.infrastructure.HttpCallBack;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiManager {
	private static final String STATUS_OK = "200";//请求成功夺的状态码
	private static ApiManager apiManager = null;// apiManager实例

	public ApiManager() {
	}

	/**
	 * 获取apiManager的实例
	 * 
	 * @return ApiManager
	 */
	public static ApiManager getInstance() {
		if (apiManager == null) {
			synchronized (new Object()) {
				apiManager = new ApiManager();
			}
		}
		return apiManager;
	}

	/**
	 * post请求
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param modelName
	 *            返回实体的名字
	 * @param callBack
	 *            请求成功的回调类
	 */
	public void post(Context context, String url, RequestParams params,
			final String modelName, final HttpCallBack callBack) {
		HttpManager.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// 成功需要解析返回的数据
				callBack.onSuccess(new String(responseBody));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				callBack.onFailure(new String("hello failure"));
			}
		});
	}
	/**
	 * post请求
	 * 
	 * @param context
	 *            上下文
	 * @param url
	 *            请求url
	 * @param params
	 *            请求参数
	 * @param modelName
	 *            返回实体的名字
	 * @param callBack
	 *            请求成功的回调类
	 */
	public void get(Context context, String url, RequestParams params,
			final String modelName, final HttpCallBack callBack) {
		HttpManager.get(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// 成功需要解析返回的数据
				callBack.onSuccess(new String("hello success"));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				callBack.onFailure(new String("hello failure"));
			}
		});
	}
	/**
	 * 取消请求
	 * @param context
	 */
	public void cancelRequest(Context context){
		HttpManager.cancelRequest(context);
	}
	/*
	 * 取消所有的请求
	 */
	public void cancelAllRequest(){
		HttpManager.cancelAllRequest();
	}
}
