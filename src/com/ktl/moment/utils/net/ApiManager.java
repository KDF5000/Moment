/**
 * API get,post��װ
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
	private static final String STATUS_OK = "200";//����ɹ����״̬��
	private static ApiManager apiManager = null;// apiManagerʵ��

	public ApiManager() {
	}

	/**
	 * ��ȡapiManager��ʵ��
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
	 * post����
	 * 
	 * @param context
	 *            ������
	 * @param url
	 *            ����url
	 * @param params
	 *            �������
	 * @param modelName
	 *            ����ʵ�������
	 * @param callBack
	 *            ����ɹ��Ļص���
	 */
	public void post(Context context, String url, RequestParams params,
			final String modelName, final HttpCallBack callBack) {
		HttpManager.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// �ɹ���Ҫ�������ص�����
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
	 * post����
	 * 
	 * @param context
	 *            ������
	 * @param url
	 *            ����url
	 * @param params
	 *            �������
	 * @param modelName
	 *            ����ʵ�������
	 * @param callBack
	 *            ����ɹ��Ļص���
	 */
	public void get(Context context, String url, RequestParams params,
			final String modelName, final HttpCallBack callBack) {
		HttpManager.get(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// �ɹ���Ҫ�������ص�����
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
	 * ȡ������
	 * @param context
	 */
	public void cancelRequest(Context context){
		HttpManager.cancelRequest(context);
	}
	/*
	 * ȡ�����е�����
	 */
	public void cancelAllRequest(){
		HttpManager.cancelAllRequest();
	}
}
