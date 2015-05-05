package com.ktl.moment.utils;

import java.security.MessageDigest;

public class EncryptUtil {

	/*public final static String md5(String string){
		byte[] byteInput = string.getBytes();
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		try {
			MessageDigest mdInstance = MessageDigest.getInstance("MD5");//获得MD5摘要算法的MessageDigest对象
			mdInstance.update(byteInput);//使用指定的字节更新摘要
			byte[] md = mdInstance.digest();//获得密文
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}*/
	/**
	 * MD5加密 
	 * @param strs
	 * @return
	 */
	public static String md5(String ...strs){
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       

		int count = strs.length;
		String res = "";
		for(int i =0;i < count;i++){
			try {
	            byte[] btInput = strs[i].getBytes();
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            //使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int n = 0; n < j; n++) {
	                byte byte0 = md[n];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            if(i==0){
	            	res = new String(str);
	            }else{
	            	res += "-" + new String(str);
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
		}
		return res;
	}	
}
