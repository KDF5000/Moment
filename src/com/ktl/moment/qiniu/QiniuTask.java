package com.ktl.moment.qiniu;

import org.json.JSONObject;

import android.content.Context;

import com.ktl.moment.infrastructure.BaseTask;
import com.ktl.moment.manager.TaskManager;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class QiniuTask extends BaseTask {

	private String token = "EoTQ78TWiW5iBa26IxIMZTdwrwkLZN8tewhxLDhd:aolAbOKx45qLb5gsRdFZjlNgSaQ=:eyJzY29wZSI6Im1vbWVudCIsImRlYWRsaW5lIjoxNDI4ODUwNTAxfQ==";

	public QiniuTask(Context context, TaskManager manager) {
		this.context = context;
		this.manager = manager;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		UploadManager uploadManager = new UploadManager();
		 
		uploadManager.put(this.localPath, "img_"+System.currentTimeMillis(), token, new UpCompletionHandler() {
			@Override
			public void complete(String fileName,
					com.qiniu.android.http.ResponseInfo info, JSONObject response) {
				// TODO Auto-generated method stub
				manager.finishTask(key, fileName);
			}
		}, null);
	}
}
