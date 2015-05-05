package com.ktl.moment.entity;

public class Message extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long msgId;
	public long sendUserId;
	public String sendUserName;
	public String sendUserAvatar;
	public long recieveUserId;
	public String recieveUserAvatar;

	public String sendTime;
	public String msgContent;
	public int msgType; // 0：send msg；1：recieve msg；2：time

	public String getRecieveUserAvatar() {
		return recieveUserAvatar;
	}

	public void setRecieveUserAvatar(String recieveUserAvatar) {
		this.recieveUserAvatar = recieveUserAvatar;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

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

	public long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(long sendUserId) {
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

	public long getRecieveUserId() {
		return recieveUserId;
	}

	public void setRecieveUserId(long recieveUserId) {
		this.recieveUserId = recieveUserId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

}
