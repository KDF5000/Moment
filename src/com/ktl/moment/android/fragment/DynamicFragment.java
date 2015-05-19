package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.AccountActivity;
import com.ktl.moment.android.adapter.MomentListViewAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;

public class DynamicFragment extends BaseFragment {
	private static final String TAG = "dynamicFragment";

	private ZrcListView dynamicListView;
	private List<Moment> momentList;// 灵感列表

	private int pageSize = 5;
	private int pageNum = 1;
	private MomentListViewAdapter dynamicListViewAdapter;
	private long userId;

	private LoadingDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dynamic, container,
				false);
		dynamicListView = (ZrcListView) view
				.findViewById(R.id.fragment_dynamic_list);
		// show loading dialog after fragment create
		loading = new LoadingDialog(getActivity());
		loading.show();

		User user = Account.getUserInfo();
		if (user == null) {
			Intent intent = new Intent(getActivity(), AccountActivity.class);
			startActivity(intent);
			getActivity().finish();
		}
		userId = user.getUserId();

		initView();
		momentList = new ArrayList<Moment>();
		dynamicListViewAdapter = new MomentListViewAdapter(getActivity(),momentList, getDisplayImageOptions());
		dynamicListView.setAdapter(dynamicListViewAdapter);
		initEvent();
		dynamicListView.refresh();
		// 从服务端获取数据
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void initView() {
		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		dynamicListView.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xff33bbee);
		dynamicListView.setFootable(footer);

		// 设置列表项出现动画（可选）
		dynamicListView.setItemAnimForTopIn(R.anim.zrc_topitem_in);
		dynamicListView.setItemAnimForBottomIn(R.anim.zrc_bottomitem_in);
		dynamicListView.stopLoadMore();
	}

	private void initEvent() {
		// 下拉刷新事件回调（可选）
		dynamicListView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				getDataFromServer();
			}
		});

		// 加载更多事件回调（可选）
		dynamicListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				loadMoreMoment();
			}
		});
	}
	
	/**
	 * 下拉刷新从服务端获取最新数据
	 */
	private void getDataFromServer() {
		pageNum = 1;
		RequestParams params = new RequestParams();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", userId);
		ApiManager.getInstance().post(getActivity(), C.API.GET_HOME_FOCUS_LIST,
				params, new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moments = (List<Moment>) res;
						if(momentList==null){
							momentList = new ArrayList<Moment>();
						}
						momentList.clear();
						momentList.addAll(moments);
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								loading.dismiss();
								dynamicListViewAdapter.notifyDataSetChanged();
								dynamicListView.setRefreshSuccess("");
								dynamicListView.startLoadMore();
							}
						}, 500);
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						final String str = (String)res;
						ToastUtil.show(getActivity(), str);

						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								dynamicListView.setRefreshSuccess("");
								loading.dismiss();
							}
						}, 1000);
					}
				}, "Moment");
	}
	/**
	 * 上拉加载更多数据
	 */
	private void loadMoreMoment(){
		RequestParams params = new RequestParams();
		params.put("pageNum", ++pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", userId);
		ApiManager.getInstance().post(getActivity(), C.API.GET_HOME_FOCUS_LIST,
				params, new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						dynamicListView.setLoadMoreSuccess();
						List<Moment> moments = (List<Moment>) res;
						if(moments == null || moments.isEmpty()){
							dynamicListView.stopLoadMore();
							ToastUtil.show(getActivity(), "没有更多了~");
							return ;
						}
						if(momentList==null){
							momentList = new ArrayList<Moment>();
						}
						momentList.addAll(moments);
						dynamicListViewAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
					}
				}, "Moment");
	}
}
