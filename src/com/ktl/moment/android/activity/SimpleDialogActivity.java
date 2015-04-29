package com.ktl.moment.android.activity;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SimpleDialogActivity extends Activity{
	
	@ViewInject(R.id.dialog_content)
	private TextView dialogContent;

	@ViewInject(R.id.cancle)
	private Button cancle;
	
	@ViewInject(R.id.confirm)
	private Button confirm;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_dialog);
		
		ViewUtils.inject(this);
		
		Intent intent = getIntent();
		CharSequence cs = intent.getStringExtra("content");
		dialogContent.setText(cs);
	}
	
	@OnClick({R.id.cancle, R.id.confirm})
	public void onClick(View v){
		Intent intent = new Intent(SimpleDialogActivity.this, EditorActivity.class);
		switch (v.getId()) {
		case R.id.cancle:
			intent.putExtra("isConfirm", false);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.confirm:
			intent.putExtra("isConfirm", true);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
