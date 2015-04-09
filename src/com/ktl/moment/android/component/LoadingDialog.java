package com.ktl.moment.android.component;

import com.ktl.moment.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends Dialog{
	
	private static final int CHANGE_TITLE_WHAT = 1;
	private static final int CHANGE_TITLE_DELAYMILLIS = 300;
	private static final int MAX_SUFFIX_NUMBER = 3;
	private static final char SUFFIX = '.';
	private RotateAnimation mAnim;
	
	private TextView titleTv;
	private TextView suffixTv;
	private ImageView routeImg;
	
	private Handler handler = new Handler(){
		private int num = 0;

		public void handleMessage(Message msg){
			if(msg.what == CHANGE_TITLE_WHAT){
				StringBuilder builder = new StringBuilder();
				if(num >= MAX_SUFFIX_NUMBER){
					num = 0;
				}
				num++;
				for(int i=0;i<num;i++){
					builder.append(SUFFIX);
				}
				suffixTv.setText(builder.toString());		
				if (isShowing()) {
					handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHANGE_TITLE_DELAYMILLIS);
				} else {
					num = 0;
				}
			}
		}
	};

	public LoadingDialog(Context context) {
		super(context,R.style.dialog_style);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		View view = View.inflate(getContext(), R.layout.custom_dialog_loading, null);
		setContentView(view);
		
		titleTv = (TextView) findViewById(R.id.tv);
		suffixTv = (TextView) findViewById(R.id.tv_point);
		routeImg = (ImageView) findViewById(R.id.route_img);
		getWindow().setWindowAnimations(R.anim.dialog_alpha_in);
	}
	
	private void initAnim() {
		mAnim = new RotateAnimation(360, 0,Animation.RESTART, 0.5f, Animation.RESTART,0.5f);
		mAnim.setDuration(1500);
		mAnim.setRepeatCount(Animation.INFINITE);
		mAnim.setRepeatMode(Animation.RESTART);
		mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		initAnim();
		routeImg.startAnimation(mAnim);
		handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
		super.show();
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		mAnim.cancel();
		super.dismiss();
	}
	
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		titleTv.setText(title);
	}
	
	@Override
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		setTitle(getContext().getString(titleId));
	}

}
