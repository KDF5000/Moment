package com.ktl.moment.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class EditTextUtil {

	public static void setEditTextEmpty(EditText et) {
		et.setText("");
	}

	public static void formatPhoneNumEditText(CharSequence string) {

	}

	/**
	 * 设置edittext的可编辑状态
	 * 
	 * @param et
	 * @param value
	 */
	public static void setEditable(EditText et, boolean value) {
		if (value) {
			et.setFilters(new InputFilter[] { new InputFilter() {

				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					// TODO Auto-generated method stub
					return null;
				}
			} });
			et.setCursorVisible(true);

		} else {
			et.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					return source.length() < 1 ? dest.subSequence(dstart, dend)
							: "";
				}
			} });
			et.setCursorVisible(false);

		}
	}
}
