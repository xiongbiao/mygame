package erb.unicomedu.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class FilterExpand extends FrameLayout {
	
	protected Scroller mScroller;
	protected boolean isOpen;
	private int width;
	
	public FilterExpand(Context context) {
		this(context, null);
	}
	public FilterExpand(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public FilterExpand(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewPosition(context);
		setFocusable(true);
		setClickable(true);
	}
	
	private void initViewPosition(Context context){
		mScroller = new Scroller(context);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = (int)(dm.widthPixels * 0.3f);
		isOpen = false;
	}
	
	public void openMoreListView(){
		openMoreList();
	}
	
	public void hideMoreListView(){
		hideMoreList();
	}
	
	protected void openMoreList(){
		mScroller.startScroll(0, 0, -(getWidth() - width), 0, 1000);
		postInvalidate();
		isOpen = true;
	}
	
	protected void hideMoreList(){
		mScroller.startScroll(-(getWidth() - width), 0, getWidth() - width, 0, 1000);
		postInvalidate();
		isOpen = false;
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

}
