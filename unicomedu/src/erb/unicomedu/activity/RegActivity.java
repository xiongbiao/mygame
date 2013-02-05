package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import erb.unicomedu.dao.LoginDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;

/***
 * 注册界面
 */
public class RegActivity extends Activity implements OnClickListener{
	private final String TAG = "RegActivity";
	
	private String mToClassName;
	private String mFromClassName;
	
	
	Button mSubmit;       //提交注册
	ImageButton mButBack; //返回按钮
	String username;	  //用户名
	String password;      //密码
	String email;         //邮箱
	EditText usernameEdit;
	EditText passwordEdit;
	EditText emailEdit;
	private Bundle mDataBundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		mSubmit = (Button) findViewById(R.id.register_submit);
		mSubmit.setOnClickListener(this);
		mButBack = (ImageButton) findViewById(R.id.title_back);
		mButBack.setOnClickListener(this);
		mToClassName = getIntent().getStringExtra(Def.TO_CLASS_NAME_TAG);
		mFromClassName = getIntent().getStringExtra(Def.FROM_CLASS_NAME_TAG);
		usernameEdit = (EditText) findViewById(R.id.register_name);
		passwordEdit = (EditText) findViewById(R.id.register_password);
		emailEdit = (EditText) findViewById(R.id.register_email);
		usernameEdit.setFocusableInTouchMode(true);
		usernameEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					LogUtil.d(TAG, "is true");
				} else {
					LogUtil.d(TAG, "is not true");
					username = usernameEdit.getText().toString().trim();
					Map<String, Object> usParam = new HashMap<String, Object>();
					usParam.put("logname", username);
					boolean isExist = LoginDao.ExistUser(usParam);
					if (TextUtils.isEmpty(username)) {
						Toast.makeText(RegActivity.this,R.string.error_username_empty,Toast.LENGTH_SHORT).show();
						return;
					} else if (username.length() != 11) {
						Toast.makeText(RegActivity.this,R.string.error_username, Toast.LENGTH_SHORT).show();
						return;
					}
					if (isExist) {
						Toast.makeText(RegActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		mDataBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				Intent mIntent = new Intent(RegActivity.this, LoginActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putString(Def.TO_CLASS_NAME_TAG, mToClassName);
				mBundle.putString(Def.FROM_CLASS_NAME_TAG, mFromClassName);
				if(mDataBundle!=null){
					mIntent.putExtra(Def.OBJ_Bundle, mDataBundle);
				} 
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
				finish();
			} catch (Exception e) {
				LogUtil.d(TAG, e.getMessage());
			}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.register_submit:
				username = usernameEdit.getText().toString().trim();
				password = passwordEdit.getText().toString().trim();
				email = emailEdit.getText().toString().trim();
				
				// 帐户密码邮箱有效检验
				if(TextUtils.isEmpty(username)){
					Toast.makeText(RegActivity.this, R.string.error_username_empty, Toast.LENGTH_SHORT).show();
					return;
				}else if(username.length() != 11){
					Toast.makeText(RegActivity.this, R.string.error_username, Toast.LENGTH_SHORT).show();
					return;
				}else if(TextUtils.isEmpty(email)){
					Toast.makeText(RegActivity.this, R.string.error_email_empty, Toast.LENGTH_SHORT).show();
					return;
				}else if(!checkMatcherEmail(email)){
					Toast.makeText(RegActivity.this, R.string.error_email, Toast.LENGTH_SHORT).show();
					return;
				}else if(TextUtils.isEmpty(password)){
					Toast.makeText(RegActivity.this, R.string.error_password_empty, Toast.LENGTH_SHORT).show();
					return;
				}else if(password.length() < 6){
					Toast.makeText(RegActivity.this, R.string.error_password, Toast.LENGTH_SHORT).show();
					return;
				}
				
				Map<String, Object> usParam =new HashMap<String, Object>();
    			usParam.put("logname", usernameEdit.getText().toString().trim());
    		    boolean isExist = LoginDao.ExistUser(usParam);
    			if(isExist){
    				Toast.makeText(RegActivity.this, "用户名已存在，请换其他用户名", Toast.LENGTH_SHORT).show();
    			} else{
    				//用户账户合法，跳转进入意向课程选择界面
					Intent tmpIntent = new Intent(this,IntentionCourseActivity.class);
					
					//把账户名称、邮箱、密码往下传递
					  Bundle regDataBundle = null;
					if(mDataBundle==null){
						regDataBundle = new Bundle();
					}else{
						regDataBundle = mDataBundle ;
					}
			        
			        regDataBundle.putString("username",username);
			        regDataBundle.putString("password", password);
			        regDataBundle.putString("email", email);
			        //绑定对象数据
			        if(mToClassName!=null&&!"".equals(mToClassName)){
			            regDataBundle.putString(Def.TO_CLASS_NAME_TAG, mToClassName);
			        }
			        if(mFromClassName!=null&&!"".equals(mFromClassName)){
			           regDataBundle.putString(Def.FROM_CLASS_NAME_TAG, mFromClassName);
			        }
			        
			        regDataBundle.putString("email", email);
			        tmpIntent.putExtra(Def.OBJ_Bundle,regDataBundle); 
			        //这里上一级传递的Bundle呢？
			        startActivity(tmpIntent); 
			        finish();
    			}
				break;
			case R.id.title_back:
				Intent mIntent = new Intent(RegActivity.this, LoginActivity.class);
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
		}
	}
	
	/**
	 * 邮箱有效性检查   
	 * @param emailAddress 
	 * @return false 不合法   true 合法
	 */
	private boolean checkMatcherEmail(String emailAddress){
		Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
	}
}
