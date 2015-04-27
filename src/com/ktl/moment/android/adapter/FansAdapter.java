package com.ktl.moment.android.adapter;

import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FansAdapter extends BaseAdapter {

	private Context context;
	private List<User> userList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	
	private final String TAG = "fansAdapter";

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
		final FansHolder fansHolder;
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.fans_lv_adapter, null);
			fansHolder = new FansHolder();
			ViewUtils.inject(fansHolder, convertView);
			convertView.setTag(fansHolder);
		} else {
			fansHolder = (FansHolder) convertView.getTag();
		}

		final User fans = userList.get(position);
		ImageLoader.getInstance().displayImage(fans.getUserAvatar(),
				fansHolder.fansAvatar, options);
		fansHolder.fansNickname.setText(fans.getNickName());
		fansHolder.fansSignature.setText(fans.getSignature());
		if(fans.getIsFocused() == 0){
			fansHolder.fansFocus.setImageResource(R.drawable.fans_add_focus);
		}else{
			fansHolder.fansFocus.setImageResource(R.drawable.fans_delete_focus);
		}
		
		fansHolder.fansFocus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int isAddFocus;
				if(fans.getIsFocused() == 0){
					fansHolder.fansFocus.setImageResource(R.drawable.fans_delete_focus);
					fans.setIsFocused(1);
					isAddFocus = 1;
				}else{
					fansHolder.fansFocus.setImageResource(R.drawable.fans_add_focus);
					fans.setIsFocused(0);
					isAddFocus = 0;
				}
				User user = (User) SharedPreferencesUtil.getInstance().getObject(C.SPKey.SPK_LOGIN_INFO);
				RequestParams params = new RequestParams();
				params.put("userId", user.getId());
				params.put("attentionUserId", fans.getUserId());
				params.put("isAddFocus", isAddFocus);
				ApiManager.getInstance().post(context, C.API.FOCUS_AUTHOR, params, new HttpCallBack() {
					
					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						Log.i(TAG, "success");
					}
					
					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub
						Log.i(TAG, "fail");
					}
				}, "User");
			}
		});

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
