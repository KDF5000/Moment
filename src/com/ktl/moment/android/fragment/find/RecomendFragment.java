package com.ktl.moment.android.fragment.find;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MomentListViewAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;

public class RecomendFragment extends BaseFragment {

	@ViewInject(R.id.recommend_listview)
	private ZrcListView recommendListview;

	private List<Moment> recommendList;
	private MomentListViewAdapter recommendAdapter;

	private int pageSize = 2;
	private int pageNum = 1;
	private boolean hasMore = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_find_recommend,
				container, false);
		ViewUtils.inject(this, view);

		recommendList = new ArrayList<Moment>();
		recommendAdapter = new MomentListViewAdapter(getActivity(),
				recommendList, getDisplayImageOptions());
		recommendListview.setAdapter(recommendAdapter);

		recommendListview.refresh();
		initEvent();
		initView();

		return view;
	}

	private void initView() {
		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(getActivity());
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		recommendListview.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(getActivity());
		footer.setCircleColor(0xff33bbee);
		recommendListview.setFootable(footer);

		// 设置列表项出现动画（可选）
		recommendListview.setItemAnimForTopIn(R.anim.zrc_topitem_in);
		recommendListview.setItemAnimForBottomIn(R.anim.zrc_bottomitem_in);
		recommendListview.stopLoadMore();
	}

	private void initEvent() {
		// 下拉刷新事件回调（可选）
		recommendListview.setOnRefreshStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 刷新开始
				getDataFromServer();
			}
		});

		// 加载更多事件回调（可选）
		recommendListview.setOnLoadMoreStartListener(new OnStartListener() {
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
		params.put("userId", Account.getUserInfo().getUserId());
		ApiManager.getInstance().post(getActivity(), C.API.GET_RECOMMEND_MOMENT,
				params, new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moments = (List<Moment>) res;
						if (recommendList == null) {
							recommendList = new ArrayList<Moment>();
						}
						recommendList.clear();
						recommendList.addAll(moments);
						recommendAdapter.notifyDataSetChanged();
						recommendListview.setRefreshSuccess("");
						recommendListview.startLoadMore();
						if (moments.size() < pageSize) {
							hasMore = false;
						} else {
							hasMore = true;
						}
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
						recommendListview.setRefreshSuccess("");
					}
				}, "Moment");
	}

	/**
	 * 上拉加载更多数据
	 */
	private void loadMoreMoment() {
		if (hasMore == false) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					recommendListview.setLoadMoreSuccess();
					recommendListview.stopLoadMore();
					ToastUtil.show(getActivity(), "没有更多了~");
				}
			}, 500);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("pageNum", ++pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", Account.getUserInfo().getUserId());
		Log.i("pageNum", pageNum+"");
		ApiManager.getInstance().post(getActivity(), C.API.GET_RECOMMEND_MOMENT,
				params, new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moments = (List<Moment>) res;
						if (recommendList == null) {
							recommendList = new ArrayList<Moment>();
						}
						recommendList.addAll(moments);
						recommendAdapter.notifyDataSetChanged();
						if (moments.size() < pageSize) {
							hasMore = false;
						} else {
							hasMore = true;
						}
						recommendListview.setLoadMoreSuccess();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), (String) res);
					}
				}, "Moment");
	}
}
