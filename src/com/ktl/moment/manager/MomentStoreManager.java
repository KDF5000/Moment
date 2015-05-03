package com.ktl.moment.manager;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.QiniuToken;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.infrastructure.HttpResult;
import com.ktl.moment.manager.TaskManager.TaskCallback;
import com.ktl.moment.qiniu.QiniuTask;
import com.ktl.moment.utils.AppUtil;
import com.ktl.moment.utils.HttpUtils;
import com.ktl.moment.utils.RichEditUtils;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;
import com.tencent.android.tpush.common.m;

public class MomentStoreManager {
	private Context mContext;
	
	public MomentStoreManager(Context context){
		this.mContext = context;
	}
	/**
	 * 
	 * @author KDF5000
	 *
	 */
	public interface syncLocalCallback{
		public void OnSuccess(Object res);
		public void OnFailed(String msg);
	}
	/**
	 * 同步一条灵感到服务器
	 * @param moment
	 * @param callback
	 * @return
	 */
	public int syncLocalMoment(Moment moment,syncLocalCallback callback){
		//1.先上传图片到途牛
		// 获取token
		 String httpResponse = HttpUtils.sendPostMethod(C.API.GET_QINIU_TOKEN, null, "utf-8");
		 if(httpResponse == null){
			 try {
				HttpResult result = AppUtil.getHttpResult(httpResponse);
				@SuppressWarnings("unchecked")
				ArrayList<QiniuToken> TokenList = (ArrayList<QiniuToken>) result.getResultList("QiniuToken");
				String token = TokenList.get(0).getToken();
				//上传文件到骑七牛
				uploadFilte2Qiniu(token,moment,callback);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				callback.OnFailed(e.getMessage());
			}
		 }
		return 0;
	}
	
	/**
	 * 上传文件到七牛
	 * @param token
	 * @param moment
	 * @param callback
	 */
	private void uploadFilte2Qiniu(String token,final Moment moment,final syncLocalCallback callback) {
		Map<String, String> imgMap = RichEditUtils.extractImg(moment.getContent());
		TaskManager manager = new TaskManager();
		manager.setTaskCallBack(new TaskCallback() {

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				callback.OnFailed(msg);
			}

			@Override
			public void onComplete(Map<String, String> resMap) {
				// TODO Auto-generated method stub
				String content = moment.getContent();
				for (Map.Entry<String, String> entry : resMap.entrySet()) {
					Log.i("URL","-->" + entry.getKey() + "=" + entry.getValue());
					// 替换et内容
					content = content.replaceAll(entry.getKey(), "<img src=\""
							+ C.API.QINIU_BASE_URL + entry.getValue() + "\"/>");
				}
				moment.setContent(content);
				//上传到业务服务器
				upload2Server(moment,callback);
			}
		});
		for (Map.Entry<String, String> entry : imgMap.entrySet()) {
			QiniuTask task = new QiniuTask(mContext, manager);
			task.setToken(token);
			task.setKey(entry.getKey());
			task.setLocalPath(entry.getValue());
			manager.addTask(task);
		}
		manager.startTask();
	}
	/**
	 * 上传灵感到业务服务器
	 * @param moment
	 * @param callback
	 */
	private void upload2Server(Moment moment,final syncLocalCallback callback){
		RequestParams params = new RequestParams();
		//设置参数
		ApiManager.getInstance().post(this.mContext, C.API.UPLOAD_MOMENT, params, new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				callback.OnSuccess(res);
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				callback.OnFailed(res.toString());
			}
		}, "");
	}
}
