package com.ktl.moment.qiniu;

import java.io.File;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.ktl.moment.common.constant.C;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class QiniuManager {
	
	private String token;
	private UploadManager uploadManager;
	private static QiniuManager qiniuManager = null;
	
	public QiniuManager getInstance(){
		if(qiniuManager==null){
			qiniuManager = new QiniuManager();
		}
		return null;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public QiniuManager(){
		this.uploadManager = new UploadManager();
	}
	public interface QiniuRequestCallbBack{
		public void OnComplate(String key);
		public void OnFailed(String msg);
	}
	
	/**
	 * 上传指定路径的文件
	 * @param filePath
	 * @param callback
	 */
	public void uploadFile(Context context,final String filePath,final String fileType,final QiniuRequestCallbBack callback){
		if(!new File(filePath).isFile()){
			callback.OnFailed("不存在这个文件");
			return ;
		}
		ApiManager.getInstance().get(context, C.API.GET_QINIU_TOKEN, null, new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				uploadManager.put(filePath, fileType + System.currentTimeMillis(),
						token, new UpCompletionHandler() {
							@Override
							public void complete(String fileName, ResponseInfo info,
									JSONObject response) {
								// TODO Auto-generated method stub
								Log.i("qiniu", info.toString());
								if(info.statusCode == 200){
									callback.OnComplate(fileName);
								}else{
									callback.OnFailed(info.error);
								}
							}
						}, null);
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				callback.OnFailed((String)res);
			}
		}, "QiniuToken");
	}
	/**
	 * 上传指定文件
	 * @param filePath
	 * @param fileType
	 * @param callback
	 */
	public void uploadFile(Context context,final File file,final String fileType,final QiniuRequestCallbBack callback){
ApiManager.getInstance().get(context, C.API.GET_QINIU_TOKEN, null, new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				uploadManager.put(file, fileType + System.currentTimeMillis(),
						token, new UpCompletionHandler() {
							@Override
							public void complete(String fileName, ResponseInfo info,
									JSONObject response) {
								// TODO Auto-generated method stub
								Log.i("qiniu", info.toString());
								if(info.statusCode == 200){
									callback.OnComplate(fileName);
								}else{
									callback.OnFailed(info.error);
								}
							}
						}, null);
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				callback.OnFailed((String)res);
			}
		}, "QiniuToken");
	}
}
