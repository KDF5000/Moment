package com.ktl.moment.entity;

public class UserTrack { 
	private long userId;
	private long momentId;
	private long stickTime;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getMomentId() {
		return momentId;
	}
	public void setMomentId(long momentId) {
		this.momentId = momentId;
	}
	public long getStickTime() {
		return stickTime;
	}
	public void setStickTime(long stickTime) {
		this.stickTime = stickTime;
	}
	
}
