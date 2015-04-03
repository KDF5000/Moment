package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.entity.Moment;

public class FindFragment extends BaseFragment {
	@SuppressWarnings("unused")
	private static final String TAG = "FindFragment";

	private ListView findListView;
	private List<Moment> momentList;// 灵感列表

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		findListView = (ListView) view.findViewById(R.id.fragment_find_list);
		momentList = new ArrayList<Moment>();
		// 从服务端获取数据
		getDataFromServer();
		findListView.setAdapter(new FindListViewAdapter(getActivity(),
				momentList, getDisplayImageOptions()));
		initEvent();
		return view;
	}

	private void initEvent(){
		findListView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				HomeActivity homeActivity = (HomeActivity)getActivity();
				if(homeActivity!=null){
					homeActivity.toggleMenu(event);
				}
				return false;
			}
		});
	}
	private void getDataFromServer() {
		// TODO Auto-generated method stub
		if (momentList == null) {
			momentList = new ArrayList<Moment>();
		}
		Moment moment = new Moment();
		for (int i = 0; i < 10; i++) {
			moment.setTitle("不再懊悔 App 自动生成器");
			moment.setContent("隔壁小禹说，10 年前，他就有做叫车服务的想法。对面小 S 说，20 年前，她就想做在线购物网站。斜对面老吴说，建国时，他就想做一款应用商店，从此不会编程的你，也可轻松制作自己的 App");
			moment.setAuthorNickName("KDF5000");
			moment.setAuthorId(1000);
			moment.setFollowNums(1234);
			moment.setMomentId(1000);
			moment.setPraiseNums(1232);
			moment.setAvatarUrl("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
			moment.setPostTime("2小时前");
			momentList.add(moment);
		}
	}
}
