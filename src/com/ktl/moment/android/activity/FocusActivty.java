package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FansAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;

public class FocusActivty extends BaseActivity {

	@ViewInject(R.id.my_focus)
	private ZrcListView myFocus;

	@ViewInject(R.id.title_back_img)
	private ImageView titleBackImg;

	private LayoutInflater inflater;
	private List<User> focusList;
	private FansAdapter fansAdapter;

	private int pageNum = 1;
	private int pageSize = 10;
	private String intentFlag;
	private long otherUserId;
	private boolean hasMore;
	
	private LoadingDialog loading;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initEvent();
	}

	private void initView() {
		this.inflater = getLayoutInflater();
		this.inflater.inflate(R.layout.activity_my_focus, contentLayout, true);
		ViewUtils.inject(this);
		
//		loading = new LoadingDialog(this);
//		loading.show();

		Intent intent = getIntent();
		intentFlag = intent.getStringExtra("intentFlag");
		otherUserId = intent.getLongExtra("otherUserId", -1);

		focusList = new ArrayList<User>();
		fansAdapter = new FansAdapter(this, focusList, getDisplayImageOptions());
		myFocus.setAdapter(fansAdapter);
		loadData();

		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		if (intentFlag.equals("focus")) {
			setMiddleTitleName("我的关注");
		} else if (intentFlag.equals("fans")) {
			setMiddleTitleName("我的粉丝");
		} else if (intentFlag.equals("userFocus")) {
			setMiddleTitleName("他的关注");
		} else {
			setMiddleTitleName("他的粉丝");
		}
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(this);
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		myFocus.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		myFocus.setFootable(footer);
	}

	private void initEvent() {
		// 下拉刷新事件回调（可选）
		myFocus.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				loadData();
			}
		});

		// 加载更多事件回调（可选）
		myFocus.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				loadMore();
			}
		});
	}

	private void loadData() {
		pageNum = 1;
		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		String url = C.API.GET_FOCUS_AUTHOR_LIST;
		if (intentFlag.equals("focus")) {
			url = C.API.GET_FOCUS_AUTHOR_LIST;
		} else if (intentFlag.equals("fans")) {
			url = C.API.GET_MY_FANS_LIST;
		} else if (intentFlag.equals("userFocus")) {
			url = C.API.GET_USER_FOCUS_LIST;
			params.put("otherUserId", otherUserId);
		} else {
			url = C.API.GET_USER_FANS_LIST;
			params.put("otherUserId", otherUserId);
		}
		ApiManager.getInstance().post(this, url, params, new HttpCallBack() {

			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				focusList.clear();
				@SuppressWarnings("unchecked")
				List<User> user = (List<User>) res;
				focusList.clear();
				focusList.addAll(user);
//				new Handler().postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						loading.dismiss();
						myFocus.setRefreshSuccess("");
						myFocus.startLoadMore();// 允许加载更多
						fansAdapter.notifyDataSetChanged();
//					}
//				}, 500);
				if (user.size() < pageSize) {
					hasMore = false;
				} else {
					hasMore = true;
				}
			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				final String str = (String)res;
//				new Handler().postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						loading.dismiss();
						ToastUtil.show(FocusActivty.this, str);
						myFocus.setRefreshFail("");
						myFocus.startLoadMore();// 允许加载更多
//					}
//				}, 1000);
			}
		}, "User");
	}

	private void loadMore() {
		if (!hasMore) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					myFocus.setLoadMoreSuccess();
					myFocus.stopLoadMore();
					ToastUtil.show(FocusActivty.this, "没有更多了~");
				}
			}, 500);
			return;
		}

		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("pageNum", ++pageNum);
		params.put("pageSize", pageSize);
		String url = C.API.GET_FOCUS_AUTHOR_LIST;
		if (intentFlag.equals("focus")) {
			url = C.API.GET_FOCUS_AUTHOR_LIST;
		} else if (intentFlag.equals("fans")) {
			url = C.API.GET_MY_FANS_LIST;
		} else if (intentFlag.equals("userFocus")) {
			url = C.API.GET_USER_FOCUS_LIST;
			params.put("otherUserId", otherUserId);
		} else {
			url = C.API.GET_USER_FANS_LIST;
			params.put("otherUserId", otherUserId);
		}
		ApiManager.getInstance().post(this, url, params, new HttpCallBack() {

			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				List<User> user = (List<User>) res;
				focusList.addAll(user);
				fansAdapter.notifyDataSetChanged();
				myFocus.setLoadMoreSuccess();
				if (user.size() < pageSize) {
					hasMore = false;
				} else {
					hasMore = true;
				}
			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(FocusActivty.this, (String) res);
			}
		}, "User");
	}

	@OnClick({ R.id.title_back_img })
	private void click(View v) {
		if (v.getId() == R.id.title_back_img) {
			SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SPK_REFRESH_ME_FG, true);
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SPK_REFRESH_ME_FG, true);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}
}
