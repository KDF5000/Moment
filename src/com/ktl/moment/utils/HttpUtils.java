/**
 * http请求的工具类
 * @author KDF5000 
 * @date 2015-5-3
 */
package com.ktl.moment.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
	/**
	 * 发送post请求 
	 * @param path
	 * @param params
	 * @param encoding
	 * @return
	 */
	public static String sendPostMethod(String path,
			Map<String, Object> params, String encoding) {
		String result = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(path);
		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					String name = entry.getKey();
					String value = entry.getValue().toString();
					BasicNameValuePair valuePair = new BasicNameValuePair(name,
							value);
					parameters.add(valuePair);
				}
			}
			//
			UrlEncodedFormEntity encodedFormEntity = new UrlEncodedFormEntity(
					parameters, encoding);
			post.setEntity(encodedFormEntity);
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),encoding);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
}
