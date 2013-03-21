package erb.unicomedu.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;
import erb.unicomedu.activity.HomeActivity;
import erb.unicomedu.activity.LoginActivity;
import erb.unicomedu.activity.PublicActivity;
import erb.unicomedu.activity.R;
import erb.unicomedu.dao.LoadDao;
import erb.unicomedu.dao.LoginDao;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.push.ServiceManager;
import erb.unicomedu.vo.PushCofigVo;

public class DataAsyncTask extends AsyncTask<String, Integer, List<Object>> {
	private ProgressDialog pd;
	private String TAG = "DataAsyncTask";
	private final int LOADING = 1;
	private final int NAVLGING = 2;
	private final int MODEL = 3;
	private Context mContext;
	private int mTypeId;
	private boolean mIsPd = true;
	private String tlist;
	private String mlist;
	private String plist;
	public DataAsyncTask(Context context) {
		mContext = context;
		pd = new ProgressDialog(mContext);
	}
	
	public DataAsyncTask(Context context,boolean isPd) {
		mContext = context;
		if(isPd){
			pd = new ProgressDialog(mContext);
		}
		mIsPd = isPd;
	}

	protected void onPreExecute() {
		if(mIsPd){
			this.pd.setTitle("数据读取中");
			this.pd.setMessage("请稍候...");
			this.pd.show();
		}
	}

	@Override
	protected List<Object> doInBackground(String... params) {
		List<Object> objectList = new ArrayList<Object>();
		try {
			if(params[0]!=null)
			mTypeId = Integer.valueOf(params[0]);
			switch (mTypeId) {
			case LOADING:
				Map<String, Object> param = new HashMap<String, Object>();
				  tlist =	SubjectDao.getNavTypeString(param);
//				objectList.add(tlist);
				Map<String, Object> mparam = new HashMap<String, Object>();
				  mlist = LoginDao.getModel(mparam);
//				  objectList.add(mlist);
				Map<String, Object> pparam = new HashMap<String, Object>();
				pparam.put("whichsize", 1);
				  plist = LoginDao.getRecommend(pparam);
//				objectList.add(plist);
//				String cofig = LoadDao.getPushCofig();
//				objectList.add(cofig);
				LoginUser();
				break;
			case NAVLGING:
				Map<String, Object> nparam = new HashMap<String, Object>();
				String nlist =	SubjectDao.getNavTypeString(nparam);
				objectList.add(nlist);
				break;
			case MODEL:
				Map<String, Object> mmparam = new HashMap<String, Object>();
				String mmlist = LoginDao.getModel(mmparam);
				objectList.add(mmlist);
				break;
			default:
				break;
			}
		} catch ( Exception e) {
			LogUtil.d(TAG,"error:\n" + e.getMessage());
			return objectList;
		}
		return objectList;
	}

	@Override
	protected void onPostExecute(List<Object> data) {
		if (null!=data&&data.size()>0) {
			// 这里对从后台取出的数据进行处理
			SharedPreferences settings = mContext.getSharedPreferences(Def.PREFS_NAME, 0); 
			SharedPreferences.Editor editor = settings.edit(); 
			switch (mTypeId) {
			case LOADING:
//				String cofig =	data.get(3)+"";
				if(!"".equals(tlist)){
				   editor.remove(Def.SP_NAV_NAME); 
				   editor.putString(Def.SP_NAV_NAME, tlist);
				}
				if(!"".equals(mlist)){
				   editor.putString(Def.SP_MODEL_NAME, mlist);
//					editor.remove(Def.SP_MODEL_NAME);
				}
				if(!"".equals(plist)){
				   editor.putString(Def.SP_Recommend_NAME, plist);
				}
//				if(!"".equals(cofig)){
//                    LogUtil.d(TAG, "cofig : "+cofig);
////                    PushCofigVo pcv = LoadDao.getCofigVo(cofig);
//                    
//					//					   editor.putString(Def.SP_Recommend_NAME, plist);
//				}
				editor.commit(); 
				break;
			case NAVLGING:
				String nlist =	data.get(0)+"";
				if(!"".equals(nlist)){
					   editor.remove(Def.SP_NAV_NAME); 
					   editor.putString(Def.SP_NAV_NAME, nlist);
					   editor.commit(); 
				}
				break;
			case MODEL:
				String mmlist =	data.get(0)+"";
				if(!"".equals(mmlist)){
					editor.remove(Def.SP_MODEL_NAME); 
					editor.putString(Def.SP_MODEL_NAME, mmlist);
					editor.commit(); 
				}
				break;
			default:
				break;
			}
			
		} else {
		}
		if(mIsPd){
		if (pd!=null&&pd.isShowing()) {
			pd.dismiss();
		}
		}
		
		initService();
		super.onPostExecute(data);
	}
	private void initService(){
		   ServiceManager serviceManager = new ServiceManager(mContext);
	        serviceManager.setNotificationIcon(R.drawable.icon);
	        serviceManager.startService();
	}
	
	private void LoginUser(){
		try {
			SharedPreferences settings = mContext.getSharedPreferences(Def.PREFS_NAME, 0); 
			SharedPreferences.Editor mEditor = settings.edit();
			String username =  settings.getString (Def.ACCT_USERID, ""); 
			String password =  settings.getString (Def.ACCT_USERPWD, ""); 
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("logname", username);
			param.put("password", password);
			if(!"".equals(password)&&!"".equals(username)){
				String resultUserInfoJson = LoginDao.LoginString(param);
				if (resultUserInfoJson != null && !resultUserInfoJson.equals("")&& !Def.TO_CLASS_NAME_TAG.equals(resultUserInfoJson)) {
					if (resultUserInfoJson.equals(Def.USER_LOGIN_RESULT)) {
						mEditor.remove(Def.ACCT_USERID);
						mEditor.remove(Def.ACCT_USERPWD);
					} else {
						//记录当前用户的账户ID、密码、自动登陆等
						mEditor.remove(Def.SP_USERINFO_JSON_OBJ);
						mEditor.putString(Def.SP_USERINFO_JSON_OBJ, resultUserInfoJson);	//UserInfo返回的Json对象整块存储
						mEditor.commit();
					}
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	
	}
	
}
