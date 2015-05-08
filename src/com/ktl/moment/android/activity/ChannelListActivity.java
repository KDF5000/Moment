package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;

public class ChannelListActivity extends BaseActivity {

	@ViewInject(R.id.channellist_listview)
	private ZrcListView channelListView;

	private Handler handler;
	private FindListViewAdapter channelListAdapter;

	private List<Moment> channelList;

	private int pageNum = 0;
	private int pageSize = 10;
	private long userId;

	private CharSequence channelName;
	private long channelId;

	private final String TAG = "ChannalListActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_channel_list,
				contentLayout, true);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		channelName = intent.getCharSequenceExtra("channelName");
		channelId = intent.getLongExtra("channelId", 0);

		userId = Account.getUserInfo().getUserId();

		initView();
		initEvent();

		channelList = new ArrayList<Moment>();
		getDataFromServer();
		channelListView.setAdapter(channelListAdapter);
	}

	private void initView() {
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		setMiddleTitleName(channelName);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(this);
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		channelListView.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		channelListView.setFootable(footer);

		// 设置列表项出现动画（可选）
		channelListView.setItemAnimForTopIn(R.anim.zrc_topitem_in);
		channelListView.setItemAnimForBottomIn(R.anim.zrc_bottomitem_in);
	}

	private void initEvent() {
		handler = new Handler();
		// 下拉刷新事件回调（可选）
		channelListView.startLoadMore();// 允许加载更多
		channelListView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				pageNum = 0;
				getDataFromServer();
				Log.i(TAG + "-->pageNum", pageNum + "");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						channelListView.setRefreshSuccess("");
					}
				}, 2 * 1000);
			}
		});

		// 加载更多事件回调（可选）
		channelListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				pageNum++;
				getDataFromServer();
				Log.i(TAG + "-->pageNum", pageNum + "");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						channelListView.setLoadMoreSuccess();
					}
				}, 2 * 1000);
			}
		});
	}

	private void getDataFromServer() {
		if (channelList == null) {
			channelList = new ArrayList<Moment>();
		}
		if (channelListAdapter == null) {
			channelListAdapter = new FindListViewAdapter(this, channelList,
					getDisplayImageOptions());
		}
		RequestParams params = new RequestParams();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", userId);
		params.put("channelId", channelId);
		ApiManager.getInstance().post(this, C.API.GET_CHANNEL_INFO_LIST, params,
				new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moment = (List<Moment>) res;
						channelList.addAll(moment);
						channelListAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(ChannelListActivity.this, (String) res);
					}
				}, "Moment");
	}

	@OnClick({ R.id.title_back_img })
	public void click(View v) {
		if (v.getId() == R.id.title_back_img) {
			finish();
		}
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}

}
