package com.ktl.moment.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.UserPageActivity;
import com.ktl.moment.android.component.CircleImageView;
import com.ktl.moment.entity.Message;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MsgAdapter extends BaseAdapter {

	private Context context;
	private List<Message> msgList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;

	private final int SEND = 0;
	private final int RECIEVE = 1;
	private final int TIME = 2;
	private final int TYPE_COUNT = 3;

	public MsgAdapter(Context context, List<Message> msgList,
			DisplayImageOptions options) {
		this.context = context;
		this.msgList = msgList;
		this.inflater = LayoutInflater.from(context);
		this.options = options;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msgList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return msgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int type = SEND;
		switch (msgList.get(position).getMsgType()) {
		case 1:
			type = RECIEVE;
			break;
		case 2:
			type = TIME;
			break;
		default:
			type = SEND;
			break;
		}
		return type;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MsgHolder msgHolder = null;
		final Message msg = msgList.get(position);
		switch (getItemViewType(position)) {
		case SEND: {
			if (convertView == null) {
				msgHolder = new MsgHolder();
				convertView = this.inflater.inflate(R.layout.msg_send_adapter,
						null);
				msgHolder.msgAvatar = (CircleImageView) convertView
						.findViewById(R.id.msg_send_user_avatar);
				msgHolder.msgContentTv = (TextView) convertView
						.findViewById(R.id.msg_send_content_tv);
				convertView.setTag(msgHolder);
			} else {
				msgHolder = (MsgHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(msg.getSendUserAvatar(),
					msgHolder.msgAvatar, options);
			msgHolder.msgContentTv.setText(msg.getMsgContent());

			msgHolder.msgAvatar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, UserPageActivity.class);
					intent.putExtra("userId", msg.getSendUserId());
					context.startActivity(intent);
				}
			});
			break;
		}
		case RECIEVE: {
			if (convertView == null) {
				msgHolder = new MsgHolder();
				convertView = this.inflater.inflate(
						R.layout.msg_recieve_adapter, null);
				msgHolder.msgAvatar = (CircleImageView) convertView
						.findViewById(R.id.msg_recieve_user_avatar);
				msgHolder.msgContentTv = (TextView) convertView
						.findViewById(R.id.msg_recieve_content_tv);
				convertView.setTag(msgHolder);
			} else {
				msgHolder = (MsgHolder) convertView.getTag();
			}
			ImageLoader.getInstance().displayImage(msg.getRecieveUserAvatar(),
					msgHolder.msgAvatar, options);
			msgHolder.msgContentTv.setText(msg.getMsgContent());

			msgHolder.msgAvatar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, UserPageActivity.class);
					intent.putExtra("userId", msg.getSendUserId());
					context.startActivity(intent);
				}
			});
			break;
		}
		default: {
			if (convertView == null) {
				msgHolder = new MsgHolder();
				convertView = this.inflater.inflate(R.layout.msg_time_adapter,
						null);
				msgHolder.msgTime = (TextView) convertView
						.findViewById(R.id.msg_time);
				convertView.setTag(msgHolder);
			} else {
				msgHolder = (MsgHolder) convertView.getTag();
			}
			msgHolder.msgTime.setText(msg.getSendTime());
			break;
		}
		}

		return convertView;
	}

	public static class MsgHolder {

		private CircleImageView msgAvatar;
		private TextView msgContentTv;
		private TextView msgTime;

	}

}
