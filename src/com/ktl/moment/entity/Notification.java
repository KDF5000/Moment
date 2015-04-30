package com.ktl.moment.entity;

public class Notification extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String userAvatar;
	public String userNickname;
	public String msgDate;
	public String msgTitle;// 消息标题，对应灵感的标题或者你的评论内容
	public String msgContent;// 消息内容，对应评论内容
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
	public int msgType;
	public int isScaned;// 是否已浏览了消息0：否，1：是

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

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

}
