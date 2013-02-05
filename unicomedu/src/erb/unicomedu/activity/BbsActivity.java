package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ExpandableListView.OnChildClickListener;
import erb.unicomedu.adapter.MExpandableListAdapter;
import erb.unicomedu.dao.BbsDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.vo.BbsVo;

public class BbsActivity extends PublicActivity implements OnClickListener,OnChildClickListener{
	private String TAG = "BbsActivity"; 
	private Button mFilter;
	private List<BbsVo> data;
	private ExpandableListView prlistView;
	private MExpandableListAdapter bbsBaseAdpter;

	private int mType = 1;
	
	LayoutInflater inflater;
	List<BbsVo> group;           //组列表
	List<List<BbsVo>> child;     //子列表
	private ImageButton home;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		String className =  this.getClass().getName();
		String fromClassName =  new HomeActivity().getClass().getName();
		isModel(Def.MODEl_BBS_list, className,fromClassName,null);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_list);
		inflater = this.getLayoutInflater();
		prlistView = (ExpandableListView) findViewById(R.id.bbs_home_list);
		data = new ArrayList<BbsVo>();
		new GetDataTask().execute();
		prlistView.setOnChildClickListener(this);
		
		mFilter = (Button)findViewById(R.id.my_bbs);
		mFilter.setFocusable(true);
		mFilter.setClickable(true);
		mFilter.setOnClickListener(this);
		home = (ImageButton)  findViewById(R.id.menu_control_level1_but);
		home.setOnClickListener(this);
	}
	
	
	  
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<BbsVo>> {
		@Override
		protected List<BbsVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				 param.put("orderby",mType);
				data = BbsDao.getBbsList(param);
			} catch (Exception e) {
				 e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<BbsVo> data) {
			InitializeData(data);
			bbsBaseAdpter = new MExpandableListAdapter(BbsActivity.this, group, child, inflater);
			prlistView.setAdapter(bbsBaseAdpter);
			prlistView.setCacheColorHint(0);
			prlistView.setGroupIndicator(null);
			prlistView.postInvalidate();
			bbsBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	/**
     * 初始化组、子列表数据
     */
    private void InitializeData(List<BbsVo> data){
    	group = new ArrayList<BbsVo>();
    	child = new ArrayList<List<BbsVo>>();
    	
    	if(data!=null){
    	for(int i=0; i<data.size(); i++) {
    		if("0".equals(data.get(i).getParentid())) {
    			group.add(data.get(i));
    		}
    	}
    	}
    	for(int j=0; j<group.size(); j++) {
    		List<BbsVo> childitem = new ArrayList<BbsVo>();
    		for(int k=0; k<data.size(); k++) {
    			if(data.get(k).getParentid().equals(group.get(j).getTopicid())) {
    				childitem.add(data.get(k));
    			}
    		}
    		child.add(childitem);
    	}
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.my_bbs:
				
				 String fromClassName =  this.getClass().getName();
				 String className =  new BbsMyThemeActivity().getClass().getName();
				boolean result = isModel(Def.MODEl_BBS_MY, className,fromClassName,null);
				if(result){
					Intent mIntent = new Intent(this,BbsMyThemeActivity.class);  
					startActivity(mIntent); 
				}
				break;
			 case R.id.menu_control_level1_but:
				  finish();
				  break;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		BbsVo bv = child.get(groupPosition).get(childPosition);
		
		Intent mIntent = new Intent(this,BbsinfoActivity.class);  
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Def.OBJ,bv);  
        mIntent.putExtra(Def.OBJ_Bundle,mBundle); 
        startActivity(mIntent); 
		return false;
	};
	
    @Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
