package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ktl.moment.R;
import com.ktl.moment.android.adapter.MsgAdapter;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.common.Account;
import com.ktl.moment.entity.Message;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class MsgActivity extends BaseActivity {

	@ViewInject(R.id.msg_input_et)
	private EditText msgInputEt;

	@ViewInject(R.id.msg_send_btn)
	private Button msgSubmitbtn;

	@ViewInject(R.id.msg_listview)
	private ListView msgListview;

	private View footView;
	private View headerView;

	private List<Message> msgList;
	private MsgAdapter msgAdapter;

	private String sendUserName;
	private long sendUserId;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater().inflate(R.layout.activity_msg, contentLayout, true);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		sendUserName = intent.getStringExtra("userName");
		sendUserId = intent.getLongExtra("userId", 0);

		/**
		 * init adapter
		 */
		getData();
		msgAdapter = new MsgAdapter(this, msgList, getDisplayImageOptions());
		msgListview.setAdapter(msgAdapter);

		footView = getLayoutInflater().inflate(R.layout.msg_blank_view, null);
		msgListview.addFooterView(footView);

		headerView = getLayoutInflater().inflate(R.layout.msg_blank_view, null);
		msgListview.addHeaderView(headerView);

		// 设置listview滚动到底部
		msgListview.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				msgListview.setSelection(msgAdapter.getCount() - 1);
			}
		});

		initView();
		addTextChanged();
	}

	private void initView() {
		/**
		 * init title
		 */
		setTitleBackImgVisible(true);
		setTitleBackImg(R.drawable.title_return_white);
		setMiddleTitleVisible(true);
		setMiddleTitleName(sendUserName);
		setBaseActivityBgColor(getResources()
				.getColor(R.color.main_title_color));
	}

	@OnClick({ R.id.title_back_img, R.id.msg_listview, R.id.msg_send_btn })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			finish();
			break;
		case R.id.msg_send_btn:
			sendMsg();
			break;
		case R.id.msg_listview:
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			break;
		default:
			break;
		}
	}

	private void sendMsg() {
		String msgContent = msgInputEt.getText().toString().trim();
		if (!TextUtils.isEmpty(msgContent)) {
			Message msg = new Message();
			msg.setRecieveUserId(sendUserId);
			msg.setSendUserAvatar(Account.getUserInfo().getUserAvatar());
			msg.setMsgContent(msgContent);
			msg.setMsgType(0);
			msgList.add(msg);
			msgAdapter.notifyDataSetChanged();
			msgInputEt.getEditableText().clear();
		}
	}

	private void getData() {
		if (msgList == null) {
			msgList = new ArrayList<Message>();
		}
		for (int i = 0; i < 15; i++) {
			Message msg = new Message();
			msg.setRecieveUserAvatar(Account.getUserInfo().getUserAvatar());
			msg.setSendUserAvatar("http://q.qlogo.cn/qqapp/1104435237/965E0D969A95D4D9C383B44FEBC76A2B/100");
			if (i % 3 == 0) {
				msg.setMsgType(0);
				msg.setMsgContent("隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。隔壁小禹说，10 年前，他就有做叫车服务的想法。");
			} else if (i % 5 == 0) {
				msg.setMsgType(2);
				msg.setSendTime("5-2 15:56");
			} else {
				msg.setMsgType(1);
				msg.setMsgContent("隔壁小禹说");
			}
			msgList.add(msg);
		}
	}

	private void addTextChanged() {
		msgInputEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String msg = msgInputEt.getText().toString().trim();
				if (!TextUtils.isEmpty(msg)) {
					msgSubmitbtn
							.setBackgroundResource(R.drawable.comment_submit_btn);
					msgSubmitbtn.setTextColor(getResources().getColor(
							R.color.white));
				} else {
					msgSubmitbtn
							.setBackgroundResource(R.drawable.msg_send_btn_unable);
					msgSubmitbtn.setTextColor(getResources().getColor(
							R.color.msg_submit_text));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void OnDbTaskComplete(android.os.Message res) {
		// TODO Auto-generated method stub

	}

}
