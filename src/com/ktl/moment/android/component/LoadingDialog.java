package com.ktl.moment.android.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;

public class LoadingDialog extends Dialog {

	private AnimationDrawable animDraw;
	private ImageView loadingImg;
	private TextView loadingTv;

	public LoadingDialog(Context context) {
		super(context, R.style.dialog_style);
		// TODO Auto-generated constructor stub

		View view = View.inflate(getContext(), R.layout.custom_dialog_loading,
				null);
		setContentView(view);

		loadingImg = (ImageView) findViewById(R.id.loading_img);
		loadingTv = (TextView) findViewById(R.id.loading_tv);

		loadingImg.setImageResource(R.drawable.loading_frame_anim_draw);
		animDraw = (AnimationDrawable) loadingImg.getDrawable();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		animDraw.start();
		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		animDraw.stop();
		super.dismiss();
	}

	public void setText(String str) {
		loadingTv.setText(str + ".....");
	}

}
