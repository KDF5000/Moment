package com.ktl.moment.android.component.clickimagespan;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

public class TouchLinkMovementMethod extends LinkMovementMethod {
	private static TouchLinkMovementMethod sInstance;

	public static MovementMethod getInstance() {
		if (sInstance == null) {
			sInstance = new TouchLinkMovementMethod();
		}
		return sInstance;
	}

	@Override
	public boolean onTouchEvent(TextView widget, Spannable buffer,
			MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();

		if (action == MotionEvent.ACTION_UP
				|| action == MotionEvent.ACTION_DOWN) {
			int x = (int) event.getX();
			int y = (int) event.getY();

			x -= widget.getTotalPaddingLeft();
			y -= widget.getTotalPaddingTop();

			x += widget.getScrollX();
			y += widget.getScrollY();

			Layout layout = widget.getLayout();
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);

			ClickableSpan[] link = buffer.getSpans(off, off,
					ClickableSpan.class);
			TouchClickSpan[] touchClickSpans = buffer.getSpans(off, off,
					TouchClickSpan.class);
			if (link.length != 0) {
				if (action == MotionEvent.ACTION_UP) {
					link[0].onClick(widget);
				} else if (action == MotionEvent.ACTION_DOWN) {
					Selection.setSelection(buffer,
							buffer.getSpanStart(link[0]),
							buffer.getSpanEnd(link[0]));
				}

				return true;
			} else if (touchClickSpans.length != 0) {
				if (action == MotionEvent.ACTION_UP) {
					touchClickSpans[0].onClick(widget, event);
				} else if (action == MotionEvent.ACTION_DOWN) {
					Selection.setSelection(buffer,
							buffer.getSpanStart(touchClickSpans[0]),
							buffer.getSpanEnd(touchClickSpans[0]));
				}
				return true;
			} else {
				Selection.removeSelection(buffer);
			}
		}

		return false;
	}
}
