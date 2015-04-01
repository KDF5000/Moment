package com.ktl.moment.android.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.CustomMenu;
import com.ktl.moment.android.component.CustomMenu.OnMenuItemClickListener;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeAtivity";
	
	private CustomMenu customMenu;
	private ListView listView;
	private List<String> mDatas;
	private int preScrollingDirection = 1;
	private int scrollingDirection = 1;//1 ���� 0������
	private int currentVisibleItem = 0;
	private float touchY;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initData();
		initEvent();
	}
	
	private void initView(){
		getLayoutInflater().inflate(R.layout.activity_home, contentLayout,true);
		
		setHomeTitleVisible(true);
		setTitleTvName(R.string.found_text_view);
		setBaseActivityBgColor(getResources().getColor(R.color.found_background_color));
		customMenu = (CustomMenu)findViewById(R.id.id_menu);
		listView = (ListView) findViewById(R.id.list);
		listView.setBackgroundColor(getResources().getColor(R.color.white));
	}
	private void initData() {
		mDatas = new ArrayList<String>();
		for (int i = 'A'; i < 'Z'; i++) {
			mDatas.add((char) i + "");
		}
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mDatas));
	}
	private void initEvent(){
		
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float currentY = event.getY();
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					touchY = currentY;
					break;
				case MotionEvent.ACTION_MOVE:
					if(currentY < touchY){//˵�������ϻ���
						scrollingDirection = 0;//
					}else{
						scrollingDirection = 1;
					}
					if((scrollingDirection!=preScrollingDirection)){
						customMenu.toggleMenu(800);
						preScrollingDirection = scrollingDirection;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		//һ��Ҫ������Ĵ���ʱ�����---����
		customMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void OnClick(View v, int pos) {
				// TODO Auto-generated method stub
				Toast.makeText(HomeActivity.this, pos+"", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
