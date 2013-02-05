package erb.unicomedu.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Scroller;
/***
 * 搜索控件
 * @author xiong
 *
 */
public class SearchView extends FrameLayout {
	
	private Scroller mScroller;
	private int offWidth;
	public boolean isOpen;
	
	public SearchView(Context context) {
		this(context, null);
	}
	
	public SearchView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		mScroller = new Scroller(context);
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
	    ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
	    offWidth = (localDisplayMetrics.widthPixels / 3);
	}
	/**
	 * 开启偏转动画
	 */
	public void openOffset(){
		mScroller.startScroll(0, 0, -(getWidth() - offWidth), 0,1000);
		isOpen = true;
	}
	
	/**
	 * 关闭偏转动画
	 */
	public void closeOffset(){
		mScroller.startScroll(-(getWidth() - offWidth), 0, getWidth() - offWidth, 0,1000);
		isOpen = false;
	}
	
	@Override
	public void computeScroll() {
		if(!mScroller.computeScrollOffset())
			return;
		scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
		postInvalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}

}
