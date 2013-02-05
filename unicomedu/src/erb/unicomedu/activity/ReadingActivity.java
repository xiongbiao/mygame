package erb.unicomedu.activity;

//import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import erb.unicomedu.listener.EduInsListJSinterface;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.TokenUtil;
import erb.unicomedu.vo.ReadVo;

/**
 * 开始阅读
 */
public class ReadingActivity extends PublicActivity implements OnClickListener {
	private WebView mWebView;
	private ImageButton mBack;
	private ReadVo mRv;
	private LoadingView lv;
	private String TAG = "ReadingActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading);
		init();
		initData();
	}

//	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		mWebView = (WebView) findViewById(R.id.reading_web);
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
			 int isOk  ;
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

			@Override
			public void onPageFinished(WebView view, String url) {
				// pd.dismiss();
				if(isOk==0){
					lv.setVisibility(View.GONE);
					mWebView.setVisibility(View.VISIBLE);
				}
			}
		});
//		loadUrl("http://www.baidu.com");

		mBack = (ImageButton) findViewById(R.id.obj_top_back);
		mBack.setOnClickListener(this);
	}

	public void loadUrl(String url) {
		if (mWebView != null) {
			lv.setVisibility(View.VISIBLE);
			lv.myShow(false, "努力加载中……");
			mWebView.loadUrl(url);
			mWebView.reload();
		}
	}

	private void initData() {
		mRv = (ReadVo) getIntent().getSerializableExtra(Def.OBJ);
		if (mRv != null) {
             String url = String.format(Def.READ_URL, mRv.getBookid(),TokenUtil.generateToken(Def.TOKEN_NAME,Def.TOKEN_PASS),userInfo.getMemberid());	
             
             LogUtil.d(TAG, url);
             
			 loadUrl(url);
//				mWebView.reload();
//			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.obj_top_back:
			finish();
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
