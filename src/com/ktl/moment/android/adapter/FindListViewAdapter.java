package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.entity.Moment;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MomentHolder momentHolder = null;
		if (convertView == null) {
			convertView = this.mInflater.inflate(
					R.layout.fragment_find_list_item, null);
			momentHolder = new MomentHolder();
			momentHolder.tittleTv = (TextView) convertView
					.findViewById(R.id.moment_title);
			momentHolder.contentTv = (TextView) convertView
					.findViewById(R.id.moment_content);
			momentHolder.avatar = (ImageView) convertView
					.findViewById(R.id.user_avatar);
			momentHolder.userNameTv = (TextView) convertView
					.findViewById(R.id.user_name);
			momentHolder.postTime = (TextView) convertView
					.findViewById(R.id.post_time);
			momentHolder.fllowNum = (TextView) convertView
					.findViewById(R.id.follow_num);
			momentHolder.praiseNum = (TextView) convertView
					.findViewById(R.id.praise_num);
			convertView.setTag(momentHolder);
		} else {
			momentHolder = (MomentHolder) convertView.getTag();
		}
		Moment moment = momentList.get(position);
		momentHolder.tittleTv.setText(moment.getTitle());
		momentHolder.contentTv.setText(moment.getContent());
		ImageLoader.getInstance().displayImage(moment.getAvatarUrl(),
				momentHolder.avatar, options);
		momentHolder.userNameTv.setText(moment.getAuthorNickName());
		momentHolder.postTime.setText(moment.getPostTime());
		momentHolder.fllowNum.setText(moment.getFollowNums() + "");
		momentHolder.praiseNum.setText(moment.getPraiseNums() + "");
		return convertView;
	}

	public static class MomentHolder {
		TextView tittleTv;// 标题
		TextView contentTv;// 内容
		ImageView avatar;// 头像
		TextView userNameTv;// 用户名
		TextView postTime;// 发布时间
		TextView fllowNum;// 关注人数
		TextView praiseNum;// 点赞人数
	}

}
