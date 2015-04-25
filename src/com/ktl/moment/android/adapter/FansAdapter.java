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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FansAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context context;
	private List<User> userList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;

	public FansAdapter(Context context, List<User> userList,
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
		FansHolder fansHolder;
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.fans_lv_adapter, null);
			fansHolder = new FansHolder();
			ViewUtils.inject(fansHolder, convertView);
			convertView.setTag(fansHolder);
		} else {
			fansHolder = (FansHolder) convertView.getTag();
		}

		User user = userList.get(position);
		ImageLoader.getInstance().displayImage(user.getUserAvatar(),
				fansHolder.fansAvatar, options);
		fansHolder.fansNickname.setText(user.getNickName());
		fansHolder.fansSignature.setText(user.getSignature());
		if(user.getIsFocused() == 0){
			fansHolder.fansFocus.setImageResource(R.drawable.fans_add_focus);
		}else{
			fansHolder.fansFocus.setImageResource(R.drawable.fans_delete_focus);
		}

		return convertView;
	}

	public static class FansHolder {
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
