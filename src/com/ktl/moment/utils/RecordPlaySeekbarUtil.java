package com.ktl.moment.utils;

import android.os.Handler;
import android.os.Message;
import android.widget.SeekBar;

/**
 * SeekBar status control class*
 * 
 * @author HUST_LH
 * 
 */
public class RecordPlaySeekbarUtil {
	private boolean isSeekBarPause; // seekbar是否暂停
	private boolean isNewStart; // 是否是从头开始播放
	private int startProgress = 0;

	private SeekBar seekBar;
	private Handler handler;

	public RecordPlaySeekbarUtil(SeekBar seekbar, Handler handler) {
		this.seekBar = seekbar;
		this.handler = handler;
	}

	/**
	 * 开始seekbar动画
	 * 
	 * @param maxProgress
	 */
	public void startSeekBar(int maxProgress) {
		isSeekBarPause = false;
		isNewStart = true;
		SeekBarThread seekThread = new SeekBarThread(maxProgress);
		seekThread.start();
		TimerCountUtil.getInstance().startTimerCount();
	}

	/**
	 * 暂停seekbar动画
	 */
	public void pauseSeekBar() {
		isSeekBarPause = true;
		isNewStart = false;
		TimerCountUtil.getInstance().pauseTimerCount();
	}

	/**
	 * 继续seekbar动画
	 * 
	 * @param maxProgress
	 */
	public void restartSeekBar(int maxProgress) {
		isSeekBarPause = false;
		isNewStart = false;
		SeekBarThread seekThread = new SeekBarThread(maxProgress);
		seekThread.start();
		TimerCountUtil.getInstance().restartTimerCount();
	}

	/**
	 * 停止seekbar
	 */
	public boolean stopSeekBar() {
		isSeekBarPause = true;
		isNewStart = true;

		TimerCountUtil.getInstance().stopTimerCount();
		return false;
	}

	public class SeekBarThread extends Thread {

		private int maxProgress; // seekbar的最大值，这里为播放音频文件的时长

		public SeekBarThread(int maxProgress) {
			this.maxProgress = maxProgress;

			seekBar.setMax(maxProgress * 20);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			int endProgress = maxProgress * 20;
			if (isNewStart) {
				startProgress = 0;
			}
			seekBar.setProgress(startProgress);
			while (startProgress <= endProgress && !isSeekBarPause) {
				seekBar.setProgress(startProgress++);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (seekBar.getProgress() == endProgress) {
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		}
	}
}
