package com.ktl.moment.android.activity;

import java.util.Calendar;
import java.util.Date;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class CustomDatePicker extends Activity {

	@ViewInject(R.id.date_picker)
	private DatePicker datePicker;

	@ViewInject(R.id.date_picker_cancel)
	private Button datePickerCancle;

	@ViewInject(R.id.date_picker_confirm)
	private Button datePickerConfirm;

	private String birthday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_picker);

		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		int year = intent.getIntExtra("year",
				Calendar.getInstance().get(Calendar.YEAR));
		int month = intent.getIntExtra("month",
				Calendar.getInstance().get(Calendar.MONTH)+1);
		int day = intent.getIntExtra("day",
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		datePicker.setMaxDate(new Date().getTime());
		datePicker.init(year, month-1, day, new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				int birthdayYear = year;
				int birthdayMonth = monthOfYear+1;
				int birthdayDay = dayOfMonth;
				
				String strMonth = birthdayMonth+"";
				String strDay = birthdayDay+"";
				if(birthdayDay<10){
					strDay = "0"+strDay;
				}
				if(birthdayMonth<10){
					strMonth = "0"+strMonth;
				}
				birthday = birthdayYear+"-"+strMonth+"-"+strDay;
			}
		});
	}

	@OnClick({ R.id.date_picker_cancel, R.id.date_picker_confirm })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.date_picker_cancel:
			finish();
			break;
		case R.id.date_picker_confirm:
			Intent intent = new Intent(this, EditInfoActivity.class);
			intent.putExtra("birthday", birthday);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
