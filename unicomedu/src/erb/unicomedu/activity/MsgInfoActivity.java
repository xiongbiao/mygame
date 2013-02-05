package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.MsgDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.MsgUtil;
import erb.unicomedu.util.TokenUtil;
import erb.unicomedu.vo.MsgVo;
import erb.unicomedu.vo.SubjectVo;

public class MsgInfoActivity extends PublicActivity implements OnClickListener {
	private WebView mWebView;
	private ImageButton mBack;
	private MsgVo mRv;
	private Bundle mObjBundle;
	private Button event;
	private TextView mTopName;
	private String isService;
	private MsgVo data;
	private LoadingView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_info);
		init();
		initData();
	}

	private void init() {
		lv = (LoadingView) findViewById(R.id.data_loading);
		mWebView = (WebView) findViewById(R.id.obj_web);
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// TODO 加载页面
		//
		mWebView.setWebViewClient(new WebViewClient() {
			int isOk;

			@Override
			public void onPageFinished(WebView view, String url) {
				// pd.dismiss();
				if (isOk == 0) {
					lv.setVisibility(View.GONE);
					mWebView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
				mWebView.loadUrl(url);
				LogUtil.d("XB", "MsInfoAc :WebViewClient.shouldOverrideUrlLoading -------- ");
				// 记得消耗掉这个事件。给不知道的朋友再解释一下，Android中返回True的意思就是到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				LogUtil.d("XB", "MsInfoAc :WebViewClient.onPageStarted -------- ");
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				LogUtil.d("XB", "MsInfoAc :onLoadResource -------- ");
				super.onLoadResource(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				lv.setVisibility(View.VISIBLE);
				lv.myShow(false, "加载失败，请稍后再试!");
				mWebView.setVisibility(View.GONE);
				isOk++;
				super.onReceivedError(view, errorCode, description, failingUrl);

			}
		});
		// loadUrl("http://www.baidu.com");

		mBack = (ImageButton) findViewById(R.id.obj_top_back);
		mBack.setOnClickListener(this);
		mTopName = (TextView) findViewById(R.id.obj_top_name);
		event = (Button) findViewById(R.id.msg_event);
		event.setOnClickListener(this);
	}

	private void initData() {
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
		if (mObjBundle != null) {
			isService = mObjBundle.getString("isService");
			mRv = (MsgVo) mObjBundle.getSerializable(Def.OBJ);
			mTopName.setText(mRv.getType());
		}
		if (mRv != null) {
			String url = String.format(Def.MSGINFO_URL, mRv.getMsgid(),
					TokenUtil.generateToken(Def.TOKEN_NAME, Def.TOKEN_PASS));
			loadUrl(url);
			new GetDataTask().execute();
		}
	}

	public void loadUrl(String url) {
		if (mWebView != null) {
			lv.setVisibility(View.VISIBLE);
			lv.myShow(false, "努力加载中……");
			mWebView.loadUrl(url);
			mWebView.reload();
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, MsgVo> {
		@Override
		protected MsgVo doInBackground(Void... params) {
			try {
				// 上报信息
				if ("true".equals(isService)) {
					Map<String, Object> paramsb = new HashMap<String, Object>();
					if (userInfo != null) {
						paramsb.put("memberid", userInfo.getMemberid());
					} else {
						paramsb.put("memberid", 0);
					}
					paramsb.put("msgid", mRv.getMsgid());
					// 暂时不上报
					MsgDao.pushMsgOk(paramsb);
				}
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("msgid", mRv.getMsgid());
				data = MsgDao.getMsgString(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(MsgVo data) {
			if (data != null) {
				// 报名
				if ("1".equals(data.getLinktype())) {
					if (!"".equals(data.getLinkinfo())) {
						// 把lsitclass 转换成 报名信息
						event.setVisibility(View.VISIBLE);
						event.setText("报名");
						LogUtil.d("-------------", data.getLinkinfo());
					}
				} else if ("2".equals(data.getLinktype())) {

				} else if ("3".equals(data.getLinktype())) {

				} else if ("4".equals(data.getLinktype())) {

				}

			}

			super.onPostExecute(data);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.obj_top_back:
			if ("true".equals(isService)) {
				Intent intent = new Intent(this, MsgActivity.class);
				intent.putExtra("isService", "true");
				startActivity(intent);
			}
			finish();
			break;
		case R.id.msg_event:
			if ("1".equals(data.getLinktype())) {
				if (!"".equals(data.getLinkinfo())) {
					// 把lsitclass 转换成 报名信息
					Intent mIntent = new Intent(this,
							OnlineRegistrationActivity.class);

					Map<String, Object> map = MsgUtil.getStringPlace(data
							.getLinkinfo());

					String[] id = (String[]) map.get("id");
					String[] name = (String[]) map.get("name");
					// id = new String[4];
					// name = new String[4];
					// for(int i =0 ;i<4;i++){
					// name[i] = "校区--"+i;
					// id[i] = ""+i;
					// }
					SubjectVo mSubjectVo = (SubjectVo) map.get("sv");
					Bundle mBundle = new Bundle();
					mBundle.putSerializable("subject_info", mSubjectVo);
					mBundle.putStringArray("data_name", name);
					mBundle.putStringArray("data_id", id);
					mIntent.putExtras(mBundle);
					startActivity(mIntent);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("true".equals(isService)) {
				Intent intent = new Intent(this, MsgActivity.class);
				startActivity(intent);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	};
}
