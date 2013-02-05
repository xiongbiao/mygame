package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.BbsTakeAskDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsVo;
/***
 * 提问界面
 */
public class BbsAskActivity extends PublicActivity {
	private Button button_TopReply;
	private Button button_TopBack;
	private TextView textView_Title;
	private EditText edAskTitle;
	private EditText edAskContent;
	private BbsVo vo;
	private Bundle mBundle;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_ask);
		vo = (BbsVo) getIntent().getSerializableExtra(Def.OBJ);
		mBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
		if (mBundle != null) {
			vo = (BbsVo) mBundle.getSerializable(Def.OBJ);
		}
		edAskTitle = (EditText) findViewById(R.id.bbs_ask_title);
		edAskContent = (EditText) findViewById(R.id.bbs_ask_context);

		textView_Title = (TextView) findViewById(R.id.title);
		if (vo != null)
			textView_Title.setText(vo.getTopic());

		// 右上角【发帖】键
		button_TopReply = (Button) findViewById(R.id.right_btn);
		button_TopReply.setText(R.string.btn_right_reply);
		button_TopReply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null==userInfo) {
					Toast.makeText(BbsAskActivity.this, "注册后才可以发帖", Toast.LENGTH_SHORT).show();
					String className = BbsAskActivity.this.getClass().getName();
					Intent mIntent = new Intent(BbsAskActivity.this, RegActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putString(Def.TO_CLASS_NAME_TAG, className);
					mIntent.putExtras(mBundle);
					startActivity(mIntent);
				}
				else
				{
					//检查是否输入数据
					String strTitle   = edAskTitle.getText().toString().trim();
					String strContent = edAskContent.getText().toString().trim();
					if ("".equals(strTitle))
					{
						Toast.makeText(BbsAskActivity.this, "请录入标题", Toast.LENGTH_SHORT).show();
					}else if (strTitle.length()>=30)
					{
						Toast.makeText(BbsAskActivity.this, "标题太长了！精简点吧.", Toast.LENGTH_SHORT).show();
					}
					else if ("".equals(strContent))
					{
						Toast.makeText(BbsAskActivity.this, "请输入问题内容", Toast.LENGTH_SHORT).show();
					}
					else 
					{
						try {
							Map<String, Object> param = new HashMap<String, Object>();
							param.put("topicid", vo.getTopicid());
							param.put("topic", edAskTitle.getText());
							param.put("title", edAskTitle.getText());
							param.put("memberid", userInfo.getMemberid());// 会员ID
							param.put("context", edAskContent.getText());
							param.put("member", userInfo.getNickname());// 会员昵称
							button_TopReply.setEnabled(false);
							BbsTakeAskDao.pushAskList(param);
							finish();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
				}
			}
		});

		// 左上角【返回】键
		button_TopBack = (Button) findViewById(R.id.back);
		button_TopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
		}
    	return super.onKeyDown(keyCode, event);
    }

}
