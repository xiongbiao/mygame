package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import erb.unicomedu.adapter.BbsInfoAdapter;
import erb.unicomedu.dao.BbsInfoAskDao;
import erb.unicomedu.ui.FilterExpand;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;
import erb.unicomedu.vo.BbsVo;

/**
 * bbs 帖区
 * @author xiongbiao
 *
 */
public class BbsinfoActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new GetDataTask().execute();
	}

	private String TAG = "BbsinfoActivity";
	private TextView title;
	private Button mFilter;
	private ImageButton backButton;
	private Button mLatest;
	private Button mPopularity;
	private Button mPrice;
	private FilterExpand mFilterExpand;
	private LinearLayout filterContent;
	private List<BbsAskVo> data;
	private BbsVo bbsVo;
	private PullToRefreshListView prlistView;
	private BbsInfoAdapter askAdapter;
	private Bundle mRBundle;
	private int mType = 1;
	private int mColorSelect = new Color().rgb(255, 255, 255);
	private int mColor = new Color().rgb(169, 161, 118);
	private LoadingView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_info);
		init();
		initData();
	}
	
	private void init(){
		lv = (LoadingView)findViewById(R.id.data_loading);
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
//		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
		mFilter = (Button)findViewById(R.id.bbs_ask_question);
		mFilter.setFocusable(true);
		mFilter.setClickable(true);
		mFilter.setOnClickListener(this);
		title = (TextView)findViewById(R.id.bbs_info_title);
		backButton = (ImageButton)findViewById(R.id.obj_info_back);
		backButton.setFocusable(true);
		backButton.setClickable(true);
		backButton.setOnClickListener(this);
		mLatest = (Button)findViewById(R.id.filter_latest);
		mLatest.setOnClickListener(this);
		mPopularity = (Button)findViewById(R.id.filter_popularity);
		mPopularity.setOnClickListener(this);
		mPrice = (Button)findViewById(R.id.filter_price);
		mPrice.setOnClickListener(this);
		filterContent = (LinearLayout)findViewById(R.id.filter_content);
		mFilterExpand = (FilterExpand)findViewById(R.id.filter_expand);
	}
    private void initData(){
//    	bbsVo = (BbsVo)getIntent().getSerializableExtra(Def.OBJ);
    	mRBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mRBundle!=null){
        	bbsVo = (BbsVo)mRBundle.getSerializable(Def.OBJ);
        } 
    	if(bbsVo!=null){
    		title.setText(bbsVo.getTopic());
    	}
	}
	
	
	  
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	public class GetDataTask extends AsyncTask<Void, Void, List<BbsAskVo>> {
		int exType = 0;
		String erMsg = "";
		boolean isFoot = false;
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}
		@Override
		protected List<BbsAskVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				 param.put("orderby",mType);
				 param.put("topicid",bbsVo.getTopicid());
				data = BbsInfoAskDao.getAskList(param);
			}catch(EuException ex){ 
				ex.printStackTrace();
				exType = 1;	
				erMsg = ex.getMessage();
				data = null;
				isFoot = false;
				LogUtil.d("XB", ""+ex.getMessage());
			}catch(Exception e) {
				 e.printStackTrace();
				 data = null;
				 isFoot = false;
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<BbsAskVo> data) {
			askAdapter = new BbsInfoAdapter(BbsinfoActivity.this, R.layout.list_bbs_askitem, data);
			prlistView.setAdapter(askAdapter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(false);
			askAdapter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		updateFilterButtonBackground(v.getId());
		switch (v.getId()) {
			case R.id.bbs_ask_question:
				 String fromClassName =  this.getClass().getName();
				 String className =  new BbsAskActivity().getClass().getName();
//				 Bundle mBundle = new Bundle();
//			     mBundle.putSerializable(Def.OBJ,bbsVo);  
				boolean result = isModel(Def.MODEl_BBS_ASK, className,fromClassName,mRBundle);
				if(result){
					Intent mIntent = new Intent(this,BbsAskActivity.class);  
//			        Bundle mBundle = new Bundle();
//			        mBundle.putSerializable(Def.OBJ,bbsVo);  
			        mIntent.putExtra(Def.OBJ_Bundle, mRBundle); 
			        startActivity(mIntent);
				}
				break;
			case R.id.obj_info_back:
				finish();
				break;
			case R.id.filter_latest:
				break;
			case R.id.filter_popularity:
				
				break;
			case R.id.filter_price:
				
				break;
		}
	}
	
	/**
	 * 更新排序按钮背景
	 * @param viewId
	 */
	private void updateFilterButtonBackground(int viewId){
		switch (viewId) {
			case R.id.filter_latest:
				mType = 1;
				mLatest.setBackgroundResource(R.drawable.filter_but_l_s);
				mPopularity.setBackgroundResource(R.drawable.filter_but_c);
				mPrice.setBackgroundResource(R.drawable.filter_but_r);
				mLatest.setTextColor(mColorSelect);
				mPopularity.setTextColor(mColor);
				mPrice.setTextColor(mColor);
				new GetDataTask().execute();
				break;
			case R.id.filter_popularity:
				mType = 2;
				mLatest.setBackgroundResource(R.drawable.filter_but_l);
				mPopularity.setBackgroundResource(R.drawable.filter_but_c_s);
				mPrice.setBackgroundResource(R.drawable.filter_but_r);
				mLatest.setTextColor(mColor);
				mPopularity.setTextColor(mColorSelect);
				mPrice.setTextColor(mColor);
				new GetDataTask().execute();
				break;
			case R.id.filter_price:
				mType = 3;
				mLatest.setBackgroundResource(R.drawable.filter_but_l);
				mPopularity.setBackgroundResource(R.drawable.filter_but_c);
				mPrice.setBackgroundResource(R.drawable.filter_but_r_s);
				mLatest.setTextColor(mColor);
				mPopularity.setTextColor(mColor);
				mPrice.setTextColor(mColorSelect);
				new GetDataTask().execute();
				break;
		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		BbsAskVo bv = (BbsAskVo)data.getAdapter().getItem(position);
//		LogUtil.d(TAG, tv.getMediaUrl());
		Intent mIntent = new Intent(this,BbsCheckAllActivity.class);  
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Def.OBJ,bv);
        mIntent.putExtra(Def.OBJ_Bundle,mBundle); 
        startActivity(mIntent);  
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(mFilterExpand.isOpen()){
    		mFilterExpand.hideMoreListView();
			mFilter.setEnabled(true);
			filterContent.setVisibility(View.GONE);
			return true;
    	}
    	if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
    	return super.onKeyDown(keyCode, event);
    };
}
