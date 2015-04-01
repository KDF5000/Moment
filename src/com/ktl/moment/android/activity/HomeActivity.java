package com.ktl.moment.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.base.BaseFragment;
import com.ktl.moment.android.component.CustomMenu;
import com.ktl.moment.android.component.CustomMenu.OnMenuItemClickListener;
import com.ktl.moment.android.fragment.FindFragment;
import com.ktl.moment.common.constant.C;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeAtivity";

	private CustomMenu customMenu;// 菜单
	private FragmentManager fragmentManager;// 管理器
	private FragmentTransaction fragmentTransaction;// fragment事务
	private String currentFgTag = "";//当前菜单的tag

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initEvent();
		fragmentManager = getSupportFragmentManager();//获取fragment的管理器
		
		//初始为发现界面
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_content_container, new FindFragment()).commit();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		getLayoutInflater()
				.inflate(R.layout.activity_home, contentLayout, true);

		setHomeTitleVisible(true);
		setTitleTvName(R.string.found_text_view);
		setBaseActivityBgColor(getResources().getColor(
				R.color.found_background_color));
		customMenu = (CustomMenu) findViewById(R.id.id_menu);
	}

	/**
	 * 初始化一些事件
	 */
	private void initEvent() {

		customMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void OnClick(View v, int pos) {
				// TODO Auto-generated method stub
				switch (pos) {
				case C.menu.FRAGMENT_FIND_MENU_ID:
					currentFgTag =  C.menu.FRAGMENT_FIND_TAG;
					break;
				case C.menu.FRAGMENT_DYNAMIC_MENU_ID:
					currentFgTag = C.menu.FRAGMENT_DYNAMIC_TAG;
					break;
				case C.menu.FRAGMENT_MOMENT_MENU_ID:
					currentFgTag = C.menu.FRAGMENT_MOMENT_TAG;
					break;
				case C.menu.FRAGMENT_ME_MENU_ID:
					currentFgTag = C.menu.FRAGMENT_ME_TAG;
					break;
				}
				switchMenuByTag(currentFgTag);
			}
		});
	}

	private void switchMenuByTag(String selectedTag){
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);//
		if(selectedTag!=null  && !selectedTag.equals("")){
			
		}
		
	}
	
	private Fragment getFragmentByTag(String tag){
		Fragment f = fragmentManager.findFragmentByTag(tag);
		if(f==null){
			f = BaseFragment.getInstance(tag);
		}
		return f;
	}
}
