package com.ktl.moment.android.activity;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.CircleImageView;
import com.ktl.moment.android.component.wheel.HoloWheelActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.EditTextUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.loopj.android.http.RequestParams;

public class EditInfoActivity extends BaseActivity {

	@ViewInject(R.id.edit_avatar)
	private CircleImageView editAvatar;

	@ViewInject(R.id.edit_nickname)
	private EditText editNickname;

	@ViewInject(R.id.edit_sex)
	private EditText editSex;

	@ViewInject(R.id.edit_area)
	private TextView editArea;

	@ViewInject(R.id.edit_birthday)
	private TextView editBirthday;

	@ViewInject(R.id.edit_signature)
	private EditText editSignature;

	@ViewInject(R.id.edit_base_layout)
	private LinearLayout editBaseLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_edit_info, contentLayout,
				true);

		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		setMiddleTitleName("编辑资料");
		setTitleRightTvVisible(true);
		setTitleRightTv("完成");
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));

		EditTextUtil.setEditable(editNickname, false);
		EditTextUtil.setEditable(editSex, false);
		EditTextUtil.setEditable(editSignature, false);
	}

	@OnClick({ R.id.title_back_img, R.id.title_right_tv, R.id.edit_area,
			R.id.edit_avatar, R.id.edit_nickname, R.id.edit_sex,
			R.id.edit_signature, R.id.edit_birthday })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			finish();
			break;
		case R.id.title_right_tv:
			complete();
			break;
		case R.id.edit_avatar:
			editAvatar();
			break;
		case R.id.edit_nickname:
			editNickname();
			break;
		case R.id.edit_sex:
			editSex();
			break;
		case R.id.edit_area:
			editArea();
			break;
		case R.id.edit_signature:
			editSignature();
			break;
		case R.id.edit_birthday:
			editBirthday();
			break;
		default:
			break;
		}
	}

	@OnTouch({ R.id.edit_base_layout })
	public boolean onTouch(View v, MotionEvent e) {
		EditTextUtil.setEditable(editNickname, false);
		EditTextUtil.setEditable(editSex, false);
		EditTextUtil.setEditable(editSignature, false);
		/**
		 * 隐藏软键盘
		 */
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			return imm.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		} catch (NullPointerException error) {
			// TODO: handle exception
			return false;
		}
	}

	private void editAvatar() {
		Intent cameraIntent = new Intent(this, CameraSelectActivity.class);
		startActivityForResult(cameraIntent,
				C.ActivityRequest.REQUEST_SELECT_CAMERA_ACTIVITY);
	}

	private void editNickname() {
		EditTextUtil.setEditable(editNickname, true);
	}

	private void editSex() {
		EditTextUtil.setEditable(editSex, true);
	}

	private void editSignature() {
		EditTextUtil.setEditable(editSignature, true);
	}

	private void editArea() {
		Intent intent = new Intent(this, HoloWheelActivity.class);
		startActivityForResult(intent,
				C.ActivityRequest.REQUEST_SELECT_CITY_ACTIVITY);

	}

	private void editBirthday() {
		String birthday = editBirthday.getText().toString().trim();
		String[] dateArray = birthday.split("-");
		int year = Integer.parseInt(dateArray[0]);
		int month = Integer.parseInt(dateArray[1]);
		int day = Integer.parseInt(dateArray[2]);
		Log.i("date", year+"-"+month+"-"+day);
		Intent intent = new Intent(this, CustomDatePicker.class);
		intent.putExtra("year", year);
		intent.putExtra("month", month);
		intent.putExtra("day", day);
		startActivityForResult(intent,
				C.ActivityRequest.REQUEST_DATE_PICKER_ACTIVITY);
	}

	private void complete() {
		String avatar = "";// 七牛地址
		String nickname = editNickname.getText().toString().trim();
		String tmpSex = editNickname.getText().toString().trim();
		String area = editArea.getText().toString().trim();
		String birthday = editBirthday.getText().toString().trim();
		String signature = editSignature.getText().toString().trim();
		int sex = 0;
		if (tmpSex.equals("男")) {
			sex = 1;
		} else {
			sex = 0;
		}

		RequestParams params = new RequestParams();
		params.put("avatar", avatar);
		params.put("nickname", nickname);
		params.put("sex", sex);
		params.put("area", area);
		params.put("birthday", birthday);
		params.put("signature", signature);
		ApiManager.getInstance().post(this, C.API.UPDATE_USER_INFO, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						finish();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "User");

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.i("tag", "1");
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case C.ActivityRequest.REQUEST_SELECT_CITY_ACTIVITY:
				String province = data.getStringExtra("provinceText");
				String city = data.getStringExtra("cityText");
				editArea.setText(province + " " + city);
				break;
			case C.ActivityRequest.REQUEST_SELECT_CAMERA_ACTIVITY:
				Uri cropUri = data.getParcelableExtra("cropPicUri");
				try {
					Bitmap bmp = MediaStore.Images.Media.getBitmap(
							this.getContentResolver(), cropUri);
					editAvatar.setImageBitmap(bmp);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case C.ActivityRequest.REQUEST_DATE_PICKER_ACTIVITY:
				Log.i("tag", "2");
				String birthday = data.getStringExtra("birthday");
				Log.i("birthday", birthday);
				editBirthday.setText(birthday);
				break;
			}
		}
	}
}
