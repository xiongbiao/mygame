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
import android.widget.TextView;
import erb.unicomedu.adapter.VideoAdapter;
import erb.unicomedu.dao.VideoDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.VideoVo;

public class VideoResultActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	
	List<VideoVo> data;
	private PullToRefreshListView prlistView;
	private VideoAdapter videoBaseAdpter;
	private String TAG = "VideoResultActivity"; 
	private ImageButton mBack;
	private String mKeyString;
	
	private SubjectVo mSubjectVo;
	private Bundle mRBundle;
	private LoadingView lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.obj_result);
		mKeyString =getIntent().getStringExtra(Def.WHERE_KEY);
		 lv = (LoadingView)findViewById(R.id.data_loading);
		mSubjectVo =  getIntent().getSerializableExtra(Def.OBJ)==null ?null:(SubjectVo) getIntent().getSerializableExtra(Def.OBJ);
		mRBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mRBundle!=null){
        	mSubjectVo = (SubjectVo)mRBundle.getSerializable(Def.OBJ);
        } 
		
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
			}
		});

		data = new ArrayList<VideoVo>();
		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
		
		mBack = (ImageButton) findViewById(R.id.video_search_back);
		mBack.setOnClickListener(this);
		
		
		
//		mWhere.setText(mKeyString);
	}

	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<VideoVo>> {
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
		protected List<VideoVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				 param.put("orderby",1);
				 if(mKeyString!=null)
				 param.put("keyword", mKeyString);
				 if(mSubjectVo!=null){
					 param.put("subjectid", mSubjectVo.getSubjectId());
				}
				 param.put("page", prlistView.getPage());
				  param.put("size", Def.M_LIST_SIZE);
				  if(userInfo!=null){
					  param.put("memberid",userInfo.getMemberid());
				  }
				  List<VideoVo>	tlist = VideoDao.getVideoList(param);
				
				if( prlistView.getPage()>0 ){
					 if(tlist!=null){
						 for(VideoVo tv :tlist ){
							 data.add(tv);
						 }
					 }
				 }else{
					 data = tlist;
				 }
				 if(tlist.size()<Def.M_LIST_SIZE){
					 isFoot = false;
				 }else{
					 if(tlist!=null&&tlist.size()>0) 
					     isFoot = true;
					 else
						 isFoot = false;
				 }
			} catch ( Exception e) {
				 e.printStackTrace();
				 data = null;
				 isFoot = false;
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<VideoVo> data) {
			
			prlistView.setLoadingGone();
			if(videoBaseAdpter==null){
				videoBaseAdpter = new VideoAdapter(VideoResultActivity.this, R.layout.video_item, data);
				prlistView.setAdapter(videoBaseAdpter);
			}
			else{
				if(prlistView.getAdapter()==null){
					prlistView.setAdapter(videoBaseAdpter);
				}
				videoBaseAdpter.setData(data);
			}
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
		    prlistView.setShowFooter(isFoot);
		    videoBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		VideoVo tv = (VideoVo)data.getAdapter().getItem(position);
		LogUtil.d(TAG, tv.getMediaUrl());
		Intent mIntent = new Intent(this,VideoInfoActivity.class);  
        Bundle mBundle = new Bundle();  
        mBundle.putSerializable(Def.OBJ,tv);  
        mIntent.putExtra(Def.OBJ_Bundle,mBundle);
        startActivity(mIntent);  
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.video_search_back:
				finish();
				break;	
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
