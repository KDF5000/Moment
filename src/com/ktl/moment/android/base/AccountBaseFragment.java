package com.ktl.moment.android.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ktl.moment.R;
import com.ktl.moment.android.fragment.LoginFragment;
import com.ktl.moment.android.fragment.RegisterFragment;
import com.ktl.moment.android.fragment.VerifyFragment;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.common.util.EditTextUtil;
import com.ktl.moment.common.util.ToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class AccountBaseFragment extends Fragment {
	
	public static Fragment getInstance(String fragmentName){
		AccountBaseFragment baseFragment = null;
		if(fragmentName.equals(C.Account.FRAGMENT_LOGIN)){
			baseFragment = new LoginFragment();
		}else if(fragmentName.equals(C.Account.FRAGMENT_REGISTER)){
			baseFragment = new RegisterFragment();
		}else if(fragmentName.equals(C.Account.FRAGMENT_VERIFY)){
			baseFragment = new VerifyFragment();
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
	
	public void toast(String string){
		ToastUtil.show(getActivity(), string);
	}
	
	/**
	 * editText 焦点改变时editText与image显示隐藏
	 * @param et
	 * @param delImg
	 * @param hasFocus
	 */
	protected void focusChange(EditText et, ImageView delImg, boolean hasFocus){
		if(hasFocus){
			if(et.length() > 0){
				delImg.setVisibility(View.VISIBLE);
			}
		}else{
			delImg.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * editText文本内容改变时editText与image显示隐藏
	 * @param et
	 * @param delImg
	 */
	protected void addTextChange(final EditText et, final ImageView delImg){
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				EditTextUtil.formatPhoneNumEditText(s);
				if(et.length() > 0){
					delImg.setVisibility(View.VISIBLE);
				}else{
					delImg.setVisibility(View.INVISIBLE);
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
	

	/**
	 * 销毁当前界面跳到指定界面
	 * @param classObj
	 */
	protected void actionStart(Class<?> classObj){
		Intent intent = new Intent(getActivity(),classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getActivity().startActivity(intent);
		getActivity().finish();
	}
	
}
