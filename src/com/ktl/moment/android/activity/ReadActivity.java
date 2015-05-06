package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.RichTextView;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.entity.Moment;
import com.ktl.moment.utils.db.DbTaskHandler;
import com.ktl.moment.utils.db.DbTaskType;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ReadActivity extends BaseActivity {

	@ViewInject(R.id.read_edit)
	private ImageView readEdit;

	@ViewInject(R.id.read_content_tv)
	private RichTextView content;

	@ViewInject(R.id.read_title_tv)
	private TextView title;

	private List<Moment> momentList;
	private long momentUid;
	private DbTaskHandler dbTaskHandler = new DbTaskHandler(this);

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getLayoutInflater()
				.inflate(R.layout.activity_read, contentLayout, true);
		ViewUtils.inject(this);

		Intent intent = this.getIntent();
		momentUid = intent.getLongExtra("momentUid", 0);

		initView();
		getData();
	}

	private void initView() {
		setTitleBackImg(R.drawable.title_return_black);
		setTitleBackImgVisible(true);
		setTitleRightImgLeftVisible(true);
		setTitleRightImgVisible(true);
		setTitleRightImg(R.drawable.editor_open_enable);
		setBaseActivityBgColor(getResources().getColor(
				R.color.editor_main_color));

	}

	@OnClick({ R.id.read_edit, R.id.title_back_img, R.id.title_right_img_left })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.title_back_img:
			finish();
			break;
		case R.id.read_edit:
			Intent editIntent = new Intent(this, EditorActivity.class);
			startActivity(editIntent);
			break;
		case R.id.title_right_img_left:
			Intent detailIntent = new Intent(this, MomentDetailActivity.class);
			startActivity(detailIntent);
			break;
		default:
			break;
		}
	}

	private void getData() {
		getDbData(C.DbTaskId.GET_MOMENT_DETAIL, DbTaskType.findByCondition,
				Moment.class, WhereBuilder.b("momentUid", "=", momentUid));
	}

	/**
	 * 
	 * @param taskId
	 * @param taskType
	 * @param entityType
	 */
	public void getDbData(int taskId, DbTaskType taskType, Class<?> entityType,
			WhereBuilder whereBuilder) {
		// 可以根据不同的任务 设置不同的tasktype
		getDbDataAsync(taskId, taskType, entityType, whereBuilder,
				dbTaskHandler);
	}

	@Override
	public void OnDbTaskComplete(Message res) {
		// TODO Auto-generated method stub
		momentList = new ArrayList<Moment>();
		@SuppressWarnings("unchecked")
		List<Moment> list = (List<Moment>) res.obj;
		momentList.addAll(list);
		title.setText(momentList.get(0).getTitle());
		content.setText(momentList.get(0).getContent());
	}

}
