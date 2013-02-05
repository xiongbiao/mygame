package erb.unicomedu.ui;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.LogUtil;

public class LoadingView extends RelativeLayout{
	private final   int STATE_LOADING = 0;
	private final   int STATE_LOADING_SUCCESSFUL = 1;
	private final   int STATE_LOADING_UNSUCCESSFUL = 2;
	private final   int RESULT_NO_DATA = 3;

	private Context mContext;

	private TextView mContentView;
	private ProgressBar mProgressBar;
	
	public LoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	private void initView(Context context) {
		mContext = context;
		RelativeLayout moreView = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.data_loading, null);
		addView(moreView);
		moreView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mContentView = (TextView)moreView.findViewById(R.id.data_lastUpdatedTextView);
		mProgressBar = (ProgressBar)moreView.findViewById(R.id.data_progressBar);
	}
	
	private void isShowProgressBar(boolean isShow){
		if(isShow){
			mProgressBar.setVisibility(View.VISIBLE);
		}else{
			mProgressBar.setVisibility(View.INVISIBLE);
		}
	}
	
	private void setContentText(String textValue){
		mContentView.setText(textValue);
	}
	
	public void show(int stateId){
		switch (stateId) {
		case STATE_LOADING:
			isShowProgressBar(true);
			setContentText(mContext.getString(R.string.data_loding));
			break;
		case STATE_LOADING_UNSUCCESSFUL:
			isShowProgressBar(false);
			setContentText(mContext.getString(R.string.data_loding_failed));
            break; 
        case RESULT_NO_DATA:
        	isShowProgressBar(false);
			setContentText(mContext.getString(R.string.data_loding_no_data));
            break; 
        case STATE_LOADING_SUCCESSFUL:
        	isShowProgressBar(false);
			setContentText(mContext.getString(R.string.data_loding_successful));
            break; 
		default:
			break;
		}
	}
	
	public void  onPost(List data,LoadingView lv,PullToRefreshListView prlistView,int exType ,String erMsg){
		if(data==null){
			 switch (exType) {
			 case 1:
					lv.myShow(false, erMsg);
					break;
			 default:
					lv.show(STATE_LOADING_UNSUCCESSFUL);
					break;
			}
		} else{
			if(prlistView.getPage()==0&&data.size()==0){
				lv.show(RESULT_NO_DATA);
			}else{
		       lv.setVisibility(View.GONE);
		   }
		   LogUtil.d("XB", "[   ] data size :"+ data.size());
		}
	}
	public void  onPost(List data,LoadingView lv,PullToRefreshListView prlistView){
		if (data == null) {
			lv.show(STATE_LOADING_UNSUCCESSFUL);
		} else {
			if (prlistView.getPage() == 0 && data.size() == 0)
				lv.show(RESULT_NO_DATA);
			else
				lv.setVisibility(View.GONE);
		}
	}
	public void myShow(boolean isShow,String text){
		setContentText(text);
		isShowProgressBar(isShow);
	}

}
