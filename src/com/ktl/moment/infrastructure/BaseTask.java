package com.ktl.moment.infrastructure;

import android.content.Context;

import com.ktl.moment.manager.TaskManager;


public class BaseTask {
	protected String key;
	protected String localPath;
	protected TaskManager manager;
	protected Context context;
	
	public TaskManager getManager() {
		return manager;
	}
	public void setManager(TaskManager manager) {
		this.manager = manager;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
	public void start(){
		
	}
}
