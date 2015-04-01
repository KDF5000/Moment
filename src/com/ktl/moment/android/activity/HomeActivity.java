package com.ktl.moment.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.ktl.moment.R;
import com.ktl.moment.android.base.BaseActivity;
import com.ktl.moment.android.component.CustomMenu;
import com.ktl.moment.android.component.CustomMenu.OnMenuItemClickListener;
import com.ktl.moment.android.fragment.DynamicFragment;
import com.ktl.moment.android.fragment.FindFragment;
import com.ktl.moment.android.fragment.MeFragment;
import com.ktl.moment.android.fragment.MomentFragment;

public class HomeActivity extends BaseActivity {
	private static final String TAG = "HomeAtivity";
	
	private CustomMenu customMenu;//菜单
	private FragmentManager fragmentManager;//管理器
	private FragmentTransaction fragmentTransation;//fragment事务
	private Fragment currentFragment = new FindFragment();//当前选中的菜单对应的fragment,默认为发现
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initEvent();
		fragmentManager = getSupportFragmentManager();
		fragmentTransation = fragmentManager.beginTransaction();
		if(currentFragment==null){
			currentFragment =  new FindFragment();
		}
		fragmentTransation.replace(R.id.home_content_container,currentFragment);
		fragmentTransation.commit();
	}
	
	private void initView(){
		getLayoutInflater().inflate(R.layout.activity_home, contentLayout,true);
		
		setHomeTitleVisible(true);
		setTitleTvName(R.string.found_text_view);
		setBaseActivityBgColor(getResources().getColor(R.color.found_background_color));
		customMenu = (CustomMenu)findViewById(R.id.id_menu);
	}
	private void initEvent(){
		
		customMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void OnClick(View v, int pos) {
				// TODO Auto-generated method stub
				switch(pos){
				case 0:
					currentFragment = new FindFragment();
					break;
				case 1:
					currentFragment = new DynamicFragment();
					break;
				case 2:
					currentFragment = new MomentFragment();
					break;
				case 3:
					currentFragment = new MeFragment();
					break;
				}
				getSupportFragmentManager().beginTransaction().replace(R.id.home_content_container, currentFragment).commit();
			}
		});
	}
	
}
