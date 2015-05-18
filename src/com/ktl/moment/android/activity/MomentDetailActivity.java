package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.CommentListViewAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.RichTextView;
import com.ktl.moment.android.component.listview.arc.widget.SimpleFooter;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnItemClickListener;
import com.ktl.moment.android.component.listview.arc.widget.ZrcListView.OnStartListener;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Comment;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.UserTrack;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.PlayUtil;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.StrUtils;
import com.ktl.moment.utils.TimeFormatUtil;
import com.ktl.moment.utils.TimerCountUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.common.m;

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
	private RichTextView momentContent;// 灵感内容

	@ViewInject(R.id.moment_label_tv)
	private TextView momentLabel;// 灵感标签

	@ViewInject(R.id.moment_comment_num)
	private TextView commentsNum;// 灵感评论数

	/******************** end *********************/

	/****************** foot tab ********************/
	@ViewInject(R.id.detail_operate_comment)
	private LinearLayout commentLayout;

	@ViewInject(R.id.detail_operate_share)
	private ImageView shareImg;

	@ViewInject(R.id.detail_operate_clipper)
	private ImageView clipperImg;

	@ViewInject(R.id.detail_operate_watch)
	private ImageView watchImg;

	@ViewInject(R.id.detail_operate_praise)
	private ImageView praiseImg;

	/*********** audio play *************/
	@ViewInject(R.id.detail_audio_layout)
	private LinearLayout audioLayout;

	@ViewInject(R.id.detail_play_seekbar)
	private SeekBar seekbar;

	@ViewInject(R.id.detail_play_status)
	private TextView playStatus;

	@ViewInject(R.id.detail_play_time)
	private TextView playTime;

	@ViewInject(R.id.detail_play_start)
	private ImageView playStart;

	private LayoutInflater mInflater;
	private View headerView;
	private CommentListViewAdapter commentListViewAdapter;
	private List<Comment> commentList;
	private Moment moment;
	private PlayUtil play;

	private long momentId;
	private long authorId;
	private long userId;
	private int pageNum = 1;
	private int pageSize = 4;
	private boolean hasMore = true;

	private boolean isPlaying = false;
	private boolean isPause = false;
	private boolean isReplay = false;
	private long stayTime;//用于计时
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Intent intent = getIntent();
		momentId = intent.getLongExtra("momentId", 0);
		authorId = intent.getLongExtra("userId", 0);
		userId = Account.getUserInfo().getUserId();
		stayTime = System.currentTimeMillis();//用于计时
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

		getMomentDetail();
		loadComments();

		commentList = new ArrayList<Comment>();
		commentListViewAdapter = new CommentListViewAdapter(this, commentList,
				getDisplayImageOptions());
		commentsListView.setAdapter(commentListViewAdapter);

		// 设置加载更多评论的样式private（可选）
		SimpleFooter footer = new SimpleFooter(this);
		footer.setCircleColor(0xff33bbee);
		commentsListView.setFootable(footer);

		commentsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(ZrcListView parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ToastUtil.show(MomentDetailActivity.this, position + "");
				Intent intent = new Intent(MomentDetailActivity.this,
						CommentActivity.class);
				intent.putExtra("momentId", momentId);
				intent.putExtra("repalyUserId", commentList.get(position - 1)
						.getUserId());
				startActivityForResult(intent, C.ActivityRequest.JUMPTOCOMMENT);
			}
		});
	}

	private void initMomentDetail() {
		momentTitle.setText(moment.getTitle());
		// if (PregUtil.pregImgUrl(moment.getUserAvatar())) {
		ImageLoader.getInstance().displayImage(moment.getUserAvatar(),
				momentUserAvatar, getDisplayImageOptions());
		// } else {
		// momentUserAvatar.setImageResource(R.drawable.default_img);
		// }
		momentUserName.setText(moment.getAuthorName());
		momentPostTime.setText(TimeFormatUtil.formatDate(moment.getPostTime()));
		if (moment.getIsFocused() == 0) {
			focusAuthor.setImageResource(R.drawable.focus_author);
		} else {
			focusAuthor.setImageResource(R.drawable.focus_author_press);
		}
		momentContent.setRichText(moment.getContent());
		if(StrUtils.isEmpty(moment.getLabel())){
			momentLabel.setText("暂无标签");
		}else{
			momentLabel.setText(moment.getLabel());
		}
		commentsNum.setText(moment.getCommentNum() + "");

		if (moment.getIsClipper() == 1) {
			clipperImg.setImageResource(R.drawable.clipper_press);
		} else {
			clipperImg.setImageResource(R.drawable.clipper);
		}
		if (moment.getIsWatched() == 1) {
			watchImg.setImageResource(R.drawable.watch_press);
		} else {
			watchImg.setImageResource(R.drawable.watch);
		}
		if (moment.getIsPraised() == 1) {
			praiseImg.setImageResource(R.drawable.like_press);
		} else {
			praiseImg.setImageResource(R.drawable.like);
		}
		if (moment.getAudioUrl().equals("") || moment.getAudioUrl() == null) {
			audioLayout.setVisibility(View.GONE);
		} else {
			audioLayout.setVisibility(View.VISIBLE);
			loadAudio();
		}
	}

	private void loadAudio() {
		seekbar.setEnabled(false);
		play = new PlayUtil(seekbar, handler, moment.getAudioUrl(), playStatus,
				playTime);
		// 开启新的线程从网络加载音频数据
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				play.initPlayer();
			}
		}).start();

		TimerCountUtil.getInstance().setTextView(playStatus);

		// playStart.setClickable(true);
	}

	/**
	 * 用于通知主线程更新播放界面UI
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				playStart
						.setImageResource(R.drawable.editor_record_replay_selector);
				play.stopSeekbar();
				isPlaying = false;
				isReplay = true;
				break;
			}
			}
			super.handleMessage(msg);
		};
	};

	private void initEvent() {
		// 加载更多事件回调（可选）
		commentsListView.setOnLoadMoreStartListener(new OnStartListener() {
			@Override
			public void onStart() {
				loadMoreComments();
			}
		});
	}

	@OnClick({ R.id.title_back_img, R.id.focus_author_icon,
			R.id.detail_operate_comment, R.id.detail_operate_share,
			R.id.detail_operate_clipper, R.id.detail_operate_watch,
			R.id.detail_operate_praise, R.id.detail_operate_share,
			R.id.detail_play_start })
	private void OnClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			if (play != null) {
				play.stopPlay();
			}
			finish();
			break;
		case R.id.focus_author_icon:
			focus();
			break;
		case R.id.detail_operate_comment:
			Intent commentIntent = new Intent(MomentDetailActivity.this,
					CommentActivity.class);
			commentIntent.putExtra("momentId", momentId);
			commentIntent.putExtra("repalyUserId", (long) 0);
			startActivityForResult(commentIntent,
					C.ActivityRequest.JUMPTOCOMMENT);
			break;
		case R.id.detail_operate_share:
			Intent shareIntent = new Intent(this, ShareActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("moment", moment);
			shareIntent.putExtra("share", bundle);
			this.startActivity(shareIntent);
			break;
		case R.id.detail_operate_clipper:
			clipper();
			break;
		case R.id.detail_operate_watch:
			watch();
			break;
		case R.id.detail_operate_praise:
			praise();
			break;
		case R.id.detail_play_start:
			if (!playStatus.getText().equals("加载中")) {
				play();
			}
			break;
		}
	}

	private void start() {
		play.startPlay();
		playStart.setImageResource(R.drawable.editor_record_pause);
	}

	private void play() {
		if (!isPlaying && !isPause) { // 没有播放,那就播放
			start();
			isPlaying = true;
			isPause = false;
			Log.i("tag", "start");
		} else if (isPlaying && !isPause) { // 没有暂停，那就暂停
			play.pausePlay();
			playStart.setImageResource(R.drawable.editor_record_start);
			isPlaying = false;
			isPause = true;
			Log.i("tag", "pause");
		} else if (isPause) {
			play.continuePlay();
			playStart.setImageResource(R.drawable.editor_record_pause);
			isPlaying = true;
			isPause = false;
			Log.i("tag", "continue");
		} else if (!isPlaying && isReplay) {
			// play.startPlay();
			start();
			playStart.setImageResource(R.drawable.editor_record_pause);
			isPlaying = true;
			isReplay = false;
			Log.i("tag", "replay");
		}
	}

	private void loadComments() {
		pageNum = 1;
		RequestParams params = new RequestParams();
		params.put("momentId", momentId);
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
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						ToastUtil.show(MomentDetailActivity.this, (String) res);
						commentsListView.setRefreshFail("");
						commentsListView.startLoadMore();// 允许加载更多
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
					ToastUtil.show(MomentDetailActivity.this, "没有更多了~");
				}
			}, 500);
			return;
		}
		RequestParams params = new RequestParams();
		params.put("momentId", momentId);
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
						initMomentDetail();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "Moment");
	}

	public void focus() {
		// TODO Auto-generated method stub
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.focus_img_anim);
		anim.setFillAfter(true);
		focusAuthor.startAnimation(anim);
		int isAddFocus;
		if (moment.getIsFocused() == 1) {
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
					focusAuthor.setImageResource(R.drawable.focus_author45);
					moment.setIsFocused(0);
				}
			});
			isAddFocus = 0;
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
					focusAuthor
							.setImageResource(R.drawable.focus_author_press45);
					moment.setIsFocused(1);
				}
			});
			isAddFocus = 1;
		}
		commentListViewAdapter.notifyDataSetChanged();

		requestServer(isAddFocus, "isAddFocus", userId, moment.getAuthorId(),
				C.API.FOCUS_AUTHOR, "User");
	}

	private void clipper() {
		int isAddClipper = 0;
		if (moment.getIsClipper() == 0) {
			clipperImg.setImageResource(R.drawable.clipper_press);
			moment.setIsClipper(1);
			isAddClipper = 1;
		} else {
			clipperImg.setImageResource(R.drawable.clipper);
			moment.setIsClipper(0);
			isAddClipper = 0;
		}
		requestServer(isAddClipper, "isAddClipper", userId, momentId,
				C.API.CLIPPER_MOMENT, "Moment");
	}

	private void watch() {
		int isAddWatch = 0;
		if (moment.getIsWatched() == 0) {
			watchImg.setImageResource(R.drawable.watch_press);
			moment.setIsWatched(1);
			isAddWatch = 1;
		} else {
			watchImg.setImageResource(R.drawable.watch);
			moment.setIsWatched(0);
			isAddWatch = 0;
		}
		requestServer(isAddWatch, "isAddWatch", userId, momentId,
				C.API.WATCH_MOMENT, "Moment");
	}

	private void praise() {
		int isAddPraise = 0;
		if (moment.getIsPraised() == 0) {
			praiseImg.setImageResource(R.drawable.like_press);
			moment.setIsPraised(1);
			isAddPraise = 1;
		} else {
			praiseImg.setImageResource(R.drawable.like);
			moment.setIsPraised(0);
			isAddPraise = 0;
		}
		requestServer(isAddPraise, "isAddPraise", userId, momentId,
				C.API.PRAISE_MOMENT, "Moment");
	}

	/**
	 * 用于向服务端发送请求，适用于点赞、围观、关注作者、剪藏灵感
	 * 
	 * @param isFlag
	 * @param flagName
	 * @param userId
	 * @param momentId
	 *            当用于关注作者时，该字段为被关注用户的id
	 * @param url
	 */
	public void requestServer(int isFlag, final String flagName, long userId,
			long momentId, String url, String modelName) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		if (modelName.equals("User")) {
			params.put("attentionUserId", momentId);
		} else {
			params.put("momentId", momentId);
		}
		params.put(flagName, isFlag);

		final int flag = isFlag;
		ApiManager.getInstance().post(this, url, params, new HttpCallBack() {

			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				if (flag == 1) {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(MomentDetailActivity.this, "关注成功~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(MomentDetailActivity.this, "围观成功~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddClipper")) {
						Toast.makeText(MomentDetailActivity.this, "剪藏成功~",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MomentDetailActivity.this, "点赞成功~",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(MomentDetailActivity.this, "取消关注成功~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(MomentDetailActivity.this, "取消围观成功~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddClipper")) {
						Toast.makeText(MomentDetailActivity.this, "剪藏成功~",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MomentDetailActivity.this, "取消点赞成功~",
								Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				if (flag == 1) {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(MomentDetailActivity.this, "关注失敗~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(MomentDetailActivity.this, "围观失敗~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddClipper")) {
						Toast.makeText(MomentDetailActivity.this, "剪藏失敗~",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MomentDetailActivity.this, "点赞失敗~",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(MomentDetailActivity.this, "取消关注失敗~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(MomentDetailActivity.this, "取消围观失敗~",
								Toast.LENGTH_SHORT).show();
					} else if (flagName.equals("isAddClipper")) {
						Toast.makeText(MomentDetailActivity.this, "剪藏失敗~",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MomentDetailActivity.this, "取消点赞失敗~",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}, modelName);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case C.ActivityRequest.JUMPTOCOMMENT:
				pageNum = 1;
				loadComments();
				getMomentDetail();
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		stayTime = System.currentTimeMillis() - stayTime;
		UserTrack userTrack = new UserTrack();
		userTrack.setUserId(userId);
		userTrack.setMomentId(momentId);
		userTrack.setStickTime(stayTime);
		List<UserTrack> userTrackList = SharedPreferencesUtil.getInstance().getList(C.SPKey.SPK_USER_TRACK_LIST);
		if(userTrackList == null){
			userTrackList = new ArrayList<UserTrack>();
		}
		userTrackList.add(userTrack);
		SharedPreferencesUtil.getInstance().putList(C.SPKey.SPK_USER_TRACK_LIST, userTrackList);
	}
}
