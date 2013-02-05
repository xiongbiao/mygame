package erb.unicomedu.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class MediaExpand extends FrameLayout {
	
	protected Scroller mScroller;
	protected boolean isOpen;
	
	public MediaExpand(Context context) {
		this(context, null);
	}
	public MediaExpand(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public MediaExpand(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewPosition(context);
	}
	
	private void initViewPosition(Context context){
		mScroller = new Scroller(context);
		isOpen = true;
	}
	
	public void openMoreListView(){
		openMoreList();
	}
	
	public void hideMoreListView(){
		hideMoreList();
	}
	
	protected void openMoreList(){
		if(!isOpen) {
			mScroller.startScroll(0, -getHeight(), 0, getHeight(), 3000);
			postInvalidate();
		}
		isOpen = true;
	}
	
	protected void hideMoreList(){
		if(isOpen) {
			mScroller.startScroll(0, 0, 0, -getHeight(), 3000);
			postInvalidate();
		}
		isOpen = false;
	}
	
	@Override
	public void computeScroll() {
		if(mScroller.computeScrollOffset()){
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

}
