package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.MomentDetailActivity;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnItemClickListener;
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
	private int pageSize = 1;
	private int pageNum = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
		findListView = (ZrcListView) view.findViewById(R.id.fragment_dynamic_list);
		momentList = new ArrayList<Moment>();
		initView();
		// 从服务端获取数据
		getData();
//		getDataFromServer();
		findListView.setAdapter(new FindListViewAdapter(getActivity(),
				momentList, getDisplayImageOptions()));
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
				getData();
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
				getData();
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
		// listviewItem 点击事件
		findListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(ZrcListView parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),MomentDetailActivity.class);
				intent.putExtra("momentId", momentList.get(position).getMomentId());
				startActivity(intent);
			}
		});
	}

//	private void getDataFromServer() {
//		// TODO Auto-generated method stub
//		if (momentList == null) {
//			momentList = new ArrayList<Moment>();
//		}
//		for (int i = 0; i < 1; i++) {
//			Moment moment = new Moment();
//			moment.setTitle("不再懊悔 App 自动生成器");
//			moment.setContent("隔壁小禹说，10 年前，他就有做叫车服务的想法。对面小 S 说，20 年前，她就想做在线购物网站。斜对面老吴说，建国时，他就想做一款应用商店，从此不会编程的你，也可轻松制作自己的 App");
//			moment.setAuthorNickName("KDF5000");
//			moment.setAuthorId(1000 + i);
//			moment.setFollowNum(1234);
//			moment.setMomentId(1000+i);
//			moment.setPraiseNum(1232);
//			moment.setCommentNum(100);
//			moment.setMomentImg("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
//			moment.setAvatarUrl("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
//			moment.setPostTime("2小时前");
//			moment.setIsFocused(0);
//			momentList.add(moment);
//		}
//	}
	
	private void getData(){
		if (momentList == null) {
			momentList = new ArrayList<Moment>();
		}
		RequestParams params = new RequestParams();
		params.put("page", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", 123);//暂时写死，这个id由登陆时服务端下发，客户端全程留存
		ApiManager.getInstance().post(getActivity(), C.API.GET_FOCUS_LIST, params, new HttpCallBack() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				List <Moment> moment = (List<Moment>) res;
				for(int i=0;i<moment.size();i++){
					momentList.add(moment.get(i));
				}
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(getActivity(), (String)res);
			}
		}, "Moment");
	}
}
