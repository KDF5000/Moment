package com.ktl.moment.android.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.RichTextView;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.utils.PlayUtil;
import com.ktl.moment.utils.TimerCountUtil;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.db.DbTaskHandler;
import com.ktl.moment.utils.db.DbTaskType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ReadActivity extends BaseActivity {

	@ViewInject(R.id.read_edit)
	private ImageView readEdit;

	@ViewInject(R.id.read_content_tv)
	private RichTextView content;

	@ViewInject(R.id.read_title_tv)
	private TextView title;

	@ViewInject(R.id.read_audio_layout)
	private LinearLayout audioLayout;

	@ViewInject(R.id.read_play_seekbar)
	private SeekBar seekbar;

	@ViewInject(R.id.read_play_status)
	private TextView playStatus;

	@ViewInject(R.id.read_play_time)
	private TextView playTime;

	@ViewInject(R.id.read_play_start)
	private ImageView playStart;

	private Moment momentDetail;
	private long momentUid;
	private DbTaskHandler dbTaskHandler = new DbTaskHandler(this);

	private PlayUtil play;
	private String audioUrl;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater()
				.inflate(R.layout.activity_read, contentLayout, true);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		momentUid = intent.getLongExtra("momentUid", 0);

		initView();
		getData();
	}

	private void initView() {
		setTitleBackImg(R.drawable.title_return_black);
		setTitleBackImgVisible(true);
		setTitleRightImgLeftVisible(true);
		setTitleRightImgVisible(true);
		setTitleRightImg(R.drawable.editor_open_enable);
		setBaseActivityBgColor(getResources().getColor(
				R.color.editor_main_color));

	}

	@OnClick({ R.id.read_edit, R.id.title_back_img, R.id.title_right_img_left,
			R.id.read_play_start })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			if(play != null){
				play.stopPlay();
			}
			finish();
			break;
		case R.id.read_edit:
			if (momentDetail == null) {
				ToastUtil.show(this, "获取详情失败");
				break;
			}
			if(play != null){
				play.stopPlay();
			}
			Intent editIntent = new Intent(this, EditorActivity.class);
			editIntent.putExtra("moment", momentDetail);
			startActivity(editIntent);
//			finish();
			break;
		case R.id.title_right_img_left:
			Intent commentIntent = new Intent(this, MomentCommentActivity.class);
			commentIntent.putExtra("momentId", momentDetail.getMomentId());
			startActivity(commentIntent);
			break;
		case R.id.read_play_start:
			if (!playStatus.getText().equals("加载中")) {
				play();
			}
			break;
		default:
			break;
		}
	}

	private void getData() {
		getDbData(C.DbTaskId.GET_MOMENT_DETAIL, DbTaskType.findByCondition,
				Moment.class, WhereBuilder.b("momentUid", "=", momentUid));
	}

	/**
	 * 
	 * @param taskId
	 * @param taskType
	 * @param entityType
	 */
	public void getDbData(int taskId, DbTaskType taskType, Class<?> entityType,
			WhereBuilder whereBuilder) {
		// 可以根据不同的任务 设置不同的tasktype
		getDbDataAsync(taskId, taskType, entityType, whereBuilder,
				dbTaskHandler);
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<Moment> list = (List<Moment>) res.obj;
		if (list == null || list.isEmpty()) {
			ToastUtil.show(this, "获取详情失败");
			return;
		}
		momentDetail = list.get(0);
		title.setText(momentDetail.getTitle());
		content.setRichText(momentDetail.getContent());

		if (momentDetail.getIsOpen() == 0) {
			titleRightImg.setImageResource(R.drawable.editor_open_enable);
		} else {
			titleRightImg.setImageResource(R.drawable.editor_open_unable);
		}
		audioUrl = momentDetail.getAudioUrl();
		if (!audioUrl.equals("") && audioUrl != null) {
			audioLayout.setVisibility(View.VISIBLE);
			seekbar.setEnabled(false);
			play = new PlayUtil(seekbar, handler, audioUrl, playStatus,
					playTime);
			loadAudio();
		}
	}

	private void loadAudio() {
		seekbar.setEnabled(false);
		play = new PlayUtil(seekbar, handler, audioUrl, playStatus, playTime);
		// 开启新的线程从网络加载音频数据
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				play.initPlayer();
			}
		}).start();

		TimerCountUtil.getInstance().setTextView(playStatus);

		// playStart.setClickable(true);
	}

	/**
	 * 用于通知主线程更新播放界面UI
	 */
	private boolean isPlaying = false;
	private boolean isReplay = false;
	private boolean isPause = false;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				playStart
						.setImageResource(R.drawable.editor_record_replay_selector);
				play.stopSeekbar();
				isPlaying = false;
				isReplay = true;
				break;
			}
			}
			super.handleMessage(msg);
		};
	};

	private void play() {
		if (!isPlaying && !isPause) { // 没有播放,那就播放
			play.startPlay();
			playStart.setImageResource(R.drawable.editor_record_pause);

			isPlaying = true;
			isPause = false;
			Log.i("tag", "start");
		} else if (isPlaying && !isPause) { // 没有暂停，那就暂停
			play.pausePlay();
			playStart.setImageResource(R.drawable.editor_record_start);
			isPlaying = false;
			isPause = true;
			Log.i("tag", "pause");
		} else if (isPause) {
			play.continuePlay();
			playStart.setImageResource(R.drawable.editor_record_pause);
			isPlaying = true;
			isPause = false;
			Log.i("tag", "continue");
		} else if (!isPlaying && isReplay) {
			// play.startPlay();
			play.startPlay();
			playStart.setImageResource(R.drawable.editor_record_pause);

			playStart.setImageResource(R.drawable.editor_record_pause);
			isPlaying = true;
			isReplay = false;
			Log.i("tag", "replay");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
}
