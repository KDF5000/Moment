package com.ktl.moment.android.fragment.message;

import java.util.ArrayList;
import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MsgNotificationAdapter;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Notification;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.manager.ImageManager;
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
import android.widget.ImageView;
import android.widget.ListView;

public class NotificationFragment extends Fragment {

	@ViewInject(R.id.msg_notify_listview)
	private ListView notifyListview;

	@ViewInject(R.id.notification_blank_img)
	private ImageView blankImg;

	private List<Notification> notifyList;
	private MsgNotificationAdapter notifyAdapter;

	private int page = 1;
	private int pageSize = 10;

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
		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("pageNum", page++);
		params.put("pageSize", pageSize);
		ApiManager.getInstance().post(getActivity(),
				C.API.GET_NOTIFICATION_LIST, params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Notification> notify = (List<Notification>) res;
						if (notify == null || notify.isEmpty()) {
							blankImg.setVisibility(View.VISIBLE);
						}
						notifyList.addAll(notify);
						notifyAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
						blankImg.setVisibility(View.VISIBLE);
					}
				}, "Notification");
	}
}
