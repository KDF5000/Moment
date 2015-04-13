package com.ktl.moment.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * 录音计时工具类
 * @author HUST_LH
 *
 */
public class TimerCountUtil extends Activity {

	private int time = 0;
	private TextView textView;
	private boolean isNewStart = false;
	
	private volatile boolean isThreadStop = false;
	private static TimerCountUtil timerCountUtil;

	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.i("thread_time", turnInt2Time(msg.arg1));
				textView.setText("" + turnInt2Time(msg.arg1));
				break;
			}
			super.handleMessage(msg);
		};
	};
	
	public static synchronized TimerCountUtil getInstance(){
		if(timerCountUtil == null){
			timerCountUtil = new TimerCountUtil();
		}
		return timerCountUtil;
	}
	
	private TimerCountUtil(){
		
	}
	
	public void setTextView(TextView textView){
		this.textView = textView;
	}

	public class TimerThread extends Thread {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (textView) {
				if(isNewStart){
					time = 0;
					Log.i("isNewStart tag", "new start");
				}
				while (!isThreadStop) {
					time++;
					try {
						Message msg = new Message();
						msg.what = 1;
						msg.arg1 = time;
						handler.sendMessage(msg);
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("thread", "thread------"+time);
				}
			}
		}
	}

	/**
	 * 将十进制计数器转化为时钟计时
	 * @param num
	 * @return
	 */
	public String turnInt2Time(int num) {
		String time = "00:00";
		int min = 0;
		int sec = 0;
		String minute = "00";
		String second = "00";
		min = num / 60;
		sec = num % 60;
		if (min < 10) {
			minute = "0" + min;
		} else {
			minute = min + "";
		}
		if (sec < 10) {
			second = "0" + sec;
		} else {
			second = sec + "";
		}
		time = minute + ":" + second;
		return time;
	}

	/**
	 * 开始计时器
	 */
	public void startTimerCount() {
		isNewStart = true;
		isThreadStop = false;
		new TimerThread().start();
		Log.i("thread tag--------->", "start");
		Log.i("thread_tag", "isNewStart = true;isThreadStop = false;");
		Log.i("line", "----------------------------------------------");
	}
	
	/**
	 * 暂停计时器
	 */
	public void pauseTimerCount(){
		isThreadStop = true;
		isNewStart = false;
		Log.i("thread tag--------->", "pause");
		Log.i("thread_tag", "isNewStart = false;isThreadStop = true;");
		Log.i("line", "----------------------------------------------");
	}
	
	/**
	 * 结束(退出)录音
	 */
	public void stopTimerCount(){
		isThreadStop = true;
		isNewStart = true;
		Log.i("thread tag--------->", "stop/over");
		Log.i("thread_tag", "isNewStart = true;isThreadStop = true;");
		Log.i("line", "----------------------------------------------");
	}
	
	/**
	 * 继续计时
	 */
	public void restartTimerCount(){
		isThreadStop = false;
		isNewStart = false;
		TimerThread thread = new TimerThread();
		thread.start();
		Log.i("thread tag--------->", "restart");
		Log.i("thread_tag", "isNewStart = false;isThreadStop = false;");
		Log.i("line", "----------------------------------------------");
	}
	
	/**
	 * 删除录音(需要清零计时器)
	 */
	public void clearTimerCount(){
		isThreadStop = true;
		isNewStart = true;
		//由于线程已终止，所以这里需要显式发送消息给handler更新UI
		time = 0;
		Message msg = new Message();
		msg.what = 1;
		msg.arg1 = time;
		handler.sendMessage(msg);
		Log.i("thread tag--------->", "clear");
		Log.i("thread_tag", "isNewStart = true;isThreadStop = true;");
		Log.i("line", "----------------------------------------------");
	}
}
