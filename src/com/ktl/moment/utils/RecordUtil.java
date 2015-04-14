package com.ktl.moment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

/**
 * 录音工具类
 * @author HUST_LH
 *
 */

public class RecordUtil{

	/**
	 * 录音文件
	 */
	private File recordAudioFile;
	/**
	 * 录音存放目录
	 */
	private File recordDir;

//	private File recordDir = FileUtil.getDir("moment/record");
	private MediaRecorder mediaRecord;
	
	/**
	 * 录音文件扩展名
	 */
	private static final String EXTENSION = ".amr";
	/**
	 * 录音文件名称前缀，以便于区分
	 */
	private static final String PREFIX = "";
	
	private List<String> recordList = new ArrayList<String>();
	
	public static RecordUtil recordUtil = null;
	
	public static synchronized RecordUtil getInstance(){
		if(recordUtil == null){
			recordUtil = new RecordUtil();
		}
		return recordUtil;
	}
	
	private RecordUtil(){
		recordDir = FileUtil.getDir("record");
	}
	
	/*********************************外部接口start**************************************/
	
	/**
	 * 开始录音
	 */
	public void start(){
		startRecord();
	}
	
	/**
	 * 暂停/继续录音
	 * @param isPause	当前状态，是否暂停
	 */
	public void pause(boolean isPause){
		//录音中
		if(!isPause){
			recordList.add(recordAudioFile.getPath());
			stopRecord();
		}else{//已暂停
			//重新开始录音
			startRecord();
		}
	}
	
	/**
	 * 完成录音
	 * @param hasPaused	录音中是否暂停过
	 * @param isPause	当前录音状态是否是暂停
	 */
	public void complete(boolean hasPaused, boolean isPause){
		if(hasPaused){
			//暂停状态下完成录音
			if(isPause){
				getWholeRecord(recordList, false);
			}else{//录音中完成录音
				recordList.add(recordAudioFile.getPath());
				stopRecord();
				getWholeRecord(recordList, true);
			}
		}else{
			if(recordAudioFile != null){
				stopRecord();
			}
		}
	}
	
	/**
	 * 清除录音
	 * @param isPause
	 */
	public void delete(boolean isPause){
		if(isPause){//暂停中清除暂存录音文件
			deleteTmpRecordList(recordList, false);
		}else{//录音中清除
			deleteTmpRecordList(recordList, true);
		}
	}
	
	/**
	 * 播放、暂停、停止录音
	 * @param recordName	录音文件名称
	 * @param status 0：开始播放，1：暂停播放，2：停止播放
	 */
	public void play(String recordName, int status){
		String recordPath = recordDir.getPath() + recordName;
		playRecord(recordPath, status);
	}


	/*********************************外部接口end**************************************/

	/***************************以下为内部接口，不对外开放******************************/
	/**
	 * 获取系统时间
	 * @return
	 */
	private String getTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
		Date currentDate = new Date();// 获取当前时间
		String time = format.format(currentDate);
		return time;
	}
	
	
	@SuppressWarnings("deprecation")
	private void startRecord() {
		/**
		 * 取系统当前时间，以系统当前时间为录音文件名称
		 */
		String file1Time = getTime();
		/**
		 * 创建音频文件
		 */
		recordAudioFile = new File(recordDir, PREFIX + file1Time + EXTENSION);

		/**
		 * 初始化Record
		 */
		mediaRecord = new MediaRecorder();
		/**
		 * 设置录音为麦克风
		 */
		mediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
		/**
		 * 设置录音文件格式和编码格式
		 */
		mediaRecord.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		/**
		 * 设置录音文件保存位置
		 */
		mediaRecord.setOutputFile(recordAudioFile.getAbsolutePath());
		try {
			mediaRecord.prepare();
			mediaRecord.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * 没搞明白这段代码的用途
		 */
		// mediaRecord.setOnInfoListener(new OnInfoListener() {
		//
		// @Override
		// public void onInfo(MediaRecorder mr, int what, int extra) {
		// // TODO Auto-generated method stub
		// int a = mr.getMaxAmplitude();
		// Toast.makeText(MainActivity.this, a, 1000).show();
		// }
		// });
	}

	/**
	 * 终止录音
	 */
	private void stopRecord() {
		if (mediaRecord != null) {

			mediaRecord.stop();
			mediaRecord.release();
			mediaRecord = null;
		}
	}

	/**
	 * 将所有零时保存的录音文件合并成一个完整的文件
	 * @param tmpRecordList
	 * @param isAddLastRecord
	 */
	private void getWholeRecord(List<String> tmpRecordList,	boolean isAddLastRecord) {
		String tmpTime = getTime();
		FileOutputStream fileOutputStream = null;
		/**
		 * 创建合并后的完整文件
		 */
		File wholeRecordFile = new File(recordDir, PREFIX + tmpTime + EXTENSION);
		if (!wholeRecordFile.exists()) {
			try {
				wholeRecordFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**
		 * 打开文件输出流
		 */
		try {
			fileOutputStream = new FileOutputStream(wholeRecordFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 下面依次处理list中记录的录音临时文件，除第一段文件外，其余的每段录音文件需要减去前6个字节(为amr文件的文件头)
		 */
		int listLength = tmpRecordList.size();
		for (int i = 0; i < listLength; i++) {
			/**
			 * 打开一个临时记录的录音文件
			 */
			File tmpFile = new File(tmpRecordList.get(i));
			try {
				/**
				 * 打开文件输入流
				 */
				FileInputStream fileInputStream = new FileInputStream(tmpFile);
				byte[] tmpFileByte = new byte[fileInputStream.available()];
				int fileLength = tmpFileByte.length;
				/**
				 * i=0说明是记录的第一个零时文件，这个文件不做处理，全部字节输出；其余文件需要略去前面6个字节
				 */
				if(i == 0){
					while(fileInputStream.read(tmpFileByte) != -1){
						fileOutputStream.write(tmpFileByte, 0, fileLength);
					}
				}else{
					while(fileInputStream.read(tmpFileByte) != -1){
						fileOutputStream.write(tmpFileByte, 6, fileLength-6);
					}
				}
				fileOutputStream.flush();
				fileInputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * 是否加入最后一段录音文件
		 */
		if(isAddLastRecord){
			try {
				FileInputStream lastFileInputStream = new FileInputStream(recordAudioFile);
				byte[] lastFileByte = new byte[lastFileInputStream.available()];
				while(lastFileInputStream.read() != -1){
					fileOutputStream.write(lastFileByte, 6, lastFileByte.length-6);
				}
				fileOutputStream.flush();
				lastFileInputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/**
		 * 最后关闭文件输出流
		 */
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * 完成所有合并操作后删除记录的零时文件
		 */
		deleteTmpRecordList(tmpRecordList, isAddLastRecord);
	}
	
	/**
	 * 删除记录的零时文件
	 * @param tmpRecordList
	 * @param isDeleteLastRecord
	 */
	private void deleteTmpRecordList(List<String> tmpRecordList, boolean isDeleteLastRecord){
		for(int i=0;i<tmpRecordList.size();i++){
			File tmpFile = new File(tmpRecordList.get(i));
			if(tmpFile.exists()){
				tmpFile.delete();
			}
		}
		if(isDeleteLastRecord){
			recordAudioFile.delete();
		}
	}
	
	private void playRecord(String recordPath, int status){
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(recordPath);
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
		switch (status) {
		case 0:	//播放
			player.start();
			break;
		case 1:
			player.pause();
			break;
		case 2:
			player.stop();
			player.release();
			player = null;
		default:
			break;
		}
	}
}
