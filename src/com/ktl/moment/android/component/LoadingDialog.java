package com.ktl.moment.android.component;

import com.ktl.moment.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingDialog extends Dialog {

	private ImageView mediaImg;
	private Animation anim;

	public LoadingDialog(Context context) {
		super(context, R.style.dialog_style);
		// TODO Auto-generated constructor stub

		View view = View.inflate(getContext(), R.layout.loading_dialog, null);
		setContentView(view);

		mediaImg = (ImageView) findViewById(R.id.loading_media);
		anim = AnimationUtils.loadAnimation(context, R.anim.loading_media_anim);
		mediaImg.startAnimation(anim);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		anim.start();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
}
