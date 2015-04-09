package com.ktl.moment.android.activity;

import java.io.File;
import java.util.Calendar;

import com.ktl.moment.R;
import com.ktl.moment.common.constant.C;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CameraSelectActivity extends Activity {
	private Button camera;
	private Button pick;
	private Button cancel;

	private Uri mPicUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_select_activity_layout);
		camera = (Button) findViewById(R.id.camera);
		pick = (Button) findViewById(R.id.pick);
		cancel = (Button) findViewById(R.id.cancel);
		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				long time = Calendar.getInstance().getTimeInMillis();
				mPicUri = Uri.fromFile(new File(Environment
						.getExternalStorageDirectory().getAbsolutePath() + "",
						time + ".jpg"));
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
						mPicUri);
				startActivityForResult(intent, C.ActivityRequest.QEQUEST_CAMERA_ACTION);
			}
		});

		//
		pick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, C.ActivityRequest.REQUEST_PICK_ALBUM_ACTION);
			}
		});

		//
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void startCutPicture() {
		Intent intent = new Intent();
		intent.setAction("com.android.camera.action.CROP");
		intent.setDataAndType(mPicUri, "image/*");// mUri是已经选择的图片Uri
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 250);// 输出图片大小
		intent.putExtra("outputY", 250);
		intent.putExtra("return-data", false);
		long time = Calendar.getInstance().getTimeInMillis();
		mPicUri = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory().getAbsolutePath() + "",
				time + ".jpg"));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mPicUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, C.ActivityRequest.REQUEST_PICTURE_CROP_ACTION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case C.ActivityRequest.QEQUEST_CAMERA_ACTION:
				if (data == null) {
					startCutPicture();
				}
				break;
			case C.ActivityRequest.REQUEST_PICK_ALBUM_ACTION:
				mPicUri = data.getData();
				startCutPicture();
				break;
			case C.ActivityRequest.REQUEST_PICTURE_CROP_ACTION:
				Intent intent = new Intent();
				intent.putExtra("cropPicUri", mPicUri);
				setResult(RESULT_OK, intent);
				finish();
				break;
			}

		} else {
			//
		}

	}
}
