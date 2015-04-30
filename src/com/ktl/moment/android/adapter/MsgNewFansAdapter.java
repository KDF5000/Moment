package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.UserPageActivity;
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

public class MsgNewFansAdapter extends BaseAdapter {

	private Context context;
	private List<User> fansList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;
	
	private String TAG = "MsgNewFansAdapter";

	public MsgNewFansAdapter(Context context, List<User> fansList,
			DisplayImageOptions option) {
		this.context = context;
		this.fansList = fansList;
		this.inflater = LayoutInflater.from(context);
		this.options = option;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fansList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fansList.get(position);
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
			convertView = this.inflater.inflate(R.layout.msg_new_fans_adapter,
					null);
			fansHolder = new FansHolder();
			ViewUtils.inject(fansHolder, convertView);
			convertView.setTag(fansHolder);
		} else {
			fansHolder = (FansHolder) convertView.getTag();
		}

		final User fans = fansList.get(position);
		ImageLoader.getInstance().displayImage(fans.getUserAvatar(),
				fansHolder.newFansAvatar, options);
		fansHolder.newFansNickname.setText(fans.getNickName());
		fansHolder.newFansDate.setText(fans.getFocusedTime());
		if (fans.getIsFocused() == 0) {
			fansHolder.newFansFocus.setImageResource(R.drawable.fans_add_focus);
		} else {
			fansHolder.newFansFocus
					.setImageResource(R.drawable.fans_delete_focus);
		}

		fansHolder.newFansFocus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int isAddFocus;
				if (fans.getIsFocused() == 0) {
					fansHolder.newFansFocus
							.setImageResource(R.drawable.fans_delete_focus);
					fans.setIsFocused(1);
					isAddFocus = 1;
				} else {
					fansHolder.newFansFocus
							.setImageResource(R.drawable.fans_add_focus);
					fans.setIsFocused(0);
					isAddFocus = 0;
				}
				User user = (User) SharedPreferencesUtil.getInstance()
						.getObject(C.SPKey.SPK_LOGIN_INFO);
				RequestParams params = new RequestParams();
				params.put("userId", user.getId());
				params.put("attentionUserId", fans.getUserId());
				params.put("isAddFocus", isAddFocus);
				ApiManager.getInstance().post(context, C.API.FOCUS_AUTHOR,
						params, new HttpCallBack() {

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
		
		fansHolder.newFansAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, UserPageActivity.class);
				context.startActivity(intent);
			}
		});
		
		fansHolder.newFansNickname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, UserPageActivity.class);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	public static class FansHolder {
		@ViewInject(R.id.msg_new_fans_avatar)
		private ImageView newFansAvatar;

		@ViewInject(R.id.msg_new_fans_nickname)
		private TextView newFansNickname;

		@ViewInject(R.id.msg_new_fans_date)
		private TextView newFansDate;

		@ViewInject(R.id.msg_new_fans_focus)
		private ImageView newFansFocus;
	}

}
