package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import erb.unicomedu.adapter.TeacherAdapter;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.AsyncTaskListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.ObjDataTask;
import erb.unicomedu.vo.TeacherVo;

/**
 * 名师介绍
 * 
 */
public class TeacherActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	ArrayList<TeacherVo> data;
	private PullToRefreshListView prlistView;
	private static TeacherAdapter teacherBaseAdpter;
	private ImageButton mTeacherSearch;
    private Button mFilterLatest;
	private Button mFilterPopularity;
	private Button mFilterStar;
	private String keyword = null;
	private int mType = 1;
	private int mColorSelect 	= Color.rgb(255, 255, 255);
	private int mColorNotSelect = Color.rgb(127, 113, 48);
	private ImageButton home;
	
	private LoadingView lv;

	private String TAG = LogUtil.makeLogTag(TeacherActivity.class);
	@Override
	protected void onResume() {
		super.onRestart();
		String className =  this.getClass().getName();
		String fromClassName =  new HomeActivity().getClass().getName();
		isModel(Def.MODEl_TE_LIST, className,fromClassName,null);
//		new GetDataTask().execute();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher);
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
//				new GetDataTask().execute();
				asyncTaskCompleteListener.lauchNewHttpTask();
			}
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
//				new GetDataTask().execute();
				asyncTaskCompleteListener.lauchNewHttpTask();
			}
		});
		 
		data = new ArrayList<TeacherVo>();
		
		prlistView.setOnItemClickListener(this);
		mTeacherSearch = (ImageButton)findViewById(R.id.teacher_search);
		mTeacherSearch.setOnClickListener(this);
		mFilterLatest = (Button) findViewById(R.id.filter_latest);
		mFilterPopularity = (Button) findViewById(R.id.filter_popularity);
		mFilterStar = (Button) findViewById(R.id.filter_price);
		mFilterLatest.setOnClickListener(this);  
		mFilterPopularity.setOnClickListener(this);  
		mFilterStar.setOnClickListener(this);  
		prlistView.setPage(0);
		home = (ImageButton)  findViewById(R.id.menu_control_level1_but);
		home.setOnClickListener(this);
		
		lv = (LoadingView)findViewById(R.id.data_loading);
		 
		asyncTaskCompleteListener.lauchNewHttpTask();
		
	}

	
	AsyncTaskListener<ArrayList<?>> asyncTaskCompleteListener = new AsyncTaskListener<ArrayList<?>>() {
		public void lauchNewHttpTask() {
			Map<String, Object> param = new HashMap<String, Object>();
			if(keyword!=null){
				param.put("keyword", keyword);
			}
			param.put("eventId","teacherList");
			  param.put("orderby",mType);
			  if(userInfo!=null){
				  param.put("memberid",userInfo.getMemberid());
			  }
			 param.put("page", prlistView.getPage());
			 param.put("size", Def.M_LIST_SIZE);
			ObjDataTask httpPostTask = new ObjDataTask(this);
			httpPostTask.execute(param);
		}

		public void onTaskComplete(ArrayList<?> result) {
			prlistView.setLoadingGone();
			data = (ArrayList<TeacherVo>)result;
			prlistView.setLoadingGone();
			if(teacherBaseAdpter==null){
				teacherBaseAdpter = new TeacherAdapter(TeacherActivity.this, R.layout.list_item, data);
				prlistView.setAdapter(teacherBaseAdpter);
			}
			else{
				if(prlistView.getAdapter()==null){
					prlistView.setAdapter(teacherBaseAdpter);
				}
				teacherBaseAdpter.setData(data);
			}
			prlistView.onRefreshComplete();
			teacherBaseAdpter.notifyDataSetChanged();
			lv.onPost(data, lv, prlistView);
		}

		@Override
		public void onPreExecute(){
			prlistView.setLoading();
		    lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}

		@Override
		public void onExclption(Exception e) {
			 String erMsg = e.getMessage();
        	 lv.onPost(data, lv, prlistView, 1, erMsg);
        	 prlistView.setShowFooter(false);
		}

		@Override
		public void showFoot(boolean showFoot) {
			prlistView.setShowFooter(showFoot);
		}
	};
	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
//	private  class GetDataTask extends AsyncTask<Void, Void, List<TeacherVo>> {
//		int exType = 0;
//		String erMsg = "";
//		boolean isFoot = true;
//		@Override
//		protected void onPreExecute() {
//			prlistView.setLoading();
//			lv.setVisibility(View.VISIBLE);
//			lv.show(0);
//		}
//		@Override
//		protected List<TeacherVo> doInBackground(Void... params) {
//			try {
//				Map<String, Object> param = new HashMap<String, Object>();
//				if(keyword!=null)
//				{
//					param.put("keyword", keyword);
//				}
//				  param.put("orderby",mType);
//				  if(userInfo!=null){
//					  param.put("memberid",userInfo.getMemberid());
//				  }
//				 param.put("page", prlistView.getPage());
//				 param.put("size", Def.M_LIST_SIZE);
//				 ArrayList<TeacherVo> tlist =TeacherDao.getTeacherList(param);
//				 TeacherDao.getTeacherListV2(param);
//				 if( prlistView.getPage()>0 ){
//					 if(tlist!=null){
//						 for(TeacherVo tv :tlist ){
//							 data.add(tv);
//						 }
//						 if(tlist.size()<Def.M_LIST_SIZE){
//							 isFoot = false;
//						 }else{
//							 isFoot = true;
//						 }
//					 }
//				 }else{
//					 data = tlist;
//					 if(tlist!=null&&tlist.size()>0) 
//					     isFoot = true;
//					 else
//						 isFoot = false;
//				 }
//			}catch(EuException ex){ 
//				ex.printStackTrace();
//				exType = 1;	
//				erMsg = ex.getMessage();
//				data = null;
//				isFoot = false;
//			}
//			catch ( Exception e) {
//				 e.printStackTrace();
//				 data = null;
//				 isFoot = false;
//			}
//			return data;
//		}
//		@Override
//		protected void onPostExecute(List<TeacherVo> data) {
//			prlistView.setLoadingGone();
//			if(teacherBaseAdpter==null){
//				teacherBaseAdpter = new TeacherAdapter(TeacherActivity.this, R.layout.list_item, data);
//				prlistView.setAdapter(teacherBaseAdpter);
//			}
//			else{
//				if(prlistView.getAdapter()==null){
//					prlistView.setAdapter(teacherBaseAdpter);
//				}
//				teacherBaseAdpter.setData(data);
//			}
//			prlistView.onRefreshComplete();
//            lv.onPost(data, lv, prlistView, exType, erMsg);
//			prlistView.setShowFooter(isFoot);
//			teacherBaseAdpter.notifyDataSetChanged();
//			super.onPostExecute(data);
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		  case R.id.teacher_search:
			  Intent intent = new Intent(this,TeacherSearchActivity.class);  
		      startActivity(intent);  
			  break;
		  case R.id.filter_latest:
			  mType = 1;
			  mFilterLatest.setBackgroundResource(R.drawable.filter_but_l_s);
			  mFilterLatest.setTextColor(mColorSelect);
			  mFilterPopularity.setTextColor(mColorNotSelect);
			  mFilterStar.setTextColor(mColorNotSelect);
			  mFilterPopularity.setBackgroundResource(R.drawable.filter_but_c);
			  mFilterStar.setBackgroundResource(R.drawable.filter_but_r);
			  prlistView.setPage(0);
//			  new GetDataTask().execute();
			  break;
		  case R.id.filter_popularity:
			  mType = 2;
			  mFilterLatest.setBackgroundResource(R.drawable.filter_but_l);
			  mFilterPopularity.setBackgroundResource(R.drawable.filter_but_c_s);
			  mFilterStar.setBackgroundResource(R.drawable.filter_but_r);
			  mFilterLatest.setTextColor(mColorNotSelect);
			  mFilterPopularity.setTextColor(mColorSelect);
			  mFilterStar.setTextColor(mColorNotSelect);
			  prlistView.setPage(0);
//			  new GetDataTask().execute();
			  break;
		  case R.id.filter_price:
			  mType = 3;
			  mFilterLatest.setBackgroundResource(R.drawable.filter_but_l);
			  mFilterPopularity.setBackgroundResource(R.drawable.filter_but_c);
			  mFilterStar.setBackgroundResource(R.drawable.filter_but_r_s);
			  mFilterLatest.setTextColor(mColorNotSelect);
			  mFilterPopularity.setTextColor(mColorNotSelect);
			  mFilterStar.setTextColor(mColorSelect);
			  prlistView.setPage(0);
//			  new GetDataTask().execute();
			  break;
		  case R.id.menu_control_level1_but:
			  finish();
			  break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		TeacherVo tv = (TeacherVo)data.getAdapter().getItem(position);
		Intent intent = new Intent(this,TeacherInfoActivity.class);  
        Bundle bundle = new Bundle();  
        bundle.putSerializable(Def.OBJ,tv);  
        intent.putExtra(Def.OBJ_Bundle, bundle);
        startActivity(intent);  
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		finish();
    	}
		return super.onKeyDown(keyCode, event);
	}

}
