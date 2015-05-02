package com.ktl.moment.common.constant;

public class C {

	/****************************************************************************
	 * api相关常量 *
	 ****************************************************************************/
	public static final class API {
		/**
		 * url基地址
		 */
//		private static final String URL_BASE = "http://115.156.249.44:8080/MomentService/";
		private static final String URL_BASE = "http://mymoment.sinaapp.com/MomentService/";
		/**
		 * 七牛基地址
		 */
		public static final String QINIU_BASE_URL = "http://7xigjm.com1.z0.glb.clouddn.com/";
		/**
		 * 手机号登录
		 */
		public static final String USER_LOGIN = URL_BASE
				+ "UserInfoService/SignIn/login";
		/**
		 * 第三方登陆
		 */
		public static final String USER_THIRD_PARTY_LOGIN = URL_BASE
				+ "UserInfoService/SignIn/thirdparty/login";
		/**
		 * 用户注册
		 */
		public static final String USER_REGISTER = URL_BASE
				+ "UserInfoService/Registration/";
		/**
		 * 获取七牛token
		 */
		public static final String GET_QINIU_TOKEN = "http://mymoment.sinaapp.com/qiniu_token.php";
		/**
		 * 获取关注首页list
		 */
		public static final String GET_HOME_FOCUS_LIST = URL_BASE
				+ "MomentsService/Attention/getMomentList";
		/**
		 * 关注/取消关注 作者
		 */
		public static final String FOCUS_AUTHOR = URL_BASE
				+ "UserInfoService/FriendRelationship/addAttention";
		/**
		 * 点赞/取消点赞
		 */
		public static final String PRAISE_MOMENT = URL_BASE
				+ "MomentsService/MomentOperate/praiseMoment";
		/**
		 * 围观/取消围观
		 */
		public static final String WATCH_MOMENT = URL_BASE
				+ "MomentsService/MomentOperate/collectMoment";
		/**
		 * 获取我的粉丝列表
		 */
		public static final String GET_MY_FANS_LIST = URL_BASE
				+ "UserInfoService/UserInfo/getFans";
		/**
		 * 获取关注的作者list
		 */
		public static final String GET_FOCUS_AUTHOR_LIST = URL_BASE
				+ "UserInfoService/UserInfo/getAtentionUsers";
		/**
		 * 更新用户信息
		 */
		public static final String UPDATE_USER_INFO = "http://192.168.95.1/focus.php";
		/**
		 * 提交推荐关注用户
		 */
		public static final String SUBMIT_FOCUS_USER = "http://192.168.95.1/focus.php";
		/**
		 * 获取灵感详情
		 */
		public static final String GET_MOMENT_DETAIL = URL_BASE
				+ "MomentsService/MomentDetails/getMomentDetails";
		/**
		 * 获取评论list
		 */
		public static final String GET_COMMENT_LIST = URL_BASE
				+ "MomentsService/Comments/getMoreComment";
		/**
		 * 上传信鸽token
		 */
		public static final String XG_UPLOAD_TOKEN = URL_BASE + "XgService/XgOperate/uploadToken";
	}

	/******************************************************************************
	 * 菜单相关的常量
	 * 
	 * @author Administrator
	 *******************************************************************************/
	public static final class menu {
		/**
		 * 菜单对应的fragment tag
		 */
		public static final String FRAGMENT_FIND_TAG = "FindFragment";// 发现
		public static final String FRAGMENT_DYNAMIC_TAG = "DynamicFragment";// 关注
		public static final String FRAGMENT_MOMENT_TAG = "MomentFragment";// 灵感
		public static final String FRAGMENT_ME_TAG = "MeFragment";// 我的
		public static final String FRAGMENT_DEFAULT_SHOW_TAG = "DynamicFragment";// 默认显示动态

		/**
		 * 菜单索引值
		 */
		public static final int FRAGMENT_FIND_MENU_ID = 1;
		public static final int FRAGMENT_DYNAMIC_MENU_ID = 0;
		public static final int FRAGMENT_ADD_MOMENT_MENU_ID = 2;
		public static final int FRAGMENT_MOMENT_MENU_ID = 3;
		public static final int FRAGMENT_ME_MENU_ID = 4;
	}

	/******************************************************************************
	 * 第三方sdk常量
	 ******************************************************************************/
	public static final class ThirdSdk {
		/**
		 * 微博登陆
		 */
		public static final String WEIBO_APP_KEY = "2578051438";
		/**
		 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
		 * 
		 * <p>
		 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
		 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
		 * </p>
		 */
		public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

		/**
		 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
		 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
		 * 
		 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
		 * 
		 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
		 * 
		 * 有关哪些 OpenAPI
		 * 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI 关于 Scope
		 * 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
		 */
		public static final String WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
				+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
				+ "follow_app_official_microblog," + "invitation_write";

		/**
		 * QQ OPEN API APPID
		 */
		public static final String QQ_OPEN_FLAT_APP_ID = "1104435237";
	}

	/****************************************************************************
	 * 账户体系相关的常量
	 * 
	 * @author HUST_LH
	 ****************************************************************************/
	public static final class Account {
		/**
		 * fragment tags
		 */
		public static final String FRAGMENT_START = "startFragment";
		public static final String FRAGMENT_LOGIN = "loginFragment";
		public static final String FRAGMENT_FORGET_PASS = "forgetPassFragment";
		public static final String FRAGMENT_REGISTER = "registerFragment";
		public static final String FRAGMENT_VERIFY = "identifyFragment";
		public static final String FRAGMENT_PROFILE = "perfectInfoFragment";
		public static final String FRAGMENT_RECOMMEN = "recommenFragment";

		public static final boolean IS_CHECK_INPUT = false;// 是否对输入框等控件做空值检测
		public static final boolean IS_SEND_VERIFY = false; // 是否开启验证码
	}

	/**
	 * 调用云之讯服务发送验证码相关的常量
	 * 
	 * @author HUST_LH
	 * 
	 */
	public static final class YunZhiXun {

		public static final String YZX_KRY = "1234567890~!@#$%^&*()Abcdefghijk";
		public static final String MD5KEY = "b1871a6961cda7efd5e522cfa31eba30";// 根据上述字符串取md5值得到的密钥
		public static final String ACCOUNT_SID = "911c24149b30feb611f46b726dd39a48";// 开发者主账号
		public static final String APP_ID = "4b4b8011d56d4342adb3719522664697";// app
																				// id
		public static final String APP_NAME = "com.ktl.moment";
		public static final int VERIFY_VALID_TIME = 1;// 验证码有效时间/minute
		public static final int BUSSINESS_TYPE = 1;// 验证业务类型，当前智能验证业务参数值为1

	}

	/**
	 * Activity 跳转的request code
	 */
	public static final class ActivityRequest {
		public static final int REQUEST_SELECT_CAMERA_ACTIVITY = 1;// 请求选择拍照还是从相册获取图片方式
		public static final int REQUEST_CAMERA_ACTION = 2;// 请求调用系统相机
		public static final int REQUEST_PICK_ALBUM_ACTION = 3;// 从相册选择
		public static final int REQUEST_PICTURE_CROP_ACTION = 4;// 裁剪图片
		public static final int REQUEST_SELECT_CITY_ACTIVITY = 5;// 选择所在城市
		public static final int REQUEST_DATE_PICKER_ACTIVITY = 6;// 选择日期
		public static final int REQUEST_DIALOG_ACTIVITY = 7;// 弹出对话框
	}

	/**
	 * 文件类型 声音 图片
	 */
	public static final class FileType {
		public static final String IMAGE_TYPE = "img_";
		public static final String AUDIO_TYPE = "audio_";
	}

	/**
	 * SharedPreferences key
	 * 
	 * @author HUST_LH
	 * 
	 */
	public static final class SPKey {
		public static final String SPK_IS_LOGIN = "login"; // 记录账号是否退出了登陆,文件中用true/false表示
		public static final String SPK_SINGUP_ACCOUNT = "signup_account"; // 记录注册账号，文件中记录注册方式名称。eg：phone/qq/weibo/wechat
		public static final String SPK_IS_PUSH = "push"; // 是否开启推送
															// true：是，false：否
		public static final String SPK_IS_SAVE_DATA = "save_data"; // 是否开启省流量模式
																	// true：是，false：否
		public static final String SPK_LOGIN_INFO = "Account"; // 用户使用登陆时的个人信息
	}

	/**
	 * 第三方登陆类型
	 * 
	 * @author KDF5000
	 * 
	 */
	public static final class ThirdLoginType {
		public static final int WEIXIN_LOGIN = 0;// 微信登陆
		public static final int QQ_LOGIN = 1;// QQ登陆
		public static final int WEIBO_LOGIN = 2;// 微博登陆
	}
	
	public static final class DbTaskId{
		public static final int EDITOR_MOMENT_SAVE = 1;//编辑界面保存灵感到本地数据库
	}
}
