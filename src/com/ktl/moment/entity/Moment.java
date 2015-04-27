package com.ktl.moment.entity;

public class Moment extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long momentId;//灵感id
	private String title;//标题
	private String content;//内容
	private String momentImgs;//灵感内部的图片
	private String label;//灵感标签
	private String postTime;//发布时间
	private int watchNum;//关注的用户个数
	private int praiseNum;//点赞的用户个数
	private int commentNum;//评论数
	private long authorId;//发表人的id
	private String authorName;//作者昵称
	private String userAvatar;//头像路径
	private String audioUrl;//音频文件存储路径
	
	private int isOpen;//是否公开	0：不公开，1：公开
	private int isCutCollect;//是否剪藏		0：不是剪藏，1：剪藏
	private int isFocused;//作者是否被关注	0:未被关注，1：关注
	private int isWatched;//灵感是否被围观		0：没有围观，1：围观
	private int isPraised;//是否赞过灵感		0：没有赞过，1：赞过

	public int getIsPraised() {
		return isPraised;
	}
	public void setIsPraised(int isPraised) {
		this.isPraised = isPraised;
	}
	public int getIsWatched() {
		return isWatched;
	}
	public void setIsWatched(int isWatched) {
		this.isWatched = isWatched;
	}
	public int getIsFocused() {
		return isFocused;
	}
	public void setIsFocused(int isFocused) {
		this.isFocused = isFocused;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	public int getIsCutCollect() {
		return isCutCollect;
	}
	public void setIsCutCollect(int isCutCollect) {
		this.isCutCollect = isCutCollect;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getMomentImgs() {
		return momentImgs;
	}
	public void setMomentImgs(String momentImgs) {
		this.momentImgs = momentImgs;
	}	
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public long getMomentId() {
		return momentId;
	}
	public void setMomentId(long momentId) {
		this.momentId = momentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public int getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(int praiseNum) {
		this.praiseNum = praiseNum;
	}
	public long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(long authorId) {
		this.authorId = authorId;
	}
	public int getWatchNum() {
		return watchNum;
	}
	public void setWatchtNum(int watchNum) {
		this.watchNum = watchNum;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	
	
}
