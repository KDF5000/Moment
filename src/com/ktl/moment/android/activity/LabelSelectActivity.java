package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.component.clickimagespan.TouchClickSpan;
import com.ktl.moment.android.component.clickimagespan.TouchLinkMovementMethod;
import com.ktl.moment.utils.StrUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class LabelSelectActivity extends Activity{

	private static final String TAG = "LabelSelectActivity";
	
	private LabelListAdapter labelListAdapter;
	private List<String> labelList;
	
	@ViewInject(R.id.label_select_input)
	private EditText inputLabelEt;
	
	@ViewInject(R.id.label_list)
	private ListView labelListView;
	
	@ViewInject(R.id.label_select_cancel)
	private Button cancelSelectBt;
	
	@ViewInject(R.id.label_select_confirm)
	private Button confirmSelectBt;
	
	private Map<String,LabelPosition> selectedLabel;
	private String afterInsertEditString = "";
	private Map<Integer,Boolean> checkBoxstate;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_select);
		ViewUtils.inject(this);
		labelList = new ArrayList<String>();
		labelList.add("餐饮");
		labelList.add("O2O");
		labelList.add("移动支付");
		labelList.add("移动APP");
		labelList.add("大数据");
		labelList.add("互联网");
		labelList.add("物联网");
		labelList.add("通信");
		labelList.add("写作平台");
		labelList.add("协作工具");
		labelList.add("小游戏");
		labelList.add("思维导图工具");
		labelList.add("理财工具");
		labelList.add("安全工具");
		checkBoxstate = new HashMap<Integer, Boolean>();
		
		selectedLabel = new HashMap<String,LabelPosition>();
		inputLabelEt.setOnKeyListener(editOnkeyListener);
		Intent intent = getIntent();
		String labelStr = intent.getStringExtra("label");
		recoverLabel(labelStr);
		
		labelListAdapter = new LabelListAdapter(this, labelList,checkBoxstate);
		labelListView.setAdapter(labelListAdapter);
	}
	/**
	 * 还原标签
	 * @param str
	 */
	private void recoverLabel(String str){
		if(StrUtils.isEmpty(str)){
			return ;
		}
		String []labelList = str.split(",");
		if(labelList!=null){
			Log.i(TAG,"label-->"+labelList.length);
			int labelCount = labelList.length;
			for(int i=0;i<labelCount;i++){
				int position = StrUtils.findPositionInList(this.labelList, labelList[i]);
				if(position>=0){
					checkBoxstate.put(position, true);
				}
				insertEditText(labelList[i],position);
			}
		}
	}
	/**
	 * 标签输入框的事件监听
	 */
	private OnKeyListener editOnkeyListener = new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			switch (keyCode) {
			case KeyEvent.KEYCODE_ENTER://回车
				String str = inputLabelEt.getEditableText().toString();
				int newStrLen = str.length() - afterInsertEditString.length();
				int cursorPosition = inputLabelEt.getSelectionStart();
				String newStr = str.substring(cursorPosition-newStrLen,cursorPosition);
				Log.i(TAG, "newStr-->"+newStr.length()+"-->bool"+StrUtils.isEmpty(newStr));
				if(StrUtils.isEmpty(newStr) || newStr=="\n" || newStr.endsWith("\n")){
					break;
				}
//				inputLabelEt.getEditableText().delete(afterInsertEditString.length(),str.length());
				inputLabelEt.getEditableText().delete(cursorPosition-newStr.length(),cursorPosition);
				insertEditText(newStr.trim(), -1);//插入
				updateSelectLabel();
				int listPosition = StrUtils.findPositionInList(labelList, new String(newStr.trim()));
				if(listPosition>=0){//说明存在list中
					labelListAdapter.setCheckboxState(listPosition, true);
				}
				break;
			case KeyEvent.KEYCODE_DEL://删除
				String deleteAfterStr = inputLabelEt.getEditableText().toString();
				//如果删除之后的长度小于之前的说明删除了标签
				if(deleteAfterStr.length() < afterInsertEditString.length()){
					String deleteStr = afterInsertEditString.substring(deleteAfterStr.length());
					List<String> list = StrUtils.extractString("<name>([\\w\\W]+)</name>", deleteStr);
					Log.i(TAG,"-->" + list.size());
					afterInsertEditString = inputLabelEt.getEditableText().toString();
					for(String label:list){
						int position = StrUtils.findPositionInList(labelList, new String(label));
						if(position>=0){//说明存在list中
							labelListAdapter.setCheckboxState(position, false);
						}
					}
				}
				break;
			default:
				break;
			}
			return false;
		}
	};
	@OnClick({R.id.label_select_confirm,R.id.label_select_cancel})
	public void OnClick(View v){
		switch (v.getId()) {
		case R.id.label_select_confirm:
			Spanned s = inputLabelEt.getText();
			ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
			List<String> labelList = new ArrayList<String>();
			for (ImageSpan span : imageSpans) {
				int start = s.getSpanStart(span);
				int end = s.getSpanEnd(span);
				String text = inputLabelEt.getText().toString();
				String name = text.substring(start+6, end-7);
				labelList.add(name);
			}
//			String inputLable = inputLabelEt.getEditableText().toString();
//			List<String> labelList = StrUtils.extractString("<name>([\\W\\w]+)</name>", inputLable);
			Intent intent = new Intent();
			intent.putStringArrayListExtra("labelList", (ArrayList<String>) labelList);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.label_select_cancel:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	public class LabelListAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		private List<String> mLabelList;
		
		private Context mContext;
		private Map<Integer,Boolean> checkBoxState;
		
		private Map<Integer,CheckBox> checkBoxMap = new HashMap<Integer, CheckBox>();
		
		public LabelListAdapter(Context context,List<String> labelList,Map<Integer,Boolean> checkBoxState) {
			// TODO Auto-generated constructor stub
			this.mInflater = LayoutInflater.from(context);
			this.mContext = context;
			this.mLabelList = labelList;
			this.checkBoxState = checkBoxState;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.mLabelList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.mLabelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		/**
		 * 获取指定位置的checkbox
		 * @param position
		 * @return
		 */
		public CheckBox getChechBox(int position){
			return checkBoxMap.get(position);
		}
		/**
		 * 设置checkbox的状态
		 * @param position
		 * @param state
		 */
		public void setCheckboxState(int position,boolean state){
			CheckBox box = checkBoxMap.get(position);
			if(box!=null){
				box.setChecked(state);
				if(state==false){
					if(checkBoxState.containsKey(position)){
						checkBoxState.remove(position);
					}else{
						checkBoxState.put(position, true);
					}
				}
			}
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				convertView = this.mInflater.inflate(R.layout.select_label_listview_item, null);
				holder = new ViewHolder();
				holder.labelTv = (TextView) convertView.findViewById(R.id.label_textview);
				holder.labelCheckBox = (CheckBox) convertView.findViewById(R.id.label_checkbox);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final int mPosition = position;
			String label = mLabelList.get(position);
			holder.labelTv.setText(label);
			holder.labelCheckBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String label = mLabelList.get(mPosition);
					if(!checkBoxState.containsKey(mPosition)){
						checkBoxState.put(mPosition,true);
						insertEditText(label,mPosition);
					}else{
						//移除标签
						removeEdittextLabel(label,mPosition);
						checkBoxState.remove(mPosition);
					}
				}
			});
//			holder.labelCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					// TODO Auto-generated method stub
//					String label = mLabelList.get(mPosition);
//					if(isChecked == true){
//						checkBoxState.put(mPosition,true);
//						insertEditText(label,mPosition);
//					}else{
//						//移除标签
//						removeEdittextLabel(label,mPosition);
//						checkBoxState.remove(mPosition);
//					}
//				}
//			});
			if(checkBoxState.containsKey(mPosition)){
				holder.labelCheckBox.setChecked(true);
			}else{
				holder.labelCheckBox.setChecked(false);
			}
			checkBoxMap.put(mPosition, holder.labelCheckBox);
			return convertView;
		}
		
		private class ViewHolder{
			TextView labelTv;
			CheckBox labelCheckBox;
		}
	}
	/**
	 * 移除标签
	 * @param str
	 * @param listPosition
	 */
	private void removeEdittextLabel(String str,int listPosition){
		LabelPosition labelLosition = selectedLabel.get(str);
		if(labelLosition==null){
			return ;
		}
		Editable edt = inputLabelEt.getEditableText();
		edt.delete(labelLosition.getmSpanStart(), labelLosition.getmSpanEnd());
		inputLabelEt.setText(edt);
		selectedLabel.remove(str);
		updateSelectLabel();
		if(listPosition>=0){
			labelListAdapter.setCheckboxState(listPosition, false);
		}
		afterInsertEditString = inputLabelEt.getEditableText().toString();
	}
	/**
	 * 获取图片并插入EditText
	 * @param str
	 * @param listPosition
	 */
	private void insertEditText(String str,int listPosition) {
		Bitmap imgBitmap = StrUtils.createBitmap(str);
		Bitmap roundBitmap = StrUtils.GetRoundedCornerBitmap(imgBitmap);
		// imageView.setImageBitmap(roundBitmap);
		if (imgBitmap != null) {
			// 根据Bitmap对象创建ImageSpan对象
			ImageSpan imageSpan = new ImageSpan(this, roundBitmap);
			// 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
			SpannableString spannableString = new SpannableString("<name>"
					+ str + "</name>");
			// 用ImageSpan对象替换face
			spannableString.setSpan(imageSpan, 0,
					("<name>" + str + "</name>").length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// 将选择的图片追加到EditText中光标所在位置
			int index = inputLabelEt.getSelectionStart(); // 获取光标所在位置
			Editable edit_text = inputLabelEt.getEditableText();
			if (index < 0 || index >= edit_text.length()) {
				edit_text.append(spannableString);
			} else {
//				edit_text.insert(edit_text.length(), spannableString);
				edit_text.insert(index, spannableString);
			}
			setSpanClickable(listPosition);
			//插入到map
			int start = edit_text.getSpanStart(imageSpan);
			int end = edit_text.getSpanEnd(imageSpan);
			LabelPosition labelPosition = new LabelPosition(start,end, listPosition);
			selectedLabel.put(str, labelPosition);
			inputLabelEt.getEditableText().insert(inputLabelEt.getSelectionStart(), " ");
			afterInsertEditString = inputLabelEt.getEditableText().toString();
//			if(StrUtils.findPositionInList(labelList, str)>=0){
//				labelListAdapter.setCheckboxState(listPosition, true);
//			}
		} else {
			Log.i("MainActivity", "插入失败");
		}
	}
	/**
	 * 更新保存的label位置
	 */
	private void updateSelectLabel(){
		Spanned s = inputLabelEt.getText();
		// setMovementMethod很重要，不然ClickableSpan无法获取点击事件。
		inputLabelEt.setMovementMethod(TouchLinkMovementMethod.getInstance());
		ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
		for (ImageSpan span : imageSpans) {
			int start = s.getSpanStart(span);
			int end = s.getSpanEnd(span);
			String text = inputLabelEt.getText().toString();
			String name = text.substring(start+6, end-7);
//			Toast.makeText(this, name+"-->"+start+"-->"+end, Toast.LENGTH_SHORT).show();
			LabelPosition labelPosition = selectedLabel.get(name);
			labelPosition.setmSpanStart(start);
			labelPosition.setmSpanEnd(end);
			selectedLabel.put(name, labelPosition);
		}
	}
	/**
	 * 设置可以点击
	 */
	private void setSpanClickable( int listPosition) {
		// 此方法比较靠谱
		Spanned s = inputLabelEt.getText();
		// setMovementMethod很重要，不然ClickableSpan无法获取点击事件。
		inputLabelEt.setMovementMethod(TouchLinkMovementMethod.getInstance());
		ImageSpan[] imageSpans = s.getSpans(0, s.length(), ImageSpan.class);
		for (final ImageSpan span : imageSpans) {
			int start = s.getSpanStart(span);
			int end = s.getSpanEnd(span);
			TouchClickSpan click_span = new TouchClickSpan() {

				@Override
				public void onClick(View widget, MotionEvent e) {
					// TODO Auto-generated method stub
					inputLabelEt.setCursorVisible(false);
//					Toast.makeText(LabelSelectActivity.this, e.getY() + "-->",
//							Toast.LENGTH_SHORT).show();
					Spanned s = inputLabelEt.getText();
					int start = s.getSpanStart(span);
					int end = s.getSpanEnd(span);
					String text = inputLabelEt.getText().toString();
					String name = text.substring(start+6, end-7);
					LabelPosition position = selectedLabel.get(name);
					removeEdittextLabel(name, position.getmListPosition());
				}
			};

			TouchClickSpan[] click_spans = s.getSpans(start, end,
					TouchClickSpan.class);
			if (click_spans.length != 0) {
				// remove all click spans
				for (TouchClickSpan c_span : click_spans) {
					((Spannable) s).removeSpan(c_span);
				}
			}

			((Spannable) s).setSpan(click_span, start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
	}
	
	private class LabelPosition{
		private int mSpanStart;
		private int mSpanEnd;
		private int mListPosition;
		
		public LabelPosition(int spanStart,int spanEnd,int listPosition){
			this.mSpanStart = spanStart;
			this.mSpanEnd = spanEnd;
			this.mListPosition = listPosition;
		}

		public int getmSpanStart() {
			return mSpanStart;
		}

		public void setmSpanStart(int mSpanStart) {
			this.mSpanStart = mSpanStart;
		}

		public int getmSpanEnd() {
			return mSpanEnd;
		}

		public void setmSpanEnd(int mSpanEnd) {
			this.mSpanEnd = mSpanEnd;
		}

		public int getmListPosition() {
			return mListPosition;
		}

		public void setmListPosition(int mListPosition) {
			this.mListPosition = mListPosition;
		}
		
	}
}
