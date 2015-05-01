package com.ktl.moment.android.fragment.message;

import java.util.ArrayList;
import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MsgNotificationAdapter;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Notification;
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

public class NotificationFragment extends Fragment {

	@ViewInject(R.id.msg_notify_listview)
	private ListView notifyListview;

	private List<Notification> notifyList;
	private MsgNotificationAdapter notifyAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_msg_notification,
				container, false);
		ViewUtils.inject(this, view);
		initView();

		return view;
	}

	private void initView() {
		notifyList = new ArrayList<Notification>();
		getData();
		notifyAdapter = new MsgNotificationAdapter(getActivity(), notifyList,
				ImageManager.getInstance().getDisplayImageOptions());
		notifyListview.setAdapter(notifyAdapter);
	}

	private void getData() {
		if (notifyList == null) {
			notifyList = new ArrayList<Notification>();
		}
		for (int i = 0; i < 9; i++) {
			Notification tmp = new Notification();
			tmp.setUserAvatar("");
			tmp.setUserNickname(i + "HUST");
			tmp.setMsgType(i);
			tmp.setMsgTitle("一女大学生赴东南亚旅游，因衣装暴露被遣返。");
			tmp.setMsgContent("被遣返回国的王某，抱怨飞机上的空调太冷，下机时还不顾劝阻强行裹走了一条飞机上的毛毯。王某称，她平时穿衣打扮都走性感路线，对其他人的异样目光从来不予理会。但是没想到这次出境，却因为穿衣打扮影响了自己的旅行。");
			tmp.setMsgDate(1 + "小时前");
			notifyList.add(tmp);
		}

		User spUser = (User) SharedPreferencesUtil.getInstance().getObject(
				C.SPKey.SPK_LOGIN_INFO);
		RequestParams params = new RequestParams();
		params.put("userId", spUser.getUserId());
		params.put("pageNum", 0);
		params.put("pageSize", 10);
		ApiManager.getInstance().post(getActivity(),
				C.API.GET_FOCUS_AUTHOR_LIST, params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Notification> notify = (List<Notification>) res;
						notifyList.addAll(notify);
						notifyAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
					}
				}, "User");
	}
}
