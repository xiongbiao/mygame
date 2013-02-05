package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import erb.unicomedu.dao.EduinsDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.PointUtil;
import erb.unicomedu.vo.AreaVo;

public class RegionalActivity extends PublicActivity implements OnTouchListener,OnClickListener{

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		mMap.setBackgroundResource(R.drawable.no);
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mMap.setBackgroundResource(R.drawable.no);
		super.onStop();
	}

	private ImageView mMap;
	private FrameLayout mMapAll;
	private List<AreaVo> data;
	private ImageButton mBack;
	private int  rint = -1;
	private PointUtil pUtil;
	private ImageButton home;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regional_map);
		mMap = (ImageView) findViewById(R.id.map_c);
		mMapAll = (FrameLayout) findViewById(R.id.map_all);
		mBack = (ImageButton) findViewById(R.id.obj_info_back);
		mBack.setOnClickListener(this);
		mMapAll.setOnTouchListener(this);
		mMapAll.setOnClickListener(this);
		home = (ImageButton)  findViewById(R.id.menu_control_level1_but);
		home.setOnClickListener(this);
    	
	    pUtil = new PointUtil(); 
		new GetDataTask().execute();
        	
	}
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<AreaVo>> {
		@Override
		protected List<AreaVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("cityid",1);
				data = EduinsDao.getAreaList(param);
			} catch ( Exception e) {
				 e.printStackTrace();
			}
			return data;
		}
		@Override
		protected void onPostExecute(List<AreaVo> data) {
			super.onPostExecute(data);
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.map_all:
			// 纠正x,y坐标值
			double eventX = (double) event.getX();
			double eventY = (double) event.getY() ;
			double y = eventY / (double) mMapAll.getHeight();
			double x = eventX / (double) mMapAll.getWidth();
	        rint = pUtil.getNowPoint(x, y);
	        LogUtil.d("event ", "eventX : " + x + "  eventY : " + y);
			break;
		default:
			break;
		}
		
        return false;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.obj_info_back:
			finish();
			break;	
		 case R.id.menu_control_level1_but:
			  finish();
			  break;
		case R.id.map_all:
			if(data!=null&&data.size()>0){
				Intent mIntent = new Intent(this,EduInsActivity.class); 
				Bundle mBundle = new Bundle();
//				Toast.makeText(this, "sss", Toast.LENGTH_SHORT).show();
				switch (rint) {
				case 0:
					 mBundle.putSerializable(Def.OBJ,data.get(6));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					 mMap.setBackgroundResource(R.drawable.map_hd);
					break;
				case 1:
					 mBundle.putSerializable(Def.OBJ,data.get(10));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					 mMap.setBackgroundResource(R.drawable.map_ch);
					break;
				case 2:
					 mBundle.putSerializable(Def.OBJ,data.get(4));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					 mMap.setBackgroundResource(R.drawable.map_by);
					 break;
				case 3:
					 mBundle.putSerializable(Def.OBJ,data.get(8));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					 mMap.setBackgroundResource(R.drawable.map_lg);
					 break;
				case 4:
					 mBundle.putSerializable(Def.OBJ,data.get(3));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					 mMap.setBackgroundResource(R.drawable.map_th);
					 break;
				case 5:
					 mBundle.putSerializable(Def.OBJ,data.get(2));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					mMap.setBackgroundResource(R.drawable.map_lw);
					break;
				case 6:
					 mBundle.putSerializable(Def.OBJ,data.get(0));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					mMap.setBackgroundResource(R.drawable.map_yx);
					break;
				case 7:
					 mBundle.putSerializable(Def.OBJ,data.get(1));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					mMap.setBackgroundResource(R.drawable.map_hz);
					break;
				case 8:
					 mBundle.putSerializable(Def.OBJ,data.get(5));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					mMap.setBackgroundResource(R.drawable.map_hp);
					break;
				case 9:
					 mBundle.putSerializable(Def.OBJ,data.get(7));  
				     mIntent.putExtras(mBundle); 
				     startActivity(mIntent); 
					mMap.setBackgroundResource(R.drawable.map_py);
					break;
				default:
					break;
				}
					LogUtil.d("event ", "event : " +rint );
		       }
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		LogUtil.d("event ", "ddddddddddd---finish");
    		pUtil.unDel();
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
