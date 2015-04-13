package com.ktl.moment.bmob;

import android.content.Context;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.ktl.moment.infrastructure.BaseTask;
import com.ktl.moment.manager.TaskManager;


public class BmobTask extends BaseTask{
	
	public BmobTask(Context context,TaskManager manager){
		this.context = context;
		this.manager = manager;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		BmobProFile bmobProFile = BmobProFile.getInstance(this.context);
		bmobProFile.upload(localPath, new UploadListener() {

            @Override
            public void onSuccess(String fileName,String url) {
                // TODO Auto-generated method stub
            	manager.finishTask(key, url);
            }

            @Override
            public void onProgress(int ratio) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
            	manager.killTask(errormsg);
            }
        });
	}
	
}
