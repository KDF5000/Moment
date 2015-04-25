package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.CommentListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnItemClickListener;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.entity.Comment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MomentDetailActivity extends BaseActivity {

	private static final String TAG = "MomentDetailActivity";
	@ViewInject(R.id.moment_comment_lv)
//	private ListView commentsListView;
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
	private ImageView focusAuthor;

	@ViewInject(R.id.moment_content)
	private TextView momentContent;// 灵感内容

	@ViewInject(R.id.moment_label_tv)
	private TextView momentLabel;// 灵感标签

	@ViewInject(R.id.moment_comment_num)
	private TextView commentsNum;// 灵感评论数
	/******************** end *********************/

	private LayoutInflater mInflater;
	private View headerView;
	private List<Comment> commentList;
	private Handler handler;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent = getIntent();
		long momentId = intent.getLongExtra("momentId", 0);
		Toast.makeText(this, momentId + "", Toast.LENGTH_SHORT).show();
		initData();
		initView();
		initEvent();
	}

	private void initData() {
		if (commentList == null) {
			commentList = new ArrayList<Comment>();
		}
		for (int i = 0; i < 10; i++) {
			Comment comment = new Comment();
			comment.setFromUserAvatar("http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg");
			comment.setFromUserId(100 + i);
			comment.setFromUserName("Luis");
			comment.setCommentTime("3分钟前");
			comment.setPraiseNum(10);
			comment.setRepalyUserId(1000 + i);
			comment.setRepalyUserName("刘浩");
			comment.setCommentContent("谢谢你的建议，很好的主意，我会考虑一下的谢谢你的建议");
			commentList.add(comment);
		}
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
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
		setMiddleTitleName(R.string.moment_detail_text_view);

		commentsListView.setAdapter(new CommentListViewAdapter(this,
				commentList, getDisplayImageOptions()));
		

		// 设置加载更多评论的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		commentsListView.setFootable(footer);
		
		ImageLoader.getInstance().displayImage(
				"http://7sbpmg.com1.z0.glb.clouddn.com/1.jpg",
				momentUserAvatar, getDisplayImageOptions());
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
			Toast.makeText(this, "关注", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
