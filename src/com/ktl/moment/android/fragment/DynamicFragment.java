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

	private ZrcListView findListView;
	private List<Moment> momentList;// 灵感列表

	private int pageSize = 2;
	private int pageNum = 1;
	private MomentListViewAdapter findListViewAdapter;
	private long userId;

	private boolean hasMore = true;
	private LoadingDialog loading;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dynamic, container,
				false);
		findListView = (ZrcListView) view.findViewById(R.id.fragment_dynamic_list);
		//show loading dialog after fragment create
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
		findListViewAdapter = new MomentListViewAdapter(getActivity(),momentList, getDisplayImageOptions());
		findListView.setAdapter(findListViewAdapter);
		initEvent();
		findListView.refresh();
		// 从服务端获取数据
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
		findListView.stopLoadMore();
	}

	private void initEvent() {
		// 下拉刷新事件回调（可选）
		findListView.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				getDataFromServer();
			}
		});

		// 加载更多事件回调（可选）
		findListView.setOnLoadMoreStartListener(new OnStartListener() {
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
		hasMore = true;
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
								findListViewAdapter.notifyDataSetChanged();
								findListView.setRefreshSuccess("");
								findListView.startLoadMore();
								loading.dismiss();
							}
						}, 500);
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
						findListView.setRefreshSuccess("");
					}
				}, "Moment");
	}
	/**
	 * 上拉加载更多数据
	 */
	private void loadMoreMoment(){
		if(hasMore == false){
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					findListView.setLoadMoreSuccess();
					findListView.stopLoadMore();
					ToastUtil.show(getActivity(), "没有更多了~");
				}
			}, 500);
			return ;
		}
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
						List<Moment> moments = (List<Moment>) res;
						if(momentList==null){
							momentList = new ArrayList<Moment>();
						}
						momentList.addAll(moments);
						findListViewAdapter.notifyDataSetChanged();
						if (moments.size() < pageSize) {
							hasMore = false;
						}else{
							hasMore = true;
						}
						findListView.setLoadMoreSuccess();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
					}
				}, "Moment");
	}
}
