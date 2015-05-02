package com.ktl.moment.utils.db;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Message;

public class DbTaskManager {
	private static ExecutorService taskPool = null;// 线程池

	public DbTaskManager() {
		taskPool = Executors.newCachedThreadPool();
	}

	public void addTask(int taskId,DbTaskType dbTaskType, List<?> entities,
			Class<?> entityType, DbTaskHandler taskHandler) {
		try {
			taskPool.execute(new TaskThread(taskId,dbTaskType, entities, entityType,
					taskHandler));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskPool.shutdown();
		}
	}

	private class TaskThread implements Runnable {
		private DbTaskHandler taskHandler;
		private DbTaskType dbTaskType;
		private List<?> entities;
		private Class<?> entityType;
		private int taskId;
		public TaskThread(int taskId,DbTaskType dbTaskType, List<?> entities,
				Class<?> entityType, DbTaskHandler taskHandler) {
			this.dbTaskType = dbTaskType;
			this.entities = entities;
			this.entityType = entityType;
			this.taskHandler = taskHandler;
			this.taskId = taskId;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Object res = null;
			switch (this.dbTaskType) {
			case saveOrUpdate:
				DBManager.getInstance().saveOrUpdate(entities.get(0));
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
			// 回调
			if (this.taskHandler != null) {
				Message msg = this.taskHandler.obtainMessage();
				msg.obj = "res";//改为结果
				msg.what = this.taskId;
				this.taskHandler.sendMessage(msg);
			}
		}
	}
}