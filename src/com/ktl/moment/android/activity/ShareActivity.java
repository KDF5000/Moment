package com.ktl.moment.android.activity;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShareActivity extends Activity {

	@ViewInject(R.id.share_content)
	private LinearLayout shareContent;

	@ViewInject(R.id.share_blank_content)
	private View blankContent;

	@ViewInject(R.id.share_cancle)
	private Button shareCancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		ViewUtils.inject(this);
		initAnim();
	}

	private void initAnim() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.share_card_in);
		shareContent.setAnimation(anim);
	}

	@OnClick({ R.id.share_blank_content, R.id.share_cancle })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.share_blank_content:
		case R.id.share_cancle:
			finish();
			break;
		default:
			break;
		}
	}
}
