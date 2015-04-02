package com.ktl.moment.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;

public class FindListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	
	public FindListViewAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView = this.mInflater.inflate(R.layout.fragment_find_list_item, null);
		}
		TextView tittleTv = (TextView) convertView.findViewById(R.id.moment_title);
		TextView contentTv = (TextView) convertView.findViewById(R.id.moment_content);
		TextView avatar = (TextView) convertView.findViewById(R.id.user_avatar);
		TextView userNameTv = (TextView) convertView.findViewById(R.id.user_name);
		TextView postTime = (TextView)convertView.findViewById(R.id.post_time);
		TextView fllowNum = (TextView)convertView.findViewById(R.id.follow_num);
		TextView praiseNum = (TextView)convertView.findViewById(R.id.praise_num);
		tittleTv.setText("一款灵感记录类应用");
		contentTv.setText("这是一款专门记录灵感的软件,用户可以通过文字，图片，声音等多种形式记录灵感");
		avatar.setBackgroundResource(R.drawable.avatar);
		userNameTv.setText("习大大");
		postTime.setText("2小时前");
		fllowNum.setText("1234");
		praiseNum.setText("1234");
		return convertView;
	}

}
