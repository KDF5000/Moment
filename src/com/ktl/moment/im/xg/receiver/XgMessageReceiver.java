package com.ktl.moment.im.xg.receiver;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.ktl.moment.R;
import com.ktl.moment.android.MomentApplication;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.im.entity.CustomContent;
import com.ktl.moment.im.entity.XgMessage;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class XgMessageReceiver extends XGPushBaseReceiver {

	private Context mContext;
	private final int CUSTOM_MSG = 1;//自定义的消息，点赞，评论，关注，收藏，围观
	private final int CHAT_MSG = 2;//聊天
	private XgMessage mxgMessage;//信鸽消息
	private static List<OnCustomMessageListener> onCustomMessageListenerList;
	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotifactionClickedResult(Context arg0,
			XGPushClickedResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotifactionShowedResult(Context arg0, XGPushShowedResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegisterResult(Context arg0, int arg1,
			XGPushRegisterResult arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		this.mContext = context;
//		int messageType = Integer.getInteger();//消息标题标示消息的类型，0:赞，评论，关注，剪藏，围观  1:私信
		String content = message.getCustomContent();
		int messageType = Integer.parseInt(message.getTitle());
//		Toast.makeText(context, messageType + "-->" +content, Toast.LENGTH_SHORT).show();
		if(mxgMessage==null){
			mxgMessage.setMessageType(messageType);
		}
		switch (messageType) {
		case CUSTOM_MSG:
			parseCustomMsg(content);
			break;
		case CHAT_MSG:
			
		default:
			break;
		}
	}

	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	/**
	 * 解析自定义的消息
	 * @param content
	 */
	private void parseCustomMsg(String content){
		Gson gson = new Gson();
		CustomContent customContent = gson.fromJson(content,CustomContent.class);
		mxgMessage.setContent(customContent);
		showNotification("您有一条新信息",customContent.getUserName(),customContent.getMessage());
		//通知监听的对象
		if(onCustomMessageListenerList!=null && onCustomMessageListenerList.size()>0){
			for(OnCustomMessageListener listener : onCustomMessageListenerList){
				listener.OnReceive(mxgMessage);
			}
		}
		
	}
	/**
	 * 显示通知
	 * @param message
	 */
	@SuppressLint("NewApi") 
	private void showNotification(String Ticker,String title,String content){
		MomentApplication application = MomentApplication.getApplication();
		NotificationManager manager = application.getNotificationManager();
		Notification.Builder builder = new Notification.Builder(this.mContext);
		Intent intent = new Intent(this.mContext,HomeActivity.class);
		builder.setTicker(Ticker);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.ic_launcher);
		PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, 1, intent,PendingIntent.FLAG_ONE_SHOT);
		builder.setContentIntent(pendingIntent);
		manager.notify((int) System.currentTimeMillis(),builder.build());
	}
	
	/**
	 * 
	 * @author KDF5000
	 *
	 */
	public interface OnCustomMessageListener{
		public void OnReceive(XgMessage msg);
	}
	/**
	 * 添加监听
	 * @param listener
	 */
	public static void addCustomMessageListener(OnCustomMessageListener listener){
		if(listener==null){
			return ;
		}
		if(onCustomMessageListenerList==null){
			onCustomMessageListenerList = new ArrayList<XgMessageReceiver.OnCustomMessageListener>();
		}
		onCustomMessageListenerList.add(listener);
	}
	/**
	 * 移除监听
	 * @param listener
	 */
	public static void removeCustomMessageListener(OnCustomMessageListener listener){
		if(listener==null || onCustomMessageListenerList==null){
			return ;
		}
		onCustomMessageListenerList.remove(listener);
	}
	
}
