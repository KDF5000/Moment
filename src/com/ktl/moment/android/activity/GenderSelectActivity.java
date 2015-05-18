package com.ktl.moment.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class GenderSelectActivity extends Activity {

	@ViewInject(R.id.gender_female)
	private RelativeLayout femaleLayout;

	@ViewInject(R.id.gender_male)
	private RelativeLayout maleLayout;

	@ViewInject(R.id.female_img)
	private ImageView femaleImg;

	@ViewInject(R.id.male_img)
	private ImageView maleImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gender_select);

		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		String genderFlag = intent.getStringExtra("gender");

		if (genderFlag.equals("女")) {
			femaleImg.setVisibility(View.VISIBLE);
		} else {
			maleImg.setVisibility(View.VISIBLE);
		}

	}

	@OnClick({ R.id.gender_female, R.id.gender_male })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.gender_male:
			femaleImg.setVisibility(View.GONE);
			maleImg.setVisibility(View.VISIBLE);
			Intent maleIntent = new Intent(this, EditInfoActivity.class);
			maleIntent.putExtra("gender", "男");
			setResult(Activity.RESULT_OK, maleIntent);
			break;
		case R.id.gender_female:
			femaleImg.setVisibility(View.VISIBLE);
			maleImg.setVisibility(View.GONE);
			Intent femaleIntent = new Intent(this, EditInfoActivity.class);
			femaleIntent.putExtra("gender", "女");
			setResult(Activity.RESULT_OK, femaleIntent);
			break;
		default:
			break;
		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
			}
		}, 500);
	}
}
