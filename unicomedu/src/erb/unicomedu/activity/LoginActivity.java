package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.LoginDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

/**
 * 登录界面
 */
public class LoginActivity extends PublicActivity implements OnClickListener,
		OnCheckedChangeListener {
	private final String TAG = "LoginActivity";

	/** Called when the activity is first created. */
	Button mButLogin; // 登录
	Button mButRegister; // 注册
	ImageButton mButBack; // 退出
	EditText loginNameEdit; // 帐号输入
	EditText passwrodEdit; // 密码输入
	TextView mOperators; // 运营商信息
	CheckBox mSaveBox; // 保存密码选项
	CheckBox mAutoLoginBox; // 自动登录选项
	String username; // 存储用户名
	String password; // 存储密码
	boolean isSavePassword; // 当前保存密码选项的状态
	boolean isAutoLogin; // 当前自动登录选项的状态
	private String mToClassName;
	private String mFromClassName;
	private Bundle mDataBundle;
	
	/** RMS */
	SharedPreferences mSettings;
	SharedPreferences.Editor mEditor;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mButLogin = (Button) findViewById(R.id.login_in);
		mButLogin.setOnClickListener(this);
		mButRegister = (Button) findViewById(R.id.login_registered);
		mButRegister.setOnClickListener(this);
		mButBack = (ImageButton) findViewById(R.id.title_back);
		mButBack.setOnClickListener(this);
		loginNameEdit = (EditText) findViewById(R.id.login_name);
		passwrodEdit = (EditText) findViewById(R.id.login_password);
		mOperators = (TextView) findViewById(R.id.login_operators);
		mSaveBox = (CheckBox) findViewById(R.id.login_save_password);
		mSaveBox.setOnCheckedChangeListener(this);
		mAutoLoginBox = (CheckBox) findViewById(R.id.login_auto_in);
		mAutoLoginBox.setOnCheckedChangeListener(this);
		mToClassName = getIntent().getStringExtra(Def.TO_CLASS_NAME_TAG);
		mFromClassName = getIntent().getStringExtra(Def.FROM_CLASS_NAME_TAG);
		mDataBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSettings = getSharedPreferences(Def.PREFS_NAME, MODE_PRIVATE);
		String userID 	= mSettings.getString(Def.ACCT_USERID, "");
		String userPwd 	= mSettings.getString(Def.ACCT_USERPWD, "");
		String savePwd  = mSettings.getString(Def.ACCT_USERPWD_SAVE, "");
		String autoLogin= mSettings.getString(Def.ACCT_AUTOLOGIN, "");
		
		LogUtil.d(TAG,"userID="+userID+",pwd="+userPwd+",savePwd="+savePwd+",autoLogin="+autoLogin);
		//如果有存储用户账户，显示账户ID
		if (!"".equals(userID))
			loginNameEdit.setText(userID);
		//如果记住密码，赋值密码控件
		if (Def.ACCT_USERPWD_SAVE_KEYWORD.equals(savePwd))
		{
			mSaveBox.setChecked(true);
			if (!"".equals(userPwd)) {
				passwrodEdit.setText(userPwd);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_in:
			username = loginNameEdit.getText().toString().trim();
			password = passwrodEdit.getText().toString().trim();
			// 帐户密码有效检验
			if (TextUtils.isEmpty(username)) {
				Toast.makeText(LoginActivity.this,
						R.string.error_username_empty, Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (TextUtils.isEmpty(password)) {
				Toast.makeText(LoginActivity.this,
						R.string.error_password_empty, Toast.LENGTH_SHORT)
						.show();
				return;
			} else if (password.length() < 6) {
				Toast.makeText(LoginActivity.this, R.string.error_password,
						Toast.LENGTH_SHORT).show();
				return;
			}

			boolean isExist = true;// LoginDao.ExistUser(usParam);
			if (!isExist) {
				Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
			} else {
				// 联网登录校验
				try {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("logname", username);
					param.put("password", password);
					String resultUserInfoJson = LoginDao.LoginString(param);
					if (resultUserInfoJson != null && !resultUserInfoJson.equals("")&& !Def.TO_CLASS_NAME_TAG.equals(resultUserInfoJson)) {
						if (resultUserInfoJson.equals(Def.USER_LOGIN_RESULT)) {
							Toast.makeText(LoginActivity.this, "帐号或者密码不对", Toast.LENGTH_SHORT).show();
						} else {
							finish();
							//记录当前用户的账户ID、密码、自动登陆等
							mEditor = mSettings.edit();
							mEditor.remove(Def.SP_USERINFO_JSON_OBJ);
							mEditor.putString(Def.SP_USERINFO_JSON_OBJ, resultUserInfoJson);	//UserInfo返回的Json对象整块存储
							
							mEditor.remove(Def.ACCT_USERID);
							mEditor.putString(Def.ACCT_USERID, username);	//User账户
							mEditor.remove(Def.ACCT_USERPWD);
							mEditor.putString(Def.ACCT_USERPWD, password);	//User密码
							if (mSaveBox.isChecked())
								mEditor.putString(Def.ACCT_USERPWD_SAVE, Def.ACCT_USERPWD_SAVE_KEYWORD);//记住密码
							else {
								mEditor.remove(Def.ACCT_USERPWD_SAVE);
								mEditor.remove(Def.ACCT_USERPWD);//取消记录密码，也要清理本地RMS
							}
							if (mAutoLoginBox.isChecked())
								mEditor.putString(Def.ACCT_AUTOLOGIN, Def.ACCT_AUTOLOGIN_KEYWORD);		//自动登陆
							else
								mEditor.remove(Def.ACCT_AUTOLOGIN);
							mEditor.commit();
							//刷新UserInfo对象数据
							resetUserInfoVo();
							
							
							// 从哪里来回那里去
							if (mToClassName != null && !"".equals(mToClassName)) {
								Intent intent = new Intent(this, Class.forName(mToClassName));
								if(mDataBundle!=null){
								    intent.putExtra(Def.OBJ_Bundle, mDataBundle);
								    LogUtil.d(TAG, "从哪里来回那里去");
								} 
								startActivity(intent);
							} else {//无指定，一律回首页
								Intent intent = new Intent(this,HomeActivity.class);
								startActivity(intent);
							}
						}
					} else {  //登陆失败，可能网络或后台服务异常无响应
						Toast.makeText(LoginActivity.this, "登陆失败，请检查网络连接或稍后重试。", Toast.LENGTH_SHORT).show();
						//这个时候，是否应该不做任何跳转了，而停留在本登陆界面，让用户自己选择
//						finish();
//						Intent intent = new Intent(this,Class.forName(mFromClassName));
//						if(mREBundle!=null){
//						    intent.putExtra(Def.OBJ_Bundle, mREBundle);
//						} 
//						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			break;
		case R.id.login_registered:
			Intent mIntent = new Intent(LoginActivity.this, RegActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putString(Def.TO_CLASS_NAME_TAG, mToClassName);
			mBundle.putString(Def.FROM_CLASS_NAME_TAG, mFromClassName);
			if(mDataBundle!=null){
				mIntent.putExtra(Def.OBJ_Bundle, mDataBundle);
			} 
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			finish();
			break;
		case R.id.title_back:
			try {
				finish();
				//从哪里来回那里去
				if (mFromClassName != null && !"".equals(mFromClassName)) {
					if(!mFromClassName.endsWith("HomeActivity")){
						Intent intent = new Intent(this, Class.forName(mFromClassName));
						if(mDataBundle!=null){
							intent.putExtra(Def.OBJ_Bundle, mDataBundle);
						} 
						startActivity(intent);
					}
				} else {//无指定，一律回首页
//					Intent intent = new Intent(this, HomeActivity.class);
//					startActivity(intent);
				}
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				finish();
				//从哪里来回那里去
				if (mFromClassName != null && !"".equals(mFromClassName)) {
					if(!mFromClassName.endsWith("HomeActivity")){
						Intent intent = new Intent(this, Class.forName(mFromClassName));
						if(mDataBundle!=null){
							intent.putExtra(Def.OBJ_Bundle, mDataBundle);
						} 
						startActivity(intent);
					}
				} else {//无指定，一律回首页
//					Intent intent = new Intent(this, HomeActivity.class);
//					startActivity(intent);
				}
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		//更新本地机的对应标记
		mEditor = mSettings.edit();
		switch (buttonView.getId()) {
		case R.id.login_save_password:
			LogUtil.d(TAG, "记住密码="+mSaveBox.isChecked());
			mEditor.remove(Def.ACCT_USERPWD_SAVE);
			if (mSaveBox.isChecked()) {
				mEditor.putString(Def.ACCT_USERPWD_SAVE, Def.ACCT_USERPWD_SAVE_KEYWORD);	//记住密码
			}
			else {
				//取消记录密码，也要清理本地RMS
				mEditor.remove(Def.ACCT_USERPWD);
			}
			break;
		case R.id.login_auto_in:
			LogUtil.d(TAG, "自动登陆="+mAutoLoginBox.isChecked());
			mEditor.remove(Def.ACCT_AUTOLOGIN);
			if (mAutoLoginBox.isChecked()) {
				mSaveBox.setChecked(true);  //自动登陆，一定要记录密码，反之则不处理
				mEditor.putString(Def.ACCT_AUTOLOGIN, Def.ACCT_AUTOLOGIN_KEYWORD);	//自动登陆
			}
			break;
		}
		
		mEditor.commit();
	}
	 
}