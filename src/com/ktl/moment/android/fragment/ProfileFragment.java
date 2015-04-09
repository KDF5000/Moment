package com.ktl.moment.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ktl.moment.R;
import com.ktl.moment.android.activity.HomeActivity;
import com.ktl.moment.android.base.AccountBaseFragment;
import com.ktl.moment.android.component.wheel.HoloWheelActivity;
import com.ktl.moment.common.constant.C;
import com.ktl.moment.utils.EditTextUtil;
import com.ktl.moment.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

public class ProfileFragment extends AccountBaseFragment{
	
	@ViewInject(R.id.profile_back)
	private ImageView verifyBack;
	
	@ViewInject(R.id.profile_photo)
	private ImageView profilePhoto;
	
	@ViewInject(R.id.profile_nickname_et)
	private EditText profileNicknameEt;
	
	@ViewInject(R.id.profile_delete_nickname_text_img)
	private ImageView profileDelImg;
	
	@ViewInject(R.id.profile_place_tv)
	private TextView profilePlaceTv;
	
	@ViewInject(R.id.profile_complete_btn)
	private Button profileCompleteBtn;
	
	@ViewInject(R.id.profile_radio_group)
	private RadioGroup profileRadioGroup;
	
	@ViewInject(R.id.profile_radio_male)
	private RadioButton profileRadioMale;
	
	@ViewInject(R.id.profile_radio_female)
	private RadioButton profileRadioFamale;
	
	private int sex;//0:male,1:female
	
	public OnBackToVerifyListener onBackToVerifyListener;

	public interface OnBackToVerifyListener{
		void backToVerifyClick();
	}
	
	public void setOnBackToVerifyListener(OnBackToVerifyListener onBackToVerifyListener){
		this.onBackToVerifyListener = onBackToVerifyListener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_account_profile, container, false);

		ViewUtils.inject(this, view);
		registerEditTextListener();
		
		return view;
	}
	
	private void registerEditTextListener(){
		addTextChange(profileNicknameEt, profileDelImg);
	}
	
	@OnFocusChange({R.id.profile_nickname_et})
	public void onFocusChanged(View v, boolean hasFocus){
		switch (v.getId()) {
		case R.id.profile_nickname_et:
			focusChange(profileNicknameEt, profileDelImg, hasFocus);
			break;
		default:
			break;
		}
	}
	
	@OnRadioGroupCheckedChange({R.id.profile_radio_group})
	public void onRadioGroupCheckdChange(RadioGroup group, int checkedId){
		switch (checkedId) {
		case R.id.profile_radio_male:
			ToastUtil.show(getActivity(), profileRadioMale.getText().toString());
			sex = 0;
			break;
		case R.id.profile_radio_female:
			ToastUtil.show(getActivity(), profileRadioFamale.getText().toString());
			sex = 1;
		default:
			break;
		}
	}
	
	@OnClick({R.id.profile_back,R.id.profile_delete_nickname_text_img,R.id.profile_place_tv,R.id.profile_complete_btn})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.profile_back:
			if(getActivity() instanceof OnBackToVerifyListener){
				((OnBackToVerifyListener)getActivity()).backToVerifyClick();
			}
			break;
		case R.id.profile_delete_nickname_text_img:
			EditTextUtil.setEditTextEmpty(profileNicknameEt);
			break;
		case R.id.profile_place_tv:
			Intent intent = new Intent(getActivity(),HoloWheelActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.profile_complete_btn:
			complete();
			break;
		default:
			break;
		}
	}
	
	public void complete(){
		String nickname = profileNicknameEt.getText().toString().trim();
		String place = profilePlaceTv.getText().toString().trim();
		if(C.Account.IS_CHECK_INPUT){
			if(nickname.isEmpty()){
				ToastUtil.show(getActivity(), "请输入昵称");
				return;
			}
			if(place.isEmpty()){
				ToastUtil.show(getActivity(), "请选择地址");
				return;
			}
		}
		Intent intent = new Intent(getActivity(),HomeActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(data!=null){
			switch(requestCode){
				case 1:
					String province = data.getStringExtra("provinceText");
					String city = data.getStringExtra("cityText");
					profilePlaceTv.setText(province+" "+ city);
					break;
				default:
				break;
			}
		}
	}
}
