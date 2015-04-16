package com.ktl.moment.android.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.MomentDialogActivity;
import com.ktl.moment.android.fragment.MomentFragment.OperateCallback;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.utils.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MomentPlaAdapter extends BaseAdapter{
	
	private Context context;
	private LayoutInflater layoutInflater;
	private List<Moment> momentList;
	private DisplayImageOptions options;
	private OperateCallback mOpCallback;
	
	public MomentPlaAdapter(Context context, List<Moment> momentList, DisplayImageOptions options,OperateCallback opCallBack){
		this.context = context;
		this.momentList = momentList;
		this.options = options;
		this.mOpCallback = opCallBack;
		this.layoutInflater = LayoutInflater.from(context);
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
//		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MomentPlaHolder momentHolder = null;
		if(convertView == null){
			convertView = this.layoutInflater.inflate(R.layout.fragment_moment_etsy_list_item, null);
//			convertView = layoutInflater.inflate(R.layout.fragment_moment_pla_list_item, parent, false);
			momentHolder = new MomentPlaHolder();
			momentHolder.createDate = (TextView) convertView.findViewById(R.id.moment_create_date);
			momentHolder.publicText = (TextView) convertView.findViewById(R.id.moment_is_public);
			momentHolder.collectImg = (ImageView) convertView.findViewById(R.id.moment_is_collect_img);
			momentHolder.articleImg = (ImageView) convertView.findViewById(R.id.moment_article_img);
			momentHolder.articleTitle = (TextView) convertView.findViewById(R.id.moment_article_title);
			momentHolder.articleContent = (TextView) convertView.findViewById(R.id.moment_article_content);
			momentHolder.label = (ImageView) convertView.findViewById(R.id.moment_label_img);
			momentHolder.labelText = (TextView) convertView.findViewById(R.id.moment_label_text);
			momentHolder.momentItemLayout = (LinearLayout) convertView.findViewById(R.id.moment_item_layout);
			
			convertView.setTag(momentHolder);
		}else{
			momentHolder = (MomentPlaHolder) convertView.getTag();
		}
		
		final Moment moment = momentList.get(position);
		momentHolder.createDate.setText(moment.getPostTime());
		if (moment.getIsCollect() == 1) {
			momentHolder.collectImg.setImageResource(R.drawable.collect);
			momentHolder.publicText.setText("");
		} else if (moment.getIsPublic() == 0) {
			momentHolder.publicText.setText("不公开");
		} else if (moment.getIsPublic() == 1) {
			momentHolder.publicText.setText("公开");
			momentHolder.publicText.setTextColor(context.getResources()
					.getColor(R.color.moment_etsy_public_color));
		}
		
		if(!moment.getMomentImg().isEmpty()){
//			ImageLoader.getInstance().displayImage(moment.getMomentImg(), momentHolder.articleImg, options);
		}
		momentHolder.articleTitle.setText(moment.getTitle());
		momentHolder.articleContent.setText(moment.getContent());
		momentHolder.label.setImageResource(R.drawable.label);
		momentHolder.labelText.setText(moment.getLabel());
		
		/**
		 * 添加事件监听
		 */
		momentHolder.momentItemLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.putExtra("momentId", moment.getMomentId());
//				context.startActivity(intent);
				ToastUtil.show(context, moment.getMomentId()+"");
			}
		});
		final int currentPosition = position;
		momentHolder.momentItemLayout.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				ToastUtil.show(context, "you click me for a long time");
				mOpCallback.OnSelected(0, currentPosition);
				return true;
			}
		});
		
		return convertView;
	}
	
	private class MomentPlaHolder{
		TextView createDate;
		TextView publicText;
		ImageView collectImg;
		ImageView articleImg;
		TextView articleTitle;
		TextView articleContent;
		ImageView label;
		TextView labelText;
		LinearLayout momentItemLayout;
		
	}
	
	
}
