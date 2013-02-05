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
import android.widget.Toast;
import erb.unicomedu.adapter.SubjectAdapter;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.SubjectVo;

/**
 * 课程搜索结果
 *
 */
public class SubjectResultActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	List<SubjectVo> data;
	private PullToRefreshListView prlistView;
	private SubjectAdapter subjectBaseAdpter;
	private String TAG = "SubjectResultActivity"; 
	
	private ImageButton mBack;
	private String mKeyString;
	
	private TextView mWhere;
	private LoadingView lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.obj_result);
		
		mKeyString =getIntent().getStringExtra(Def.WHERE_KEY);
		 lv = (LoadingView)findViewById(R.id.data_loading);
		mWhere = (TextView) findViewById(R.id.result_where);
		
		mWhere.setText(mKeyString);
		
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

		data = new ArrayList<SubjectVo>();
		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
		
		mBack = (ImageButton) findViewById(R.id.video_search_back);
		mBack.setOnClickListener(this);
		
		
	}

	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<SubjectVo>> {
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
		protected List<SubjectVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				 param.put("orderby",1);
				 param.put("keyword", mKeyString);
				  param.put("page", prlistView.getPage());
				  param.put("size", Def.M_LIST_SIZE);
				  if(userInfo!=null){
					  param.put("memberid",userInfo.getMemberid());
				  }
				  List<SubjectVo>	tlist = SubjectDao.getSubjectList(param);
					
					if( prlistView.getPage()>0 ){
						 if(tlist!=null){
							 for(SubjectVo tv :tlist ){
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
		protected void onPostExecute(List<SubjectVo> data) {

			prlistView.setLoadingGone();
			if(subjectBaseAdpter==null){
				subjectBaseAdpter = new SubjectAdapter(SubjectResultActivity.this, R.layout.subject_list_item, data);
				prlistView.setAdapter(subjectBaseAdpter);
			}
			else{
				if(prlistView.getAdapter()==null){
					prlistView.setAdapter(subjectBaseAdpter);
				}
				subjectBaseAdpter.setData(data);
			}
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
		    prlistView.setShowFooter(isFoot);
		    subjectBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		SubjectVo tv = (SubjectVo)data.getAdapter().getItem(position);
		Intent mIntent = new Intent(this,SubjectInfoActivity.class);  
        Bundle mBundle = new Bundle();  
        mBundle.putSerializable(Def.OBJ,tv);  
        mIntent.putExtras(mBundle); 
        mIntent.putExtra(Def.OBJ_Bundle, mBundle);
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
