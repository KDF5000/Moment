package com.ktl.moment.android.adapter;

import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.UserPageActivity;
import com.ktl.moment.entity.Notification;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgNotificationAdapter extends BaseAdapter {

	private static Context context;
	private List<Notification> notificationList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;

	public MsgNotificationAdapter(Context context,
			List<Notification> notificationList, DisplayImageOptions options) {
		MsgNotificationAdapter.context = context;
		this.notificationList = notificationList;
		this.inflater = LayoutInflater.from(context);
		this.options = options;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notificationList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return notificationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		NotifyHolder notifyHolder;
		if (convertView == null) {
			notifyHolder = new NotifyHolder();
			convertView = this.inflater.inflate(
					R.layout.msg_notificaiton_adapter, null);
			ViewUtils.inject(notifyHolder, convertView);
			convertView.setTag(notifyHolder);
		} else {
			notifyHolder = (NotifyHolder) convertView.getTag();
		}

		Notification notification = notificationList.get(position);
		ImageLoader.getInstance().displayImage(notification.getUserAvatar(),
				notifyHolder.avatar, options);
		notifyHolder.nickname.setText(notification.getUserNickname());
		notifyHolder.date.setText(notification.getNotifyDate());
		notifyHolder.title.setText(notification.getNotifyTitle());
		CharSequence typeChar = "";
		switch (notification.getNotifyType()) {
		case 0:
			typeChar = "评论灵感：";
			notifyHolder.content.setVisibility(View.VISIBLE);
			notifyHolder.content.setText(notification.getNotifyContent());
			break;
		case 1:
			typeChar = "赞了灵感：";
			notifyHolder.content.setVisibility(View.GONE);
			break;
		case 2:
			typeChar = "剪藏了灵感：";
			notifyHolder.content.setVisibility(View.GONE);
			break;
		case 3:
			typeChar = "围观了灵感：";
			notifyHolder.content.setVisibility(View.GONE);
			break;
		case 4:
			typeChar = "分享了灵感：";
			notifyHolder.content.setVisibility(View.GONE);
			break;
		case 5:
			typeChar = "回复了评论：";
			notifyHolder.content.setVisibility(View.VISIBLE);
			notifyHolder.content.setText(notification.getNotifyContent());
			break;
		case 6:
			typeChar = "你关注的灵感更新了：";
			notifyHolder.content.setVisibility(View.GONE);
			break;
		case 7:
			typeChar = "评论你围观的灵感：";
			notifyHolder.content.setVisibility(View.VISIBLE);
			notifyHolder.content.setText(notification.getNotifyContent());
			break;
		case 8:
			typeChar = "赞了评论：";
			notifyHolder.content.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		notifyHolder.type.setText(typeChar);

		return convertView;
	}

	public static class NotifyHolder {
		@ViewInject(R.id.notification_avatar)
		private ImageView avatar;

		@ViewInject(R.id.notification_nickname)
		private TextView nickname;

		@ViewInject(R.id.notification_date)
		private TextView date;

		@ViewInject(R.id.notification_type)
		private TextView type;

		@ViewInject(R.id.notification_title)
		private TextView title;

		@ViewInject(R.id.notification_comment_content)
		private TextView content;

		@OnClick({ R.id.notification_avatar, R.id.notification_nickname })
		public void click(View v) {
			switch (v.getId()) {
			case R.id.notification_avatar:
			case R.id.notification_nickname:
				Intent intent = new Intent(context, UserPageActivity.class);
				context.startActivity(intent);
			default:
				break;
			}
		}
	}

}
