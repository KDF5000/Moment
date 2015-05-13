package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.activity.MomentDialogActivity;
import com.ktl.moment.android.activity.ReadActivity;
import com.ktl.moment.android.adapter.MomentPlaAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.etsy.StaggeredGridView;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.momentstore.MomentSyncTask;
import com.ktl.moment.momentstore.MomentSyncTaskManager;
import com.ktl.moment.momentstore.MomentSyncTaskManager.MomentSyncCallback;
import com.ktl.moment.utils.EncryptUtil;
import com.ktl.moment.utils.TimeFormatUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.db.DbTaskType;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.loopj.android.http.RequestParams;

public class MomentFragment extends BaseFragment implements OnScrollListener,
		OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = "MomentFragment";

	
	private StaggeredGridView staggeredGridView;
	private List<Moment> momentList;
	private MomentPlaAdapter momentPlaAdapter;

	private static final int REAUEST_CODE_OPEN = 1000;
	private static final int REAUEST_CODE_LABEL = 1001;
	private static final int REQUEST_CODE_DELETE = 1002;

	private boolean mHasRequestedMore = true;

	private int pageSize = 20;
	private int pageNum = 1;

	private boolean isSyncing = false;//是否正在同步
	MomentSyncTaskManager syncMomentManager = new MomentSyncTaskManager();
	
	private ImageView navRightImg;//同步图标
	private AnimationDrawable syncAnimationDrawable ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_moment, container, false);
		navRightImg = ((HomeActivity) getActivity()).getTitleRightImg();
		momentList = new ArrayList<Moment>();
		staggeredGridView = (StaggeredGridView) view
				.findViewById(R.id.moment_pla_list);
		getDataFromDB();
	
		
		momentList = new ArrayList<Moment>();
		momentPlaAdapter = new MomentPlaAdapter(getActivity(), momentList,
				getDisplayImageOptions());
		momentPlaAdapter.notifyDataSetChanged();
		staggeredGridView.setAdapter(momentPlaAdapter);

		
		initEvent();
		return view;
	}
	/**
	 * 开始同步动画
	 */
	private void startSyncAnimation(){
		if(syncAnimationDrawable==null || !syncAnimationDrawable.isRunning()){
			navRightImg.setImageResource(R.anim.sync_animation);
			syncAnimationDrawable = (AnimationDrawable) navRightImg.getDrawable();
			syncAnimationDrawable.start();
			isSyncing = true;
		}
	}
	/**
	 * 停止同步动画
	 */
	private void stopSyncAnimation(){
		if(syncAnimationDrawable!=null && syncAnimationDrawable.isRunning()){
			syncAnimationDrawable.stop();
			navRightImg.setImageResource(R.drawable.sync_1);
			isSyncing = false;
			syncAnimationDrawable = null;
		}
	}
	/**
	 * 初始化事件
	 */
	private void initEvent() {
		if (navRightImg != null) {
			navRightImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					ToastUtil.show(getActivity(), "开始同步");
					if(isSyncing == false){
						startSyncAnimation();
						// 1.从本地数据库查找所有没有上传到服务端的灵感
						((HomeActivity) getActivity()).getDbData(
								C.DbTaskId.MOMENT_GET_DIRTY_MOMENT,
								DbTaskType.findByCondition, Moment.class,
								WhereBuilder.b("dirty", "=", 1));//
					}else{
						stopSyncAnimation();
						//取消所有的请求
						ApiManager.getInstance().cancelAllRequest();
					}
				}
			});
		}
		staggeredGridView.setOnScrollListener(this);
		staggeredGridView.setOnItemClickListener(this);
		staggeredGridView.setOnItemLongClickListener(this);
	}

	private void getDataFromDB() {
		pageNum = 1;
		Selector selector = Selector.from(Moment.class)
				.orderBy("postTime", true).limit(pageSize)
				.offset(pageSize * (pageNum-1));
		((HomeActivity) getActivity()).getDbData(C.DbTaskId.GET_MOMENT_LIST,
				DbTaskType.selectByCustom, selector);
	}

	/**
	 * 获取取数据
	 */
	private void getDataFromServer() {
		   /*userId:31243,        //用户id
		    authorId:212,        //被查看的用户id
		    pageNum:0,
		    pageSize:10*/
		RequestParams params = new  RequestParams();
		User userInfo = Account.getUserInfo();
		if(userInfo == null){
			ToastUtil.show(getActivity(), "用户没有登录，请重新登录");
			((HomeActivity) getActivity()).returnLogin();
			return ;
		}
		params.put("userId", userInfo.getUserId());
		params.put("pageNum", pageNum++);
		params.put("pageSize", pageSize);
		ApiManager.getInstance().post(getActivity(), C.API.GET_USER_MOMENT_LIST, params, new HttpCallBack() {
			
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				List<Moment> list = (List<Moment>) res;
				if(list==null){
					ToastUtil.show(getActivity(), "同步完成");
					return;
				}
				if(momentList==null){
					momentList = new ArrayList<Moment>();
				}
				List<Moment> loacalList = new ArrayList<Moment>();
				for(Moment moment : list){
					moment.setMomentUid(EncryptUtil.md5(moment.getAuthorId()+"",moment.getTitle(), moment.getPostTime()).hashCode());
					loacalList.add(moment);
				}
				momentList.clear();
				momentList.addAll(loacalList);
				momentPlaAdapter.notifyDataSetChanged();
				//保存到本地数据库
				
				((HomeActivity) getActivity()).saveDbData(C.DbTaskId.MOMENT_LIST_SAVE,
						Moment.class, loacalList);
				stopSyncAnimation();
				ToastUtil.show(getActivity(), "同步完成");
			}
			
			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(getActivity(), res.toString());
				stopSyncAnimation();
			}
		}, "Moment");
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REAUEST_CODE_OPEN: {
				boolean isOpen = data.getBooleanExtra("isClick", false);
				int position = data.getIntExtra("position", 0);
				if (isOpen) {
					if (momentList.get(position).getIsOpen() == 1) {
						momentList.get(position).setIsOpen(0);
					} else {
						momentList.get(position).setIsOpen(1);
					}
					momentPlaAdapter.notifyDataSetChanged();
				}
				break;
			}
			case REAUEST_CODE_LABEL:
				
				break;
			case REQUEST_CODE_DELETE:

				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Item Clicked: " + position,
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getActivity(), ReadActivity.class);
		intent.putExtra("momentUid", momentList.get(position).getMomentUid());
		startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem
				+ " visibleItemCount:" + visibleItemCount + " totalItemCount:"
				+ totalItemCount);
		if (!mHasRequestedMore) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if (lastInScreen >= totalItemCount) {
				Log.d(TAG, "onScroll lastInScreen - so load more");
				mHasRequestedMore = true;
				getDataFromServer();
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		Intent dialogIntent = new Intent(getActivity(),
				MomentDialogActivity.class);
		dialogIntent.putExtra("position", position);
		if (momentList.get(position).getIsOpen() == 1) {
			dialogIntent.putExtra("isPublic", 1);
		} else {
			dialogIntent.putExtra("isPublic", 0);
		}
		startActivityForResult(dialogIntent, REAUEST_CODE_OPEN);
		return true;
	}
	/**
	 * 
	 * @param taskId
	 * @param res
	 */
	@SuppressWarnings("unchecked")
	public void dataFinish(int taskId, Object res) {
		switch (taskId) {
		case C.DbTaskId.MOMENT_GET_DIRTY_MOMENT: {

			List<Moment> dirtyMoments = (List<Moment>) res;
			if (dirtyMoments != null && !dirtyMoments.isEmpty()) {
				// 上传灵感
				int len = dirtyMoments.size();
				for (int i = 0; i < len; i++) {
					MomentSyncTask task = new MomentSyncTask(
							dirtyMoments.get(i), syncMomentManager);
					syncMomentManager.addSyncTask(task);
				}
				syncMomentManager.setTaskCallBack(new MomentSyncCallback() {

					@Override
					public void onError(String msg) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), msg);
						stopSyncAnimation();
					}

					@Override
					public void onComplete(int syncCount) {
						// TODO Auto-generated method stub
						// 从服务端获取数据
						ToastUtil.show(getActivity(), syncCount + "");
						//从服务端获取数据
						getDataFromServer();
					}
				});
				if(isSyncing == true){
					syncMomentManager.startSync();
				}else{
					syncMomentManager.killSync("取消同步");
				}
			} else {
				pageNum =1;
				getDataFromServer();
			}
			break;
		}
		case C.DbTaskId.GET_MOMENT_LIST:
			List<Moment> list = (List<Moment>) res;
			if(list == null){
				Toast.makeText(getActivity(), "加载完成~", Toast.LENGTH_SHORT).show();
				return ;
			}
			try {
				if(momentList==null){
					momentList = new ArrayList<Moment>();
				}
				momentList.clear();
				momentList.addAll(list);
				momentPlaAdapter.notifyDataSetChanged();
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("MomentFragment", "MomentFragment-->resum");
	}

	@Override
	public void refreshFragmentContent() {
		// TODO Auto-generated method stub
		super.refreshFragmentContent();
		getDataFromDB();
	}
}
