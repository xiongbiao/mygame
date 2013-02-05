package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import erb.unicomedu.adapter.EduinsAdapter;
import erb.unicomedu.dao.EduinsDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.AreaVo;
import erb.unicomedu.vo.EduinsVo;

/**
 * 教育机构 主界面 Educational institutions
 * 列表  暂时不用
 */
public class EduInsActivity extends PublicActivity implements OnClickListener,OnItemClickListener {
    private String TAG = "EduInsActivity";
	private ImageButton mBack;
	private LinearLayout mLExqcx;
	private TextView eduinsName;
	List<EduinsVo> data;
	private PullToRefreshListView prlistView;
	private EduinsAdapter eduinsBaseAdpter;
	private AreaVo mAv;
	private LoadingView lv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eduins);
		viewInit();
	}

	private void viewInit() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		eduinsName = (TextView) findViewById(R.id.eduins_name);
		mBack = (ImageButton) findViewById(R.id.eduins_back);
		mBack.setOnClickListener(this);
		mLExqcx = (LinearLayout) findViewById(R.id.edu_ll_xqjs);
		mAv = (AreaVo) getIntent().getSerializableExtra(Def.OBJ);
		eduinsName.setText(mAv.getDistrict());
		
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

		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
	}

	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<EduinsVo>> {
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
		protected List<EduinsVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("districtid", mAv.getDistrcitid());
				data = EduinsDao.getEduinsList(param);
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
		protected void onPostExecute(List<EduinsVo> data) {
			eduinsBaseAdpter = new EduinsAdapter(EduInsActivity.this, R.layout.eduins_item, data);
			prlistView.setAdapter(eduinsBaseAdpter);
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.onRefreshComplete();
			eduinsBaseAdpter.notifyDataSetChanged();
			prlistView.setShowFooter(false);
			super.onPostExecute(data);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filter_xqcx:
//			mLinfo.setVisibility(View.GONE);
			mLExqcx.setVisibility(View.VISIBLE);
			break;
		case R.id.eduins_back:
			finish();
			break;	
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		EduinsVo tv = (EduinsVo)data.getAdapter().getItem(position);
		Intent mIntent = new Intent(this,EduInsInfoActivity.class);  
        Bundle mBundle = new Bundle();  
        mBundle.putSerializable(Def.OBJ,tv);  
        mIntent.putExtra(Def.OBJ_Bundle,mBundle); 
        startActivity(mIntent);  
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
