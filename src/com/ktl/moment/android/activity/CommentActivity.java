package com.ktl.moment.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.common.constant.C;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class CommentActivity extends BaseActivity{
	
	@ViewInject(R.id.comment_et)
	private EditText commentEt;
	
	@ViewInject(R.id.comment_submit)
	private Button commentSubmit;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_comment, contentLayout, true);
		
		ViewUtils.inject(this);
		initView();
	}
	
	private void initView(){
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		setMiddleTitleName("评论");
		setBaseActivityBgColor(getResources().getColor(R.color.main_title_color));
	}
	
	@OnClick({R.id.title_back_img,R.id.comment_submit})
	public void click(View v){
		switch (v.getId()) {
		case R.id.comment_submit:
			submit();
			break;
		case R.id.title_back_img:
			back();
			break;
		default:
			break;
		}
	}
	
	private void submit(){
		
	}
	
	private void back(){
		String comment = commentEt.getText().toString().trim();
		if(!comment.equals("")){
			Intent intent = new Intent(this, SimpleDialogActivity.class);
			intent.putExtra("content", "确认放弃评论？");
			startActivityForResult(intent, C.ActivityRequest.REQUEST_DIALOG_ACTIVITY);
		}else{
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case C.ActivityRequest.REQUEST_DIALOG_ACTIVITY:
				boolean isConfirm = data.getBooleanExtra("isConfirm", false);
				if(isConfirm){
					finish();
				}
				break;

			default:
				break;
			}
		}
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		
	}
}
