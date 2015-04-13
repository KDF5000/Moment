package com.ktl.moment.android.activity;

import java.lang.reflect.Field;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.ResizeLayout;
import com.ktl.moment.android.component.ResizeLayout.OnResizeListener;
import com.ktl.moment.android.component.RippleBackground;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.utils.TimerCountUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 灵感编辑
 * @author HUST_LH
 *
 */
@SuppressLint("HandlerLeak")
public class EditorActivity extends BaseActivity{
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
	
	@ViewInject(R.id.editor_edit_area)
	private EditText editText;

	@ViewInject(R.id.editor_tool_content)
	private RelativeLayout toolContent;
	
	@ViewInject(R.id.editor_base_content)
	private ResizeLayout baseContent;
	
	@ViewInject(R.id.editor_wave_content)
	private RippleBackground ripple;
	
	//录音时需要显示的控件
	
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
	
	//父类控件
	@ViewInject(R.id.title_back_img)
	private ImageView titleBackImg;
	
	@ViewInject(R.id.activity_base_title_container_layout)
	private RelativeLayout baseTitleContainer;

	private int appHeight;
	private int baseLayoutHeight;

	private int currentStatus;
	private static final int SHOW_TOOLS = 1;
	private static final int SHOW_KEY_BOARD = 2;
	private static final int RESIZE_LAYOUT = 1;
	
	private boolean flag = false;	//控制何时显示下方tools
	private boolean recordFlag = false;		//标识是否在录音中
	
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

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_editor, contentLayout, true);
		
		init();
		setTitleBackImgVisible(true);
		setBaseActivityBgColor(getResources().getColor(R.color.main_title_color));
		appHeight = getAppHeight();
	}
	
	private void init(){
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
	}
	
	/**
	 * 获取应用显示区域高度。。。
	 * PS:该方法放到工具类使用会报NPE	，怀疑是没有传入activity所致，没有深究
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
		if (id == R.id.editor_keyboard_img || id == R.id.editor_gallery_img) {
			keyboardImg.setImageResource(R.drawable.editor_keyboard_enable_selector);
			flag = false;
			if (currentStatus == SHOW_TOOLS && editText.hasFocus()) {//&& contentLayout.getVisibility() == View.VISIBLE
				showSoftKeyBoard();
			} else {
				setToolsVisible(false);
			}
		} else {
			keyboardImg.setImageResource(R.drawable.editor_keyboard_unable_selector);
			flag = true;
			if (currentStatus == SHOW_KEY_BOARD) {
				showSoftKeyBoard();
			} else {
				setToolsVisible(true);
			}
		}
		if (id == R.id.editor_record_img) {
			recordImg.setImageResource(R.drawable.editor_record_enable_selector);
		} else {
			recordImg.setImageResource(R.drawable.editor_record_unable_selector);
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
	 * @param isShow
	 */
	private void setToolsVisible(boolean isShow) {
		if (toolContent != null) {
			if (isShow && flag) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, appHeight
								- baseLayoutHeight - baseTitleContainer.getHeight());
//				Log.i("height", appHeight+"-"+ baseLayoutHeight+"-"+baseTitleContainer.getHeight());
				toolContent.setLayoutParams(params);
				toolContent.setVisibility(View.VISIBLE);
			} else {
				toolContent.setVisibility(View.GONE);
			}
		}
	}

	@OnClick({R.id.editor_gallery_img,R.id.editor_record_img,R.id.editor_keyboard_img,R.id.editor_edit_complete,R.id.editor_record_big_img,
		R.id.title_back_img,R.id.editor_record_delete,R.id.editor_record_pause,R.id.editor_record_over})
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
			complete();
			break;
		case R.id.editor_record_delete:
			recordDelete();
			break;
		case R.id.editor_record_big_img:
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
		default:
			break;
		}
	}
	
	public void record(){
		showTools(R.id.editor_record_img);
		ripple.setVisibility(View.VISIBLE);
		
		toolsLayout.setVisibility(View.GONE);
		recordLayout.setVisibility(View.VISIBLE);
		
		//开始计时
		TimerCountUtil.getInstance().startTimerCount();
		TimerCountUtil.getInstance().setTextView(editorRecordTimeTv);
		
		editorRecordPause.setImageResource(R.drawable.editor_record_pause);
		ripple.startRippleAnimation();
		
		recordFlag = true;
	}
	
	public void gallery(){
		showTools(R.id.editor_gallery_img);
		ripple.setVisibility(View.GONE);

		//调用系统图库
		Intent getImg = new Intent(Intent.ACTION_GET_CONTENT);
		getImg.addCategory(Intent.CATEGORY_OPENABLE);
		getImg.setType("image/*");
		startActivityForResult(getImg, C.ActivityRequest.REQUEST_PICTURE_CROP_ACTION);
	}
	
	public void keyboard(){
		showTools(R.id.editor_keyboard_img);
		ripple.setVisibility(View.GONE);
	}
	
	public void recordDelete(){
		//清零计时器
		TimerCountUtil.getInstance().clearTimerCount();
		
		editorRecordPause.setImageResource(R.drawable.editor_record_start);
		ripple.stopRippleAnimation();
		recordFlag = false;
	}
	
	public void recordPause(){
		if(recordFlag){
			editorRecordPause.setImageResource(R.drawable.editor_record_start);
			
			//暂停计时
			TimerCountUtil.getInstance().pauseTimerCount();
			
			ripple.stopRippleAnimation();
			
			recordFlag = false;
		}else{
			editorRecordPause.setImageResource(R.drawable.editor_record_pause);
			
			//继续计时
			TimerCountUtil.getInstance().restartTimerCount();
			
			ripple.startRippleAnimation();
			recordFlag = true;
		}
	}
	
	public void recordOver(){
		toolsLayout.setVisibility(View.VISIBLE);
		recordLayout.setVisibility(View.GONE);

		//终止计时
		TimerCountUtil.getInstance().stopTimerCount();
		
		ripple.stopRippleAnimation();
	}
	
	public void complete(){
		
	}
}
