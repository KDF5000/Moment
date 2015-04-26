package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FansAdapter;
import com.ktl.moment.android.base.BaseActivity;
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

public class RecommendAuthorActivity extends BaseActivity{
	
	@ViewInject(R.id.recommend_author)
	private ListView recommendAuthor;
	
	@ViewInject(R.id.title_right_tv)
	private TextView titleRightTv;
	
	private FansAdapter fansAdapter;
	private List<User> userList;
	private long userId;
	private int pageNum = 0;
	private int pageSize = 12;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_recommend_author, contentLayout, true);
		
		ViewUtils.inject(this);
		initView();
	}
	
	private void initView(){
		setMiddleTitleVisible(true);
		setMiddleTitleName("推荐用户");
		setTitleRightTvVisible(true);
		setTitleRightTv("完成");
		setBaseActivityBgColor(getResources().getColor(R.color.main_title_color));
		
		//从SP获取用户id
		List<User> spUser = SharedPreferencesUtil.getInstance().getList(C.SPKey.SPK_LOGIN_INFO);
		userId = spUser.get(0).getId();
		
		getData();
		fansAdapter = new FansAdapter(this, userList, getDisplayImageOptions());
		recommendAuthor.setAdapter(fansAdapter);
	}
	
	private void getData(){
		if(userList == null){
			userList = new ArrayList<User>();
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
				userList.addAll(user);
				fansAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(RecommendAuthorActivity.this, (String)res);
			}
		}, "User");
	}
	
	@OnClick({R.id.title_right_tv})
	public void click(View v){
		if(v.getId() == R.id.title_right_tv){
			actionStart(HomeActivity.class);
		}
	}
}
