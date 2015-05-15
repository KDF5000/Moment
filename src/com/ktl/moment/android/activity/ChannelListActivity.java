package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MomentListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.LoadingDialog;
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

	private MomentListViewAdapter channelListAdapter;

	private List<Moment> channelList;

	private int pageNum = 1;
	private int pageSize = 10;
	private long userId;
	private boolean hasMore = true;

	private CharSequence channelName;
	private long channelId;
	private LoadingDialog loading;

	private final String TAG = "ChannalListActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_channel_list,
				contentLayout, true);
		ViewUtils.inject(this);
		
		loading = new LoadingDialog(this);
		loading.show();

		Intent intent = this.getIntent();
		channelName = intent.getCharSequenceExtra("channelName");
		channelId = intent.getLongExtra("channelId", 0);

		userId = Account.getUserInfo().getUserId();

		initView();
		initEvent();

		channelList = new ArrayList<Moment>();
		channelListAdapter = new MomentListViewAdapter(this, channelList,
				getDisplayImageOptions());
		channelListView.setAdapter(channelListAdapter);
		loadData();
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
		// 下拉刷新事件回调（可选）
		channelListView.startLoadMore();// 允许加载更多
		channelListView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				loadData();
			}
		});

		// 加载更多事件回调（可选）
		channelListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				loadMoreData();
			}
		});
	}

	private void loadData() {
		RequestParams params = new RequestParams();
		params.put("pageNum", 1);
		params.put("pageSize", pageSize);
		params.put("userId", userId);
		params.put("channelId", channelId);
		ApiManager.getInstance().post(this, C.API.GET_CHANNEL_INFO_LIST,
				params, new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moment = (List<Moment>) res;
						channelList.clear();
						channelList.addAll(moment);
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								loading.dismiss();
								channelListAdapter.notifyDataSetChanged();
								channelListView.setRefreshSuccess("");
								channelListView.startLoadMore();
							}
						}, 500);
						if (moment.size() < pageSize) {
							hasMore = false;
						} else {
							hasMore = true;
						}
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						final String str = (String)res;
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								loading.dismiss();
								ToastUtil.show(ChannelListActivity.this, str);
								channelListView.setRefreshFail("");
							}
						}, 1000);
					}
				}, "Moment");
	}

	private void loadMoreData() {
		if (!hasMore) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method
					// stubfindListView.setLoadMoreSuccess();
					channelListView.stopLoadMore();
					ToastUtil.show(ChannelListActivity.this, "没有更多了~");
				}
			}, 500);
			return;
		}

		RequestParams params = new RequestParams();
		params.put("pageNum", ++pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", userId);
		params.put("channelId", channelId);
		ApiManager.getInstance().post(this, C.API.GET_CHANNEL_INFO_LIST,
				params, new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moment = (List<Moment>) res;
						channelList.addAll(moment);
						channelListAdapter.notifyDataSetChanged();
						channelListView.setLoadMoreSuccess();
						if (moment.size() < pageSize) {
							hasMore = false;
						} else {
							hasMore = true;
						}
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
