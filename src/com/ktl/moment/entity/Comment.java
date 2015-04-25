package com.ktl.moment.entity;

public class Comment extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long commentId;//评论id
	private long fromUserId;//评论人id
	private String fromUserName;//评论人昵称
	private String fromUserAvatar;//评论人头像
	private String commentTime;//评论发布时间
	private String commentContent;//评论内容
	private int praiseNum;//点赞数
	
	private long repalyUserId;//被回复人id
	private String repalyUserName;//被回复人昵称
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	public long getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getFromUserAvatar() {
		return fromUserAvatar;
	}
	public void setFromUserAvatar(String fromUserAvatar) {
		this.fromUserAvatar = fromUserAvatar;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
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
	
}
