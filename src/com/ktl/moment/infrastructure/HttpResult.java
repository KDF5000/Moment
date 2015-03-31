package com.ktl.moment.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.ktl.moment.entity.BaseEntity;

public class HttpResult {
	private int status;// ���󷵻���
	private String msg;// ���󷵻ص���Ϣ
	private String result;// �ַ������
	private Map<String, ArrayList<? extends BaseEntity>> mapResultList;// map���ϣ��ֶ�ֵ�Ƕ�����list

	public HttpResult(){
		this.mapResultList = new HashMap<String, ArrayList<? extends BaseEntity>>();
	}
	/**
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 
	 * @return msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 
	 * @return result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * ���map��ָ��model����ʵ��list
	 * 
	 * @param modelName
	 * @return ArrayList
	 */
	public ArrayList<? extends BaseEntity> getResultList(String modelName) {
		return this.mapResultList.get(modelName);
	}

	/**
	 * ���÷��ؽ��
	 * @param result
	 * @throws JSONException
	 */
	public void setResult(String result) throws JSONException {
		this.result = result;
		if (result.length() > 0) {
			JSONObject jsonObject = null;
			jsonObject = new JSONObject(result);
			@SuppressWarnings("unchecked")
			Iterator<String> iterators = jsonObject.keys();
			while (iterators.hasNext()) {
				try {
					String jsonKey = iterators.next();
					String modelName = getModelName(jsonKey);
					String modelClassName = "com.ktl.moment.entity."
							+ modelName;
					JSONArray modelJsonArray = jsonObject.optJSONArray(jsonKey);
					// ��������Ǹ�json����
					if (modelJsonArray == null) {
						JSONObject modelJsonObject = jsonObject
								.optJSONObject(jsonKey);
						if (modelJsonObject == null) {
							continue;
						}
				 
						ArrayList<BaseEntity> modelList = new ArrayList<BaseEntity>();
						BaseEntity entity = json2Model(modelClassName,
								modelJsonObject);
						if (entity == null) {
							continue;
						}
						modelList.add(entity);
						this.mapResultList.put(modelName, modelList);
					} else {
						// The data is JSONArray
						ArrayList<BaseEntity> modelList = new ArrayList<BaseEntity>();
						for (int i = 0; i < modelJsonArray.length(); i++) {
							JSONObject modelJsonObject = modelJsonArray
									.optJSONObject(i);
							modelList.add(json2Model(modelClassName,
									modelJsonObject));
						}
						this.mapResultList.put(modelName, modelList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * jsonת��Ϊ����
	 * @param modelClassName
	 * @param modelJsonObject
	 * @return
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 */
	private BaseEntity json2Model(String modelClassName,
			JSONObject modelJsonObject) throws InstantiationException,
			ClassNotFoundException, IllegalAccessException {
		if (modelJsonObject == null) {
			return null;
		}

		Gson gson = new Gson();
		BaseEntity modelObj = (BaseEntity) gson.fromJson(
				modelJsonObject.toString(), Class.forName(modelClassName));
		return modelObj;
	}
	/**
	 * ���ģ����
	 * @param str
	 * @return
	 */
	private String getModelName(String str) {
		if(str.contains("List")){
			str = str.substring(0,str.indexOf("List"));
		}
		//����ĸת��Ϊ��д
		if (str != null && str != "") {
			str = str.replaceFirst(str.substring(0, 1), str.substring(0,1).toUpperCase(Locale.getDefault()));
		}
		return str;
	}
}
