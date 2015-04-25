/**
 * π§æﬂ¿‡
 */
package com.ktl.moment.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.ktl.moment.infrastructure.HttpResult;

public class AppUtil {
	public static HttpResult getHttpResult(String httpResponse) throws JSONException{
		HttpResult httpResult = new HttpResult();
		if(httpResponse == "" || httpResponse==null){
			return httpResult;
		}
		JSONObject jsonObject = new JSONObject(httpResponse.trim());
		if(httpResult!=null){
			httpResult.setStatus(jsonObject.getInt("status"));
			httpResult.setMsg(jsonObject.getString("msg"));
			httpResult.setResult(jsonObject.getString("result"));
		}
		return httpResult;
	}
}
