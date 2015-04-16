package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MomentPlaAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.etsy.StaggeredGridView;
import com.ktl.moment.entity.Moment;

public class MomentFragment extends BaseFragment {
	
	private static final String TAG = "MomentFragment";
	
	private StaggeredGridView staggeredGridView;
	private List<Moment> momentList;
	private MomentPlaAdapter momentPlaAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_moment, container, false);
		staggeredGridView = (StaggeredGridView) view.findViewById(R.id.moment_pla_list);
		
		momentList = new ArrayList<Moment>();
		getData();
		momentPlaAdapter = new MomentPlaAdapter(getActivity(), momentList, getDisplayImageOptions());
		staggeredGridView.setAdapter(momentPlaAdapter);
		
		return view;
	}
	
	private void getData(){
		if(momentList == null){
			momentList = new ArrayList<Moment>();
		}
		for(int i=0;i<10;i++){
			Moment moment = new Moment();
			moment.setPostTime("4月10日");
			if(i%3 == 0){
				moment.setPublic(1);
			}
			if(i%4 == 0){
				moment.setCollect(1);
			}
			moment.setTitle(i+"不再懊悔 App 自动生成器");
			String content = "隔壁小禹说，10 年前，他就有做叫车服务的想法。对面小 S 说，20 年前，她就想做在线购物网站。斜对面老吴说，建国时，他就想做一款应用商店，从此不会编程的你，也可轻松制作自己的 App。隔壁小禹说，10 年前，他就有做叫车服务的想法。对面小 S 说，20 年前，她就想做在线购物网站。斜对面老吴说，建国时，他就想做一款应用商店，从此不会编程的你，也可轻松制作自己的 App";
			moment.setContent(content.substring(0, 20*i));
			moment.setLabel("创意、果蔬");
			if(i%2 == 1){
				moment.setMomentImg("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
			}else{
				moment.setMomentImg("");
			}
			momentList.add(moment);
		}
	}
}
