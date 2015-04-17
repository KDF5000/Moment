package com.ktl.moment.android.activity;

import com.ktl.moment.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class EditorDialogActivity extends Activity{

	@ViewInject(R.id.editor_play_delete_cancle)
	private Button cancle;
	
	@ViewInject(R.id.editor_play_delete_confirm)
	private Button confirm;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_dialog);
		
		ViewUtils.inject(this);
	}
	
	@OnClick({R.id.editor_play_delete_cancle, R.id.editor_play_delete_confirm})
	public void onClick(View v){
		Intent intent = new Intent(EditorDialogActivity.this, EditorActivity.class);
		switch (v.getId()) {
		case R.id.editor_play_delete_cancle:
			intent.putExtra("isDelete", false);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.editor_play_delete_confirm:
			intent.putExtra("isDelete", true);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
