/**
 * 获取账户信息
 */
package com.ktl.moment.common;

import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.utils.SharedPreferencesUtil;

public class Account {
	/**
	 * 获取账户的用户信息
	 * 
	 * @return User
	 */
	public static User getUserInfo() {
		return (User) SharedPreferencesUtil.getInstance().getObject(
				C.SPKey.SPK_LOGIN_INFO);
	}

	/**
	 * 保存用户信息s
	 * 
	 * @param user
	 */
	public static void saveUserInfo(User user) {
		SharedPreferencesUtil.getInstance().putObject(C.SPKey.SPK_LOGIN_INFO,user);
	}
}
