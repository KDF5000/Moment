package com.ktl.moment.android.component;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ktl.moment.R;
import com.ktl.moment.common.constant.C;

public class BottomMenu extends RelativeLayout implements OnClickListener {

	private Context mContext;
	private MenuImageText homeItem;
	private MenuImageText friendItem;
	private MenuImageText notificationItem;
	private MenuImageText meItem;
	private LinearLayout menuMainLayout;
	private LinearLayout menuAdd;
	private SparseArray<MenuImageText> menuItemMap;
	private int mCurrentCheckedId;//当前选中的id
	private OnMenuItemClickListener mClickMenu;
	
	
	public void setOnMenuItemClick(OnMenuItemClickListener mClickMenu) {
		this.mClickMenu = mClickMenu;
	}
	public BottomMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		menuMainLayout = (LinearLayout) findViewById(R.id.menu_main);
		menuAdd = (LinearLayout) findViewById(R.id.menu_add);
		homeItem = (MenuImageText) findViewById(R.id.menu_find);
		friendItem = (MenuImageText) findViewById(R.id.menu_foucs);
		notificationItem = (MenuImageText) findViewById(R.id.menu_idea);
		meItem = (MenuImageText) findViewById(R.id.menu_me);
		if(menuItemMap==null){
			menuItemMap = new SparseArray< MenuImageText>();
		}
		menuItemMap.put(R.id.menu_find, homeItem);
		menuItemMap.put(R.id.menu_foucs, friendItem);
		menuItemMap.put(R.id.menu_idea, notificationItem);
		menuItemMap.put(R.id.menu_me, meItem);
		
		setItemOnClickListener();
		super.onFinishInflate();
	}
	/**
	 * 设置监听
	 */
	private void setItemOnClickListener() {
		// TODO Auto-generated method stub
		int count = menuMainLayout.getChildCount();
		Log.i("Menu","-->count:"+count);
		for(int i=0;i< count;i++){
			if(i==2){
				continue;//跳过占位的菜单项
			}
			View v = menuMainLayout.getChildAt(i);
			v.setOnClickListener(this);
		}
		if(menuAdd!=null){
			menuAdd.setOnClickListener(this);
		}
	}
	public void setDefaultCheckedMenu(int id){
		this.mCurrentCheckedId = id;
		MenuImageText item = menuItemMap.get(id);
		item.setChecked();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id==R.id.menu_add){
			if(this.mClickMenu!=null){
				this.mClickMenu.OnClick(getPosition(id));
			}
			return ;
		}
		MenuImageText item = menuItemMap.get(this.mCurrentCheckedId);
		item.setUnCheck();
		
		item = menuItemMap.get(id);
		item.setChecked();
		this.mCurrentCheckedId = id;
		if(this.mClickMenu!=null){
			this.mClickMenu.OnClick(getPosition(id));
		}
	}
	/**
	 * 通过id获取菜单的位置
	 * @param id
	 * @return
	 */
	private int getPosition(int id){
		int position = -1;
		switch(id){
		case R.id.menu_find:
			position = C.menu.FRAGMENT_FIND_MENU_ID;//换成常量
			break;
		case R.id.menu_foucs:
			position = C.menu.FRAGMENT_DYNAMIC_MENU_ID;
			break;
		case R.id.menu_add:
			position = C.menu.FRAGMENT_ADD_MOMENT_MENU_ID;
			break;
		case R.id.menu_idea:
			position = C.menu.FRAGMENT_MOMENT_MENU_ID;
			break;
		case R.id.menu_me:
			position = C.menu.FRAGMENT_ME_MENU_ID;
			break;
		}
		return position;
	}
	public interface OnMenuItemClickListener{
		public void OnClick(int id);
	}
	/**
	 * 点击菜单
	 * @param id
	 */
	@SuppressLint("NewApi")
	public void clickMenuItem(int id){
		int viewId = 0;
		switch(id){
		case C.menu.FRAGMENT_FIND_MENU_ID:
			viewId = R.id.menu_find;//换成常量
			break;
		case C.menu.FRAGMENT_DYNAMIC_MENU_ID:
			viewId = R.id.menu_foucs;
			break;
		case C.menu.FRAGMENT_ADD_MOMENT_MENU_ID:
			viewId = R.id.menu_add;
			break;
		case C.menu.FRAGMENT_MOMENT_MENU_ID:
			viewId = R.id.menu_idea;
			break;
		case C.menu.FRAGMENT_ME_MENU_ID:
			viewId = R.id.menu_me;
			break;
		default:
			viewId = R.id.menu_find;
		}
		MenuImageText menuItem = menuItemMap.get(viewId);
		menuItem.callOnClick();
	}
}
