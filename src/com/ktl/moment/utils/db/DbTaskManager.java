package com.ktl.moment.utils.db;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lidroid.xutils.util.LogUtils;

public class DbTaskManager {
	private static ExecutorService taskPool = null;//线程池
	
	public DbTaskManager(){
		taskPool = Executors.newCachedThreadPool();
	}
	
	public void addTask(DbTaskType dbTaskType,List<?> entities,Class<?> entityType,DbTaskListeners dbTaskListener){
		try {
			taskPool.execute(new TaskThread(dbTaskType, entities, entityType, dbTaskListener));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskPool.shutdown();
 		}
	}
	/**
	 * 任务完执行监听
	 * @author KDF5000
	 *
	 */
	public interface DbTaskListeners{
		public void OnComplete(Object res);
		public void OnCancel();
		public void OnFailure();
	}
	
	private class TaskThread implements Runnable{
		private DbTaskListeners dbTaskListener;
		private DbTaskType dbTaskType;
		private List<?> entities;
		private Class<?> entityType;
		
		public TaskThread(DbTaskType dbTaskType,List<?> entities,Class<?> entityType,DbTaskListeners dbTaskListener){
			this.dbTaskType = dbTaskType;
			this.entities = entities;
			this.entityType = entityType;
			this.dbTaskListener = dbTaskListener;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Object res = null;
			switch (this.dbTaskType) {
			case saveOrUpdate:
				
				break;
			case saveOrUpdateAll:
				
				break;
			case findById:
				
				break;
			case findByPage:
				
				break;
			case findByCondition:
				
				break;
			case deleteAll:
				
				break;
			case detele:
				break;
			default:
				break;
			}
			//回调
			this.dbTaskListener.OnComplete(res);
		}
	}
}
