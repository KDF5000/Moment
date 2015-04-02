package com.ktl.moment.common.constant;

public class C {
	
	/****************************************************************************
	 * api相关常量                                                                                                                                                                                                       *
	 ****************************************************************************/
	public static final class api{
		/**
		 * url基地址
		 */
		private static final String URL_BASE = "http://192.168.56.1/Moment/";
		
		/**
		 * 手机号登录
		 */
		public static final String USER_LOGIN = URL_BASE + "login.php";
	}
	
	
	/******************************************************************************
	 * 菜单相关的常量
	 * @author Administrator
	 *******************************************************************************/
	public static final class menu{
		/**
		 * 菜单对应的fragment tag
		 */
		public static final String FRAGMENT_FIND_TAG = "FindFragment";//发现
		public static final String FRAGMENT_DYNAMIC_TAG = "DynamicFragment";//动态
		public static final String FRAGMENT_MOMENT_TAG = "MomentFragment";//灵感
		public static final String FRAGMENT_ME_TAG = "MeFragment";//我的
		public static final String FRAGMENT_DEFAULT_SHOW_TAG = "FindFragment";//默认显示发现
		
		/**
		 * 菜单索引值
		 */
		public static final int FRAGMENT_FIND_MENU_ID = 0;
		public static final int FRAGMENT_DYNAMIC_MENU_ID = 1;
		public static final int FRAGMENT_MOMENT_MENU_ID = 2;
		public static final int FRAGMENT_ME_MENU_ID = 3;
	}
}
