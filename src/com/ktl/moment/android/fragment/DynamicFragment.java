package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;

public class DynamicFragment extends BaseFragment {
	private static final String TAG = "dynamicFragment";

	private ZrcListView findListView;
	private List<Moment> momentList;// 灵感列表

	private Handler handler;
	private int pageSize = 10;
	private int pageNum = 0;
	private FindListViewAdapter findListViewAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
		findListView = (ZrcListView) view.findViewById(R.id.fragment_dynamic_list);
		momentList = new ArrayList<Moment>();
		initView();
		// 从服务端获取数据
		getDataFromServer();
		findListView.setAdapter(findListViewAdapter);
		initEvent();
		return view;
	}

	private void initView() {
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

	private void initEvent() {
		handler = new Handler();
		// 下拉刷新事件回调（可选）
		findListView.startLoadMore();// 允许加载更多
		findListView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				pageNum = 0;
				getDataFromServer();
//				findListViewAdapter.notifyDataSetChanged();
				Log.i("pageNum", pageNum+"");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						findListView.setRefreshSuccess("");
					}
				}, 2 * 1000);
			}
		});

		// 加载更多事件回调（可选）
		findListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				pageNum++;
				getDataFromServer();
				Log.i("pageNum", pageNum+"");
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						findListView.setLoadMoreSuccess();
					}
				}, 2 * 1000);
			}
		});
	}
	
	private void getDataFromServer(){
		if (momentList == null) {
			momentList = new ArrayList<Moment>();
		}
		if(findListViewAdapter == null){
			findListViewAdapter = new FindListViewAdapter(getActivity(),momentList, getDisplayImageOptions());
		}
		RequestParams params = new RequestParams();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", 123);//暂时写死，这个id由登陆时服务端下发，客户端全程留存
		ApiManager.getInstance().post(getActivity(), C.API.GET_HOME_FOCUS_LIST, params, new HttpCallBack() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				List <Moment> moment = (List<Moment>) res;
				momentList.addAll(moment);
				findListViewAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(getActivity(), (String)res);
			}
		}, "Moment");
	}
}
