package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.FindListViewAdapter;
import com.ktl.moment.android.component.CustomListViewPullZoom;
import com.ktl.moment.android.component.CustomListViewPullZoom.OnScrollListener;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserPageActivity extends Activity {
	
	@ViewInject(R.id.top_nav)
	private RelativeLayout topNavRl;//导航栏
	
	@ViewInject(R.id.user_page_list_view)
	private CustomListViewPullZoom userPageListView;

	@ViewInject(R.id.userpage_back_iv)
	private ImageView backIv;// 后退

	@ViewInject(R.id.userpage_more_iv)
	private ImageView moreIv;// 更多

	@ViewInject(R.id.userpage_avatar_iv)
	private ImageView userAvatar;// 头像

	@ViewInject(R.id.userpage_nickname_tv)
	private ImageView userNickName;// 昵称

	@ViewInject(R.id.userpage_set_iv)
	private ImageView userSex;// 性别

	@ViewInject(R.id.userpage_signature_tv)
	private ImageView userSignature;// 个人签名

	@ViewInject(R.id.userpage_cancel_ly)
	private LinearLayout cancelLy;// 取消关注

	@ViewInject(R.id.userpage_sendmsg_ly)
	private LinearLayout sendMsgLy;// 私信

	private List<Moment> momentList;
	private FindListViewAdapter momentListAdapter;
	private int pageNum = 0;
	private int pageSize = 10;
	private long userId;

	@Override
	protected void onCreate(Bundle saveInstanceBundle) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceBundle);
		setContentView(R.layout.activity_user_page);
		ViewUtils.inject(this);

		// String[] adapterData = new String[] {};

		// userPageListView.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, adapterData));
		getDataFromServer();
		userPageListView.setAdapter(momentListAdapter);

		userPageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.e("zhuwenwu", "position = " + position);
					}
				});
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//		int mScreenHeight = localDisplayMetrics.heightPixels;
		int mScreenWidth = localDisplayMetrics.widthPixels;
		AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(
				mScreenWidth, (int) (16.0F * (mScreenWidth / 20.0F)));
		userPageListView.setHeaderLayoutParams(localObject);

		// 设置头像
		ImageLoader.getInstance().displayImage(
				"http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg", userAvatar,
				getDisplayImageOptions());
		
		userPageListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void OnScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount, int headerBottom) {
				// TODO Auto-generated method stub
				Log.i("UserPageActivity", "headerBottom-->"+headerBottom);
				Log.i("UserPageActivity", "topNavRl-->"+topNavRl.getHeight());
				if(headerBottom <= topNavRl.getHeight()){
					topNavRl.setBackgroundColor(getResources().getColor(R.color.transparent));
				}else{
					topNavRl.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				}
			}
		});
	}

	@OnClick({ R.id.userpage_back_iv, R.id.userpage_cancel_ly,
			R.id.userpage_sendmsg_ly })
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.userpage_back_iv:
			finish();
			break;
		case R.id.userpage_cancel_ly:
			Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
			break;
		case R.id.userpage_sendmsg_ly:
			Intent intent = new Intent(this, MsgActivity.class);
			intent.putExtra("userName", "TEST");
			intent.putExtra("userId", 0);
			startActivity(intent);
			break;
		}
	}

	private DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_img)
				.showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img).cacheOnDisk(true)
				.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true).build();
		return options;
	}

	private void getDataFromServer() {
		if (momentList == null) {
			momentList = new ArrayList<Moment>();
		}
		if (momentListAdapter == null) {
			momentListAdapter = new FindListViewAdapter(this, momentList,
					getDisplayImageOptions());
		}
		RequestParams params = new RequestParams();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", userId);
		ApiManager.getInstance().post(this, C.API.GET_HOME_FOCUS_LIST, params,
				new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> moment = (List<Moment>) res;
						momentList.addAll(moment);
						momentListAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(UserPageActivity.this, (String) res);
					}
				}, "Moment");
	}
	
	private void initEvent(){
	}
}
