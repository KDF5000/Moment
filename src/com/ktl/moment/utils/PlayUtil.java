package com.ktl.moment.utils;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

public class PlayUtil implements OnBufferingUpdateListener, OnPreparedListener {
	private SeekBar seekbar;
	private Handler handler;
	private RecordPlaySeekbarUtil seekbarUtil;
	private String path;
	private MediaPlayer player;
	private int duration;
	private boolean isAuto = false;
	private boolean isPrepared;

	public PlayUtil(SeekBar seekbar, Handler handler, String path) {
		this.seekbar = seekbar;
		this.handler = handler;
		this.path = path;

		// this.seekbar.setEnabled(false);
		seekbarUtil = new RecordPlaySeekbarUtil(this.seekbar, this.handler);

		player = new MediaPlayer();
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnBufferingUpdateListener(this);
		player.setOnPreparedListener(this);
	}

	public void initPlayer() {
		if (player == null) {
			player = new MediaPlayer();
		}
		try {
			player.reset();
			player.setDataSource(path);
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		duration = player.getDuration();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		seekbar.setSecondaryProgress(percent);
		Log.i("percent", percent+"");
	}

	public void startPlay() {
		player.start();
		Log.i("start duration", duration+"");
		seekbarUtil.startSeekBar(duration / 1000);
	}

	public void pausePlay() {
		player.pause();
		seekbarUtil.pauseSeekBar();
	}

	public void continuePlay() {
		player.start();
		seekbarUtil.restartSeekBar(duration / 1000);
	}

	public void stopPlay() {
		if (player != null) {
			player.stop();
			player.release();
			player = null;
			seekbarUtil.stopSeekBar();
		}
	}

	public int getDuration() {
		Log.i("get duration",  duration+"");
		return duration;
	}

	public void stopSeekbar() {
		seekbarUtil.stopSeekBar();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
//		duration = mp.getDuration();
//		Log.i("duration", duration + "");
		isPrepared = true;
		if (isAuto && mp == player) {
			startPlay();
		}
	}
	
	public boolean getIsPrepared(){
		return isPrepared;
	}

	public void setIsAutoPlay(boolean isAuto) {
		this.isAuto = isAuto;
	}

}
