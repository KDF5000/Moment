package com.ktl.moment.android.fragment.find;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.ChannelAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.DraggableGridViewPager;
import com.ktl.moment.android.component.DraggableGridViewPager.OnPageChangeListener;
import com.ktl.moment.android.component.DraggableGridViewPager.OnRearrangeListener;
import com.ktl.moment.entity.Channel;

public class ChannelFragment extends BaseFragment {

	private DraggableGridViewPager draggableGridViewPager;
	private List<Channel> channelList;
	private ChannelAdapter channelAdapter;
	private final String TAG = "channel";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find_channel, container,
				false);
		draggableGridViewPager = (DraggableGridViewPager) view
				.findViewById(R.id.find_channel_drag_gridview);

		channelList = new ArrayList<Channel>();
		getData();
		channelAdapter = new ChannelAdapter(getActivity(), channelList);
		draggableGridViewPager.setAdapter(channelAdapter);

		setListener();
		
		return view;
	}

	private void getData() {
		int[] img = { R.drawable.channel_architecture,
				R.drawable.channel_fashion, R.drawable.channel_internet,
				R.drawable.channel_photography, R.drawable.channel_room_design,
				R.drawable.channel_ui_design,R.drawable.login_forget,
				R.drawable.channel_fashion, R.drawable.channel_internet,
				R.drawable.channel_photography, R.drawable.channel_room_design,
				R.drawable.channel_ui_design,R.drawable.login_forget };

		if (channelList == null) {
			channelList = new ArrayList<Channel>();
		}
		for (int i = 0; i < img.length; i++) {
			Channel channel = new Channel();
			channel.setChannelImgResId(img[i]);
			channelList.add(channel);
		}
	}

	private void setListener() {
		draggableGridViewPager
				.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						Log.v(TAG, "onPageScrolled position=" + position
								+ ", positionOffset=" + positionOffset
								+ ", positionOffsetPixels="
								+ positionOffsetPixels);
					}

					@Override
					public void onPageSelected(int position) {
						Log.i(TAG, "onPageSelected position=" + position);
					}

					@Override
					public void onPageScrollStateChanged(int state) {
						Log.d(TAG, "onPageScrollStateChanged state=" + state);
					}
				});
		draggableGridViewPager
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}
				});
		draggableGridViewPager
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						return true;
					}
				});
		draggableGridViewPager
				.setOnRearrangeListener(new OnRearrangeListener() {
					@Override
					public void onRearrange(int oldIndex, int newIndex) {
						Log.i(TAG, "OnRearrangeListener.onRearrange from="
								+ oldIndex + ", to=" + newIndex);
						Channel item = (Channel) channelAdapter.getItem(oldIndex);
//						channelAdapter.setNotifyOnChange(false);
						channelAdapter.removeItem(oldIndex);
						channelAdapter.insertItem(item, newIndex);
						channelAdapter.notifyDataSetChanged();
					}
				});
	}
}
