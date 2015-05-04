package com.ktl.moment.utils.db;

import java.util.List;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.DbUtils.DbUpgradeListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

public class DBManager {

	//数据库的名字
	private static final String DB_NAME ="moment.db";
	//数据库的版本
	private static final int VERSION = 1;
	
	private static DbUtils db = null;
	private static DBManager dbManager = null;
	
	public DBManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 初始化dbManager
	 * @param context
	 */
	public void init(Context context){
		db = DbUtils.create(context, DB_NAME, VERSION, new DbUpgradeListener() {
			
			@Override
			public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
				// TODO Auto-generated method stub
				//更新数据库时的操作
			}
		});
		db.configDebug(true);
	}
	/**
	 * 获取一个实例
	 * @return dbManager
	 */
	public static DBManager getInstance(){
		if(dbManager == null){
			synchronized (new Object()) {
				dbManager = new DBManager();
			}
		}
		return dbManager;
	}
	/**
	 * 关闭数据库
	 */
	public void closeDb(){
		if(db!=null){
			db.close();
		}
	}
	/**
	 * 保存一个实体
	 * @param entity
	 */
	public void saveOrUpdate(Object entity){
		try {
			db.replace(entity);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 保存或者更新多个实体
	 * @param entities
	 */
	public void saveOrUpdateAll(List<?> entities){
		try {
			db.replaceAll(entities);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	/**
	 * 通过id查询
	 * @param entityClass
	 * @param idValue
	 * @return
	 */
	public <T> T finbById(Class<T> entityClass,Object idValue){
		try {
			  return db.findById(entityClass, idValue);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过条件查询
	 * @param objClass
	 * @param columnName
	 * @param op
	 * @param value
	 * @return
	 */
	public <T> List<T> findByCondition(Class<?> objClass,WhereBuilder whereBuilder){
		try {
			return db.findAll(Selector.from(objClass).where(whereBuilder));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据条件分页查找
	 * @param entityType
	 * @param page  --从1开始
	 * @param pageSize
	 * @return
	 */
	public <T> List<T> findByPage(Class<?> entityType,int page,int pageSize,WhereBuilder whereBuilder){
		try {
			return db.findAll(Selector.from(entityType).where(whereBuilder).limit(pageSize).offset((page-1)*pageSize));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	/**
	 * 更新实体
	 * @param entity
	 * @param updateColumnNames
	 */
	public void update(Object entity,String ...updateColumnNames){
		try {
			db.update(entity, updateColumnNames);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
