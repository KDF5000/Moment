package com.ktl.moment.android.adapter;

import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.entity.Channel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ChannelAdapter extends BaseAdapter{
	
	private Context context;
	private List<Channel> channelList;
	private LayoutInflater inflater;
	
	public ChannelAdapter(Context context, List<Channel> channelList){
		this.context = context;
		this.channelList = channelList;
		this.inflater  = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return channelList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return channelList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void removeItem(int position){
		channelList.remove(position);
	}
	
	public void insertItem(Channel item, int position){
		channelList.add(position, item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChannelHolder holder = null;
		if(convertView == null){
			convertView = this.inflater.inflate(R.layout.fragment_find_channel_item_list, null);
			holder = new ChannelHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder = (ChannelHolder) convertView.getTag();
		}
		Channel channel = channelList.get(position);
		holder.channelImg.setImageResource(channel.getChannelImgResId());
		return convertView;
	}
	
	private static class ChannelHolder{
		@ViewInject(R.id.find_channel_img)
		ImageView channelImg;
	}

}
