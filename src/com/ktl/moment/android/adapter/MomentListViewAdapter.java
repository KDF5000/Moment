package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.MomentDetailActivity;
import com.ktl.moment.android.activity.ShareActivity;
import com.ktl.moment.android.activity.UserPageActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.StrUtils;
import com.ktl.moment.utils.TimeFormatUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MomentListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Moment> momentList;// 灵感列表
	private DisplayImageOptions options;// 图片设置选项

	// private User user;

	public MomentListViewAdapter(Context context, List<Moment> momentList,
			DisplayImageOptions options) {
		this.momentList = momentList;
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.options = options;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return momentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return momentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final MomentHolder momentHolder;
		if (convertView == null) {
			convertView = this.mInflater.inflate(
					R.layout.fragment_dynamic_list_item, null);
			momentHolder = new MomentHolder();
			ViewUtils.inject(momentHolder, convertView);
			convertView.setTag(momentHolder);
		} else {
			momentHolder = (MomentHolder) convertView.getTag();
		}
		final Moment moment = momentList.get(position);
		momentHolder.tittleTv.setText(moment.getTitle());
		if(StrUtils.isEmpty(moment.getContentAbstract())){
			momentHolder.contentTv.setVisibility(View.GONE);
		}else{
			momentHolder.contentTv.setVisibility(View.VISIBLE);
			momentHolder.contentTv.setText(moment.getContentAbstract());
		}
		ImageLoader.getInstance().displayImage(moment.getUserAvatar(),
				momentHolder.avatar, options);
		if (StrUtils.isEmpty(moment.getMomentImgs())) {
			momentHolder.momentImg.setVisibility(View.GONE);
		} else {
			momentHolder.momentImg.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(moment.getMomentImgs(),
					momentHolder.momentImg, options);
		}
		momentHolder.userNameTv.setText(moment.getAuthorName());
		momentHolder.postTime.setText(TimeFormatUtil.formatDate(moment
				.getPostTime()));

		if (moment.getIsFocused() == 1) {
			momentHolder.focusAuthorImg
					.setImageResource(R.drawable.focus_author_press);
		} else {
			momentHolder.focusAuthorImg
					.setImageResource(R.drawable.focus_author);
		}

		if (moment.getIsWatched() == 1) {
			momentHolder.watchImg.setImageResource(R.drawable.watch_press);
			momentHolder.watchTv.setTextColor(context.getResources().getColor(
					R.color.watch_color));
			momentHolder.watchNum.setTextColor(context.getResources().getColor(
					R.color.watch_color));
		} else {
			momentHolder.watchImg.setImageResource(R.drawable.watch);
			momentHolder.watchTv.setTextColor(context.getResources().getColor(
					R.color.text_color));
			momentHolder.watchNum.setTextColor(context.getResources().getColor(
					R.color.text_color));
		}
		momentHolder.watchNum.setText(moment.getWatchNum() + "");

		if (moment.getIsPraised() == 0) {
			momentHolder.praiseImg.setImageResource(R.drawable.like);
			momentHolder.praiseTv.setTextColor(context.getResources().getColor(
					R.color.text_color));
			momentHolder.praiseNum.setTextColor(context.getResources()
					.getColor(R.color.text_color));
		} else {
			momentHolder.praiseImg.setImageResource(R.drawable.like_press);
			momentHolder.praiseTv.setTextColor(context.getResources().getColor(
					R.color.praise_color));
			momentHolder.praiseNum.setTextColor(context.getResources()
					.getColor(R.color.praise_color));
		}
		momentHolder.praiseNum.setText(moment.getPraiseNum() + "");

		momentHolder.commentNum.setText(moment.getCommentNum() + "");
		if(StrUtils.isEmpty(moment.getLabel())){
			momentHolder.labelTv.setText("暂无标签");
		}else{
			momentHolder.labelTv.setText(moment.getLabel());
		}
	
		final User user = (User) SharedPreferencesUtil.getInstance().getObject(
				C.SPKey.SPK_LOGIN_INFO);

		// listviewItem 点击进入详情页
		momentHolder.articleContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MomentDetailActivity.class);
				intent.putExtra("momentId", moment.getMomentId());
				intent.putExtra("userId", moment.getAuthorId());
				context.startActivity(intent);
			}
		});

		// 进入作者个人页面
		momentHolder.avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, UserPageActivity.class);
				intent.putExtra("userId", moment.getAuthorId());
				context.startActivity(intent);
			}
		});
		
		// 关注作者
		momentHolder.focusAuthorImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(context,
						R.anim.focus_img_anim);
				anim.setFillAfter(true);
				momentHolder.focusAuthorImg.setAnimation(anim);
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
							momentHolder.focusAuthorImg.setImageResource(R.drawable.focus_author45);
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
							momentHolder.focusAuthorImg
									.setImageResource(R.drawable.focus_author_press45);
							moment.setIsFocused(1);
						}
					});
					isAddFocus = 1;
				}
//				momentList.set(position, moment);
				notifyDataSetChanged();
				requestServer(isAddFocus, "isAddFocus", user.getUserId(),
						moment.getAuthorId(), C.API.FOCUS_AUTHOR, "User");
			}
		});

		// listviewItem 点击分享
		momentHolder.shareArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ShareActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("moment", moment);
				intent.putExtra("share", bundle);
				context.startActivity(intent);
			}
		});

		// 点击评论
		momentHolder.commentArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MomentDetailActivity.class);
				intent.putExtra("momentId", moment.getMomentId());
				intent.putExtra("userId", moment.getAuthorId());
				context.startActivity(intent);
			}
		});

		// 围观
		momentHolder.watchArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int isAddWatch;
				int num = moment.getWatchNum();
				if (moment.getIsWatched() == 0) {
					moment.setWatchtNum(++num);
					moment.setIsWatched(1);
					momentHolder.watchImg
							.setImageResource(R.drawable.watch_press);
					momentHolder.watchTv.setTextColor(context.getResources()
							.getColor(R.color.watch_color));
					momentHolder.watchNum.setTextColor(context.getResources()
							.getColor(R.color.watch_color));
					isAddWatch = 1;
				} else {
					moment.setWatchtNum(--num);
					moment.setIsWatched(0);
					momentHolder.watchImg.setImageResource(R.drawable.watch);
					momentHolder.watchTv.setTextColor(context.getResources()
							.getColor(R.color.text_color));
					momentHolder.watchNum.setTextColor(context.getResources()
							.getColor(R.color.text_color));
					isAddWatch = 0;
				}
				notifyDataSetChanged();
				requestServer(isAddWatch, "isAddWatch", user.getUserId(),
						moment.getMomentId(), C.API.WATCH_MOMENT, "Moment");
			}
		});

		// 点赞
		momentHolder.praiseArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int isAddPraise;
				int num = moment.getPraiseNum();
				if (moment.getIsPraised() == 0) {
					moment.setPraiseNum(++num);
					moment.setIsPraised(1);
					momentHolder.praiseImg
							.setImageResource(R.drawable.like_press);
					momentHolder.praiseTv.setTextColor(context.getResources()
							.getColor(R.color.praise_color));
					momentHolder.praiseNum.setTextColor(context.getResources()
							.getColor(R.color.praise_color));
					isAddPraise = 1;
				} else {
					moment.setPraiseNum(--num);
					moment.setIsPraised(0);
					momentHolder.praiseImg.setImageResource(R.drawable.like);
					momentHolder.praiseTv.setTextColor(context.getResources()
							.getColor(R.color.text_color));
					momentHolder.praiseNum.setTextColor(context.getResources()
							.getColor(R.color.text_color));
					isAddPraise = 0;
				}
				notifyDataSetChanged();
				// 可以在这里向服务器请求，也可以设置一个回调或者消息 给fragment 让他发送请求
				requestServer(isAddPraise, "isAddPraise", user.getUserId(),
						moment.getMomentId(), C.API.PRAISE_MOMENT, "Moment");
			}
		});

		return convertView;
	}

	/**
	 * 用于向服务端发送请求，适用于点赞、围观、关注作者
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
		ApiManager.getInstance().post(context, url, params, new HttpCallBack() {

			@Override
			public void onSuccess(Object res) {
				// TODO Auto-generated method stub
				if (flag == 1) {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(context, "关注成功~", Toast.LENGTH_SHORT)
								.show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(context, "围观成功~", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "点赞成功~", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(context, "取消关注成功~", Toast.LENGTH_SHORT)
								.show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(context, "取消围观成功~", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "取消点赞成功~", Toast.LENGTH_SHORT)
								.show();
					}
				}

			}

			@Override
			public void onFailure(Object res) {
				// TODO Auto-generated method stub
				if (flag == 1) {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(context, "关注失败~", Toast.LENGTH_SHORT)
								.show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(context, "围观失败~", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "点赞失败~", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					if (flagName.equals("isAddFocus")) {
						Toast.makeText(context, "取消关注失败~", Toast.LENGTH_SHORT)
								.show();
					} else if (flagName.equals("isAddWatch")) {
						Toast.makeText(context, "取消围观失败~", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "取消点赞失败~", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		}, modelName);
	}

	public static class MomentHolder {
		@ViewInject(R.id.moment_title)
		TextView tittleTv;// 标题

		@ViewInject(R.id.moment_content)
		TextView contentTv;// 内容

		@ViewInject(R.id.moment_img)
		ImageView momentImg;// 笔记中的代表图

		@ViewInject(R.id.user_avatar)
		ImageView avatar;// 头像

		@ViewInject(R.id.user_name)
		TextView userNameTv;// 用户名

		@ViewInject(R.id.post_time)
		TextView postTime;// 发布时间

		@ViewInject(R.id.focus_author)
		ImageView focusAuthorImg;// 关注作者

		@ViewInject(R.id.label_tv)
		TextView labelTv;// 灵感标签

		@ViewInject(R.id.moment_article_content)
		LinearLayout articleContent;// 内容区域
		/***********************************/
		@ViewInject(R.id.share_area)
		// 分享区域
		LinearLayout shareArea;

		@ViewInject(R.id.share_img)
		// 分享img
		ImageView shareImg;

		@ViewInject(R.id.share_tv)
		// 分享文本
		TextView shareTv;
		/***********************************/
		@ViewInject(R.id.comments_num)
		TextView commentNum;// 评论人数

		@ViewInject(R.id.comment_area)
		LinearLayout commentArea;// 评论人数
		/***********************************/
		@ViewInject(R.id.watch_area)
		// 围观区域
		LinearLayout watchArea;

		@ViewInject(R.id.watch_img)
		// 围观图标
		ImageView watchImg;

		@ViewInject(R.id.watch_tv)
		// 围观文本
		TextView watchTv;

		@ViewInject(R.id.watch_num)
		// 围观人数
		TextView watchNum;
		/***********************************/
		@ViewInject(R.id.praise_area)
		// 点赞区域
		LinearLayout praiseArea;

		@ViewInject(R.id.praise_img)
		// 点赞图标
		ImageView praiseImg;

		@ViewInject(R.id.praise_tv)
		// 点赞文本
		TextView praiseTv;

		@ViewInject(R.id.praise_num)
		// 点赞人数
		TextView praiseNum;

	}

}
