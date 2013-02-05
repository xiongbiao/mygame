package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.LoginDao;
import erb.unicomedu.ui.KeywordsFlow;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.KeywordsFlowUtil;
import erb.unicomedu.util.LogUtil;

/**
 * 意向课程
 * 
 * @author Administrator
 */

public class IntentionCourseActivity extends PublicActivity implements OnClickListener {
	private final String TAG = "IntentionCourseActivity";
	private KeywordsFlow keywordsFlow;
	private Button mCourseMore;
	private Button mCourseSkip;
	private Button mCourseOk;
	private boolean isOutIn = false;
	private LinearLayout mCourseValues;
	private TextView mCourseSelect;
	private HorizontalScrollView mHorScroll;
	private ImageView hLeft;
	private ImageView hRight;
	private KeywordsFlowUtil mKeyUtil;
	private SensorManager mSensorManager;
	
	//接收前一个界面录入的参数
	private String mUsername;	  //用户名
	private String mPassword;      //密码
	private String mEmail;         //邮箱 
	private String mToClassName;
	private String mFromClassName;
	
	private Bundle mObjBundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.intention_course);
		viewInit();
		initData();
	}
	
	private void initData(){
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
		if(mObjBundle!=null){
			mUsername  = mObjBundle.getString ("username");
			mPassword  = mObjBundle.getString ("password");
			mEmail  = mObjBundle.getString ("email");
			mToClassName = mObjBundle.getString(Def.TO_CLASS_NAME_TAG);
			mFromClassName = mObjBundle.getString(Def.FROM_CLASS_NAME_TAG);
		}
	}
	
	private void viewInit(){
		keywordsFlow = (KeywordsFlow) findViewById(R.id.intention_course_keywordsFlow);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		mKeyUtil = new KeywordsFlowUtil(keywordsFlow);
		mKeyUtil.setUrlType(Def.Intention_KEY);
		KeywordsFlowUtil.AnimationIn();
		
		mCourseMore = (Button) findViewById(R.id.intention_course_more);
		mCourseMore.setOnClickListener(this);

		mHorScroll = (HorizontalScrollView) findViewById(R.id.intention_course_values_scroll);
		mCourseValues = (LinearLayout) findViewById(R.id.intention_course_values);

		mCourseSelect = (TextView) findViewById(R.id.intention_course_select_empty);

		mCourseSkip = (Button) findViewById(R.id.title_skip);
		mCourseSkip.setOnClickListener(this);
		mCourseOk = (Button) findViewById(R.id.title_ok);
		mCourseOk.setOnClickListener(this);

		hLeft = (ImageView) findViewById(R.id.h_left);
		hRight = (ImageView) findViewById(R.id.h_right);
		hLeft.setOnClickListener(this);
		hRight.setOnClickListener(this);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mKeyUtil, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onClick(View view) {
		if (view instanceof TextView && view.getId() < 0) {
			int count = mCourseValues.getChildCount();
			boolean b = true;
			for (int i = 0; i < count; i++) {
				if (view.getId() == mCourseValues.getChildAt(i).getId()) {
					b = false;
				}
			}
			if (b) {
				String keyword = ((TextView) view).getText().toString();
				final TextView txt = new TextView(this);
				txt.setText(keyword);
				txt.setId(((TextView) view).getId());
				txt.setTextColor(Color.rgb(000, 00, 00));
				txt.setGravity(Gravity.CENTER);
				txt.setBackgroundResource(R.drawable.intention_course_select_bg);
				LayoutParams textParams = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				textParams.setMargins(5, 0, 5, 0);
				mCourseValues.addView(txt, textParams);
				mCourseSelect.setVisibility(View.GONE);
				mHorScroll.setVisibility(View.VISIBLE);// arrowScroll
				mHorScroll.arrowScroll(1);
				
				txt.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mCourseValues.removeView(v);
						if(mCourseValues.getChildCount()==0){
							mCourseSelect.setVisibility(View.VISIBLE);
						}
						WindowManager windowManager = getWindowManager();
						Display display = windowManager.getDefaultDisplay();
						Paint pFont = new Paint();
						float wid = pFont.measureText(((TextView)v).getText() + "");
						float maxWidth =  mCourseValues.getWidth() - wid - 10* mCourseValues.getChildCount();
						if (display.getWidth() >maxWidth) {
							hLeft.setVisibility(View.GONE);
							hRight.setVisibility(View.GONE);
						}
					}
				});

				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				Paint pFont = new Paint();
				float wid = pFont.measureText(txt.getText() + "");
				float maxWidth =  mCourseValues.getWidth() + wid + 10* mCourseValues.getChildCount();
				if (display.getWidth() <maxWidth) {
					hLeft.setVisibility(View.VISIBLE);
					hRight.setVisibility(View.VISIBLE);
				}
			}
		}
		
		//按钮事件处理
		switch (view.getId()) {
		case R.id.intention_course_more:
			if (isOutIn) {
				KeywordsFlowUtil.AnimationIn();
				isOutIn = false;
			} else {
				KeywordsFlowUtil.AnimationOut();
				isOutIn = true;
			}
			break;
		case R.id.title_ok:
			Map<String, Object> paramOk = new HashMap<String, Object>();
			paramOk.put("logname",  mUsername);
			paramOk.put("password", mPassword);
			paramOk.put("email", mEmail);
			String intentionid = "";
			String intention = "";
			int size = mCourseValues.getChildCount();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					if (i < size - 1) {
						int item = -1 * mCourseValues.getChildAt(i).getId();
						intentionid = intentionid + item + ",";
						intention = intention + ((TextView) mCourseValues.getChildAt(i)).getText() + ",";
					} else {
						int item = -1 * mCourseValues.getChildAt(i).getId();
						intentionid = intentionid + item;
						intention = intention + ((TextView) mCourseValues.getChildAt(i)).getText();
					}
				}
			}
			paramOk.put("intentionid", intentionid);
			paramOk.put("intention", intention);

			// 注册信息
			String result = LoginDao.RegUserToString(paramOk);
			doRegData(result);
			break;
			
		case R.id.title_skip:
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("logname", mUsername);
			param.put("password", mPassword);
			param.put("email", mEmail);
			// 注册信息
			String resultList = LoginDao.RegUserToString(param);
			doRegData(resultList);
			break;
			
		case R.id.h_right:
			mHorScroll.scrollTo(mHorScroll.getScrollX() + 30,
					mHorScroll.getScrollY());
			break;
			
		case R.id.h_left:
			mHorScroll.scrollTo(mHorScroll.getScrollX() - 30,
					mHorScroll.getScrollY());
			break;
		default:
			break;
		}
	}

	private void doRegData(String result){
		try {
		 if(result!=null){
         	if(Def.REG_RESULT_REPEAT.equals(result)){
         		Toast.makeText(this, "帐号已经被注册", Toast.LENGTH_SHORT).show();
         	}else{
         		//注册成功
         		Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
         		
         		//在本地记录注册成功的userInfoObj对象
         		SharedPreferences settings = getSharedPreferences(Def.PREFS_NAME, 0); 
         		SharedPreferences.Editor editor = settings.edit(); 
         		editor.remove(Def.SP_USERINFO_JSON_OBJ); 
         		editor.putString(Def.SP_USERINFO_JSON_OBJ, result); 
         		//缓存用户的账户，以便下次登录是不用再输入手机号码
         		editor.remove(Def.ACCT_USERID);
         		editor.putString(Def.ACCT_USERID, mUsername);
         		editor.commit(); 
         		
         		//刷新系统用户数据userInfo
         		resetUserInfoVo();
         		
         		//从哪里来回到那里去
         		if(mToClassName!=null&&!"".equals(mToClassName)){
         			 Intent intent = new Intent(this, Class.forName(mToClassName) );
         			 intent.putExtra(Def.OBJ_Bundle, mObjBundle);
             		 startActivity(intent);
             		 finish();
         		}else{//无指定则一律回首页
         			 Intent intent = new Intent(this, HomeActivity.class);
             		 startActivity(intent);
             		 finish();
         		}
         	}
         }else{
        	 Toast.makeText(this, "网络不通或系统繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
         }
		} catch (Exception e) {
			LogUtil.e(TAG, e.getMessage());
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				Intent mIntent = new Intent(IntentionCourseActivity.this, RegActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString(Def.TO_CLASS_NAME_TAG, mToClassName);
				mBundle.putString(Def.FROM_CLASS_NAME_TAG, mFromClassName);
				if(mObjBundle!=null){
					mIntent.putExtra(Def.OBJ_Bundle, mObjBundle);
				} 
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
				finish();
			}
	    	return super.onKeyDown(keyCode, event);
	    }
}
