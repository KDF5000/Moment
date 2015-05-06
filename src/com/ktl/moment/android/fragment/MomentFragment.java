package com.ktl.moment.android.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.momentstore.MomentSyncTask;
import com.ktl.moment.momentstore.MomentSyncTaskManager;
import com.ktl.moment.momentstore.MomentSyncTaskManager.MomentSyncCallback;
import com.ktl.moment.utils.EncryptUtil;
import com.ktl.moment.utils.TimeFormatUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.db.DbTaskType;
import com.lidroid.xutils.db.sqlite.WhereBuilder;

public class MomentFragment extends BaseFragment implements OnScrollListener,
		OnItemClickListener, OnItemLongClickListener {

	private static final String TAG = "MomentFragment";

	private StaggeredGridView staggeredGridView;
	private List<Moment> momentList;
	private MomentPlaAdapter momentPlaAdapter;

	private static final int REAUEST_CODE_OPEN = 1000;
	private static final int REAUEST_CODE_LABEL = 1001;
	private static final int REQUEST_CODE_DELETE = 1002;

	private String postTime;
	private boolean mHasRequestedMore = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_moment, container, false);
		staggeredGridView = (StaggeredGridView) view
				.findViewById(R.id.moment_pla_list);
		momentList = new ArrayList<Moment>();
		momentPlaAdapter = new MomentPlaAdapter(getActivity(), momentList,
				getDisplayImageOptions());
		momentPlaAdapter.notifyDataSetChanged();
		staggeredGridView.setAdapter(momentPlaAdapter);

		staggeredGridView.setOnScrollListener(this);
		staggeredGridView.setOnItemClickListener(this);
		staggeredGridView.setOnItemLongClickListener(this);
	    postTime = TimeFormatUtil.getCurrentDateTime();
//		getDataFromServer();
		initEvent();
		return view;
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		ImageView navRightImg = ((HomeActivity) getActivity())
				.getTitleRightImg();
		if (navRightImg != null) {
			navRightImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ToastUtil.show(getActivity(), "开始同步");
					// 1.从本地数据库查找所有没有上传到服务端的灵感
					((HomeActivity) getActivity()).getDbData(
							C.DbTaskId.MOMENT_GET_DIRTY_MOMENT,
							DbTaskType.findByCondition, Moment.class,
							WhereBuilder.b("dirty", "=", 1));//
				}
			});
		}
	}
	
	private void getDataFromDB(){
//		((HomeActivity) getActivity()).getDbData(C.DbTaskId.GET_MOMENT_LIST, DbTaskType.findByPage, Moment.class, WhereBuilder.b(columnName, op, value));
	}

	/**
	 * 从服务器拉取数据
	 */
	private void getDataFromServer() {
		if (momentList == null) {
			momentList = new ArrayList<Moment>();
		}
		for (int i = 0; i < 20; i++) {
			Moment moment = new Moment();
			moment.setAuthorId(i+123);
			moment.setMomentId(i);
			moment.setPostTime(postTime);
			if (i % 3 == 0) {
				moment.setIsOpen(1);
				moment.setDirty(1);
			}
			if (i % 4 == 0) {
				moment.setIsCutCollect(1);
			}
			moment.setTitle(i + "不再懊悔 App 自动生成器");
			String content = "隔壁小禹说，10 年前，他就有做叫车服务的想法。对面小 S 说";
			moment.setContent(content);
			moment.setLabel("创意、果蔬");
			if (i % 2 == 1) {
				moment.setMomentImgs("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
			} else {
				moment.setMomentImgs(null);
			}
			momentList.add(moment);
			moment.setMomentUid(EncryptUtil.md5((i+123)+"",i + "不再懊悔 App 自动生成器",postTime).hashCode());
		}
		
		((HomeActivity)getActivity()).saveDbData(C.DbTaskId.MOMENT_LIST_SAVE, Moment.class, momentList);
		momentPlaAdapter.notifyDataSetChanged();
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
				// onLoadMoreItems();
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

	// private void onLoadMoreItems() {
	// // final ArrayList<String> sampleData = SampleData.generateSampleData();
	// for (String data : sampleData) {
	// momentPlaAdapter.add(data);
	// }
	// // stash all the data in our backing store
	// momentList.addAll(sampleData);
	// // notify the adapter that we can update now
	// momentPlaAdapter.notifyDataSetChanged();
	// mHasRequestedMore = false;
	// }
	/**
	 * 
	 * @param taskId
	 * @param res
	 */
	public void dataFinish(int taskId,Object res){
		switch (taskId) {
		case C.DbTaskId.MOMENT_GET_DIRTY_MOMENT:

			@SuppressWarnings("unchecked")
			List<Moment> dirtyMoments = (List<Moment>)res;
			if(dirtyMoments!=null && !dirtyMoments.isEmpty()){
				//上传灵感
				int len = dirtyMoments.size();
				MomentSyncTaskManager syncMomentManager = new MomentSyncTaskManager();
				for(int i=0;i<len;i++){
					MomentSyncTask task = new MomentSyncTask(dirtyMoments.get(i), syncMomentManager);
					syncMomentManager.addSyncTask(task);
				}
				syncMomentManager.setTaskCallBack(new MomentSyncCallback() {
					
					@Override
					public void onError(String msg) {
						// TODO Auto-generated method stub
						ToastUtil.show(getActivity(), msg);
					}
					
					@Override
					public void onComplete(int syncCount) {
						// TODO Auto-generated method stub
						//从服务端获取数据
						ToastUtil.show(getActivity(), syncCount+"");
						getDataFromServer();
					}
				});
				syncMomentManager.startSync();
			}else{
				getDataFromServer();
			}
			break;

		default:
			break;
		}
	}

}
