package com.ktl.moment.android.fragment.find;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.ChannelListActivity;
import com.ktl.moment.android.adapter.ChannelAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.DragGridView;
import com.ktl.moment.android.component.DragGridView.OnChanageListener;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Channel;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;

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

		getData();
		channelAdapter = new ChannelAdapter(getActivity(), channelList,
				getDisplayImageOptions());
		draggableGridView.setAdapter(channelAdapter);

		setListener();

		return view;
	}

	private void getData() {
		if(channelList == null){
			channelList = new ArrayList<Channel>();
		}
		ApiManager.getInstance().get(getActivity(), C.API.GET_CHENNAL_LIST,
				null, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Channel> list = (List<Channel>) res;
						channelList.clear();
						channelList.addAll(list);
						channelAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "Channel");
	}

	private void setListener() {
		draggableGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						ChannelListActivity.class);
				intent.putExtra("channelName", channelList.get(position)
						.getChannelName());
				intent.putExtra("channelId", channelList.get(position)
						.getChannelId());
				startActivity(intent);
			}
		});
		draggableGridView.setOnChangeListener(new OnChanageListener() {

			@Override
			public void onChange(int from, int to) {
				// TODO Auto-generated method stub
				// Channel temp = channelList.get(to);
				// 直接交换
				// channelList.set(to, channelList.get(form));
				// channelList.set(form, temp);
				// 这里的处理需要注意下
				Channel temp = channelList.get(from);
				if (from < to) {
					for (int i = from; i < to; i++) {
						Collections.swap(channelList, i, i + 1);
					}
				} else if (from > to) {
					for (int i = from; i > to; i--) {
						Collections.swap(channelList, i, i - 1);
					}
				}

				channelList.set(to, temp);
				channelAdapter.notifyDataSetChanged();
			}
		});

	}
}
