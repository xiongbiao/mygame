package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import erb.unicomedu.adapter.MyAskAdapter;
import erb.unicomedu.dao.BbsInfoAskDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;

public class BbsMyThemeActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	private final String TAG = "BbsMyThemeActivity"; 
	private ImageButton mFilter;
	private List<BbsAskVo> data;
	private PullToRefreshListView prlistView;
	private MyAskAdapter myAskAdapter;
	private int mType = 1;
//	private int mColorSelect = new Color().rgb(255, 255, 255);
//	private int mColor = new Color().rgb(169, 161, 118);
	private LoadingView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.bbs_myask);
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

		data = new ArrayList<BbsAskVo>();
		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
		
		mFilter = (ImageButton)findViewById(R.id.bbs_myask_back);
		mFilter.setFocusable(true);
		mFilter.setClickable(true);
		mFilter.setOnClickListener(this);
		
		 
	}
	
	
	  
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<BbsAskVo>> {
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
				if(userInfo!=null){
					Map<String, Object> param = new HashMap<String, Object>();
					 param.put("orderby",mType);
					 param.put("memberid", userInfo.getMemberid());
					data = BbsInfoAskDao.getMyAskList(param);
				}else{
					Toast.makeText(BbsMyThemeActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
				}
				
			} catch(EuException ex){ 
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
			myAskAdapter = new MyAskAdapter(BbsMyThemeActivity.this, R.layout.list_bbs_myaskitem, data);
			prlistView.setAdapter(myAskAdapter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(false);
			myAskAdapter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		updateFilterButtonBackground(v.getId());
		switch (v.getId()) {
			case R.id.bbs_myask_back:
				finish();
				break;
		}
	}
	
	/**
	 * 更新排序按钮背景
	 * @param viewId
	 */
	private void updateFilterButtonBackground(int viewId){
		switch (viewId) {

		}
	}
	

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		BbsAskVo bv = (BbsAskVo)data.getAdapter().getItem(position);
//		LogUtil.d(TAG, tv.getMediaUrl());
		Intent mIntent = new Intent(this,BbsCheckAllActivity.class);  
        Bundle mBundle = new Bundle();  
        mBundle.putBoolean(Def.OBJ_bbs, true);
        mBundle.putSerializable(Def.OBJ,bv);  
        mIntent.putExtra(Def.OBJ_Bundle,mBundle); 
        startActivity(mIntent); 
	}
	
	 
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				finish();
			}
	    	return super.onKeyDown(keyCode, event);
	    }
	
}
