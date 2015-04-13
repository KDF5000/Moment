package com.ktl.moment.android.activity;

import com.ktl.moment.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class UserPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle saveInstanceBundle) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceBundle);
		setContentView(R.layout.activity_user_page);
		long userId = getIntent().getLongExtra("userId", 0);
		Toast.makeText(UserPageActivity.this, userId + "", Toast.LENGTH_SHORT)
				.show();
	}
}
