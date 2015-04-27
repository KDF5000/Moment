package com.ktl.moment.android.fragment.find;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.ChannelAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.DragGridView;
import com.ktl.moment.android.component.DragGridView.OnChanageListener;
import com.ktl.moment.entity.Channel;

public class ChannelFragment extends BaseFragment {

	private DragGridView draggableGridView;
	private List<Channel> channelList;
	private ChannelAdapter channelAdapter;
	private final String TAG = "channel";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find_channel, container,
				false);
		draggableGridView = (DragGridView) view
				.findViewById(R.id.find_channel_drag_gridview);

		channelList = new ArrayList<Channel>();
		getData();
		channelAdapter = new ChannelAdapter(getActivity(), channelList);
		draggableGridView.setAdapter(channelAdapter);

		setListener();

		return view;
	}

	private void getData() {
		int[] img = { R.drawable.channel_internet,
				R.drawable.channel_internet,
				R.drawable.channel_internet,
				R.drawable.channel_internet,
				R.drawable.channel_internet ,R.drawable.channel_internet,R.drawable.channel_internet};
//		 R.drawable.channel_fashion, R.drawable.channel_internet,
//		 R.drawable.channel_photography, R.drawable.channel_room_design,
//		 R.drawable.channel_ui_design,R.drawable.login_forget,
//		 R.drawable.channel_fashion, R.drawable.channel_internet,
//		 R.drawable.channel_photography, R.drawable.channel_room_design,
//		 R.drawable.channel_ui_design,R.drawable.login_forget };

		if (channelList == null) {
			channelList = new ArrayList<Channel>();
		}
		for (int i = 0; i < img.length; i++) {
			Channel channel = new Channel();
			channel.setChannelImgResId(img[i]);
			channel.setChannelName("频道"+i);
			channelList.add(channel);
		}
	}

	private void setListener() {
		draggableGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		draggableGridView.setOnChangeListener(new OnChanageListener() {
			
			@Override
			public void onChange(int form, int to) {
				// TODO Auto-generated method stub
				Channel temp = channelList.get(to);
				channelList.set(to, channelList.get(form));
				channelList.set(form, temp);
				channelAdapter.notifyDataSetChanged();
			}
		});
	}
}
