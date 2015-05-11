package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.RecommendAuthorAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;

public class RecommendAuthorActivity extends BaseActivity {

	@ViewInject(R.id.recommend_author)
	private ListView recommendAuthor;

	private RecommendAuthorAdapter recommendAuthorAdapter;
	private List<User> userList;
	private long userId;
	private int pageNum = 0;
	private int pageSize = 12;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_recommend_author,
				contentLayout, true);

		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		setMiddleTitleVisible(true);
		setMiddleTitleName("推荐用户");
		setTitleRightTvVisible(true);
		setTitleRightTv("完成");
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		// 从SP获取用户id
		userId = Account.getUserInfo().getUserId();

		getData();
		recommendAuthorAdapter = new RecommendAuthorAdapter(this, userList,
				getDisplayImageOptions());
		recommendAuthor.setAdapter(recommendAuthorAdapter);
	}

	private void getData() {
		if (userList == null) {
			userList = new ArrayList<User>();
		}
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		ApiManager.getInstance().post(this,
				C.API.GET_RECOMMEND_FOCUS_USER_LIST, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<User> user = (List<User>) res;
						userList.addAll(user);
						recommendAuthorAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(RecommendAuthorActivity.this,
								(String) res);
					}
				}, "User");
	}

	@OnClick({ R.id.title_right_tv })
	public void click(View v) {
		if (v.getId() == R.id.title_right_tv) {
			complete();
		}
	}

	private void complete() {
		String focusIds = "";
		List<Long> tmpIds = new ArrayList<Long>();
		for (int i = 0; i < userList.size(); i++) {
			User tmpUser = (User) recommendAuthorAdapter.getItem(i);
			if (tmpUser.getIsFocused() == 1) {
				tmpIds.add(tmpUser.getUserId());
			}
		}
		focusIds = list2String(tmpIds);
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("focusList", focusIds);
		final LoadingDialog dialog = new LoadingDialog(this);
		dialog.show();
		ApiManager.getInstance().post(this, C.API.SUBMIT_RECOMMEND_FOCUS_USER,
				params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						actionStart(HomeActivity.class);
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						ToastUtil.show(RecommendAuthorActivity.this, "请稍后重试");
					}
				}, "User");
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}
	
	private String list2String(List<Long> list){
		if(list == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		for (long id : list) {
			if(flag){
				sb.append(",");
			}else{
				flag = true;
			}
			sb.append(id);
		}
		return sb.toString();
	}
}
