package erb.unicomedu.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import erb.unicomedu.activity.HomeActivity;
import erb.unicomedu.activity.R;

public class MenuControl extends LinearLayout implements OnClickListener{
	
	private View view;
	
	private boolean areLevelShowing = false;
	private RelativeLayout relate_level2, relate_level3;

	private ImageButton home,level3_but1,level3_but2,level3_but3,level3_but4,
			level3_but5,level3_but6,level3_but7,level2_but1,level2_but2,level2_but3;
	private ImageView image;
	private GestureDetector mGestureDetector,tGestureDetector;
	private PanelOnGestureListener mGestureListener;
	private SlidingDrawer sd;
	private View mContent;
	Context mContext;
	private boolean flag=false;
	
	public MenuControl(Context context) {
		
		this(context, null);
		
	}
	public MenuControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.menu_control,null);
		addView(view);
		mContext = context;
		findViews();
		setListener();
		
		image=(ImageView) findViewById(R.id.menu_control_handle);
		image.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction()==MotionEvent.ACTION_UP){
					sd.scrollTo(0, 100);
				}
				
				return tGestureDetector.onTouchEvent(event);
			}
		});
		
		sd=(SlidingDrawer) findViewById(R.id.menu_control_sliding);
		sd.animateOpen();
		mContent=findViewById(R.id.menu_control_content);
		sd.setOnDrawerScrollListener(new OnDrawerScrollListener() {
			
			@Override
			public void onScrollStarted() {				
			}
			
			@Override
			public void onScrollEnded() {				
			}
		});
		
		mGestureListener = new PanelOnGestureListener();
		mGestureDetector = new GestureDetector(mGestureListener);
		mGestureDetector.setIsLongpressEnabled(true);
	}
	
	private void findViews() {
		relate_level2 = (RelativeLayout) view.findViewById(R.id.menu_control_relate_level2);
		relate_level3 = (RelativeLayout) view.findViewById(R.id.menu_control_relate_level3);
		home = (ImageButton) view.findViewById(R.id.menu_control_level1_but);
		level3_but1 = (ImageButton)view.findViewById(R.id.menu_control_level3_but1);
		level3_but2 = (ImageButton)view.findViewById(R.id.menu_control_level3_but2);
		level3_but3 = (ImageButton)view.findViewById(R.id.menu_control_level3_but3);
		level3_but4 = (ImageButton)view.findViewById(R.id.menu_control_level3_but4);
		level3_but5 = (ImageButton)view.findViewById(R.id.menu_control_level3_but5);
		level3_but6 = (ImageButton)view.findViewById(R.id.menu_control_level3_but6);
		level3_but7 = (ImageButton)view.findViewById(R.id.menu_control_level3_but7);
		level2_but1 = (ImageButton)view.findViewById(R.id.menu_control_level2_but1);
		level2_but2 = (ImageButton)view.findViewById(R.id.menu_control_level2_but2);
		level2_but3 = (ImageButton)view.findViewById(R.id.menu_control_level2_but3);
	}
	
	private void setListener() {
		
		home.setOnClickListener(this);
		level3_but1.setOnClickListener(this);
		level3_but2.setOnClickListener(this);
		level3_but3.setOnClickListener(this);
		level3_but4.setOnClickListener(this);
		level3_but5.setOnClickListener(this);
		level3_but6.setOnClickListener(this);
		level3_but7.setOnClickListener(this);
		level2_but1.setOnClickListener(this);
		level2_but2.setOnClickListener(this);
		level2_but3.setOnClickListener(this);
	}
	
	class PanelOnGestureListener implements OnGestureListener {
		float scrollY;
		float scrollX;
		
		public void setScroll(int initScrollX, int initScrollY) {
			scrollX = initScrollX;
			scrollY = initScrollY;
			flag|=flag;
		}
		@Override
		public boolean onDown(MotionEvent e) {
			scrollX = scrollY = 0;
			
			return true;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			return true;
		}
		@Override
		public void onLongPress(MotionEvent e) {
			// not used
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			sd.animateClose();			
			return true;
		}
		@Override
		public void onShowPress(MotionEvent e) {
			// not used
		}
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return true;
		}
	}
	
	private void startAnimationsIn(ViewGroup viewgroup, int durationMillis, int startOffset) {

		viewgroup.setVisibility(View.VISIBLE);
		for (int i = 0; i < viewgroup.getChildCount(); i++) {
			viewgroup.getChildAt(i).setVisibility(0);
			viewgroup.getChildAt(i).setClickable(true);
			viewgroup.getChildAt(i).setFocusable(true);
		}
		Animation animation;
		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
		viewgroup.startAnimation(animation);

	}
	
	private void startAnimationsOut(final ViewGroup viewgroup,
			int durationMillis, int startOffset) {

		Animation animation;
		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(durationMillis);
		animation.setStartOffset(startOffset);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				viewgroup.setVisibility(View.GONE);
				for (int i = 0; i < viewgroup.getChildCount(); i++) {
					viewgroup.getChildAt(i).setVisibility(View.GONE);
					viewgroup.getChildAt(i).setClickable(false);
					viewgroup.getChildAt(i).setFocusable(false);
				}
			}
		});
		viewgroup.startAnimation(animation);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_control_level1_but:
//				if (!areLevelShowing) {
//					home.setBackgroundResource(R.drawable.icon_menu);
//					startAnimationsIn(relate_level2, 500,0);
//					startAnimationsIn(relate_level3, 500,500);
//				} else {
//					home.setBackgroundResource(R.drawable.icon_home);
//					startAnimationsOut(relate_level2, 500, 500);
//					startAnimationsOut(relate_level3, 500, 0);
//				}
//				areLevelShowing = !areLevelShowing;
				
//				mContext.startActivity(new Intent(mContext,HomeActivity.class));
				break;
			case R.id.menu_control_level3_but1:
				System.out.println("menu_control_level3_but1");
				break;
			case R.id.menu_control_level3_but2:
				System.out.println("menu_control_level3_but2");
				break;
			case R.id.menu_control_level3_but3:
				System.out.println("menu_control_level3_but3");
				break;
			case R.id.menu_control_level3_but4:
				System.out.println("menu_control_level3_but4");
				break;
			case R.id.menu_control_level3_but5:
				System.out.println("menu_control_level3_but5");
				break;
			case R.id.menu_control_level3_but6:
				System.out.println("menu_control_level3_but6");
				break;
			case R.id.menu_control_level3_but7:
				System.out.println("menu_control_level3_but7");
				break;
			case R.id.menu_control_level2_but1:
				System.out.println("menu_control_level2_but1");
				break;
			case R.id.menu_control_level2_but2:
				System.out.println("menu_control_level2_but2");
				break;
			case R.id.menu_control_level2_but3:
				System.out.println("menu_control_level2_but3");
				break;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU){
			if (!areLevelShowing) {
				home.setBackgroundResource(R.drawable.icon_menu);
				startAnimationsIn(relate_level2, 500,0);
				startAnimationsIn(relate_level3, 500,500);
				areLevelShowing = !areLevelShowing;
			}
		}else if(keyCode == KeyEvent.KEYCODE_BACK){
			if (areLevelShowing) {
				home.setBackgroundResource(R.drawable.icon_home);
				startAnimationsOut(relate_level2, 500, 500);
				startAnimationsOut(relate_level3, 500, 0);
				areLevelShowing = !areLevelShowing;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
