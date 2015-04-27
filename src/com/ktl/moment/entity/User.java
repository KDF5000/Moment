package com.ktl.moment.entity;

public class User extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long userId;//用户id
	private String nickName;//用户名
	private String password;//用户密码
	private String mobilePhone;//用户手机号
	private String userAvatar;//用户头像
	private String userArea;//用户所在城市
	private String signature;//用户飞人签名
	private int sex;//性别
	
	private int isFocused;//用户是否被关注 0：否，1：是
	
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
