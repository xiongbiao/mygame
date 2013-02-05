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
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import erb.unicomedu.adapter.MsgAdapter;
import erb.unicomedu.dao.MsgDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsVo;
import erb.unicomedu.vo.MsgTypeVo;
import erb.unicomedu.vo.MsgVo;

public class MsgActivity extends PublicActivity implements OnClickListener ,OnChildClickListener{
    private ExpandableListView elist;
    private ImageButton mBack;
    private List<MsgTypeVo> data;
    private MsgAdapter mAdapter;
    List<MsgTypeVo> group; // 组列表
	List<List<MsgVo>> child; // 子列表
	private String 	isService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_list);
		init();
		initData();
	}
	private void init(){
		elist = (ExpandableListView)findViewById(R.id.msg_list);
		elist.setOnChildClickListener(this);
		mBack = (ImageButton) findViewById(R.id.obj_info_back);
		mBack.setOnClickListener(this);
		data = new ArrayList<MsgTypeVo>();
	}
	private void initData(){
		isService = getIntent().getStringExtra("isService");
		new GetDataTask().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.obj_info_back:
			if(isService!=null&&"true".equals(isService)){
				Intent intent = new Intent(this,HomeActivity.class);
				startActivity(intent);
			}
			finish();
			break;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
        MsgVo bv = group.get(groupPosition).getMsgList().get(childPosition);
		Intent mIntent = new Intent(this,MsgInfoActivity.class);  
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Def.OBJ,bv);  
        mIntent.putExtra(Def.OBJ_Bundle,mBundle); 
        startActivity(mIntent); 
		return false;
	}
	
	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<MsgTypeVo>> {
		@Override
		protected List<MsgTypeVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("page", 0);
				param.put("size", 30);
				data =  MsgDao.getMsgList(param);
			} catch (Exception e) {
				 e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<MsgTypeVo> data) {
			initData( data) ;
			mAdapter = new MsgAdapter(MsgActivity.this, group,child);
			elist.setAdapter(mAdapter);
			elist.setCacheColorHint(0);
			elist.setGroupIndicator(null);
			elist.postInvalidate();
			if(group!=null){
			for(int i =0 ;i<group.size();i++){
				elist.expandGroup(i);
			}
			}
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
		
		
	}
	
	/**
	 * 初始化组、子列表数据
	 */
	private void initData(List<MsgTypeVo> data) {
		group = new ArrayList<MsgTypeVo>();
		child = new ArrayList<List<MsgVo>>();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				   group.add(data.get(i));
			}
			for (int j = 0; j < group.size(); j++) {
				List<MsgVo> childitem =  group.get(j).getMsgList();
				child.add(childitem);
			}
		}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if(isService!=null&&"true".equals(isService)){
				Intent intent = new Intent(this,HomeActivity.class);
				startActivity(intent);
			}
			finish();
		}
    	return super.onKeyDown(keyCode, event);
    }
}
