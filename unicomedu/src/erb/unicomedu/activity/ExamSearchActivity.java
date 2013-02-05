package erb.unicomedu.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.ui.KeywordsFlow;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.KeywordsFlowUtil;
import erb.unicomedu.vo.KeyVo;

public class ExamSearchActivity extends PublicActivity implements OnClickListener{
	private KeywordsFlow keywordsFlow;
	private Button  mCourseMore;
	private EditText mSearchValue;
	private ImageButton mBack;
	private ImageButton mSearch;
	private  boolean isOutIn = false;
	private KeywordsFlowUtil mKeyUtil;
	private SensorManager mSensorManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.exam_search);
		init();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(mKeyUtil);
		super.onStop();
	}
	private void init(){
		keywordsFlow = (KeywordsFlow) findViewById(R.id.intention_course_keywordsFlow);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		mKeyUtil = new KeywordsFlowUtil(keywordsFlow);
		mKeyUtil.setUrlType(Def.Exam_KEY);
		KeywordsFlowUtil.AnimationIn();
		
		mCourseMore = (Button) findViewById(R.id.intention_course_more);
		mCourseMore.setOnClickListener(this);
		
		mSearchValue = (EditText)findViewById(R.id.obj_search_value);
		mSearchValue.setOnKeyListener(onKey); 
		
		mBack = (ImageButton) findViewById(R.id.top_back);
		mBack.setOnClickListener(this);
		mSearch = (ImageButton) findViewById(R.id.obj_search);
		mSearch.setOnClickListener(this);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mKeyUtil,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	} 
	
	@Override
	public void onClick(View v) {
		
		if (v instanceof TextView && v.getId()< 0) {
				String keyword = ((TextView) v).getText().toString();
				mSearchValue.setText(keyword);
				Intent mIntent = new Intent(this,ExamResultActivity.class);  
		        Bundle mBundle = new Bundle();  
		        mBundle.putString(Def.WHERE_KEY,keyword);  
		        mIntent.putExtras(mBundle); 
		        startActivity(mIntent);  
		}
		switch (v.getId()) {
		case R.id.intention_course_more:
			if (isOutIn) {
				KeywordsFlowUtil.AnimationIn();
				isOutIn = false;
			} else {
				KeywordsFlowUtil.AnimationOut();
				isOutIn = true;
			}
			break;
		case R.id.top_back:
			finish();
			break;	
		case R.id.obj_search:
			String keyword  = mSearchValue.getText().toString();
			if(keyword==null || "".equals(keyword)){
				Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT).show();
			}else{
				KeyVo kv = new KeyVo();
				kv.setKeyValue(keyword);
				KeywordsFlowUtil.addKeyVo(kv);
				mSensorManager.unregisterListener(mKeyUtil);
				Intent mIntent = new Intent(this,ExamResultActivity.class);  
		        Bundle mBundle = new Bundle();  
		        mBundle.putString(Def.WHERE_KEY,keyword);  
		        mIntent.putExtras(mBundle); 
		        startActivity(mIntent); 
	        }
			break;		
		default:
			break;
		}		
	}

	OnKeyListener onKey = new OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if ((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode == KeyEvent.KEYCODE_ENTER)) {
				mSearch.performClick();
				return true;
			}
			return false;
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		mSensorManager.unregisterListener(mKeyUtil);
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
