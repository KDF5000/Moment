package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.entity.Channel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ChannelAdapter extends BaseAdapter {

	private Context context;
	private List<Channel> channelList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;

	public ChannelAdapter(Context context, List<Channel> channelList,
			DisplayImageOptions options) {
		this.context = context;
		this.channelList = channelList;
		this.inflater = LayoutInflater.from(this.context);
		this.options = options;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChannelHolder holder = null;
		if (convertView == null) {
			convertView = this.inflater.inflate(
					R.layout.fragment_find_channel_item_list, null);
			holder = new ChannelHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ChannelHolder) convertView.getTag();
		}
		Channel channel = channelList.get(position);
//		 holder.channelImg.setImageResource(R.drawable.img__1431395925288);
//		if (PregUtil.pregImgUrl(channel.getChannelImg())) {
			ImageLoader.getInstance().displayImage(channel.getChannelImg(),
					holder.channelImg, options);
//		}else{
//			holder.channelImg.setImageResource(R.drawable.default_img);
//		}
		holder.channelName.setText(channel.getChannelName());
		return convertView;
	}

	private static class ChannelHolder {
		@ViewInject(R.id.find_channel_img)
		ImageView channelImg;

		@ViewInject(R.id.find_channel_name)
		TextView channelName;
	}

}
