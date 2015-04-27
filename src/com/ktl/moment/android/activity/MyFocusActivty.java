package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FansAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
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

public class MyFocusActivty extends BaseActivity{
	
	@ViewInject(R.id.my_focus)
	private ZrcListView myFocus;
	
	@ViewInject(R.id.title_back_img)
	private ImageView titleBackImg;
	
	private LayoutInflater inflater;
	private List<User> focusList;
	private Handler handler;
	private FansAdapter fansAdapter;
	
	private int pageNum = 0;
	private int pageSize = 10;
	private long userId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initEvent();
	}
	
	private void initView(){
		this.inflater = getLayoutInflater();
		this.inflater.inflate(R.layout.activity_my_focus, contentLayout, true);
		ViewUtils.inject(this);
		
		Intent intent = getIntent();
		String intentFlag = intent.getStringExtra("intentFlag");

		//从SP获取用户id
		User spUser = (User) SharedPreferencesUtil.getInstance().getObject(C.SPKey.SPK_LOGIN_INFO);
		userId = spUser.getId();
		
		getData();
		fansAdapter = new FansAdapter(this, focusList, getDisplayImageOptions());
		myFocus.setAdapter(fansAdapter);
		
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		if(intentFlag.equals("focus")){
			setMiddleTitleName("我的关注");
		}else if(intentFlag.equals("fans")){
			setMiddleTitleName("我的粉丝");
		}
		setBaseActivityBgColor(getResources().getColor(R.color.main_title_color));
		
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
		handler = new Handler();
		// 下拉刷新事件回调（可选）
		myFocus.startLoadMore();// 允许加载更多
		myFocus.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				pageNum = 0;
				Log.i("pageNum", pageNum+"");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						myFocus.setRefreshSuccess("");
					}
				}, 2 * 1000);
				getData();
				fansAdapter.notifyDataSetChanged();
			}
		});

		// 加载更多事件回调（可选）
		myFocus.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				pageNum++;
				Log.i("pageNum", pageNum+"");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						myFocus.setLoadMoreSuccess();
					}
				}, 2 * 1000);
				getData();
				fansAdapter.notifyDataSetChanged();
			}
		});
	}
	
	private void getData(){
		if(focusList == null){
			focusList = new ArrayList<User>();
		}
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		ApiManager.getInstance().post(this, C.API.GET_FOCUS_AUTHOR_LIST, params, new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				List<User> user = (List<User>) res;
				focusList.addAll(user);
				fansAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(MyFocusActivty.this, (String)res);
			}
		}, "User");
	}
	
	@OnClick({R.id.title_back_img})
	private void click(View v){
		if(v.getId() == R.id.title_back_img){
			finish();
		}
	}
}
