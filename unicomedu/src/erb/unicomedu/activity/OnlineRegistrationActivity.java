package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.vo.PlaceVo;
import erb.unicomedu.vo.SubjectVo;

/**
 * 在线报名 2012年9月25日 23:40:11
 * 
 * @author xiong
 * 
 */
public class OnlineRegistrationActivity extends PublicActivity implements
		OnClickListener {

	private Spinner category;
	private ImageButton mBack;
	private TextView className;
	private EditText mUserName;
	private EditText mCall;
	// private PlaceVo mPlaceVo;
	private SubjectVo mSubjectVo;
	private Button mRegister;
	private String[] id;
	private String[] name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.online_registration);
		mSubjectVo = (SubjectVo) getIntent().getSerializableExtra(
				"subject_info");
		// mPlaceVo = (PlaceVo)getIntent().getSerializableExtra("place_info");
		id = getIntent().getStringArrayExtra("data_id");
		name = getIntent().getStringArrayExtra("data_name");
		init();
		initData();
	}

	private void init() {
		category = (Spinner) findViewById(R.id.category);
		if (name != null) {
			ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, R.drawable.drop_list_hover, name);
			categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			categoryAdapter.setDropDownViewResource(R.drawable.drop_list_ys);
			category.setAdapter(categoryAdapter);
			category.setSelection(0);
		}
		mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setOnClickListener(this);

		className = (TextView) findViewById(R.id.register_class);
		mUserName = (EditText) findViewById(R.id.register_user_name);
		mCall = (EditText) findViewById(R.id.register_call);

		mRegister = (Button) findViewById(R.id.register_submit);
		mRegister.setOnClickListener(this);
	}

	private void initData() {

		if (mSubjectVo != null) {
			className.setText(mSubjectVo.getSubjectName());
		}
		// if(mPlaceVo!=null){
		// //选择上课地点
		// }
		if (userInfo != null) {
			mCall.setText(userInfo.getUserName());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.register_submit:
			String userName = mUserName.getText().toString().trim();
			String call = mCall.getText().toString().trim();
			if (TextUtils.isEmpty(userName)) {
				Toast.makeText(OnlineRegistrationActivity.this, "学生姓名不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(call)) {
				Toast.makeText(OnlineRegistrationActivity.this, "联系电话不能为空",
						Toast.LENGTH_SHORT).show();
				return;
			}

			Toast.makeText(OnlineRegistrationActivity.this, "提交数据",
					Toast.LENGTH_SHORT).show();

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("nickname", userName);
			param.put("mobile", call);
			if (mSubjectVo != null) {
				param.put("subjectid", mSubjectVo.getSubjectId());
			} else {

			}
			if (id != null)
				param.put("schoollocation",
						id[Integer.valueOf((int) category.getSelectedItemId())]);
			if (userInfo != null)
				param.put("memberid", userInfo.getMemberid());
			mRegister.setEnabled(false);
			try {

				String result = SubjectDao.OnlineRegistration(param);

				if (result.equals("success")) {
					mRegister.setText("报名成功");
					mUserName.setFocusable(false);
					mUserName.setFocusable(false);
					category.setEnabled(false);
					Toast.makeText(OnlineRegistrationActivity.this, "报名成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(OnlineRegistrationActivity.this, "报名失败",
							Toast.LENGTH_SHORT).show();
					mRegister.setEnabled(true);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	};
}
