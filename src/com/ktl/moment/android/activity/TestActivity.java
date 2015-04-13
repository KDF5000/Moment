package com.ktl.moment.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.component.RichTextView;
import com.ktl.moment.android.component.wheel.HoloWheelActivity;

public class TestActivity extends Activity {
	private Button button;
	private RichTextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_test);
		
		
		button = (Button) findViewById(R.id.button1);
		textView = (RichTextView)findViewById(R.id.textView1);
		String content = getIntent().getStringExtra("data");
		textView.setRichText(content);
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TestActivity.this,
						HoloWheelActivity.class);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(data!=null){
			switch(requestCode){
			case 1:
				String province = data.getStringExtra("provinceText");
				String city = data.getStringExtra("cityText");
				Toast.makeText(this, "省份:" + province + " 城市:" + city,
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}

}
