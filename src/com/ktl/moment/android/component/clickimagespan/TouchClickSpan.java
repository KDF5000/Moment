package com.ktl.moment.android.component.clickimagespan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.MotionEvent;
import android.view.View;

public abstract class TouchClickSpan extends CharacterStyle implements
		UpdateAppearance {

	public TouchClickSpan() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Performs the click action associated with this span.
	 */
	public abstract void onClick(View widget,MotionEvent e);

	/**
	 * Makes the text underlined and in the link color.
	 */
	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(ds.linkColor);
		ds.setUnderlineText(true);
	}
}
