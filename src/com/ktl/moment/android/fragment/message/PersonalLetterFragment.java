package com.ktl.moment.android.fragment.message;

import java.util.ArrayList;
import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MsgPersonalAdapter;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Message;
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
import android.widget.ListView;

public class PersonalLetterFragment extends Fragment {

	@ViewInject(R.id.personal_msg_listview)
	private ListView msgListview;

	private List<Message> msgList;
	private MsgPersonalAdapter msgPersonalAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_msg_personal_letter,
				container, false);
		ViewUtils.inject(this, view);
		initView();

		return view;
	}

	private void initView() {
		msgList = new ArrayList<Message>();
		getData();
		msgPersonalAdapter = new MsgPersonalAdapter(getActivity(), msgList,
				ImageManager.getInstance().getDisplayImageOptions());
		msgListview.setAdapter(msgPersonalAdapter);
	}

	private void getData() {
		if (msgList == null) {
			msgList = new ArrayList<Message>();
		}

		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("pageNum", 0);
		params.put("pageSize", 10);
		ApiManager.getInstance().post(getActivity(), C.API.GET_MSG_LIST,
				params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Message> msg = (List<Message>) res;
						msgList.addAll(msg);
						msgPersonalAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
					}
				}, "Message");
	}
}
