package com.ktl.moment.android.fragment.message;

import java.util.ArrayList;
import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MsgNewFansAdapter;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.manager.ImageManager;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewFansFragment extends Fragment {

	@ViewInject(R.id.msg_new_fans_listview)
	private ListView fansListView;

	private List<User> fansList;
	private MsgNewFansAdapter newFansAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_msg_new_fans, container,
				false);
		ViewUtils.inject(this, view);

		initView();
		return view;
	}

	private void initView() {
		fansList = new ArrayList<User>();
		getData();
		newFansAdapter = new MsgNewFansAdapter(getActivity(), fansList,
				ImageManager.getInstance().getDisplayImageOptions());
		fansListView.setAdapter(newFansAdapter);
	}
	
	private void getData(){
		if(fansList == null){
			fansList = new ArrayList<User>();
		}
		for(int i=0;i<10;i++){
			User tmp = new User();
			tmp.setUserAvatar("");
			tmp.setNickName("张三");
			tmp.setFocusedTime("2015-04-28");
			tmp.setIsFocused(0);
			fansList.add(tmp);
		}

		User spUser = (User) SharedPreferencesUtil.getInstance().getObject(C.SPKey.SPK_LOGIN_INFO);
		RequestParams params = new RequestParams();
		params.put("userId", spUser.getUserId());
		params.put("pageNum", 0);
		params.put("pageSize", 10);
		ApiManager.getInstance().post(getActivity(), C.API.GET_FOCUS_AUTHOR_LIST, params, new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				List<User> user = (List<User>) res;
				fansList.addAll(user);
				newFansAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(getActivity(), (String)res);
			}
		}, "User");
	}
}
