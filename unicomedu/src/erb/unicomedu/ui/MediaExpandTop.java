package erb.unicomedu.ui;

import android.content.Context;
import android.util.AttributeSet;

public class MediaExpandTop extends MediaExpand {

	public MediaExpandTop(Context context) {
		super(context);
	}
	public MediaExpandTop(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MediaExpandTop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void openMoreList() {
		if(!isOpen) {
			mScroller.startScroll(0, getHeight(), 0, -getHeight(), 3000);
			postInvalidate();
		}
		isOpen = true;
	}
	
	@Override
	protected void hideMoreList() {
		if(isOpen) {
			mScroller.startScroll(0, 0, 0, getHeight(), 3000);
			postInvalidate();
		}
		isOpen = false;
	}
	
}
