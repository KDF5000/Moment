package com.ktl.moment.android.adapter;

import java.util.List;

import com.ktl.moment.R;
import com.ktl.moment.entity.Message;
import com.ktl.moment.utils.TimeFormatUtil;
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

public class MsgPersonalAdapter extends BaseAdapter {

	private Context context;
	private List<Message> msgList;
	private LayoutInflater inflater;
	private DisplayImageOptions options;

	public MsgPersonalAdapter(Context context, List<Message> msgList,
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MsgHolder msgHolder;
		if (convertView == null) {
			msgHolder = new MsgHolder();
			convertView = this.inflater.inflate(
					R.layout.msg_personal_msg_adapter, null);
			ViewUtils.inject(msgHolder, convertView);
			convertView.setTag(msgHolder);
		} else {
			msgHolder = (MsgHolder) convertView.getTag();
		}

		Message msg = msgList.get(position);
		ImageLoader.getInstance().displayImage(msg.getSendUserAvatar(),
				msgHolder.sendUserAvatar, options);
		msgHolder.sendUserNickname.setText(msg.getSendUserName());
		msgHolder.sendTime
				.setText(TimeFormatUtil.formatDate(msg.getSendTime()));
		msgHolder.msgContent.setText(msg.getMsgContent());

		return convertView;
	}

	public static class MsgHolder {
		@ViewInject(R.id.personal_msg_avatar)
		private ImageView sendUserAvatar;

		@ViewInject(R.id.personal_msg_nickname)
		private TextView sendUserNickname;

		@ViewInject(R.id.personal_msg_time)
		private TextView sendTime;

		@ViewInject(R.id.personal_msg_content)
		private TextView msgContent;
	}

}
