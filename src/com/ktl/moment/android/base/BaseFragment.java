package com.ktl.moment.android.base;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.ktl.moment.R;
import com.ktl.moment.android.fragment.DynamicFragment;
import com.ktl.moment.android.fragment.MeFragment;
import com.ktl.moment.android.fragment.MomentFragment;
import com.ktl.moment.android.fragment.find.FindFragment;
import com.ktl.moment.common.constant.C;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class BaseFragment extends Fragment {
	
	public static Fragment getInstance(String fragmentName){
		BaseFragment baseFragment = null;
		if(fragmentName.equals(C.menu.FRAGMENT_FIND_TAG)){
			baseFragment = new FindFragment();
		}else if(fragmentName.equals(C.menu.FRAGMENT_DYNAMIC_TAG)){
			baseFragment = new DynamicFragment();
		}else if(fragmentName.equals(C.menu.FRAGMENT_MOMENT_TAG)){
			baseFragment = new MomentFragment();
		}else if(fragmentName.equals(C.menu.FRAGMENT_ME_TAG)){
			baseFragment = new MeFragment();
		}
		return baseFragment;
	}
	
	public  DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .showImageOnLoading(R.drawable.default_img)
	        .showImageForEmptyUri(R.drawable.default_img)
	        .showImageOnFail(R.drawable.default_img)
	        .cacheOnDisk(true)
	        .cacheInMemory(true)
	        .bitmapConfig(Bitmap.Config.RGB_565)
	        .considerExifParams(true).build();
		return options;
	}
	
}
