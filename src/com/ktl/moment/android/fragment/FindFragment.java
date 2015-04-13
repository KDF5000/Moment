package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.entity.Moment;

public class FindFragment extends BaseFragment {
	@SuppressWarnings("unused")
	private static final String TAG = "FindFragment";

	private ZrcListView findListView;
	private List<Moment> momentList;// 灵感列表

	private Handler handler;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		findListView = (ZrcListView) view.findViewById(R.id.fragment_find_list);
		momentList = new ArrayList<Moment>();
		initView();
		// 从服务端获取数据
		getDataFromServer();
		
		findListView.setAdapter(new FindListViewAdapter(getActivity(),
				momentList, getDisplayImageOptions()));
		initEvent();
		return view;
	}

	private void initView(){
		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getActivity());
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        findListView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(0xff33bbee);
        findListView.setFootable(footer);

        // 设置列表项出现动画（可选）
        findListView.setItemAnimForTopIn(R.anim.zrc_topitem_in);
        findListView.setItemAnimForBottomIn(R.anim.zrc_bottomitem_in);
	}
	private void initEvent(){
		handler = new Handler();
		 // 下拉刷新事件回调（可选）
        findListView.setOnRefreshStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                //刷新开始
            	handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						findListView.setRefreshSuccess("");
						findListView.startLoadMore();//允许加载更多
					}
				}, 2*1000);
            }
        });

        // 加载更多事件回调（可选）
        findListView.setOnLoadMoreStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                //加载更多
            	 handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						findListView.setLoadMoreSuccess();
					}
				}, 2*1000);
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
			moment.setCommentsNum(100);
			moment.setMomentImg("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
			moment.setAvatarUrl("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
			moment.setPostTime("2小时前");
			momentList.add(moment);
		}
	}
}
