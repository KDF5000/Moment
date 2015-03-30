/**
 * API get,post��װ
 * @author KDF5000 
 * @date 2015-03-29
 */
package com.ktl.moment.utils.net;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Context;

import com.ktl.moment.entity.BaseEntity;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.infrastructure.HttpResult;
import com.ktl.moment.utils.AppUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiManager {
	private static final int STATUS_OK =  200;//����ɹ����״̬��
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
	public void post(Context context, String url, RequestParams params,final HttpCallBack callBack,
			final String ... modelNameList) {
		HttpManager.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// �ɹ���Ҫ�������ص�����
				handleHttpResponse(responseBody,callBack,modelNameList);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				callBack.onFailure(error.getMessage());
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
			final HttpCallBack callBack,final String ... modelNameList) {
		HttpManager.get(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// �ɹ���Ҫ�������ص�����
				handleHttpResponse(responseBody,callBack,modelNameList);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				callBack.onFailure(error.getMessage());
			}
		});
	}
	@SuppressWarnings("unchecked")
	private void handleHttpResponse (byte[] responseBody ,HttpCallBack callBack,String ...modelNameList ){
		if(responseBody==null){
			return ;
		}
		if(callBack==null){
			return ;
		}
		String response = new String(responseBody);
		try {
			HttpResult httpResult = AppUtil.getHttpResult(response);
			if(STATUS_OK != httpResult.getStatus()){
				callBack.onFailure(httpResult.getMsg());
				return;
			}
			Object result = null;
			if(modelNameList==null){
				callBack.onSuccess(httpResult.getMsg());
			}else if(modelNameList.length==1){
				callBack.onSuccess(httpResult.getResultList(modelNameList[0]));
			}else if(modelNameList.length>1){
				HashMap<String,ArrayList<? extends BaseEntity>> mapList = new HashMap<String, ArrayList<? extends BaseEntity>>();
				for(String modelName : modelNameList){
					result = httpResult.getResultList(modelName);
					if(result==null){
						continue;
					}
					mapList.put(modelName, (ArrayList<? extends BaseEntity>) result);
				}
				callBack.onSuccess(mapList);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			callBack.onFailure("JSON���������쳣");
		}
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
