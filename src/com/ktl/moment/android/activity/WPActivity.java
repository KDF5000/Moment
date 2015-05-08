package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.android.fragment.DynamicFragment;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;

public class WPActivity extends BaseActivity {

	@ViewInject(R.id.wp_list_view)
	private ZrcListView wpListview;

	private List<Moment> momentList;
	private FindListViewAdapter momentAdapter;
	private Handler handler;

	private int pageSize = 2;
	private int pageNum = 1;
	private boolean hasMore = true;

	private String wp;
	private final String TAG = "WPActivity";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_wp, contentLayout, true);
		ViewUtils.inject(this);

		momentList = new ArrayList<Moment>();
		initView();
		// 从服务端获取数据
		getDataFromServer();
		wpListview.setAdapter(momentAdapter);

		initView();
		initEvent();

		titleBackImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initView() {
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		Intent intent = getIntent();
		wp = intent.getStringExtra("wp");
		if (wp.equals("watch")) {
			setMiddleTitleName("我的围观");
		} else {
			setMiddleTitleName("我的点赞");
		}
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(this);
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		wpListview.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		wpListview.setFootable(footer);

		// 设置列表项出现动画（可选）
		wpListview.setItemAnimForTopIn(R.anim.zrc_topitem_in);
		wpListview.setItemAnimForBottomIn(R.anim.zrc_bottomitem_in);
	}

	private void initEvent() {
		handler = new Handler();
		// 下拉刷新事件回调（可选）
		wpListview.startLoadMore();// 允许加载更多
		wpListview.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				pageNum = 1;
				momentList.clear();
				getDataFromServer();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						wpListview.setRefreshSuccess("");
					}
				}, 2 * 1000);
			}
		});

		// 加载更多事件回调（可选）
		wpListview.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				if (hasMore) {
					pageNum++;
					getDataFromServer();
				}
				Log.i(TAG + "-->pageNum", pageNum + "");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						wpListview.setLoadMoreSuccess();
					}
				}, 2 * 1000);
			}
		});
	}

	private void getDataFromServer() {
		if (momentList == null) {
			momentList = new ArrayList<Moment>();
		}
		if (momentAdapter == null) {
			momentAdapter = new FindListViewAdapter(this, momentList,
					getDisplayImageOptions());
		}
		RequestParams params = new RequestParams();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", Account.getUserInfo().getUserId());

		String url = C.API.GET_MY_WATCH_LIST;
		if (wp.equals("watch")) {
			url = C.API.GET_MY_WATCH_LIST;
		} else {
			url = C.API.GET_MY_PRAISE_LIST;
		}

		ApiManager.getInstance().post(this, url, params, new HttpCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				List<Moment> moment = (List<Moment>) res;
				momentList.addAll(moment);
				momentAdapter.notifyDataSetChanged();
				if (moment.size() < pageSize) {
					hasMore = false;
					ToastUtil.show(WPActivity.this, "没有更多了");
				} else {
					hasMore = true;
				}
			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(WPActivity.this, (String) res);
			}
		}, "Moment");
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}

}
