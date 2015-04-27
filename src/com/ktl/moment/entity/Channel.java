package com.ktl.moment.entity;

public class Channel extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long channelId;	//频道id
	public int channelImgResId;	//频道对应的图片资源(暂时使用)
	public String channelName; //频道名称
	public String channelImg;  //频道图片url
	

	public int getChannelImgResId() {
		return channelImgResId;
	}
	public void setChannelImgResId(int channelImgResId) {
		this.channelImgResId = channelImgResId;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelImg() {
		return channelImg;
	}
	public void setChannelImg(String channelImg) {
		this.channelImg = channelImg;
	}


}
