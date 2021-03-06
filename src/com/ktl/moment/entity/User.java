package com.ktl.moment.entity;

public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;// 用户id
	private String nickName;// 用户名
	private String password;// 用户密码
	private String mobilePhone;// 用户手机号
	private String userAvatar;// 用户头像
	private String userArea;// 用户所在城市
	private String signature;// 用户飞人签名
	private String birthday;
	private int sex;// 性别
	private int isNewUser;//标识该用户是否是新用户

	private int isFocused;// 用户是否被关注 0：否，1：是
	private String focusedTime;// 用户被关注的时间

	private int momentNum; // 用户灵感总数
	private int watchNum; // 用户围观的灵感数
	private int praiseNum;// 用户赞过的灵感
	private int attentionNum; // 用户关注的作者数
	private int FansNum; // 用户的粉丝数

	public int getIsNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(int isNewUser) {
		this.isNewUser = isNewUser;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public int getMomentNum() {
		return momentNum;
	}

	public void setMomentNum(int momentNum) {
		this.momentNum = momentNum;
	}

	public int getWatchNum() {
		return watchNum;
	}

	public void setWatchNum(int watchNum) {
		this.watchNum = watchNum;
	}

	public int getAttentionNum() {
		return attentionNum;
	}

	public void setAttentionNum(int attentionNum) {
		this.attentionNum = attentionNum;
	}

	public int getFansNum() {
		return FansNum;
	}

	public void setFansNum(int fansNum) {
		FansNum = fansNum;
	}

	public String getFocusedTime() {
		return focusedTime;
	}

	public void setFocusedTime(String focusedTime) {
		this.focusedTime = focusedTime;
	}

	public int getIsFocused() {
		return isFocused;
	}

	public void setIsFocused(int isFocused) {
		this.isFocused = isFocused;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getUserArea() {
		return userArea;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}
