package com.ktl.moment.entity;

public class Comment extends BaseEntity {
	private long commentFromUserId;//评论id
	private String commentFromUserName;//评论人昵称
	private String commentFromUserAvatar;//评论人头像
	private String commentTime;//评论发布时间
	private String commentContent;//评论内容
	private int praiseNum;//点赞数
	
	private long commentToUserId;//被评论人id
	private String commentToUserName;//被评论人昵称
	public long getCommentFromUserId() {
		return commentFromUserId;
	}
	public void setCommentFromUserId(long commentFromUserId) {
		this.commentFromUserId = commentFromUserId;
	}
	public String getCommentFromUserName() {
		return commentFromUserName;
	}
	public void setCommentFromUserName(String commentFromUserName) {
		this.commentFromUserName = commentFromUserName;
	}
	public String getCommentFromUserAvatar() {
		return commentFromUserAvatar;
	}
	public void setCommentFromUserAvatar(String commentFromUserAvatar) {
		this.commentFromUserAvatar = commentFromUserAvatar;
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
	public long getCommentToUserId() {
		return commentToUserId;
	}
	public void setCommentToUserId(long commentToUserId) {
		this.commentToUserId = commentToUserId;
	}
	public String getCommentToUserName() {
		return commentToUserName;
	}
	public void setCommentToUserName(String commentToUserName) {
		this.commentToUserName = commentToUserName;
	}
}
