package com.ktl.moment.entity;

public class Comment extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long momentId;
	private long commentId;// 评论id
	private long userId;// 评论人id
	private String userName;// 评论人昵称
	private String userAvatar;// 评论人头像
	private String postTime;// 评论发布时间
	private String content;// 评论内容
	private int praiseNum;// 点赞数
	private int isPraised;// 是否点赞评论0：是，1：不是

	private long repalyUserId;// 被回复用户id
	private String repalyUserName;// 被回复用户昵称

	public long getMomentId() {
		return momentId;
	}

	public void setMomentId(long momentId) {
		this.momentId = momentId;
	}

	public int getIsPraised() {
		return isPraised;
	}

	public void setIsPraised(int isPraised) {
		this.isPraised = isPraised;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}

	public long getRepalyUserId() {
		return repalyUserId;
	}

	public void setRepalyUserId(long repalyUserId) {
		this.repalyUserId = repalyUserId;
	}

	public String getRepalyUserName() {
		return repalyUserName;
	}

	public void setRepalyUserName(String repalyUserName) {
		this.repalyUserName = repalyUserName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
