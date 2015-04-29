package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.entity.Comment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentListViewAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Comment> commentList;
	private DisplayImageOptions options;// 图片设置选项

	public CommentListViewAdapter(Context context, List<Comment> commentList,
			DisplayImageOptions options) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.commentList = commentList;
		this.options = options;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final CommentHolder holder;
		if(convertView==null){
			convertView = this.mInflater.inflate(R.layout.comment_lv_item, null);
			holder = new CommentHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder = (CommentHolder) convertView.getTag();
		}
		final Comment comment = commentList.get(position);
		
		ImageLoader.getInstance().displayImage(comment.getUserAvatar(),
				holder.userAvatar, options);
		holder.userFromName.setText(comment.getUserName());
		holder.commentTime.setText(comment.getPostTime());
		
		if(comment.getIsPraised() == 0){
			holder.praiseIcon.setImageResource(R.drawable.like_small);
			holder.praiseNum.setTextColor(mContext.getResources().getColor(R.color.text_color));
		}else{
			holder.praiseIcon.setImageResource(R.drawable.like_small_press);
			holder.praiseNum.setTextColor(mContext.getResources().getColor(R.color.praise_color));
		}
		holder.praiseNum.setText(comment.getPraiseNum()+"");
		holder.content.setText(comment.getContent());
		
		if(comment.getRepalyUserId() == 0){
			holder.repalyLayout.setVisibility(View.GONE);
		}else{
			holder.repalyLayout.setVisibility(View.VISIBLE);
			holder.userToName.setText(comment.getRepalyUserName());
		}
		
		holder.praiseIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int praiseNum = comment.getPraiseNum();
				if(comment.getIsPraised() == 0){
					comment.setPraiseNum(++praiseNum);
					holder.praiseIcon.setImageResource(R.drawable.like_small_press);
					holder.praiseNum.setTextColor(mContext.getResources().getColor(R.color.praise_color));
					comment.setIsPraised(1);
				}else{
					comment.setPraiseNum(--praiseNum);
					holder.praiseIcon.setImageResource(R.drawable.like_small);
					holder.praiseNum.setTextColor(mContext.getResources().getColor(R.color.text_color));
					comment.setIsPraised(0);
				}
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	
	public static class CommentHolder{
		@ViewInject(R.id.comment_user_avatar)
		ImageView userAvatar;
		
		@ViewInject(R.id.comment_from_username)
		TextView userFromName;
		
		@ViewInject(R.id.comment_post_time)
		TextView commentTime;
		
		@ViewInject(R.id.comment_praise_num)
		TextView praiseNum;
		
		@ViewInject(R.id.comment_to_username)
		TextView userToName;
		
		@ViewInject(R.id.comment_content)
		TextView content;
		
		@ViewInject(R.id.comment_praise_icon)
		ImageView praiseIcon;
		
		@ViewInject(R.id.repaly_layout)
		LinearLayout repalyLayout;
	}

}
