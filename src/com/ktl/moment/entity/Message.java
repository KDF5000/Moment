package com.ktl.moment.entity;

public class Message extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long msgId;
	public String sendUserId;
	public String sendUserName;
	public String sendUserAvatar;
	public String recieveUserId;
	public String sendTime;
	public String msgContent;

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getSendUserAvatar() {
		return sendUserAvatar;
	}

	public void setSendUserAvatar(String sendUserAvatar) {
		this.sendUserAvatar = sendUserAvatar;
	}

	public String getRecieveUserId() {
		return recieveUserId;
	}

	public void setRecieveUserId(String recieveUserId) {
		this.recieveUserId = recieveUserId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

}
