package com.ktl.moment.android.component;

import com.ktl.moment.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuImageText extends LinearLayout {

	private Context mContext;
	private ImageView itemImage;// 菜单选项的图片
	private TextView textView; // 菜单文字
	private TypedArray mAttr;

	public MenuImageText(Context context) {
		this(context, null);
	}

	public MenuImageText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MenuImageText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.menu_item_img_text, this, true);
		itemImage = (ImageView) findViewById(R.id.menu_item_image);
		textView = (TextView) findViewById(R.id.menu_item_text);

		mAttr = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.menu_image_text, defStyle, 0);
		textView.setText(mAttr.getString(R.styleable.menu_image_text_menu_text));
		textView.setTextColor(mAttr.getColor(
				R.styleable.menu_image_text_menu_item_text_unchecked_color, 0));
		itemImage
				.setImageDrawable(mAttr
						.getDrawable(R.styleable.menu_image_text_menu_item_unchecked_src));
	}

	/**
	 * 设置被选中的状态
	 */
	public void setChecked() {
		if (itemImage != null && textView != null) {
			itemImage.setImageDrawable(mAttr
							.getDrawable(R.styleable.menu_image_text_menu_item_checked_src));
			textView.setTextColor(mAttr.getColor(
							R.styleable.menu_image_text_menu_item_text_checked_color,
							0));
		}
	}

	/**
	 * 设置未被选中状态
	 */
	public void setUnCheck() {
		if (itemImage != null && textView != null) {
			itemImage.setImageDrawable(mAttr
							.getDrawable(R.styleable.menu_image_text_menu_item_unchecked_src));
			textView.setTextColor(mAttr.getColor(
					R.styleable.menu_image_text_menu_item_text_unchecked_color,
					0));
		}
	}
}
