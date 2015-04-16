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

public class MomentDialogActivity extends Activity{
	
	@ViewInject(R.id.moment_dialog_edit)
	private Button editBtn;
	
	@ViewInject(R.id.moment_dialog_open)
	private Button openBtn;
	
	@ViewInject(R.id.moment_dialog_share)
	private Button shareBtn;
	
	@ViewInject(R.id.moment_dialog_label)
	private Button labelBtn;
	
	@ViewInject(R.id.moment_dialog_delete)
	private Button deleteBtn;
	
	private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moment_dialog);
		
		ViewUtils.inject(this);
		
		init();
	}
	
	private void init(){
		Intent intent = this.getIntent();
		position = intent.getIntExtra("position", 0);
	}
	
	@OnClick({R.id.moment_dialog_edit,R.id.moment_dialog_open,R.id.moment_dialog_share,R.id.moment_dialog_label,R.id.moment_dialog_delete})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.moment_dialog_edit:
			
			break;
		case R.id.moment_dialog_open:
			open();
			break;
		case R.id.moment_dialog_share:
			
			break;
		case R.id.moment_dialog_label:
			
			break;
		case R.id.moment_dialog_delete:
			
			break;
		default:
			break;
		}
	}
	
	public void open(){
		Intent intent = new Intent(this,HomeActivity.class );
		intent.putExtra("position", position);
		intent.putExtra("isOpen", true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
