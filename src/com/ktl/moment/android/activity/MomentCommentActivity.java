package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.CommentListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.SimpleHeader;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Comment;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.loopj.android.http.RequestParams;

public class MomentCommentActivity extends BaseActivity {

	private ZrcListView commentsListView;
	private List<Comment> commentList;
	private CommentListViewAdapter commentListViewAdapter;
	private Moment moment;

	private long momentId;
	private int pageNum = 0;
	private int pageSize = 10;
	private boolean hasMore = false;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_moment_comment,
				contentLayout, true);

		commentsListView = (ZrcListView) findViewById(R.id.my_detail_comment_listview);

		Intent intent = this.getIntent();
		momentId = intent.getLongExtra("momentId", -1);

		commentList = new ArrayList<Comment>();
		commentListViewAdapter = new CommentListViewAdapter(this, commentList,
				getDisplayImageOptions());
		commentsListView.setAdapter(commentListViewAdapter);

		getMomentDetail();
		loadComments();
		initEvent();
		initView();
	}

	private void initView() {
		setMiddleTitleVisible(true);
		setMiddleTitleName("评论（0）");
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
		titleBackImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		// 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
		SimpleHeader header = new SimpleHeader(this);
		header.setTextColor(0xff0066aa);
		header.setCircleColor(0xff33bbee);
		commentsListView.setHeadable(header);

		// 设置加载更多的样式（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		commentsListView.setFootable(footer);

		// 设置列表项出现动画（可选）
		commentsListView.setItemAnimForTopIn(R.anim.zrc_topitem_in);
		commentsListView.setItemAnimForBottomIn(R.anim.zrc_bottomitem_in);
		commentsListView.stopLoadMore();

	}

	private void initTitleName() {
		StringBuilder sb = new StringBuilder("评论（");
		sb.append(moment.getCommentNum());
		sb.append("）");
		setMiddleTitleName(sb.toString());
	}

	private void initEvent() {
		// 加载更多事件回调（可选）
		commentsListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMoreComments();
			}
		});

		commentsListView.setOnRefreshStartListener(new OnStartListener() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				loadComments();
			}
		});
	}

	private void getMomentDetail() {
		if (moment == null) {
			moment = new Moment();
		}
		RequestParams params = new RequestParams();
		params.put("momentId", momentId);
		params.put("userId", Account.getUserInfo().getUserId());
		ApiManager.getInstance().post(this, C.API.GET_MOMENT_DETAIL, params,
				new HttpCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						List<Moment> momentList = (List<Moment>) res;
						moment = momentList.get(0);
						initTitleName();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "Moment");
	}

	private void loadComments() {
		pageNum = 1;
		RequestParams params = new RequestParams();
		params.put("momentId", 2);
		params.put("pageSize", pageSize);
		params.put("pageNum", pageNum);
		ApiManager.getInstance().post(this, C.API.GET_COMMENT_LIST, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						commentList.clear();
						@SuppressWarnings("unchecked")
						List<Comment> list = (List<Comment>) res;
						commentList.clear();
						commentList.addAll(list);
						commentListViewAdapter.notifyDataSetChanged();
						commentsListView.setRefreshSuccess("");
						commentsListView.startLoadMore();// 允许加载更多
						if (list.size() < pageSize) {
							hasMore = false;
						} else {
							hasMore = true;
						}
						if (list.size() == 0) {
							commentsListView.setVisibility(View.GONE);

						}
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil
								.show(MomentCommentActivity.this, (String) res);
						commentsListView.setRefreshFail("");
						commentsListView.startLoadMore();// 允许加载更多
						commentsListView.setVisibility(View.GONE);
					}
				}, "Comment");
	}

	private void loadMoreComments() {
		if (!hasMore) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					commentsListView.setLoadMoreSuccess();
					commentsListView.stopLoadMore();
					ToastUtil.show(MomentCommentActivity.this, "没有更多了~");
				}
			}, 500);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("momentId", 2);
		params.put("pageSize", pageSize);
		params.put("pageNum", ++pageNum);
		ApiManager.getInstance().post(this, C.API.GET_COMMENT_LIST, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						@SuppressWarnings("unchecked")
						List<Comment> list = (List<Comment>) res;
						commentList.addAll(list);
						commentListViewAdapter.notifyDataSetChanged();
						commentsListView.setLoadMoreSuccess();
						if (list.size() < pageSize) {
							hasMore = false;
						} else {
							hasMore = true;
						}
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(MomentCommentActivity.this,
								"网络加载失败，请稍后重试");
					}
				}, "Comment");
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}

}
