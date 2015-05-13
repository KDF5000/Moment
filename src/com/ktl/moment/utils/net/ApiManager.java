/**
 * API get,post封装
 * @author KDF5000 
 * @date 2015-03-29
 */
package com.ktl.moment.utils.net;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.ktl.moment.entity.BaseEntity;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.infrastructure.HttpResult;
import com.ktl.moment.utils.AppUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ApiManager {
	private static final int STATUS_OK =  200;//请求成功夺的状态码
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
	public void post(Context context, String url, RequestParams params,final HttpCallBack callBack,
			final String ... modelNameList) {
		Log.i("http request", params+"");
		HttpManager.post(context, url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// 成功需要解析返回的数据
				handleHttpResponse(responseBody,callBack,modelNameList);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(callBack != null){
					callBack.onFailure(error.getMessage());
				}
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
			final HttpCallBack callBack,final String ... modelNameList) {
		HttpManager.get(context, url, params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// 成功需要解析返回的数据
				handleHttpResponse(responseBody,callBack,modelNameList);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				if(callBack != null){
					callBack.onFailure(error.getMessage());
				}
				
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
		Log.i("http response", response);
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
				if(callBack != null){
					callBack.onSuccess(mapList);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(callBack != null){
				callBack.onFailure("JSON解析出现异常");
			}
		}
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
