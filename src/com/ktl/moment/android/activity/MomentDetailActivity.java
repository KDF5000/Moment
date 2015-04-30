package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.CommentListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnItemClickListener;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Comment;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MomentDetailActivity extends BaseActivity {

	private static final String TAG = "MomentDetailActivity";
	@ViewInject(R.id.moment_comment_lv)
	// private ListView commentsListView;
	private ZrcListView commentsListView;

	/************** headerview *********************/
	@ViewInject(R.id.moment_title)
	private TextView momentTitle;// 标题

	@ViewInject(R.id.moment_detail_user_avatar)
	private ImageView momentUserAvatar;// 用户头像

	@ViewInject(R.id.moment_user_nickname)
	private TextView momentUserName;// 用户名

	@ViewInject(R.id.moment_post_time)
	private TextView momentPostTime;// 灵感发布时间

	@ViewInject(R.id.focus_author_icon)
	private ImageView focusAuthor;// 关注作者img

	@ViewInject(R.id.moment_content)
	private TextView momentContent;// 灵感内容

	@ViewInject(R.id.moment_label_tv)
	private TextView momentLabel;// 灵感标签

	@ViewInject(R.id.moment_comment_num)
	private TextView commentsNum;// 灵感评论数
	/******************** end *********************/

	private LayoutInflater mInflater;
	private View headerView;
	private CommentListViewAdapter commentListViewAdapter;
	private List<Comment> commentList;
	private Moment moment;
	private Handler handler;

	private long momentId;
	private long userId;
	private int pageNum = 0;
	private int pageSize = 10;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent = getIntent();
		momentId = intent.getLongExtra("momentId", 0);
		userId = intent.getLongExtra("userId", 0);
		Toast.makeText(this, momentId + "", Toast.LENGTH_SHORT).show();
		initView();
		initEvent();
	}

	private void initView() {
		this.mInflater = getLayoutInflater();

		this.mInflater.inflate(R.layout.activity_moment_detail, contentLayout,
				true);
		headerView = this.mInflater.inflate(
				R.layout.moment_detail_listview_header, null);
		ViewUtils.inject(this);
		ViewUtils.inject(this, headerView);
		commentsListView.addHeaderView(headerView, null, false);

		setMiddleTitleVisible(true);
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
		setMiddleTitleName(R.string.moment_detail_text_view);
		setBaseContainerBgColor(0xffffffff);

		initMomentDetail();
		getCommentData();

		commentListViewAdapter = new CommentListViewAdapter(this, commentList,
				getDisplayImageOptions());
		commentsListView.setAdapter(commentListViewAdapter);

		// 设置加载更多评论的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		commentsListView.setFootable(footer);
		
		commentsListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(ZrcListView parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MomentDetailActivity.this, CommentActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initMomentDetail() {
		getMomentDetail();
		momentTitle.setText(moment.getTitle());
		ImageLoader.getInstance().displayImage(moment.getUserAvatar(),
				momentUserAvatar, getDisplayImageOptions());
		momentUserName.setText(moment.getAuthorName());
		momentPostTime.setText(moment.getPostTime());
		if (moment.getIsFocused() == 0) {
			focusAuthor.setImageResource(R.drawable.focus_author);
		} else {
			focusAuthor.setImageResource(R.drawable.focus_author_press);
		}
		momentContent.setText(moment.getContent());
		momentLabel.setText(moment.getLabel());
		commentsNum.setText(moment.getCommentNum()+"");
	}

	private void initEvent() {
		handler = new Handler();
		commentsListView.startLoadMore();// 允许加载更多
		// 加载更多事件回调（可选）
		commentsListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				// 加载更多
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						commentsListView.setLoadMoreSuccess();
					}
				}, 2 * 1000);
			}
		});
	}

	@OnClick({ R.id.title_back_img, R.id.focus_author_icon })
	private void OnClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			finish();
			break;
		case R.id.focus_author_icon:
			focusClick();
			break;
		}
	}

	private void getCommentData() {
		if (commentList == null) {
			commentList = new ArrayList<Comment>();
		}
		RequestParams params = new RequestParams();
		params.put("momentId", momentId);
		params.put("pageSize", pageSize);
		params.put("pageNum", pageNum);
		ApiManager.getInstance().post(this, C.API.GET_COMMENT_LIST, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Comment> list = (List<Comment>) res;
						commentList.addAll(list);
						commentListViewAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(MomentDetailActivity.this,
								"网络加载失败，请稍后重试");
					}
				}, "Comment");
	}

	private void getMomentDetail() {
		if (moment == null) {
			moment = new Moment();
		}
		RequestParams params = new RequestParams();
		params.put("momentId", momentId);
		params.put("userId", userId);
		ApiManager.getInstance().post(this, C.API.GET_MOMENT_DETAIL, params,
				new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> momentList = (List<Moment>) res;
						moment = momentList.get(0);
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "Moment");
	}

	private void focusClick() {
		// TODO Auto-generated method stub
		Animation anim = AnimationUtils.loadAnimation(
				MomentDetailActivity.this, R.anim.focus_img_anim);
		focusAuthor.setAnimation(anim);
		int isAddFocus = 0;
		if (moment.getIsFocused() == 0) {
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					focusAuthor.setImageResource(R.drawable.focus_author);
					moment.setIsFocused(1);
				}
			});
		} else {
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					focusAuthor.setImageResource(R.drawable.focus_author_press);
					moment.setIsFocused(0);
				}
			});
		}
		
		RequestParams params = new RequestParams();
		User user = (User) SharedPreferencesUtil.getInstance().getObject(C.SPKey.SPK_LOGIN_INFO);
		params.put("userId", user.getUserId());
		params.put("attentionUserId", moment.getAuthorId());
		params.put("isAddFocus", isAddFocus);
		ApiManager.getInstance().post(this, C.API.FOCUS_AUTHOR, params, new HttpCallBack() {
			
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

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		
	}

}
