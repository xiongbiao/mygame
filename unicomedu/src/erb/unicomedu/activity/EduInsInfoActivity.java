package erb.unicomedu.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import erb.unicomedu.dao.EduinsDao;
import erb.unicomedu.listener.EduInsListJSinterface;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.EduinsVo;
import erb.unicomedu.vo.PlaceVo;

/**
 * 教育机构的信息介绍
 * 
 * @author xiong
 * 
 */
public class EduInsInfoActivity extends PublicActivity implements OnClickListener {
	private Button mEInfo;
	private Button mExqcx;
	private ImageButton mBack;
	private LinearLayout mLinfo;
	private LinearLayout mLExqcx;

	private TextView mCampusName;
	private TextView mCampusAddress;
	private TextView mCampusCall;
	private TextView mCampusClass;
	private TextView mCampusTraffic;
	private String TAG = "EduInsInfoActivity";
	private int isOk = 0 ;
	 int 	screenHeight ;
	/**
	 * 经度23.099944,113.291016
	 */
	private static double latitude  = 0 ;
	/**
	 * 纬度
	 * */
	private static double longitude = 0; 
	private static Location locationNetWork = null;
	private LocationManager mLocationManager;
	private LoadingView lv;
	private EduinsVo ev;
	private WebView mWebView;
	private Bundle mBundle ;
	private PlaceVo mPlaceVo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eduins_info);
		try {
		init();
		DataInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		mEInfo = (Button) findViewById(R.id.filter_info);
		mExqcx = (Button) findViewById(R.id.filter_xqcx);
		mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setOnClickListener(this);
		mEInfo.setOnClickListener(this);
		mExqcx.setOnClickListener(this);
		mLinfo = (LinearLayout) findViewById(R.id.edu_ll_info);
		mLExqcx = (LinearLayout) findViewById(R.id.edu_ll_xqjs);
		mCampusName = (TextView) findViewById(R.id.eduins_info_campus_name);
		mCampusAddress = (TextView) findViewById(R.id.eduins_info_address_info);
		mCampusCall = (TextView) findViewById(R.id.eduins_info_call_info);
		mCampusClass = (TextView) findViewById(R.id.eduins_info_zykc_info);
		mCampusTraffic = (TextView) findViewById(R.id.tv_traffic);
		lv = (LoadingView) findViewById(R.id.data_loading);
	        mWebView = (WebView) findViewById(R.id.wv_map);
//			mWebView.setScrollbarFadingEnabled(true);
//			mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
			mWebView.getSettings().setJavaScriptEnabled(true);
//			mWebView.getSettings().setAllowFileAccess(true);
//			mWebView.getSettings().setBuiltInZoomControls(true);
//			mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
//			mWebView.getSettings().setSupportZoom(false);
//			mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//			mWebView.addJavascriptInterface(new EduInsListJSinterface(this),EduInsListJSinterface.MORE_LIST_ELEMENT);
			// TODO 加载页面
			//
			mWebView.setWebViewClient(new WebViewClient() {
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
						isOk = -1;
					}
				}
			});  
			
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
        	screenHeight = display.getHeight();
	}


	class SItude{
        public String latitude;
        public String longitude;

    }
	
	/**
	 * 获取基站信息
	 * 
	 * @throws Exception
	 */
	private SCell getCellInfo() throws Exception {
	    SCell cell = new SCell();
	 
	    /** 调用API获取基站信息 */
	    TelephonyManager mTelNet = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    GsmCellLocation location = (GsmCellLocation) mTelNet.getCellLocation();
	    if (location == null)
	        throw new Exception("获取基站信息失败");
	 
	    String operator = mTelNet.getNetworkOperator();
	    int mcc = Integer.parseInt(operator.substring(0, 3));
	    int mnc = Integer.parseInt(operator.substring(3));
	    int cid = location.getCid();
	    int lac = location.getLac();
	 
	    /** 将获得的数据放到结构体中 */
	    cell.MCC = mcc;
	    cell.MNC = mnc;
	    cell.LAC = lac;
	    cell.CID = cid;
	 
	    return cell;
	}
	
	class SCell{
        public int MCC;
        public int MNC;
        public int LAC;
        public int CID;

    }
	/**
	 * 获取经纬度
	 * 
	 * @throws Exception
	 */
	private SItude getItude(SCell cell) throws Exception {
	    SItude itude = new SItude();
	 
	    /** 采用Android默认的HttpClient */
	    HttpClient client = new DefaultHttpClient();
	    /** 采用POST方法 */
	    HttpPost post = new HttpPost("http://www.google.com/loc/json");
	    try {
	        /** 构造POST的JSON数据 */
	        JSONObject holder = new JSONObject();
	        holder.put("version", "1.1.0");
	        holder.put("host", "maps.google.com");
	        holder.put("address_language", "zh_CN");
	        holder.put("request_address", true);
	        holder.put("radio_type", "gsm");
	        holder.put("carrier", "HTC");
	 
	        JSONObject tower = new JSONObject();
	        tower.put("mobile_country_code", cell.MCC);
	        tower.put("mobile_network_code", cell.MNC);
	        tower.put("cell_id", cell.CID);
	        tower.put("location_area_code", cell.LAC);
	 
	        JSONArray towerarray = new JSONArray();
	        towerarray.put(tower);
	        holder.put("cell_towers", towerarray);
	 
	        StringEntity query = new StringEntity(holder.toString());
	        post.setEntity(query);
	 
	        /** 发出POST数据并获取返回数据 */
	        HttpResponse response = client.execute(post);
	        HttpEntity entity = response.getEntity();
	        BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	        StringBuffer strBuff = new StringBuffer();
	        String result = null;
	        while ((result = buffReader.readLine()) != null) {
	            strBuff.append(result);
	        }
	 
	        /** 解析返回的JSON数据获得经纬度 */
	        JSONObject json = new JSONObject(strBuff.toString());
	        JSONObject subjosn = new JSONObject(json.getString("location"));
	 
	        itude.latitude = subjosn.getString("latitude");
	        itude.longitude = subjosn.getString("longitude");
	         
	        Log.i("Itude", itude.latitude + itude.longitude);
	         
	    } catch (Exception e) {
	        Log.e(e.getMessage(), e.toString());
	        throw new Exception("获取经纬度出现错误:"+e.getMessage());
	    } finally{
	        post.abort();
	        client = null;
	    }
	     
	    return itude;
	}

	
	
	
	private void locationInit(){
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0, networkListener);
	}
	
	private LocationListener networkListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationNetWork = location;
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}

	};
	
	private void DataInit() {
		
		mBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
		if(mBundle!=null){
			ev = (EduinsVo) mBundle.getSerializable(Def.OBJ);
		}
		if (ev != null) {
			mCampusName.setText(ev.getEduinsName());
			mCampusAddress.setText(ev.getEduinsAddress());
			mCampusCall.setText(ev.getEduinsCall());
			mCampusClass.setText(ev.getEduinsSchedule());
			mCampusTraffic.setText(ev.getTraffic());
		   
		}else{
			mPlaceVo = (PlaceVo)getIntent().getSerializableExtra("place_info");
			new GetDataTask().execute();
		}
		locationInit();
		if(locationNetWork != null){
			latitude =locationNetWork.getLatitude();
			longitude =locationNetWork.getLongitude();
		}
		if (latitude == 0 && longitude == 0)
			new GetDataLTask().execute();
		
		if(isOk==0){
			int w = 300 ;
			if(screenHeight>900){
				w = 600;
			}
		
			    String url = String.format(Def.EMAP_URL, ev.getEduinsID(),latitude,longitude, ""+w,""+w);
//			    String url = String.format(Def.EMAP_URL, ev.getEduinsID(),latitude,longitude);
			    LogUtil.d(TAG, url);
			    lv.setVisibility(View.VISIBLE);
				lv.myShow(false, "努力加载中……");
			    mWebView.loadUrl(url); 
				mWebView.reload();
		}
	}
	
	private class GetDataLTask extends AsyncTask<Void, Void, Object> {

		@Override
		protected Object doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
		if (locationNetWork == null) {
			try {
				SItude si= getItude(getCellInfo());
				latitude =Double.valueOf( si.latitude);
				longitude  = Double.valueOf( si.longitude);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			//获取上次数据来处理
		}else{
			latitude =locationNetWork.getLatitude();
			longitude =locationNetWork.getLongitude();
		}
		
			return null;
		}
		
		@Override
		protected void onPostExecute(Object data) {
			
			super.onPostExecute(data);
		}
		
	}
	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, EduinsVo> {
		@Override
		protected  EduinsVo doInBackground(Void... params) {
			try {
				if(mPlaceVo!=null){
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("locationid", mPlaceVo.getLocationid());
					ev = EduinsDao.getEduinsInfo(param);
				}
			} catch ( Exception e) {
				 LogUtil.d(TAG, e.getMessage());
			}
			return ev;
		}
		@Override
		protected void onPostExecute(EduinsVo data) {
			DataInit();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filter_info:
			mEInfo.setBackgroundResource(R.drawable.button_4);
			mEInfo.setTextColor(getResources().getColor(R.color.text_color_orange));
			mExqcx.setBackgroundResource(R.drawable.button_4_uncheck);
			mExqcx.setTextColor(getResources().getColor(R.color.black));
			mLinfo.setVisibility(View.VISIBLE);
			mLExqcx.setVisibility(View.INVISIBLE);
			break;
		case R.id.filter_xqcx:
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mEInfo.setBackgroundResource(R.drawable.button_4_uncheck);
			mEInfo.setTextColor(getResources().getColor(R.color.black));
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mExqcx.setTextColor(getResources().getColor(R.color.text_color_orange));
			mLinfo.setVisibility(View.GONE);
			mLExqcx.setVisibility(View.VISIBLE);
			break;
		case R.id.title_back:
			mLocationManager.removeUpdates(networkListener);
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		mLocationManager.removeUpdates(networkListener);
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
