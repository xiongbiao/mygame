package erb.unicomedu.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.SignAdapter;
import erb.unicomedu.dao.SignDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.SignAddress;
import erb.unicomedu.vo.SignRankingVo;

public class SignActivity extends PublicActivity implements OnClickListener {

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(networkListener);
		}
		super.onDestroy();
	}

	private String TAG = "SignActivity";
	private ImageButton mSignRanking;
	private ImageButton mSignBack;
	private Button mEInfo;
	private Button mExqcx;
	private Button mSign;
	private List<SignRankingVo> data;
	private PullToRefreshListView prlistView;
	private SignAdapter mSignBaseAdpter;
	private SignAddress mSignAddress;
	private LoadingView lv;
	private TextView mSiginCampusName;
	private TextView mSiginInfo;

	private int mTypeId = 0;
	/**
	 * 经度23.099944,113.291016
	 */
	private static double latitude = 0;
	/**
	 * 纬度
	 * */
	private static double longitude = 0;
	private static Location locationNetWork = null;
	private LocationManager mLocationManager;

	private static boolean isL = true;
	SharedPreferences settings;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign);
		locationInit();
		init();
		initData();
	}

	private void locationInit() {
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 1000, 0, networkListener);
	}

	private LocationListener networkListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			locationNetWork = location;
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			LogUtil.d(TAG, "wifi ok     ");
			// Toast.makeText(SignActivity.this, "基站位置 - latitude： "+
			// latitude+" longitude :" +longitude, Toast.LENGTH_SHORT).show();
			LogUtil.d(TAG, "基站位置  - latitude： " + latitude + " longitude :" + longitude);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
			LogUtil.d(TAG, "wifi not ok ------------    ");
		}

	};

	private void init() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		mSignRanking = (ImageButton) findViewById(R.id.sign_ranking_btn);
		mSignRanking.setOnClickListener(this);
		mSignBack = (ImageButton) findViewById(R.id.sign_back);
		mSignBack.setOnClickListener(this);
		mEInfo = (Button) findViewById(R.id.filter_info);
		mExqcx = (Button) findViewById(R.id.filter_xqcx);
		mEInfo.setOnClickListener(this);
		mExqcx.setOnClickListener(this);
		mSign = (Button) findViewById(R.id.bt_sign);
		mSign.setOnClickListener(this);
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);

		mSiginCampusName = (TextView) findViewById(R.id.sgin_info_campus_name);
		mSiginInfo = (TextView) findViewById(R.id.sgin_info);
		settings = getSharedPreferences(Def.PREFS_NAME, 0); 
	    editor = settings.edit(); 

	}

	class SItude {
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

	class SCell {
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
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(entity.getContent()));
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
			throw new Exception("获取经纬度出现错误:" + e.getMessage());
		} finally {
			post.abort();
			client = null;
		}

		return itude;
	}

	private void initData() {
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub

			}
		});
		new GetDataTask().execute();
	}

	/**
	 * 刷新数据
	 * 
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<SignRankingVo>> {
		int exType = 0;
		String erMsg = "";
		boolean isFoot = false;
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
		    lv.show(0);
		}
		@Override
		protected List<SignRankingVo> doInBackground(Void... params) {

			if (locationNetWork == null) {
				try {
					SItude si = getItude(getCellInfo());
					latitude = Double.valueOf(si.latitude);
					longitude = Double.valueOf(si.longitude);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				// 获取上次数据来处理
			} else {
				latitude = locationNetWork.getLatitude();
				longitude = locationNetWork.getLongitude();
			}
			
			try {
				if (userInfo != null) {

					Map<String, Object> param = new HashMap<String, Object>();
					param.put("memberid", userInfo.getMemberid());
					param.put("page", 0);
					param.put("size", 20);
					param.put("smis", MyApplication.mSIMCard.getImsi());
					param.put("latitude", latitude);
					param.put("longitude", longitude);
					if (mTypeId == 0) {
						data = SignDao.getMySignList(param);
					} else {
						data = SignDao.getOtherSignList(param);
					}
					if (isL) {

						Map<String, Object> paramLocation = new HashMap<String, Object>();
						paramLocation.put("latitude", latitude);
						paramLocation.put("longitude", longitude);
						paramLocation.put("memberid", userInfo.getMemberid());
						mSignAddress = SignDao.MyLocationSign(paramLocation);
					}
				}

			}catch(EuException ex){ 
				ex.printStackTrace();
				exType = 1;	
				erMsg = ex.getMessage();
				data = null;
				isFoot = false;
				LogUtil.d("XB", ""+ex.getMessage());
			}catch(Exception e) {
				 e.printStackTrace();
				 data = null;
				 isFoot = false;
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<SignRankingVo> data) {
			if (mSignAddress != null && mSignAddress.getLocationname() != null) {
				mSiginCampusName.setText("你在" + mSignAddress.getLocationname()+ "附近");
				mSiginInfo.setText(mSignAddress.getInfo());
			}
			mSignBaseAdpter = new SignAdapter(SignActivity.this,
					R.layout.sign_item, data);
			if (mTypeId == 0) {
				mSignBaseAdpter.isMy(true);
			} else {
				mSignBaseAdpter.isMy(false);
			}
			prlistView.setAdapter(mSignBaseAdpter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(false);
			mSignBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sign_ranking_btn:
			Intent mIntent = new Intent(this, SignRankingActivity.class);
			Bundle bundle = new Bundle();
			mIntent.putExtra(Def.OBJ_Bundle, bundle);
			startActivity(mIntent);
			break;
		case R.id.sign_back:
			if (mLocationManager != null) {
				mLocationManager.removeUpdates(networkListener);
			}
			finish();
			break;
		case R.id.filter_info:
			mTypeId = 0;
			new GetDataTask().execute();
			mEInfo.setBackgroundResource(R.drawable.button_4);
			mEInfo.setTextColor(getResources().getColor(R.color.text_color_green));
			mExqcx.setBackgroundResource(R.drawable.button_4_uncheck);
			mExqcx.setTextColor(getResources().getColor(R.color.black));
			break;
		case R.id.filter_xqcx:
			mTypeId = 1;
			new GetDataTask().execute();
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mEInfo.setBackgroundResource(R.drawable.button_4_uncheck);
			mEInfo.setTextColor(getResources().getColor(R.color.black));
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mExqcx.setTextColor(getResources().getColor( R.color.text_color_green));
			break;
		case R.id.bt_sign:
			if (mSignAddress == null) {
				Toast.makeText(this, "未获得校区位置消息", Toast.LENGTH_SHORT) .show();
			    return ;
			} 
			//  
		    String signData = 	settings.getString(Def.SIGN_DATA, "");
		    String signNum = 	settings.getString(Def.SIGN_NUM, "");
		    boolean isSign = false;
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		    if(signNum==null||"".equals(signNum)){
		    	String date= format.format(new Date());
		    	editor.putString(Def.SIGN_NUM, date);
		    	editor.commit();
		    	isSign = true;
		    }else{
		    	try {
		    		Date dtiem =  format.parse(signNum);
		    		String date= format.format(new Date());
		    		if(dtiem.before(format.parse(date))||dtiem.after(format.parse(date))){
			    	     editor.remove(Def.SIGN_DATA);
			    	     editor.commit();
			    	     isSign = true;
		    		}else{
		    			if(signData!=null&&!signData.equals("")){
                           String[] dataList = signData.split(",");
                           boolean isC = true;
                           for(String s : dataList){
                        	   if(mSignAddress.getLocationid().equals(s)){
                        		   isC = false;
                        		   break;
                        	   }
                           }
                           isSign = isC;
		    			}else{
		    				isSign = true;
		    			}
		    		}
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		     
			if(isSign) {
				String fromClassName = this.getClass().getName();
				String className = this.getClass().getName();
				boolean isOK = isModel(Def.MODEl_SIGN_POST, className, fromClassName, null);
				if (isOK) {
						if (userInfo != null) {
							// Toast.makeText(SignActivity.this,
							// longitude+"---"+ latitude,
							// Toast.LENGTH_SHORT).show();
							try {
								Map<String, Object> param = new HashMap<String, Object>();
								param.put("memberid", userInfo.getMemberid());
								param.put("locationid", mSignAddress.getLocationid());
								param.put("locationname", mSignAddress.getLocationname());
								param.put("latitude", latitude);
								param.put("longitude", longitude);
								param.put("smis",MyApplication.mSIMCard.getImsi());
								String res = SignDao.SignUser(param);
								if (Def.FAV_OBJ_RESULT.equals(res)) {
									Toast.makeText(this, "已经签到",Toast.LENGTH_SHORT).show();
								} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
									editor.putString(Def.SIGN_DATA,signData+","+ mSignAddress.getLocationid());
									editor.commit();
									Toast.makeText(this, "签到成功", Toast.LENGTH_SHORT).show();
//									MyApplication.signNum++;
								} else {
									Toast.makeText(this, "签到失败", Toast.LENGTH_SHORT).show();
								}

							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								Toast.makeText(this, "签到失败", Toast.LENGTH_SHORT).show();
							
							}
						}
				}
			} else {
				Toast.makeText(this, "您已经签到了，谢谢！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mLocationManager != null) {
				mLocationManager.removeUpdates(networkListener);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	};
}
