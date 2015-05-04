package com.ktl.moment.momentstore;

import java.util.LinkedList;
import java.util.Map;

public class MomentSyncTaskManager {

	private LinkedList<MomentSyncTask> momentSyncTasks;
	private int workingThreadNUm = 0;
	private int maxThreadNum = 1;
	private int syncCount = 0;
	private  MomentSyncCallback momentSyncCallback;
	
	public MomentSyncTaskManager() {
		// TODO Auto-generated constructor stub
		momentSyncTasks = new LinkedList<MomentSyncTask>();
	}

	
	public interface MomentSyncCallback{
		public void onComplete(int syncCount);
		public void onError(String msg);
	}
	/**
	 * 
	 * @param momentSyncCallback
	 */
	public void setTaskCallBack(MomentSyncCallback momentSyncCallback) {
		this.momentSyncCallback = momentSyncCallback;
	}
	/**
	 * 
	 * @param num
	 */
	public void setMaxThreadNum(int num){
		this.maxThreadNum = num;
	}
	/**
	 * 添加一个任务
	 * @param task
	 */
	public synchronized void addSyncTask(MomentSyncTask momentSyncTask){
		momentSyncTasks.add(momentSyncTask);
	}
	
	public void startSync(){
		if(workingThreadNUm>=maxThreadNum){
			return ;
		}
		nextSync();
	}
	/**
	 * 
	 * @return
	 */
	protected synchronized MomentSyncTask getNextMomentSyncTask(){
		if(momentSyncTasks.size() == 0){
			return null;
		}else{
			return momentSyncTasks.removeLast();
		}
	}
	/**
	 * 
	 */
	protected synchronized void activateSync(){
		workingThreadNUm++;
	}
	/**
	 * 
	 * @param key
	 * @param url
	 */
	public synchronized void finishSync(Object res){
		workingThreadNUm--;
		syncCount++;
		nextSync();
	}
	/**
	 * 
	 * @param msg
	 */
	public synchronized void killSync(String msg){
		if(momentSyncTasks.size()>0){
			momentSyncTasks.clear();
		}
		this.momentSyncCallback.onError(msg);
	}
	/**
	 * 
	 */
	public void nextSync(){
		MomentSyncTask momentSyncTask = this.getNextMomentSyncTask();
		if(momentSyncTask == null){
			if(this.momentSyncCallback != null){
				this.momentSyncCallback.onComplete(syncCount);
			}
			return ;
		}else{
			activateSync();
			momentSyncTask.startTask();
		}
	}
}
