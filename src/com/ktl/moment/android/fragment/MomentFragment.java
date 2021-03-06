package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.ktl.moment.android.activity.EditorActivity;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.activity.MomentDialogActivity;
import com.ktl.moment.android.activity.ReadActivity;
import com.ktl.moment.android.adapter.MomentPlaAdapter;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.LoadingDialog;
import com.ktl.moment.android.component.etsy.StaggeredGridView;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.momentstore.MomentSyncTask;
import com.ktl.moment.momentstore.MomentSyncTaskManager;
import com.ktl.moment.momentstore.MomentSyncTaskManager.MomentSyncCallback;
import com.ktl.moment.utils.BasicInfoUtil;
import com.ktl.moment.utils.EncryptUtil;
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
	private ImageView blankImg;

	private static final int REAUEST_CODE_OPEN = 1000;
	private static final int REAUEST_CODE_LABEL = 1001;
	private static final int REQUEST_CODE_DELETE = 1002;

	private boolean mHasRequestedMore = true;

	private int pageSize = 10;
	private int pageNum = 1;
    private int dbPageNum = 1;
	private boolean isSyncing = false;// 是否正在同步

	private ImageView navRightImg;// 同步图标
	private AnimationDrawable syncAnimationDrawable;
	private LoadingDialog loadingDlg;//加载动态图标
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_moment, container, false);
		navRightImg = ((HomeActivity) getActivity()).getTitleRightImg();
		momentList = new ArrayList<Moment>();
		staggeredGridView = (StaggeredGridView) view
				.findViewById(R.id.moment_pla_list);
		blankImg = (ImageView) view.findViewById(R.id.moment_blank);
		pageSize = 10;
		pageNum = 1;
	    dbPageNum = 1;
		isSyncing = false;// 是否正在同步
		momentList = new ArrayList<Moment>();
		//加载中动态图标
		loadingDlg = new LoadingDialog(getActivity());
		loadingDlg.show();
		
		getDataFromDB();
		
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
	private void startSyncAnimation() {
		if (syncAnimationDrawable == null || !syncAnimationDrawable.isRunning()) {
			navRightImg.setImageResource(R.anim.sync_animation);
			syncAnimationDrawable = (AnimationDrawable) navRightImg
					.getDrawable();
			syncAnimationDrawable.start();
			isSyncing = true;
		}
	}

	/**
	 * 停止同步动画
	 */
	private void stopSyncAnimation() {
		if (syncAnimationDrawable != null && syncAnimationDrawable.isRunning()) {
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
					// ToastUtil.show(getActivity(), "开始同步");
					if (isSyncing == false) {
						startSyncAnimation();
						// 1.从本地数据库查找所有没有上传到服务端的灵感
						((HomeActivity) getActivity()).getDbData(
								C.DbTaskId.MOMENT_GET_DIRTY_MOMENT,
								DbTaskType.findByCondition, Moment.class,
								WhereBuilder.b("dirty", "=", 1));//
					} else {
						stopSyncAnimation();
						// 取消所有的请求
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
		Selector selector = Selector.from(Moment.class)
				.orderBy("postTime", true).limit(pageSize)
				.offset(pageSize * ((dbPageNum++) - 1));
		((HomeActivity) getActivity()).getDbData(C.DbTaskId.GET_MOMENT_LIST,
				DbTaskType.selectByCustom, selector);
	}

	/**
	 * 获取取数据
	 */
	private void getDataFromServer() {
		if (!BasicInfoUtil.getInstance(getActivity()).isNetworkConnected()) {
			ToastUtil.show(getActivity(), "请检查您的网络连接~");
			return;
		}
		RequestParams params = new RequestParams();
		User userInfo = Account.getUserInfo();
		if (userInfo == null) {
			ToastUtil.show(getActivity(), "用户没有登录，请重新登录");
			((HomeActivity) getActivity()).returnLogin();
			return;
		}
		params.put("userId", userInfo.getUserId());
		params.put("pageNum", pageNum++);
		params.put("pageSize", pageSize);
		ApiManager.getInstance().post(getActivity(), C.API.GET_MY_MOMENT_LIST,
				params, new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Moment> list = (List<Moment>) res;
						if (list == null || list.isEmpty()) {
							// 保存到本地数据库
							((HomeActivity) getActivity()).saveDbData(
									C.DbTaskId.MOMENT_LIST_SAVE, Moment.class,
									momentList);
							/*ToastUtil.show(getActivity(), "同步完成");
							if(momentList==null || momentList.isEmpty()){
								blankImg.setVisibility(View.VISIBLE);
							}else{
								blankImg.setVisibility(View.GONE);
							}
							stopSyncAnimation();*/
							return;
						}
						if (momentList == null) {
							momentList = new ArrayList<Moment>();
						}
						List<Moment> loacalList = new ArrayList<Moment>();
						for (Moment moment : list) {
							moment.setMomentUid(EncryptUtil.md5(
									moment.getAuthorId() + "",
									moment.getTitle(), moment.getPostTime())
									.hashCode());
							loacalList.add(moment);
						}
						if(pageNum == 2){
							momentList.clear();
						}
						momentList.addAll(loacalList);
						if(!momentList.isEmpty()){
							if(blankImg.getVisibility() == View.VISIBLE){
								blankImg.setVisibility(View.GONE);
							}
						}else{
							if(blankImg.getVisibility() == View.GONE){
								blankImg.setVisibility(View.VISIBLE);
							}
						}
						momentPlaAdapter.notifyDataSetChanged();
						// 保存到本地数据库
						/*((HomeActivity) getActivity()).saveDbData(
								C.DbTaskId.MOMENT_LIST_SAVE, Moment.class,
								loacalList);*/
						// 继续获取数据
						getDataFromServer();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
//						ToastUtil.show(getActivity(), res.toString());
						Toast.makeText(getActivity(), "网络错误!", Toast.LENGTH_SHORT).show();
						stopSyncAnimation();
					}
				}, "Moment");

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			int menuSelect = data.getIntExtra("menuSelect", 0);
			int position = data.getIntExtra("position", 0);
			switch (menuSelect) {
			case 2: {//open
				boolean isOpen = data.getBooleanExtra("isClick", false);
				if (isOpen) {
					if (momentList.get(position).getIsPublic() == 1) {
						momentList.get(position).setIsPublic(0);
					} else {
						momentList.get(position).setIsPublic(1);
					}
					momentPlaAdapter.notifyDataSetChanged();
					//请求服务器更新 并保存到本地
					List<Moment> saveList = new ArrayList<Moment>();
					saveList.add(momentList.get(position));
					((HomeActivity) getActivity()).saveDbData(
							C.DbTaskId.EDITOR_MOMENT_SAVE, Moment.class,
							saveList);
				}
				break;
			}
			case 1://编辑
				Intent editIntent = new Intent(getActivity(), EditorActivity.class);
				editIntent.putExtra("moment", momentList.get(position));
				startActivity(editIntent);
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
		Intent intent = new Intent(getActivity(), ReadActivity.class);
		intent.putExtra("momentUid", momentList.get(position).getMomentUid());
		Log.i(TAG, "momentUid-->"+momentList.get(position).getMomentUid());
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
		if (momentList.get(position).getIsPublic() == 1) {
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
				MomentSyncTaskManager syncMomentManager = new MomentSyncTaskManager();
				for (int i = 0; i < len; i++) {
					MomentSyncTask task = new MomentSyncTask(
							dirtyMoments.get(i), syncMomentManager);
					syncMomentManager.addSyncTask(task);
				}
				syncMomentManager.setTaskCallBack(new MomentSyncCallback() {

					@Override
					public void onError(String msg) {
						// TODO Auto-generated method stub
//						ToastUtil.show(getActivity(), msg);
						Toast.makeText(getActivity(), "同步失败", Toast.LENGTH_SHORT).show();
						stopSyncAnimation();
					}

					@Override
					public void onComplete(int syncCount) {
						// TODO Auto-generated method stub
						// 从服务端获取数据
						pageNum =1;
						getDataFromServer();
					}
				});
				if (isSyncing == true) {
					syncMomentManager.startSync();
				} else {
					syncMomentManager.killSync("取消同步");
				}
			} else {
				pageNum = 1;
				getDataFromServer();
			}
			break;
		}
		case C.DbTaskId.GET_MOMENT_LIST:
			final List<Moment> list = (List<Moment>) res;
			if(loadingDlg.isShowing()){
				loadingDlg.dismiss();
			}
			if (list == null || list.isEmpty()) {
//				Toast.makeText(getActivity(), "加载完成~", Toast.LENGTH_SHORT).show();
				if(momentList == null || momentList.isEmpty()){
					blankImg.setVisibility(View.VISIBLE);
				}else{
					blankImg.setVisibility(View.GONE);
				}
				return;
			}
			try {
				if (momentList == null) {
					momentList = new ArrayList<Moment>();
				}
				if(dbPageNum == 2){
					momentList.clear();
				}
				momentList.addAll(list);
				if(loadingDlg.isShowing()){
					loadingDlg.dismiss();
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							momentPlaAdapter.notifyDataSetChanged();
							getDataFromDB();
						}
					}, 800);
				}else{
					momentPlaAdapter.notifyDataSetChanged();
					getDataFromDB();
				}
				
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;
		case C.DbTaskId.MOMENT_LIST_SAVE:
			ToastUtil.show(getActivity(), "同步完成");
			stopSyncAnimation();
			break;
		case C.DbTaskId.EDITOR_MOMENT_SAVE:
			ToastUtil.show(getActivity(), "修改成功");
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
		dbPageNum = 1;
		getDataFromDB();
	}
}
