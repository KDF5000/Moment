package com.ktl.moment.android.adapter;

import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.entity.User;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendAuthorAdapter extends BaseAdapter{

	@SuppressWarnings("unused")
	private Context context;
	private List<User> userList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	
	private final String TAG = "recommendUserAdapter";

	public RecommendAuthorAdapter(Context context, List<User> userList,
			DisplayImageOptions options) {
		this.context = context;
		this.userList = userList;
		this.options = options;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final AuthorHolder authorHolder;
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.fans_lv_adapter, null);
			authorHolder = new AuthorHolder();
			ViewUtils.inject(authorHolder, convertView);
			convertView.setTag(authorHolder);
		} else {
			authorHolder = (AuthorHolder) convertView.getTag();
		}

		final User user = userList.get(position);
		ImageLoader.getInstance().displayImage(user.getUserAvatar(),
				authorHolder.fansAvatar, options);
		authorHolder.fansNickname.setText(user.getNickName());
		authorHolder.fansSignature.setText(user.getSignature());
		if(user.getIsFocused() == 0){
			authorHolder.fansFocus.setImageResource(R.drawable.select_unable);
		}else{
			authorHolder.fansFocus.setImageResource(R.drawable.select_enable);
		}
		
		authorHolder.fansFocus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(user.getIsFocused() == 0){
					authorHolder.fansFocus.setImageResource(R.drawable.select_enable);
					user.setIsFocused(1);
				}else{
					authorHolder.fansFocus.setImageResource(R.drawable.select_unable);
					user.setIsFocused(0);
				}
			}
		});

		return convertView;
	}

	public static class AuthorHolder {
		@ViewInject(R.id.fans_avatar)
		private ImageView fansAvatar;

		@ViewInject(R.id.fans_nickname)
		private TextView fansNickname;

		@ViewInject(R.id.fans_signature)
		private TextView fansSignature;

		@ViewInject(R.id.fans_focus)
		private ImageView fansFocus;
	}

}

