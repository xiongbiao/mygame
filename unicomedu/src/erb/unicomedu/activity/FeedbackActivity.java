package erb.unicomedu.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import erb.unicomedu.dao.FeedbackDao;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.FeedbackTypeVo;

public class FeedbackActivity extends PublicActivity implements OnClickListener {
	private ImageButton mBack;
	private Spinner feedbackType;
	private Button mSubmit;
	private String strFeedbackLink;
	private String strFeedbackContent;
	private EditText mFeedbackContent;
	private EditText mFeedbackLink;
	ArrayList<FeedbackTypeVo> flist;
	/** 提交成功消息标识 */
	private final static int MSG_OK = 200;
	private MyHandler m_Handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		init();
		initData();
		m_Handler = new MyHandler(this);  
	}

	private void init() {
		mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setOnClickListener(this);
		feedbackType = (Spinner) findViewById(R.id.category);
		mSubmit = (Button) findViewById(R.id.feedback_submit);
		mSubmit.setOnClickListener(this);
		mFeedbackContent = (EditText) findViewById(R.id.feedback_content);
		mFeedbackLink = (EditText) findViewById(R.id.feedback_link);
	}

	private void initData() {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			flist = FeedbackDao.getFeedbackType(param);
			if (flist != null && flist.size() > 0) {
				String[] sA = new String[flist.size()];
				for (int i = 0; i < flist.size(); i++) {
					sA[i] = flist.get(i).getType();
				}
				ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(
						this, R.drawable.drop_list_hover, sA);
				categoryAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				feedbackType.setAdapter(categoryAdapter);
				feedbackType.setSelection(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.feedback_submit:
			postFeedback();
			break;
		}
	}
	
	private Map<String, Object> m_param;
	/** 提交反馈数据 */
	private void postFeedback() {
		strFeedbackContent = mFeedbackContent.getText().toString().trim();
		strFeedbackLink = mFeedbackLink.getText().toString().trim();
		if ("".equals(strFeedbackContent)) {
			Toast.makeText(this, "你认为我们哪些地方需要改进的呢？", Toast.LENGTH_SHORT).show();
			mFeedbackContent.requestFocus();
			return;
		} else if ("".equals(strFeedbackLink)) {
			Toast.makeText(this, "请留个联系方式吧，感谢你的支持", Toast.LENGTH_SHORT).show();
			mFeedbackLink.requestFocus();
			return;
		}

		mSubmit.setEnabled(false);
		LogUtil.e("aaa", "构建数据");
		
		m_param = new HashMap<String, Object>();
		m_param.put("memberid", userInfo==null?"0":userInfo.getMemberid());   /* 熊彪，这句代码有BUG，用户没有登录的情况下，这里会异常*/
		m_param.put("nickname", strFeedbackLink);
		m_param.put("context", strFeedbackContent);
		if (flist != null && flist.size() > 0) {
			m_param.put("typeid",flist.get(Integer.valueOf((int) feedbackType.getSelectedItemId())).getTypeid());
		} else {
			m_param.put("typeid", "-1");
		}
		LogUtil.e("bbb", "抛出子线程");
		
		//抛出子线程处理数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean bFlag;
				try {
					bFlag = FeedbackDao.submitFeedback(m_param);
					if (bFlag) {
						m_Handler.sendEmptyMessage(MSG_OK);
					} else {
						m_Handler.sendEmptyMessage(MSG_OK-1);
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Handler处理界面消息
	 */
	static class MyHandler extends Handler {
		WeakReference<FeedbackActivity> mActivity;

		MyHandler(FeedbackActivity activity) {
			mActivity = new WeakReference<FeedbackActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			FeedbackActivity theActivity = mActivity.get();
			if (msg.what == MSG_OK) {
				Toast.makeText(theActivity,"感谢你的支持！", Toast.LENGTH_SHORT).show();
				//关闭本界面
				theActivity.finish();
			}
			else
			{
				Toast.makeText(theActivity, "网络不太给力，请稍后再试", Toast.LENGTH_SHORT).show();
				theActivity.mSubmit.setEnabled(true);
			}
			super.handleMessage(msg);
		}
	};

}
