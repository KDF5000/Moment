package com.ktl.moment.entity;

public class Notification extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long userId;
	public long notificationId;
	public String userAvatar;
	public String userNickname;
	public String notifyDate;
	public String notifyTitle;// 消息标题，对应灵感的标题或者你的评论内容
	public String notifyContent;// 消息内容，对应评论内容
	/*
	 * 消息类型 
	 * 0：评论了 
	 * 1：赞了 
	 * 2：剪藏了 
	 * 3：围观了 
	 * 4：分享了 
	 * 5：评论被回复了 
	 * 6：关注的更新了 
	 * 7：关注的被评论了 
	 * 8：赞了你的评论
	 */
	public int notifyType;
	public int isScaned;// 是否已浏览了消息0：否，1：是

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	public int getIsScaned() {
		return isScaned;
	}

	public void setIsScaned(int isScaned) {
		this.isScaned = isScaned;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getNotifyDate() {
		return notifyDate;
	}

	public void setNotifyDate(String notifyDate) {
		this.notifyDate = notifyDate;
	}

	public String getNotifyTitle() {
		return notifyTitle;
	}

	public void setNotifyTitle(String notifyTitle) {
		this.notifyTitle = notifyTitle;
	}

	public String getNotifyContent() {
		return notifyContent;
	}

	public void setNotifyContent(String notifyContent) {
		this.notifyContent = notifyContent;
	}

	public int getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(int notifyType) {
		this.notifyType = notifyType;
	}

	

}
