package com.ktl.moment.android.activity;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.ResizeLinearLayout;
import com.ktl.moment.android.component.ResizeLinearLayout.OnResizeListener;
import com.ktl.moment.android.component.RichEditText;
import com.ktl.moment.android.component.RippleBackground;
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.entity.QiniuToken;
import com.ktl.moment.entity.User;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.manager.TaskManager;
import com.ktl.moment.manager.TaskManager.TaskCallback;
import com.ktl.moment.qiniu.QiniuManager;
import com.ktl.moment.qiniu.QiniuManager.QiniuRequestCallbBack;
import com.ktl.moment.qiniu.QiniuTask;
import com.ktl.moment.utils.BasicInfoUtil;
import com.ktl.moment.utils.EncryptUtil;
import com.ktl.moment.utils.FileUtil;
import com.ktl.moment.utils.PlayUtil;
import com.ktl.moment.utils.RecordUtil;
import com.ktl.moment.utils.RichEditUtils;
import com.ktl.moment.utils.SharedPreferencesUtil;
import com.ktl.moment.utils.StrUtils;
import com.ktl.moment.utils.TimeFormatUtil;
import com.ktl.moment.utils.TimerCountUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.db.DbTaskHandler;
import com.ktl.moment.utils.db.DbTaskType;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;

/**
 * 灵感编辑
 * 
 * @author HUST_LH
 * 
 */
@SuppressLint("HandlerLeak")
public class EditorActivity extends BaseActivity {
	@ViewInject(R.id.tools_layout)
	private LinearLayout toolsLayout;

	@ViewInject(R.id.editor_record_img)
	private ImageView recordImg;

	@ViewInject(R.id.editor_gallery_img)
	private ImageView galleryImg;

	@ViewInject(R.id.editor_keyboard_img)
	private ImageView keyboardImg;

	@ViewInject(R.id.editor_record_big_img)
	private ImageView recordBigImg;

	@ViewInject(R.id.editor_article_title)
	private EditText articleTitle;

	@ViewInject(R.id.editor_edit_area)
	private RichEditText contentRichEditText;

	@ViewInject(R.id.editor_tool_content)
	private RelativeLayout toolContent;

	@ViewInject(R.id.editor_base_content)
	private ResizeLinearLayout baseContent;

	@ViewInject(R.id.editor_wave_content)
	private RippleBackground ripple;

	// 录音时需要显示的控件

	@ViewInject(R.id.record_layout)
	private LinearLayout recordLayout;

	@ViewInject(R.id.editor_record_delete)
	private ImageView editorRecordDeleteImg;

	@ViewInject(R.id.editor_record_time_view)
	private TextView editorRecordTimeTv;

	@ViewInject(R.id.editor_record_pause)
	private ImageView editorRecordPause;

	@ViewInject(R.id.editor_record_over)
	private ImageView editorRecordOver;

	// 播放录音时需要的组件
	@ViewInject(R.id.editor_record_audio)
	private ImageView editorRecordAudio;

	@ViewInject(R.id.audio_play_layout)
	private LinearLayout audioPlayLayout;

	@ViewInject(R.id.editor_play_delete)
	private ImageView editorPlayDeleteImg;

	@ViewInject(R.id.editor_play_seekbar)
	private SeekBar editorPlaySeekbar;

	@ViewInject(R.id.editor_play_start)
	private ImageView editorPlayStartImg;

	@ViewInject(R.id.editor_play_status)
	private TextView editorPlayStatusTv;

	@ViewInject(R.id.editor_play_time)
	private TextView editorPlayTimeTv;

	@ViewInject(R.id.editor_play_close)
	private ImageView editorPlayCloseImg;

	private AnimationDrawable animationDrawable;
	// 父类控件
	@ViewInject(R.id.title_back_img)
	private ImageView titleBackImg;

	@ViewInject(R.id.activity_base_title_container_layout)
	private RelativeLayout baseTitleContainer;

	private Moment moment;// 灵感类

	private int appHeight;
	private int baseLayoutHeight;

	private int currentStatus;
	private static final int SHOW_TOOLS = 1;
	private static final int SHOW_KEY_BOARD = 2;
	private static final int RESIZE_LAYOUT = 1;

	private boolean flag = false; // 控制何时显示下方tools
	private boolean isPause = true; // 标识录音是否暂停,true=暂停中,false=录音中
	private boolean hasPause = false; // 标识录音中是否暂停过
	private boolean isOpen = false;// 表示是否公开灵感

	private String recordAudioPath = null;
	// private static final int PLAY_START = 0;
	// private static final int PLAY_PAUSE = 1;
	// private static final int PLAY_STOP = 2;
	private boolean isClickPlayer = false;// 控制录音播放按钮不被重复点击，原则上只允许点击一次，除非退出播放界面
	private boolean isPlaying = false; // 是否播放中
	private boolean isReplay = false;// 是否重新播放

	private boolean isConfirmDelete; // 录音播放栏删除音频的标识

	// private RecordPlaySeekbarUtil recordPlaySeekbarUtil;// seekbar工具类
	private PlayUtil playUtil;

	private InputHandler inputHandler = new InputHandler();

	private class InputHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case RESIZE_LAYOUT:
				if (msg.arg1 == SHOW_TOOLS) {
					currentStatus = SHOW_TOOLS;
					setToolsVisible(true);
				} else {
					currentStatus = SHOW_KEY_BOARD;
					baseLayoutHeight = baseContent.getHeight();
					setToolsVisible(false);
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	private DbTaskHandler dbTaskHandler = new DbTaskHandler(this);// 保存数据库的handler

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_editor, contentLayout,
				true);

		init();
		FileUtil.createDir("record");

		appHeight = getAppHeight();
		// 设置灵感内容
		Intent intent = getIntent();
		moment = (Moment) intent.getSerializableExtra("moment");
		if (moment != null) {
			articleTitle.setText(moment.getTitle());
			contentRichEditText.setText(moment.getContent());
			recordAudioPath = moment.getAudioUrl();
			if(recordAudioPath != null){
				editorRecordAudio.setVisibility(View.VISIBLE);
			}
		}
	}

	private void init() {
		ViewUtils.inject(this);

		baseContent.setOnResizeListener(new OnResizeListener() {
			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				// TODO Auto-generated method stub
				int selector = SHOW_TOOLS;
				if (h < oldh) {
					selector = SHOW_KEY_BOARD;
				}
				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = selector;
				inputHandler.sendMessage(msg);
			}
		});

		setTitleBackImgVisible(true);
		setTitleRightImgVisible(true);
		setTitleRightImgLeftVisible(true);
		setTitleRightImgLeft(R.drawable.label_big);
		setTitleBackImg(R.drawable.title_return_black);
		if (isOpen) {
			setTitleRightImg(R.drawable.editor_open_enable);
		} else {
			setTitleRightImg(R.drawable.editor_open_unable);
		}
		setBaseActivityBgColor(getResources().getColor(
				R.color.editor_main_color));

	}

	/**
	 * 获取应用显示区域高度。。。 PS:该方法放到工具类使用会报NPE ，怀疑是没有传入activity所致，没有深究
	 * 
	 * @return
	 */
	public int getAppHeight() {
		/**
		 * 获取屏幕物理尺寸高(单位：px)
		 */
		DisplayMetrics ds = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(ds);

		/**
		 * 获取设备状态栏高度
		 */
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, top = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			top = getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		/**
		 * 屏幕高度减去状态栏高度即为应用显示区域高度
		 */
		return ds.heightPixels - top;
	}

	/**
	 * 系统软键盘与工具栏的切换显示
	 */
	private void showTools(int id) {
		if (id == R.id.editor_keyboard_img || id == R.id.editor_gallery_img
				|| id == R.id.editor_record_over) {
			keyboardImg
					.setImageResource(R.drawable.editor_keyboard_enable_selector);
			flag = false;
			// if (currentStatus == SHOW_TOOLS &&
			// contentRichEditText.hasFocus()) {
			if (currentStatus == SHOW_TOOLS) {
				showSoftKeyBoard();
			} else {
				setToolsVisible(false);
			}
		} else {
			keyboardImg
					.setImageResource(R.drawable.editor_keyboard_unable_selector);
			flag = true;
			if (currentStatus == SHOW_KEY_BOARD) {
				showSoftKeyBoard();
			} else {
				setToolsVisible(true);
			}
		}
		if (id == R.id.editor_record_img) {
			recordImg
					.setImageResource(R.drawable.editor_record_enable_selector);
		} else {
			recordImg
					.setImageResource(R.drawable.editor_record_unable_selector);
		}
	}

	/**
	 * 反复切换系统软键盘
	 */
	private void showSoftKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 显示下方的tool content
	 * 
	 * @param isShow
	 */
	private void setToolsVisible(boolean isShow) {
		if (toolContent != null) {
			if (isShow && flag) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, appHeight - baseLayoutHeight
								- baseTitleContainer.getHeight());
				// Log.i("height", appHeight+"-"+
				// baseLayoutHeight+"-"+baseTitleContainer.getHeight());
				toolContent.setLayoutParams(params);
				toolContent.setVisibility(View.VISIBLE);
			} else {
				toolContent.setVisibility(View.GONE);
			}
		}
	}

	@OnClick({ R.id.editor_gallery_img, R.id.editor_record_img,
			R.id.editor_keyboard_img, R.id.editor_edit_complete,
			R.id.editor_record_big_img, R.id.title_back_img,
			R.id.editor_record_delete, R.id.editor_record_pause,
			R.id.editor_record_over, R.id.editor_record_audio,
			R.id.editor_play_close, R.id.editor_play_start,
			R.id.title_right_img, R.id.editor_play_delete ,R.id.title_right_img_left})
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.editor_record_img:
			record();
			break;
		case R.id.editor_gallery_img:
			gallery();
			break;
		case R.id.editor_keyboard_img:
			keyboard();
			break;
		case R.id.editor_edit_complete:
			saveContent();
			break;
		case R.id.editor_record_delete:
			recordDelete();
			break;
		case R.id.editor_record_big_img:
			recordBig();
			break;
		case R.id.editor_record_pause:
			recordPause();
			break;
		case R.id.editor_record_over:
			recordOver();
			break;
		case R.id.title_back_img:
			actionStart(HomeActivity.class);
			TimerCountUtil.getInstance().stopTimerCount();
			break;
		case R.id.title_right_img:
			setOpen();
			break;
		case R.id.editor_record_audio:
			player();
			break;
		case R.id.editor_play_close:
			closePlayer();
			break;
		case R.id.editor_play_start:
			if (!isReplay) {
				playerPause();
			} else {
				playerRestart();
			}
			break;
		case R.id.editor_play_delete: {
			// 弹出dialog
			Intent intent = new Intent(this, SimpleDialogActivity.class);
			intent.putExtra("content", "您确定彻底删除该录音？");
			startActivityForResult(intent,
					C.ActivityRequest.REQUEST_DIALOG_ACTIVITY);

			// 先暂停音频播放
			// RecordUtil.getInstance().play(recordAudioPath, PLAY_PAUSE);
			// recordPlaySeekbarUtil.pauseSeekBar();
			playUtil.pausePlay();
			editorPlayStartImg.setImageResource(R.drawable.editor_record_start);
			break;
		}
		case R.id.title_right_img_left:
			//弹出标签选择
			Intent intent = new Intent(this,LabelSelectActivity.class);
			startActivityForResult(intent, C.ActivityRequest.REQUEST_SELECT_LABEL);
		default:
			break;
		}
	}

	/**
	 * 设置灵感是否公开
	 */
	public void setOpen() {
		if (isOpen) {
			setTitleRightImg(R.drawable.editor_open_enable);
			isOpen = false;
		} else {
			setTitleRightImg(R.drawable.editor_open_unable);
			isOpen = true;
		}
	}

	/**
	 * 工具栏录音图标相应的逻辑
	 */
	public void record() {
		showTools(R.id.editor_record_img);
		ripple.setVisibility(View.VISIBLE);

		toolsLayout.setVisibility(View.GONE);
		recordLayout.setVisibility(View.VISIBLE);

		// 开始计时

		TimerCountUtil.getInstance().setTextView(editorRecordTimeTv);
		TimerCountUtil.getInstance().startTimerCount();
		// 开始录音
		RecordUtil.getInstance().start();

		editorRecordPause.setImageResource(R.drawable.editor_record_pause);
		ripple.startRippleAnimation();

		isPause = false;
	}

	/**
	 * 工具栏添加图片的逻辑
	 */
	public void gallery() {
		showTools(R.id.editor_gallery_img);
		ripple.setVisibility(View.GONE);

		// 调用系统图库
		// Intent getImg = new Intent(Intent.ACTION_GET_CONTENT);
		// getImg.addCategory(Intent.CATEGORY_OPENABLE);
		// getImg.setType("image/*");
		Intent getImg = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(getImg,
				C.ActivityRequest.REQUEST_PICTURE_CROP_ACTION);
	}

	/**
	 * 工具栏键盘图标
	 */
	public void keyboard() {
		showTools(R.id.editor_keyboard_img);
		ripple.setVisibility(View.GONE);
	}

	/**
	 * 工具栏下方区域的录音机图标
	 */
	public void recordBig() {

		if (recordLayout.getVisibility() == View.GONE) {
			toolsLayout.setVisibility(View.GONE);
			recordLayout.setVisibility(View.VISIBLE);

			Log.i("tag", "big");
			TimerCountUtil.getInstance().setTextView(editorRecordTimeTv);
			TimerCountUtil.getInstance().startTimerCount();
			RecordUtil.getInstance().start();

			editorRecordPause.setImageResource(R.drawable.editor_record_pause);
			ripple.startRippleAnimation();
			isPause = false;

		} else {
			Log.i("tag", "small");
			recordPause();
		}
	}

	/**
	 * 录音时删除录音
	 */
	public void recordDelete() {
		// 清零计时器
		TimerCountUtil.getInstance().clearTimerCount();
		// 清除暂存的录音文件
		RecordUtil.getInstance().delete(isPause);
		RecordUtil.getInstance().stop();

		editorRecordPause.setImageResource(R.drawable.editor_record_start);

		ripple.stopRippleAnimation();
		isPause = true;
	}

	/**
	 * 录音暂停
	 */
	public void recordPause() {
		if (!isPause) {// 录音中
			editorRecordPause.setImageResource(R.drawable.editor_record_start);

			// 暂停计时
			TimerCountUtil.getInstance().pauseTimerCount();
			RecordUtil.getInstance().pause(isPause);

			ripple.stopRippleAnimation();

			hasPause = true;
			isPause = true;
		} else {// 已暂停
			editorRecordPause.setImageResource(R.drawable.editor_record_pause);

			// 继续计时
			TimerCountUtil.getInstance().restartTimerCount();
			RecordUtil.getInstance().pause(isPause);

			ripple.startRippleAnimation();
			isPause = false;
		}
	}

	/**
	 * 录音结束
	 */
	public void recordOver() {
		showTools(R.id.editor_record_over);
		toolsLayout.setVisibility(View.VISIBLE);
		recordLayout.setVisibility(View.GONE);

		RecordUtil.getInstance().complete(hasPause, isPause);

		// // 终止计时
		TimerCountUtil.getInstance().stopTimerCount();
		// //计时器清零
		TimerCountUtil.getInstance().clearTimerCount();

		ripple.stopRippleAnimation();
		isPause = true;

		// 完成录音后获取音频文件的路径
		recordAudioPath = RecordUtil.getInstance().getRecordPath();

		if (recordAudioPath != null) {
			editorRecordAudio.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 用于通知主线程更新播放界面UI
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				editorRecordAudio.setImageResource(R.drawable.editor_audio3);
				editorPlayStartImg
						.setImageResource(R.drawable.editor_record_replay_selector);
				// isPlaying = recordPlaySeekbarUtil.stopSeekBar();
				playUtil.stopSeekbar();
				isPlaying = false;
				isReplay = true; // 播放完录音时将重播表示置为true
				break;
			}
			}
			super.handleMessage(msg);
		};
	};

	/**
	 * 进入录音播放栏,同时播放录音
	 */
	public void player() {
		editorPlaySeekbar.setEnabled(false);
		playUtil = new PlayUtil(editorPlaySeekbar, handler, recordAudioPath,
				editorPlayStatusTv, editorPlayTimeTv);
		playUtil.initPlayer();
		if (!isClickPlayer) {
			Animation animLeftOut = AnimationUtils.loadAnimation(this,
					R.anim.left_out);
			toolsLayout.setAnimation(animLeftOut);
			toolsLayout.setVisibility(View.GONE);

			audioPlayLayout.setVisibility(View.VISIBLE);
			Animation animRightIn = AnimationUtils.loadAnimation(this,
					R.anim.right_in);
			audioPlayLayout.setAnimation(animRightIn);

			// 初始化seekbar
			// editorPlaySeekbar.setEnabled(false);
			// recordPlaySeekbarUtil = new RecordPlaySeekbarUtil(
			// editorPlaySeekbar, handler);

			// 没有播放，那就开始播放
			if (!isPlaying) {
				playerPlay();
			}

			isClickPlayer = true;
		}
	}

	/**
	 * 开始录音播放
	 */
	public void playerPlay() {
		TimerCountUtil.getInstance().clearTimerCount();// 播放前进行一次强制清零计时
		editorRecordAudio.setImageResource(R.drawable.audio_frame_anim);
		animationDrawable = (AnimationDrawable) editorRecordAudio.getDrawable();
		animationDrawable.start();

		editorPlayStartImg.setImageResource(R.drawable.editor_record_pause);

		// int duration = RecordUtil.getInstance().play(recordAudioPath,
		// PLAY_START);
		int duration = playUtil.getDuration();
		String recordTime = TimerCountUtil.getInstance().turnInt2Time(
				duration / 1000 + 1);
		// recordPlaySeekbarUtil.startSeekBar(duration / 1000);
		editorPlayTimeTv.setText(recordTime);
		playUtil.startPlay();

		TimerCountUtil.getInstance().setTextView(editorPlayStatusTv);
		isPlaying = true;
	}

	/**
	 * 暂停音频播放
	 */
	public void playerPause() {
		// 播放中
		if (isPlaying) {
			editorPlayStartImg.setImageResource(R.drawable.editor_record_start);
			// RecordUtil.getInstance().play(recordAudioPath, PLAY_PAUSE);
			// recordPlaySeekbarUtil.pauseSeekBar();
			playUtil.pausePlay();
			animationDrawable.stop();
			isPlaying = false;
		} else {
			editorPlayStartImg.setImageResource(R.drawable.editor_record_pause);
			// int duration = RecordUtil.getInstance().play(recordAudioPath,
			// PLAY_START);
			// recordPlaySeekbarUtil.restartSeekBar(duration / 1000);
			playUtil.continuePlay();
			animationDrawable.start();
			isPlaying = true;
		}
	}

	/**
	 * 重播录音
	 */
	public void playerRestart() {
		isReplay = false; // 重播录音时将重播表示置为false
		playerPlay();
	}

	/**
	 * 从磁盘删除录音文件
	 */
	public void playerDelete() {
		isPlaying = false;

		if (isConfirmDelete) {
			boolean isDelete = FileUtil.deleteFile(recordAudioPath);
			if (isDelete) {
				// 调出系统软键盘
				showSoftKeyBoard();

				ToastUtil.show(this, "录音文件删除成功");
				TimerCountUtil.getInstance().clearTimerCount();

				// 删除录音后关闭音频播放栏
				toolsLayout.setVisibility(View.VISIBLE);
				Animation animLeftIn = AnimationUtils.loadAnimation(this,
						R.anim.left_in);
				toolsLayout.setAnimation(animLeftIn);

				Animation animRightOut = AnimationUtils.loadAnimation(this,
						R.anim.right_out);
				audioPlayLayout.setAnimation(animRightOut);
				audioPlayLayout.setVisibility(View.GONE);

				// 隐藏小喇叭图标
				editorRecordAudio.setImageResource(R.drawable.editor_audio3);
				editorRecordAudio.setVisibility(View.GONE);

				isClickPlayer = false;
			} else {
				ToastUtil.show(this, "录音文件删除失败");
			}
		}
	}

	/**
	 * 关闭录音播放栏
	 */
	public void closePlayer() {
		// RecordUtil.getInstance().play(recordAudioPath, PLAY_STOP);

		toolsLayout.setVisibility(View.VISIBLE);
		Animation animLeftIn = AnimationUtils.loadAnimation(this,
				R.anim.left_in);
		toolsLayout.setAnimation(animLeftIn);

		Animation animRightOut = AnimationUtils.loadAnimation(this,
				R.anim.right_out);
		audioPlayLayout.setAnimation(animRightOut);
		audioPlayLayout.setVisibility(View.GONE);

		editorRecordAudio.setImageResource(R.drawable.editor_audio3);

		// recordPlaySeekbarUtil.stopSeekBar();
		playUtil.stopPlay();
		isPlaying = false;
		isClickPlayer = false;
	}

	private void saveContent() {
		User user = Account.getUserInfo();
		if( user == null){
			ToastUtil.show(this, "请先登录");
			actionStart(AccountActivity.class);
			return;
		}
		// 没有网络的话保存到本地
		BasicInfoUtil basicInfoUtil = BasicInfoUtil.getInstance(this);
		if (moment == null) {
			moment = new Moment();
		}
		//需要判断一下标题内容是否为空
		String title = articleTitle.getText().toString();
		String content = contentRichEditText.getText().toString();
		if(content == null || content == "" || content.equals("")){
			ToastUtil.show(this, "内容不能为空");
			return ;
		}
		if(StrUtils.isEmpty(title)){
			title = RichEditUtils.extactAbstract(content, 10);
			 if(StrUtils.isEmpty(title)){
				 title = "一张图片";
			 }
		}
			
		moment.setTitle(title);
		moment.setContent(content);
		moment.setAuthorId(1);
		moment.setAuthorName("KDF5000");
		String postTime = TimeFormatUtil.getCurrentDateTime();
		moment.setPostTime(postTime);
		// 用用户id，灵感标题，发布时间作为保存数据库的momentid
		moment.setMomentUid(EncryptUtil.md5(user.getUserId()+"",articleTitle.getText().toString(), postTime).hashCode());

		if (basicInfoUtil.isNetworkConnected()) {// 有网
			moment.setDirty(1);// 本地的标志
			// 获取token
			ApiManager.getInstance().get(this, C.API.GET_QINIU_TOKEN, null,
					new HttpCallBack() {

						@Override
						public void onSuccess(Object res) {
							// TODO Auto-generated method stub
							@SuppressWarnings("unchecked")
							ArrayList<QiniuToken> TokenList = (ArrayList<QiniuToken>) res;
							final String token = TokenList.get(0).getToken();
							// 如果有音频上传音频
							if (recordAudioPath != null) {
								// 上传音频
								QiniuManager.getInstance().uploadFile(
										EditorActivity.this, recordAudioPath,
										"audio", new QiniuRequestCallbBack() {

											@Override
											public void OnFailed(String msg) {
												// TODO Auto-generated method
												// stub
												moment.setDirty(1);
												moment.setAudioUrl(recordAudioPath);
												saveMomentDb(moment);
											}

											@Override
											public void OnComplate(String key) {
												// TODO Auto-generated method
												// stub
												moment.setAudioUrl(C.API.QINIU_BASE_URL+ key);
												uploadFilte2Qiniu(token);
											}
										});
							} else {
								uploadFilte2Qiniu(token);
							}

						}

						@Override
						public void onFailure(Object res) {
							// TODO Auto-generated method stub
							Toast.makeText(EditorActivity.this, (String) res,
									Toast.LENGTH_SHORT).show();
							moment.setDirty(1);
							saveMomentDb(moment);
						}
					}, "QiniuToken");

		} else {
			moment.setDirty(1);
			saveMomentDb(moment);
		}
	}

	/**
	 * 上传文件到七牛
	 */
	private void uploadFilte2Qiniu(String token) {
		Map<String, String> imgMap = RichEditUtils.extractImg(contentRichEditText.getText().toString());
		TaskManager manager = new TaskManager();
		manager.setTaskCallBack(new TaskCallback() {

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(EditorActivity.this, msg, Toast.LENGTH_SHORT).show();
				saveMomentDb(moment);
			}

			@Override
			public void onComplete(Map<String, String> resMap) {
				// TODO Auto-generated method stub
				String content = contentRichEditText.getText().toString();
				for (Map.Entry<String, String> entry : resMap.entrySet()) {
					Log.i("URL","-->" + entry.getKey() + "=" + entry.getValue());
					// 替换et内容
					content = content.replaceAll(entry.getKey(), "<img src = \""
							+ C.API.QINIU_BASE_URL + entry.getValue() + "\"/>");
					// 最好取第一个
					moment.setMomentImgs(C.API.QINIU_BASE_URL + entry.getValue());
				}
				
				// 上传到服务器 上传成功后保存到本地
				moment.setContent(content);
				moment.setDirty(0);
				RequestParams params = new RequestParams();
				User userInfo = Account.getUserInfo();
				if (userInfo == null) {
					actionStart(AccountActivity.class);
				}
				moment.setLabel("大数据");
				params.put("userId", userInfo.getUserId());
				params.put("title", moment.getTitle());
				params.put("content", moment.getContent());
				if(moment.getLabel()==null || moment.getLabel().equals("") || moment.getLabel() == ""){
					moment.setLabel("");
				}
				params.put("label", moment.getLabel());
				params.put("isPublic", moment.getIsOpen());
				if(moment.getMomentId() != 0){
					params.put("momentId",moment.getMomentId());//id为0说明是新增灵感，否则是更新灵感
				}
				if(moment.getMomentImgs() == null){
					moment.setMomentImgs("");
				}
				params.put("momentImgs", moment.getMomentImgs());
				if( moment.getAudioUrl() == null){
					 moment.setAudioUrl("");
				}
				params.put("audioUrl", moment.getAudioUrl());
				params.put("isClipper", 0);
				
				ApiManager.getInstance().post(EditorActivity.this,
						C.API.UPLOAD_MOMENT, params, new HttpCallBack() {

							@Override
							public void onSuccess(Object res) {
								// TODO Auto-generated method stub
								// 保存数据库
								saveMomentDb(moment);
								// 跳回到主页面刷新moment页面的标志
//								SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SWITCH_TO_MOMENT_FG,true);
//								finish();
							}

							@Override
							public void onFailure(Object res) {
								// TODO Auto-generated method stub
								// 保存到本地数据库
								Log.i("EditorActivity", res.toString());
								ToastUtil.show(EditorActivity.this,"网络出错，保存到本地-->" + res.toString());
								moment.setContent(contentRichEditText.getText()
										.toString());
								moment.setDirty(1);
								saveMomentDb(moment);
//								// 跳回到主页面刷新moment页面的标志
//								SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SWITCH_TO_MOMENT_FG,true);
//								finish();
							}
						}, "");
			}
		});
		for (Map.Entry<String, String> entry : imgMap.entrySet()) {
			QiniuTask task = new QiniuTask(EditorActivity.this, manager);
			task.setToken(token);
			task.setKey(entry.getKey());
			task.setLocalPath(entry.getValue());
			manager.addTask(task);
		}
		manager.startTask();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case C.ActivityRequest.REQUEST_DIALOG_ACTIVITY:
				// 确认删除录音
				isConfirmDelete = data.getBooleanExtra("isConfirm", false);
				playerDelete();
				break;
			case C.ActivityRequest.REQUEST_PICTURE_CROP_ACTION: {
				// 添加图片
				ContentResolver resolver = getContentResolver();
				Bitmap originalBitmap = null;
				Uri originalUri = data.getData();
				try {
					originalBitmap = BitmapFactory.decodeStream(resolver
							.openInputStream(originalUri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (originalBitmap != null) {
					contentRichEditText.addImage(originalBitmap,
							getAbsoluteImagePath(originalUri));
				} else {
					Toast.makeText(this, "获取图片失败", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case C.ActivityRequest.REQUEST_SELECT_LABEL:
				List<String> labelList= data.getStringArrayListExtra("labelList");
				String strLabel = "";
				for(int i=0;i<labelList.size();i++){
					if(i==labelList.size()-1){
						strLabel += labelList.get(i);
					}else{
						strLabel += labelList.get(i) +",";
					}
				}
				ToastUtil.show(this, strLabel);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 保存灵感到本地数据库
	 * 
	 * @param moment
	 */
	private void saveMomentDb(Moment moment) {
		if (moment == null) {
			return;
		}
		List<Moment> list = new ArrayList<Moment>();
		list.add(moment);
		saveDbDataAsync(C.DbTaskId.EDITOR_MOMENT_SAVE, DbTaskType.saveOrUpdate,
				Moment.class, list, dbTaskHandler);
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		int taskId = res.what;
		switch (taskId) {
		case C.DbTaskId.EDITOR_MOMENT_SAVE:
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
			// 跳回到主页面刷新moment页面的标志
			SharedPreferencesUtil.getInstance().setBoolean(C.SPKey.SWITCH_TO_MOMENT_FG,true);
			finish();
			break;
		default:
			break;
		}
	}
}
