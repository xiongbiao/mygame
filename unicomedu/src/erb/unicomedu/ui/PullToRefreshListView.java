package erb.unicomedu.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

/**   
* @ClassName: PullToRefreshListView   
* @Function: TODO 下拉刷新的listview.需要在Activity设置OnRefreshListener才会有效果。用到的Util类只是处理一下时间。
* @author 
* @date 2012-7-11 下午5:28:24      
*/ 
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;
	private LayoutInflater mInflater;
	private LinearLayout mHeadView;
	private RelativeLayout mFooterView;

	private TextView mTipsTextView;
	private TextView mLastUpdatedView;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;


	private RotateAnimation mPullAnimation;
	private RotateAnimation mReverseAnimation;
	private TranslateAnimation mTranslateAnimation;
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	private int mHeadViewWidth;
	private int mHeadViewHeight;

	private int mStartY;
	private int mFirstItemIndex;

	private int mState;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	private boolean head;    //设置头部是否隐藏
	private boolean foot = true;     //设置底部是否隐藏
	private boolean remove = true; //是否移除底部
    private	ProgressBar pbFoot ;
    private TextView tvFoot ;
    
    private  int page = 0;
	public boolean isHead() {
		return head;
	}

	public void setHead(boolean head) {   
		this.head = head;
	}

	public boolean isFoot() {
		return foot;
	}

	public void setFoot(boolean foot) {
		this.foot = foot;
	}
	
	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public int getPage(){
		return page;
	}
	public void setPage(int mpage){
		this.page = mpage;
	}
	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(R.color.alpha));
		mInflater = LayoutInflater.from(context);
		mHeadView = (LinearLayout) mInflater.inflate(R.layout.pull_to_list_head, null);
		mArrowImageView = (ImageView) mHeadView.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(70);
		mArrowImageView.setMinimumHeight(50);
		mProgressBar = (ProgressBar) mHeadView.findViewById(R.id.head_progressBar);
		mTipsTextView = (TextView) mHeadView.findViewById(R.id.head_tipsTextView);
		mLastUpdatedView = (TextView) mHeadView.findViewById(R.id.head_lastUpdatedTextView);
		mTipsTextView.setTextColor(context.getResources().getColor(R.color.white));
		measureView(mHeadView);
		mHeadViewHeight = mHeadView.getMeasuredHeight();
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		//保证在数据不够或者无数据时，隐藏headview。
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
		mHeadView.invalidate();

		addHeaderView(mHeadView, null, false);
		
		setOnScrollListener(this);
		
		mFooterView =(RelativeLayout) mInflater.inflate(R.layout.listview_footer, null);
		pbFoot = (ProgressBar) mFooterView.findViewById(R.information.listview_footer_progressbar);
		tvFoot = (TextView) mFooterView.findViewById(R.information.listview_footer_hint_textview);
		addFoot();
		

		mPullAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mPullAnimation.setInterpolator(new LinearInterpolator());
		mPullAnimation.setDuration(250);
		mPullAnimation.setFillAfter(true);

		mReverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);

		mState = DONE;
		isRefreshable = false;
	}

	public void removeFoot(){
		if(!remove){
			this.remove = true;
			removeFooterView(mFooterView);
		}
	}
	public void addFoot(){
		if(remove){
		this.remove = false;
		addFooterView(mFooterView);
		mFooterView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				onRefresh();
				
				onLoadMore();
				LogUtil.d("ptrList ", "1------------");
				return ;
			}
		});
		}
	}
	@Override
	public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount,
			int arg3) {
		mFirstItemIndex = firstVisiableItem;
	   int lastVisibleIndex = firstVisiableItem + visibleItemCount - 1;  
        if (lastVisibleIndex==0||lastVisibleIndex%Def.M_LIST_SIZE>0) {  
//        	removeFoot();  
        }  
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//记录第一次下拉的位置
				if (mFirstItemIndex == 0 && !isRecored) {
					isRecored = true;
					mStartY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (mState != REFRESHING && mState != LOADING) {
					if (mState == DONE) {
						// 什么都不做
					}
					if (mState == PULL_To_REFRESH) {
						mState = DONE;
						changeHeaderViewByState();
					}
					if (mState == RELEASE_To_REFRESH) {
						mState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				//保证位置只能被记录一次
				if (!isRecored && mFirstItemIndex == 0) {
					isRecored = true;
					mStartY = tempY;
				}

				if (mState != REFRESHING && isRecored && mState != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

					// 可以松手去刷新了
					if (mState == RELEASE_To_REFRESH) {
//						setSelection(0);
						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - mStartY) / RATIO < mHeadViewHeight)
								&& (tempY - mStartY) > 0) {
							mState = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - mStartY <= 0) {
							mState = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (mState == PULL_To_REFRESH) {

//						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - mStartY) / RATIO >= mHeadViewHeight) {
							mState = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						}
						else if (tempY - mStartY <= 0) {
							mState = DONE;
							changeHeaderViewByState();
						}
					}

					// done状态下
					if (mState == DONE) {
						if (tempY - mStartY > 0) {
							mState = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (mState == PULL_To_REFRESH) {
						mHeadView.setPadding(0, -1 * mHeadViewHeight
								+ (tempY - mStartY) / RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (mState == RELEASE_To_REFRESH) {
						mHeadView.setPadding(0, (tempY - mStartY) / RATIO
								- mHeadViewHeight, 0, 0);
					}

				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	public void setOnRefreshing(){
		mState = REFRESHING;
		this.setSelection(0);
		changeHeaderViewByState();
	}
	
	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (mState) {
		case RELEASE_To_REFRESH:
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mLastUpdatedView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mPullAnimation);
			mTipsTextView.setText("松开刷新");
			break;
		case PULL_To_REFRESH:
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			mLastUpdatedView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mReverseAnimation);
				mTipsTextView.setText("下拉刷新");
			} else {
				mTipsTextView.setText("下拉刷新");
			}
			break;
		case REFRESHING:
			
			mHeadView.setPadding(0, 0, 0, 0);
			mTranslateAnimation = new TranslateAnimation(0, 0, mHeadView.getBottom()-mHeadViewHeight, 0);
			mTranslateAnimation.setDuration(150);
			mTranslateAnimation.setInterpolator(new LinearInterpolator());
			this.startAnimation(mTranslateAnimation);
			mProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mTipsTextView.setText("正在刷新...");
			mLastUpdatedView.setVisibility(View.VISIBLE);
			break;
		case DONE:
			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			mTranslateAnimation = new TranslateAnimation(0, 0, mHeadViewHeight, 0);
			mTranslateAnimation.setDuration(150);
			mTranslateAnimation.setInterpolator(new LinearInterpolator());
			this.startAnimation(mTranslateAnimation);
			mProgressBar.setVisibility(View.GONE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			mTipsTextView.setText("下拉刷新");
			mLastUpdatedView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
		public void onLoadMore();
	}

	public void onRefreshComplete() {
		mState = DONE;
		mLastUpdatedView.setText("最近更新:" + getRefreshTime(System.currentTimeMillis()/1000));
		if(page==0){
		   changeHeaderViewByState();
		   if(mFooterView!=null){
				mFooterView.setVisibility(View.VISIBLE);
				setLoadingGone();
			}
		}
	}
	public void setLoading(){
		pbFoot.setVisibility(View.VISIBLE);
		tvFoot.setText(getResources().getString(R.string.loading));
	}
	public void setLoadingGone(){
		pbFoot.setVisibility(View.GONE);
		tvFoot.setText(getResources().getString(R.string.listview_footer_hint_normal));
	}
	
	public void setShowFooter(boolean isFoot){
		if(mFooterView!=null){
			if(isFoot){
				addFoot();
				mFooterView.setVisibility(View.VISIBLE);
			}else{
				mFooterView.setVisibility(View.GONE);
				removeFoot();
			}
			setLoadingGone();
		}
		
	}

	private void onRefresh() {
		if (refreshListener != null) {
			page = 0;
			refreshListener.onRefresh();
		}
	}
	
	private void onLoadMore(){
		if (refreshListener != null) {
			if(isFoot() == true && !remove){
				refreshListener.onLoadMore();
				page ++ ;
			}
			else{
				if(mFooterView!=null){
					mFooterView.setVisibility(View.GONE);
				}
			}
		}
	}
	
	public static String getRefreshTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		Date date = new Date(timestamp * 1000);
		sdf.format(date);
		return sdf.format(date);
	}
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		mLastUpdatedView.setText("最近更新:" + new Date().toLocaleString());
		super.setAdapter(adapter);
	}

}
