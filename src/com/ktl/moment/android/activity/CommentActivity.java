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
import com.ktl.moment.common.Account;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.infrastructure.HttpCallBack;
import com.ktl.moment.utils.ToastUtil;
import com.ktl.moment.utils.net.ApiManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.RequestParams;

public class CommentActivity extends BaseActivity {

	@ViewInject(R.id.comment_et)
	private EditText commentEt;

	@ViewInject(R.id.comment_submit)
	private Button commentSubmit;

	private long momentId;
	private long repalyUserId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_comment, contentLayout,
				true);

		ViewUtils.inject(this);
		initView();

		Intent intent = this.getIntent();
		momentId = intent.getLongExtra("momentId", 0);
		repalyUserId = intent.getLongExtra("repalyUserId", 0);
	}

	private void initView() {
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		setMiddleTitleName("评论");
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
	}

	@OnClick({ R.id.title_back_img, R.id.comment_submit })
	public void click(View v) {
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

	private void submit() {
		String comment = commentEt.getText().toString().trim();
		if (comment.equals("") || comment.isEmpty()) {
			ToastUtil.show(this, "请输入评论内容");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("userId", Account.getUserInfo().getUserId());
		params.put("momentId", momentId);
		params.put("repalyUserId", repalyUserId);
		params.put("content", comment);
		ApiManager.getInstance().post(this, C.API.COMMENT_MOMENT, params,
				new HttpCallBack() {

					@Override
					public void onSuccess(Object res) {
						// TODO Auto-generated method stub
						// 回传评论数据
						Intent intent = new Intent(CommentActivity.this,
								MomentDetailActivity.class);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}

					@Override
					public void onFailure(Object res) {
						// TODO Auto-generated method stub

					}
				}, "Comment");
	}

	private void back() {
		String comment = commentEt.getText().toString().trim();
		if (!comment.equals("")) {
			Intent intent = new Intent(this, SimpleDialogActivity.class);
			intent.putExtra("content", "确认放弃评论？");
			startActivityForResult(intent,
					C.ActivityRequest.REQUEST_DIALOG_ACTIVITY);
		} else {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case C.ActivityRequest.REQUEST_DIALOG_ACTIVITY:
				boolean isConfirm = data.getBooleanExtra("isConfirm", false);
				if (isConfirm) {
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
