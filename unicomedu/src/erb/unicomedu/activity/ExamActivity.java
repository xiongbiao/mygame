package erb.unicomedu.activity;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.ExamAdapter;
import erb.unicomedu.adapter.ReadAdapter;
import erb.unicomedu.adapter.SuperTreeViewAdapter;
import erb.unicomedu.adapter.TreeViewAdapter;
import erb.unicomedu.dao.ExamDao;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.ui.FilterExpand;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.DataAsyncTask;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.ExamVo;
import erb.unicomedu.vo.ReadVo;
import erb.unicomedu.vo.TreeVo;

/**
 * 在线考试
 * @author xiong
 *
 */
public class ExamActivity extends PublicActivity implements OnClickListener , OnItemClickListener{
	
	private PullToRefreshListView prlistView;
	private List<ExamVo> data;
	private ExamAdapter examBaseAdpter;
	private Button mFilter;
	private Button mLatest;
	private Button mPopularity;
	private Button mPrice;
	private FilterExpand mFilterExpand;
	private LinearLayout filterContent;
	private ImageButton home;
	private ImageButton mSearch;
	
	private int mType = 1;
	private int mColorSelect = Color.rgb(255, 255, 255);
	private int mColor =Color.rgb(169, 161, 118);
	
	private ExpandableListView mNavEListView;
	private SuperTreeViewAdapter superAdapter;
	private LoadingView lv;
	private String typeid = "";
	private String subjectid = "";
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		String className =  this.getClass().getName();
		String fromClassName =  new HomeActivity().getClass().getName();
		isModel(Def.MODEl_EX_LIST, className,fromClassName,null);
		new GetDataTask().execute();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.exam_list);
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
				new GetDataTask().execute();
			}
		});
		
		data = new ArrayList<ExamVo>();
	
		prlistView.setOnItemClickListener(this);
		
		mFilter = (Button)findViewById(R.id.online_read_filter);
		mFilter.setFocusable(true);
		mFilter.setClickable(true);
		mFilter.setOnClickListener(this);
		mLatest = (Button)findViewById(R.id.filter_latest);
		mLatest.setOnClickListener(this);
		mPopularity = (Button)findViewById(R.id.filter_popularity);
		mPopularity.setOnClickListener(this);
		mPrice = (Button)findViewById(R.id.filter_price);
		mPrice.setOnClickListener(this);
		
		filterContent = (LinearLayout)findViewById(R.id.filter_content);
		
		mFilterExpand = (FilterExpand)findViewById(R.id.filter_expand);
		home = (ImageButton)  findViewById(R.id.menu_control_level1_but);
		home.setOnClickListener(this);
		mSearch = (ImageButton)findViewById(R.id.search_value);
		mSearch.setOnClickListener(this);
		loadAnim(filterContent);
		initNav();
	}
	private void initNav(){
		// 导航 begin
		mNavEListView = (ExpandableListView)findViewById( R.id.nav_elv);
		superAdapter=new SuperTreeViewAdapter(this,stvClickEvent); 
		superAdapter.setHeight(navHeight);
        ArrayList<TreeVo> tlist = new ArrayList<TreeVo>() ; 
		SharedPreferences settings = getSharedPreferences(Def.PREFS_NAME, 0); 
		String njson = settings.getString (Def.SP_NAV_NAME, ""); 
		try {
			if(njson==null||"".equals(njson)){
				new DataAsyncTask(this,false).execute("2");
			}
			JSONArray jArr = new JSONArray(njson);
			tlist = SubjectDao.getArrayToTree(jArr);
		} catch (Exception e) {
         e.printStackTrace();
		}
		//
		List<SuperTreeViewAdapter.SuperTreeNode> superTreeNode = superAdapter.GetTreeNode();
		
		if(tlist!=null&&tlist.size()>0){
			for(int i=0;i<tlist.size();i++){
				if(tlist.get(i).getParentid()==null || tlist.get(i).getParentid().equals("0")){
					SuperTreeViewAdapter.SuperTreeNode superNode=new SuperTreeViewAdapter.SuperTreeNode();
		        	superNode.parent=tlist.get(i);
		        	for(int j =0;j<tlist.size();j++){
		        		if(tlist.get(i).getTreeId().equals(tlist.get(j).getParentid())){
		        			TreeViewAdapter.TreeNode node=new TreeViewAdapter.TreeNode();
			            	node.parent= tlist.get(j) ;
			            	for(int k =0;k<tlist.size();k++){
			            		if(tlist.get(j).getTreeId().equals(tlist.get(k).getParentid())){
			            			node.childs.add(tlist.get(k));
			            		}
			            	}
			            	superNode.childs.add(node);
		        		}
		        	}
		        	superTreeNode.add(superNode);
				}
			}
		}
        superAdapter.UpdateTreeNode(superTreeNode);
        mNavEListView.setAdapter(superAdapter);
        mNavEListView.setOnGroupExpandListener(new OnGroupExpandListener(){
        	@Override
        	 public void onGroupExpand(int arg0) {
        	         for(int i=0;i<superAdapter.getGroupCount();i++)
        	         {   if(arg0!=i)
        	             {
        	                	 mNavEListView.collapseGroup(i);
        	             }
        	         }
        	}
       });
    	// 导航 end
	}
	
	/**
     * 三级树形菜单的事件不再可用，本函数由三级树形菜单的子项（二级菜单）进行回调
     */
    OnChildClickListener stvClickEvent=new OnChildClickListener(){

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			if (v.findViewById(R.id.filter_list_item_item) instanceof TextView ){
				TextView    textView = (TextView)v.findViewById(R.id.filter_list_item_item);;
				TreeVo treeVo = (TreeVo)textView.getTag();
				typeid = treeVo.getTreeId();
				new GetDataTask().execute();
			}
			return false;
		}
    	
    };
	
	@Override
	public void onClick(View v) {
		updateFilterButtonBackground(v.getId());
		switch (v.getId()) {
			case R.id.online_read_filter:
				if(filterContent.getVisibility()==0){
					mFilterExpand.hideMoreListView();
					filterContent.startAnimation(mAnim);
					filterContent.setVisibility(View.INVISIBLE);
				}else{
					mFilterExpand.openMoreListView();
					filterContent.startAnimation(mLeftAnim);
					filterContent.setVisibility(View.VISIBLE);
				}
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
				 prlistView.setPage(0);
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
				 prlistView.setPage(0);
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
				 prlistView.setPage(0);
				  new GetDataTask().execute();
				break;
			case R.id.search_value:
				Intent mIntent = new Intent(this,ExamSearchActivity.class);  
		        startActivity(mIntent); 
				break;
			 case R.id.menu_control_level1_but:
				  finish();
				  break;
		}
	}
	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<ExamVo>> {
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
		protected List<ExamVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("subjectid", subjectid);
				 param.put("orderby",mType);
			     param.put("typeid", typeid);
			     param.put("page", prlistView.getPage());
				  param.put("size", Def.M_LIST_SIZE);
				 if(userInfo!=null){
					  param.put("memberid",userInfo.getMemberid());
				 }
				 List<ExamVo> tlist = ExamDao.getExamList (param); 
				if( prlistView.getPage()>0 ){
					 if(tlist!=null){
						 for(ExamVo tv :tlist ){
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
		protected void onPostExecute(List<ExamVo> data) {
			 
			prlistView.setLoadingGone();
			if(examBaseAdpter==null){
				examBaseAdpter = new ExamAdapter(ExamActivity.this, R.layout.exam_item, data);
				prlistView.setAdapter(examBaseAdpter);
			}
			else{
				if(prlistView.getAdapter()==null){
					prlistView.setAdapter(examBaseAdpter);
				}
				examBaseAdpter.setData(data);
			}
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(isFoot);
		    examBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		ExamVo tv = (ExamVo)data.getAdapter().getItem(position);
		Intent mIntent = new Intent(this,ExamInfoActivity.class);  
        Bundle mBundle = new Bundle();  
        mBundle.putSerializable(Def.OBJ,tv);  
        mIntent.putExtra(Def.OBJ_Bundle, mBundle);
        startActivity(mIntent);  
	}
	
    @Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(mFilterExpand.isOpen()){
    		mFilter.performClick();
			return true;
    	}
    	else
    	{
	    	if(keyCode == KeyEvent.KEYCODE_BACK) {
	    		//按手机返回键则返回首页
	    		finish();
	    	}
	    	return super.onKeyDown(keyCode, event);
    	}
    };
}
