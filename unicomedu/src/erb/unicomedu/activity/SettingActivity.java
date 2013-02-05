package erb.unicomedu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

/**
 * 设置
 * 
 */
public class SettingActivity extends PublicActivity implements OnClickListener {
	private final String TAG = "SettingActivity";
	private ImageButton mBack;
	private RelativeLayout mHelp;
	private RelativeLayout mFeedBack;
	private RelativeLayout mLogin;
	private RelativeLayout mAbout;
	private TextView tvUserinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting);

		viewInit();
	}
	
	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}

	private void viewInit() {
		mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setOnClickListener(this);
		mHelp = (RelativeLayout) findViewById(R.id.setting_help);
		mHelp.setOnClickListener(this);
		mFeedBack = (RelativeLayout) findViewById(R.id.setting_feedback);
		mFeedBack.setOnClickListener(this);
		mLogin = (RelativeLayout) findViewById(R.id.setting_login);
		mLogin.setOnClickListener(this);
		mAbout = (RelativeLayout) findViewById(R.id.setting_about);
		mAbout.setOnClickListener(this);

		tvUserinfo = (TextView) findViewById(R.id.tv_userinfo);

	}

	private void initData() {
		LogUtil.d(TAG, "userinfo="+userInfo);
		if (userInfo != null&&!"0".equals(userInfo.getMemberid())) {
			tvUserinfo.setText("注销("+userInfo.getUserName()+")");
		} else {
			tvUserinfo.setText("登录或注册");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.setting_help:
			startActivity(new Intent(SettingActivity.this, HelpActivity.class));
			break;
		case R.id.setting_login:
			if (userInfo == null||"0".equals(userInfo.getMemberid())) {
				finish();
				String className = this.getClass().getName();
				Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString(Def.TO_CLASS_NAME_TAG, className);
				intent.putExtras(mBundle);
				startActivity(intent);
			} else {
				//虚假退出，目前没有向后台提交退出请求
				
				//清除SharedPreferences的本地userInfoJsonOBJ对象数据
				SharedPreferences settings = getSharedPreferences(Def.PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.remove(Def.SP_USERINFO_JSON_OBJ);
				editor.commit();
				//重置用户信息实例数据
				resetUserInfoVo();
				//清理已签到标记
//				MyApplication.signNum = 0;
				initData();
				Toast.makeText(this, "已退出登陆", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.setting_about:
			startActivity(new Intent(SettingActivity.this, AboutActivity.class));
			break;
		case R.id.setting_feedback:
			startActivity(new Intent(SettingActivity.this, FeedbackActivity.class));
			break;
		default:
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
