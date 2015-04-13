package com.ktl.moment.manager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.ktl.moment.infrastructure.BaseTask;

public class TaskManager {
	
	private LinkedList<BaseTask> taskList;
	private int workingThreadNUm = 0;
	private int maxThreadNum = 1;
	private Map<String,String> resMap;
	private TaskCallback taskCallBack;
	public TaskManager(){
		resMap = new HashMap<String, String>();
		taskList = new LinkedList<BaseTask>();
	}
	public interface TaskCallback{
		public void onComplete(Map<String,String> resMap);
		public void onError(String msg);
	}
	
	public void setTaskCallBack(TaskCallback taskCallBack) {
		this.taskCallBack = taskCallBack;
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
	public synchronized void addTask(BaseTask task){
		taskList.add(task);
	}
	
	public void startTask(){
		if(workingThreadNUm>=maxThreadNum){
			return ;
		}
		nextTask();
	}
	/**
	 * 
	 * @return
	 */
	protected synchronized BaseTask getNextTask(){
		if(taskList.size() == 0){
			return null;
		}else{
			return taskList.removeLast();
		}
	}
	/**
	 * 
	 */
	protected synchronized void activateTask(){
		workingThreadNUm++;
	}
	/**
	 * 
	 * @param key
	 * @param url
	 */
	public synchronized void finishTask(String key,String url){
		resMap.put(key, url);
		workingThreadNUm--;
		nextTask();
	}
	/**
	 * 
	 * @param msg
	 */
	public synchronized void killTask(String msg){
		if(taskList.size()>0){
			taskList.clear();
		}
		this.taskCallBack.onError(msg);
		
	}
	/**
	 * 
	 */
	public void nextTask(){
		BaseTask task = this.getNextTask();
		if(task==null){
			taskCallBack.onComplete(resMap);
			return ;
		}else{
			activateTask();
			task.start();
		}
	}
}
