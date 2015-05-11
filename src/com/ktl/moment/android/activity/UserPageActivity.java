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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MomentListViewAdapter;
import com.ktl.moment.android.component.CustomListViewPullZoom;
import com.ktl.moment.android.component.CustomListViewPullZoom.OnScrollListener;
import com.ktl.moment.android.component.CustomListViewPullZoom.OnScrollStateChangedListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
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
	private RelativeLayout topNavRl;// 导航栏

	@ViewInject(R.id.top_nav_title)
	private TextView topNavTitle;// 导航栏标题

	@ViewInject(R.id.user_page_list_view)
	private CustomListViewPullZoom userPageListView;

	@ViewInject(R.id.userpage_back_iv)
	private ImageView backIv;// 后退

	@ViewInject(R.id.userpage_more_iv)
	private ImageView moreIv;// 更多

	@ViewInject(R.id.userpage_avatar_iv)
	private ImageView userAvatar;// 头像

	@ViewInject(R.id.userpage_nickname_tv)
	private TextView userNickName;// 昵称

	@ViewInject(R.id.userpage_set_iv)
	private ImageView userSex;// 性别

	@ViewInject(R.id.userpage_signature_tv)
	private TextView userSignature;// 个人签名

	@ViewInject(R.id.userpage_cancel_ly)
	private LinearLayout cancelLy;// 取消关注

	@ViewInject(R.id.userpage_focus_tv)
	private TextView focustv;

	@ViewInject(R.id.userpage_sendmsg_ly)
	private LinearLayout sendMsgLy;// 私信

	@ViewInject(R.id.user_moment_nav)
	private LinearLayout momentNav;

	@ViewInject(R.id.userpage_moment_num)
	private TextView momentNum;

	@ViewInject(R.id.user_watch_nav)
	private LinearLayout watchNav;

	@ViewInject(R.id.userpage_watch_num)
	private TextView watchNum;

	@ViewInject(R.id.user_focus_nav)
	private LinearLayout focusNav;

	@ViewInject(R.id.userpage_focus_num)
	private TextView focusNum;

	@ViewInject(R.id.user_fans_nav)
	private LinearLayout fansNav;

	@ViewInject(R.id.userpage_fans_num)
	private TextView fansNum;

	@ViewInject(R.id.userpage_focus_img)
	private ImageView focusImg;

	private List<Moment> momentList;
	private User user;
	private MomentListViewAdapter momentListAdapter;
	private int pageNum = 0;
	private int pageSize = 2;
	private long otherUserId;

	private String currentNavSelect = "灵感";// 当前选中的菜单
	private String currentNavFlag = "moment";
	private boolean isLastRow = false;
	private boolean hasMore = true;

	@Override
	protected void onCreate(Bundle saveInstanceBundle) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceBundle);
		setContentView(R.layout.activity_user_page);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		otherUserId = intent.getLongExtra("userId", -1);

		momentList = new ArrayList<Moment>();
		momentListAdapter = new MomentListViewAdapter(this, momentList,
				getDisplayImageOptions());
		userPageListView.setAdapter(momentListAdapter);

		getUserData();
		initEvent();
		refresh();

		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
		// int mScreenHeight = localDisplayMetrics.heightPixels;
		int mScreenWidth = localDisplayMetrics.widthPixels;
		AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(
				mScreenWidth, (int) (16.0F * (mScreenWidth / 20.0F)));
		userPageListView.setHeaderLayoutParams(localObject);

		// 设置头像
		ImageLoader.getInstance().displayImage(
				"http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg", userAvatar,
				getDisplayImageOptions());
		topNavRl.getBackground().setAlpha(0);// 设置title背景色透明

	}

	private void initEvent() {

		userPageListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Log.e("zhuwenwu", "position = " + position);
					}
				});

		userPageListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void OnScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount,
					FrameLayout headerContainer) {
				// TODO Auto-generated method stub
				Log.i("UserPageActivity",
						"headerBottom-->" + headerContainer.getBottom());
				Log.i("UserPageActivity", "topNavRl-->" + topNavRl.getHeight());
				Log.i("UserPageActivity", "headercontainterheight-->"
						+ headerContainer.getHeight());
				int headerHeight = headerContainer.getHeight();// 容器高度
				int headerBottom = headerContainer.getBottom();// 容器底部位置

				float alp = (float) (headerHeight - headerBottom)
						/ (float) headerHeight;
				headerContainer.setAlpha(1 - alp * 0.75f);
				int topNavHeight = topNavRl.getHeight();
				if (headerBottom <= 2 * topNavHeight) {
					int topNavAlp = 255 * (2 * topNavHeight - headerBottom)
							/ (2 * topNavHeight);
					topNavRl.getBackground().setAlpha(topNavAlp);
					topNavTitle.setVisibility(View.VISIBLE);
					topNavTitle.setText(currentNavSelect);
					topNavTitle
							.setAlpha((float) (2 * topNavHeight - headerBottom)
									/ (2 * topNavHeight));
					Log.i("UserPageActivity", "TITLE_ALPHA-->"
							+ (float) (2 * topNavHeight - headerBottom)
							/ (2 * topNavHeight));
				} else {
					topNavRl.getBackground().setAlpha(0);
					topNavTitle.setVisibility(View.GONE);
				}

				// 判断是否滚到最后一行
				if (firstVisibleItem + visibleItemCount == totalItemCount
						&& totalItemCount > 0) {
					isLastRow = true;
				}
			}
		});
		userPageListView
				.setOnScrollStateChangedListener(new OnScrollStateChangedListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub
						if (isLastRow
								&& scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
							loadMore();
						}
					}
				});

	}

	private void initUserData() {
		ImageLoader.getInstance().displayImage(user.getUserAvatar(),
				userAvatar, getDisplayImageOptions());
		userNickName.setText(user.getNickName());
		if (user.getSex() == 1) {
			userSex.setImageResource(R.drawable.male);
		} else {
			userSex.setImageResource(R.drawable.female);
		}
		if (!user.getSignature().equals("")) {
			userSignature.setText(user.getSignature());
		} else {
			userSignature.setText("这个童鞋TA很懒，还没有发表个性签名");
		}
		if (user.getIsFocused() == 0) {
			cancelLy.setBackgroundResource(R.drawable.oval_shape_enable);
			focustv.setText("关注");
			focusImg.setImageResource(R.drawable.add);
		} else {
			focustv.setText("取消关注");
			cancelLy.setBackgroundResource(R.drawable.oval_shape);
			focusImg.setImageResource(R.drawable.cancel);
		}
		momentNum.setText(user.getMomentNum() + "");
		watchNum.setText(user.getWatchNum() + "");
		focusNum.setText(user.getAttentionNum() + "");
		fansNum.setText(user.getFansNum() + "");
	}

	@OnClick({ R.id.userpage_back_iv, R.id.userpage_cancel_ly,
			R.id.userpage_sendmsg_ly, R.id.user_moment_nav,
			R.id.user_watch_nav, R.id.user_focus_nav, R.id.user_fans_nav,
			R.id.userpage_more_iv })
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.userpage_back_iv:
			finish();
			break;
		case R.id.userpage_cancel_ly:
			focusAuthor();
			break;
		case R.id.userpage_sendmsg_ly:
			Intent msgIntent = new Intent(this, MsgActivity.class);
			msgIntent.putExtra("userName", "TEST");
			msgIntent.putExtra("userId", 0);
			startActivity(msgIntent);
			break;
		case R.id.user_moment_nav:
			currentNavFlag = "moment";
			currentNavSelect = "灵感";
			refresh();
			break;
		case R.id.user_watch_nav:
			currentNavFlag = "watch";
			currentNavSelect = "围观";
			refresh();
			break;
		case R.id.user_focus_nav:
			Intent focusIntent = new Intent(this, FocusActivty.class);
			focusIntent.putExtra("intentFlag", "userFocus");
			focusIntent.putExtra("otherUserId", otherUserId);
			startActivity(focusIntent);
			break;
		case R.id.user_fans_nav:
			Intent fansIntent = new Intent(this, FocusActivty.class);
			fansIntent.putExtra("intentFlag", "userFans");
			fansIntent.putExtra("otherUserId", otherUserId);
			startActivity(fansIntent);
			break;
		case R.id.userpage_more_iv:
			Intent moreIntent = new Intent(this, UserInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);
			moreIntent.putExtra("user", bundle);
			startActivity(moreIntent);
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

	private void refresh() {
		pageNum = 1;
		RequestParams params = new RequestParams();
		params.put("pageNum", pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("otherUserId", otherUserId);
		String url = C.API.GET_USER_MOMENT_LIST;
		if (currentNavFlag.equals("moment")) {
			url = C.API.GET_USER_MOMENT_LIST;
		} else {
			url = C.API.GET_USER_WATCH_LIST;
		}
		ApiManager.getInstance().post(this, url, params, new HttpCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				List<Moment> moment = (List<Moment>) res;
				momentList.clear();
				momentList.addAll(moment);
				momentListAdapter.notifyDataSetChanged();
				if (moment.size() < pageSize) {
					hasMore = false;
				} else {
					hasMore = true;
				}
			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(UserPageActivity.this, (String) res);
			}
		}, "Moment");
	}

	private void loadMore() {
		if (!hasMore) {
			ToastUtil.show(this, "没有更多了~");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("pageNum", ++pageNum);
		params.put("pageSize", pageSize);
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("otherUserId", otherUserId);
		String url = C.API.GET_USER_MOMENT_LIST;
		if (currentNavFlag.equals("moment")) {
			url = C.API.GET_USER_MOMENT_LIST;
		} else {
			url = C.API.GET_USER_WATCH_LIST;
		}
		ApiManager.getInstance().post(this, url, params, new HttpCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				List<Moment> moment = (List<Moment>) res;
				momentList.addAll(moment);
				momentListAdapter.notifyDataSetChanged();
				if (moment.size() < pageSize) {
					hasMore = false;
				} else {
					hasMore = true;
				}
			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				ToastUtil.show(UserPageActivity.this, (String) res);
			}
		}, "Moment");
	}

	private void getUserData() {
		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("otherUserId", otherUserId);
		ApiManager.getInstance().post(this, C.API.GET_USER_INFO, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<User> list = (List<User>) res;
						user = list.get(0);
						initUserData();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(UserPageActivity.this, (String) res);
					}
				}, "User");
	}

	private void focusAuthor() {
		int isAddFocus = 0;
		if (user.getIsFocused() == 0) {
			cancelLy.setBackgroundResource(R.drawable.oval_shape);
			focustv.setText("取消关注");
			focusImg.setImageResource(R.drawable.cancel);
			user.setIsFocused(1);
			isAddFocus = 1;
		} else {
			cancelLy.setBackgroundResource(R.drawable.oval_shape_enable);
			focustv.setText("关注");
			focusImg.setImageResource(R.drawable.add);
			user.setIsFocused(0);
			isAddFocus = 0;
		}
		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("attentionUserId", otherUserId);
		params.put("isAddFocus", isAddFocus);
		ApiManager.getInstance().post(this, C.API.FOCUS_AUTHOR, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "User");
	}
}
