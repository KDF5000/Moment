package com.ktl.moment.momentstore;

import java.util.ArrayList;
import java.util.Map;


import android.util.Log;

import com.ktl.moment.android.MomentApplication;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.QiniuToken;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.manager.TaskManager;
import com.ktl.moment.manager.TaskManager.TaskCallback;
import com.ktl.moment.qiniu.QiniuTask;
import com.ktl.moment.utils.RichEditUtils;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;

public class MomentStoreUtil {
	
	public MomentStoreUtil(){
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
	public void syncLocalMoment(final Moment moment,final syncLocalCallback callback){
		//1.先上传图片到途牛
		// 获取token
		ApiManager.getInstance().get(MomentApplication.getApplication(), C.API.GET_QINIU_TOKEN, null,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						ArrayList<QiniuToken> TokenList = (ArrayList<QiniuToken>) res;
						String token = TokenList.get(0).getToken();
						uploadFilte2Qiniu(token,moment,callback);
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						callback.OnFailed(res.toString());
					}
				}, "QiniuToken");
		 // 获取token
//		 String httpResponse = HttpUtils.sendPostMethod(C.API.GET_QINIU_TOKEN, null, "utf-8");
//		 if(httpResponse == null){
//			 try {
//				HttpResult result = AppUtil.getHttpResult(httpResponse);
//				@SuppressWarnings("unchecked")
//				ArrayList<QiniuToken> TokenList = (ArrayList<QiniuToken>) result.getResultList("QiniuToken");
//				String token = TokenList.get(0).getToken();
//				//上传文件到骑七牛
//				uploadFilte2Qiniu(token,moment,callback);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				callback.OnFailed(e.getMessage());
//			}
//		}
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
			QiniuTask task = new QiniuTask(MomentApplication.getApplication(), manager);
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
		User userInfo = Account.getUserInfo();
		if (userInfo == null) {
			callback.OnFailed("请先登录");
			return ;
		}
		params.put("userId", userInfo.getUserId());
		params.put("title", moment.getTitle());
		params.put("content", moment.getContent());
		params.put("contentAbstract", moment.getContentAbstract());
		if(moment.getLabel()==null || moment.getLabel().equals("") || moment.getLabel() == ""){
			moment.setLabel("");
		}
		params.put("label", moment.getLabel());
		params.put("isPublic", moment.getIsOpen());
		if(moment.getMomentId() != 0){
			params.put("momentId",moment.getMomentId());//id为0说明是新增灵感，否则是更新灵感
		}
		if(moment.getMomentImgs() == null){
			moment.setMomentImgs("");
		}
		params.put("momentImgs", moment.getMomentImgs());
		if( moment.getAudioUrl() == null){
			 moment.setAudioUrl("");
		}
		params.put("audioUrl", moment.getAudioUrl());
		params.put("isClipper", 0);
		//设置参数
		ApiManager.getInstance().post(MomentApplication.getApplication(), C.API.UPLOAD_MOMENT, params, new HttpCallBack() {
			
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
		}, "Moment");
	}
}
