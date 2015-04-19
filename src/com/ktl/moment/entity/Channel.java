package com.ktl.moment.entity;

public class Channel extends BaseEntity{
	
	public long id;	//频道id
	public int channelImgResId;	//频道对应的图片url
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getChannelImgResId() {
		return channelImgResId;
	}
	public void setChannelImgResId(int channelImgResId) {
		this.channelImgResId = channelImgResId;
	}


}
