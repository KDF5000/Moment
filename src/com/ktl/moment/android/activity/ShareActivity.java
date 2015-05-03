package com.ktl.moment.android.activity;

import com.ktl.moment.R;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.utils.TencentQQUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShareActivity extends Activity {

	@ViewInject(R.id.share_content)
	private LinearLayout shareContent;

	@ViewInject(R.id.share_blank_content)
	private View blankContent;

	@ViewInject(R.id.share_cancle)
	private Button shareCancle;

	@ViewInject(R.id.share_qq)
	private ImageView shareQQ;

	@ViewInject(R.id.share_zone)
	private ImageView shareZone;

	@ViewInject(R.id.share_wechat_friends)
	private ImageView shareWechatFriends;

	@ViewInject(R.id.share_moments)
	private ImageView shareMoments;

	@ViewInject(R.id.share_weibo)
	private ImageView shareWeibo;

	private Moment moment;
	private TencentQQUtils tencentQQUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		ViewUtils.inject(this);
		initAnim();

		Bundle bundle = this.getIntent().getBundleExtra("share");
		moment = (Moment) bundle.getSerializable("moment");

		tencentQQUtils = new TencentQQUtils(this);
	}

	private void initAnim() {
		Animation animIn = AnimationUtils.loadAnimation(this,
				R.anim.share_card_in);
		shareContent.setAnimation(animIn);
	}

	@OnClick({ R.id.share_blank_content, R.id.share_cancle, R.id.share_qq,
			R.id.share_zone })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.share_blank_content:
		case R.id.share_cancle:
			Animation animOut = AnimationUtils.loadAnimation(this,
					R.anim.share_card_out);
			shareContent.startAnimation(animOut);
			Log.i("tag", "click");
			animOut.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					Log.i("anim", "start");
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					Log.i("anim", "repeat");
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					Log.i("anim", "end");
					finish();
				}
			});
			break;
		case R.id.share_qq:
			tencentQQUtils.shareToQQFriends(moment);
			break;
		case R.id.share_zone:
			tencentQQUtils.shareToQzone(moment);
			break;
		default:
			break;
		}
	}
}
