package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
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

import com.ktl.moment.R;
import com.ktl.moment.android.activity.MomentDetailActivity;
import com.ktl.moment.android.activity.ShareActivity;
import com.ktl.moment.android.activity.UserPageActivity;
import com.ktl.moment.entity.Moment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FindListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<Moment> momentList;// 灵感列表
	private DisplayImageOptions options;// 图片设置选项

	public FindListViewAdapter(Context context, List<Moment> momentList,
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
		momentHolder.contentTv.setText(moment.getContent());
		ImageLoader.getInstance().displayImage(moment.getUserAvatar(),
				momentHolder.avatar, options);
		ImageLoader.getInstance().displayImage(moment.getMomentImg(),
				momentHolder.momentImg, options);
		momentHolder.userNameTv.setText(moment.getAuthorName());
		momentHolder.postTime.setText(moment.getPostTime());
		momentHolder.followNum.setText(moment.getCollectNum() + "");
		momentHolder.praiseNum.setText(moment.getPraiseNum() + "");
		momentHolder.commentNum.setText(moment.getCommentNum() + "");
		momentHolder.labelTv.setText(moment.getLabel());

		momentHolder.avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, UserPageActivity.class);
				intent.putExtra("userId", moment.getAuthorId());
				context.startActivity(intent);
			}
		});

		momentHolder.praiseArea.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int num = moment.getPraiseNum();
				moment.setPraiseNum(++num);
				notifyDataSetChanged();
				// 可以在这里向服务器请求，也可以设置一个回调或者消息 给fragment 让他发送请求

			}
		});

		momentHolder.focusAuthorImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Animation anim = AnimationUtils.loadAnimation(context,
						R.anim.focus_img_anim);
				momentHolder.focusAuthorImg.startAnimation(anim);
				if (moment.getIsFocused() == 0) {
					momentHolder.focusAuthorImg.setRotation(0);
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
									.setImageResource(R.drawable.focus_author);
							moment.setIsFocused(1);
						}
					});
				} else {
					momentHolder.focusAuthorImg.setRotation(45);
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
									.setImageResource(R.drawable.focus_author_press);
							moment.setIsFocused(0);
						}
					});
				}
				notifyDataSetChanged();
			}
		});
		
		momentHolder.shareArea.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,ShareActivity.class);
				context.startActivity(intent);
			}
		});
		

		// listviewItem 点击事件
		momentHolder.articleContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,MomentDetailActivity.class);
				intent.putExtra("momentId", moment.getMomentId());
				context.startActivity(intent);
			}
		});

		return convertView;
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
		
		@ViewInject(R.id.moment_title)
		TextView postTime;// 发布时间
		
		@ViewInject(R.id.focus_author)
		ImageView focusAuthorImg;// 关注作者
		
		@ViewInject(R.id.follow_num)
		TextView followNum;// 收藏人数
		
		@ViewInject(R.id.praise_num)
		TextView praiseNum;// 点赞人数
		
		@ViewInject(R.id.comments_num)
		TextView commentNum;// 评论人数
		
		@ViewInject(R.id.label_tv)
		TextView labelTv;// 灵感标签
		
		@ViewInject(R.id.moment_article_content)
		LinearLayout articleContent;// 内容区域
		
		@ViewInject(R.id.praise_area)
		LinearLayout praiseArea;// 点赞区域
		
		@ViewInject(R.id.share_area)//分享区域
		LinearLayout shareArea;
		
	}

}
