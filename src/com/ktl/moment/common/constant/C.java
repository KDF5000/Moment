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
		public static final int FRAGMENT_ADD_MOMENT = 2;
		public static final int FRAGMENT_MOMENT_MENU_ID = 3;
		public static final int FRAGMENT_ME_MENU_ID = 4;
	}
	/******************************************************************************
	 * 第三方sdk常量
	 ******************************************************************************/
	public static final class ThirdSdk{
		/**
		 * 微博登陆
		 */
		public static final String WEIBO_APP_KEY = "2578051438";
		/** 
	     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	     * 
	     * <p>
	     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
	     * 但是没有定义将无法使用 SDK 认证登录。
	     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	     * </p>
	     */
	    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

	    /**
	     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
	     * 选择赋予应用的功能。
	     * 
	     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
	     * 使用权限，高级权限需要进行申请。
	     * 
	     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	     * 
	     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	     */
	    public static final String WEIBO_SCOPE = 
	            "email,direct_messages_read,direct_messages_write,"
	            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	            + "follow_app_official_microblog," + "invitation_write";
	    
	    /**
	     * QQ OPEN API APPID
	     */
		public static final String QQ_OPEN_FLAT_APP_ID="1104435237";
	}
	
	
	/****************************************************************************
	 * 账户体系相关的常量
	 * @author HUST_LH
	 ****************************************************************************/
	public static final class Account{
		public static final String FRAGMENT_LOGIN = "loginFragment";
		public static final String FRAGMENT_FORGET_PASS = "forgetPassFragment";
		public static final String FRAGMENT_REGISTER = "registerFragment";
		public static final String FRAGMENT_VERIFY = "verifyFragment";
		public static final String FRAGMENT_PERFECT_INFO = "perfectInfoFragment";
		public static final String FRAGMENT_RECOMMEN = "recommenFragment";
	}
}
