package erb.unicomedu.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import erb.unicomedu.dao.LoginDao;
import erb.unicomedu.util.DataAsyncTask;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.ModelVo;
import erb.unicomedu.vo.UserVo;

public class PublicActivity extends Activity{
	 

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
        if(MyApplication.activityList.size()>1)
		 MyApplication.activityList.remove(MyApplication.activityList.size()-1);
		super.onDestroy();
	}

	private String TAG = "PublicActivity";
	protected Animation mAnim ; 
	protected Animation mLeftAnim ; 
	public static UserVo userInfo = null;	//全系统唯一实例
	protected ArrayList<ModelVo> mlist ;
	protected int navHeight = 60;
	
//	//广播的内部类，当收到关闭事件时，调用finish方法结束activity  
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  
//        @Override  
//        public void onReceive(Context context, Intent intent) {  
//    		String action = intent.getAction();
//    		LogUtil.d(TAG,"Receive BroadCast Message:"+action);
//    		if (action.equals(Def.APP_EXIT_BROADCAST_MSG)) {
//                unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错   
//                finish();  
//    		}
//        }  
//    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加Activity到集合中
		MyApplication.addActivity(this);
		
//        //在当前的activity中注册广播  
//        IntentFilter filter = new IntentFilter();  
//        filter.addAction(Def.APP_EXIT_BROADCAST_MSG); 
//       	registerReceiver(this.broadcastReceiver, filter);  
//        LogUtil.d(TAG,"Register Receiver->"+Def.APP_EXIT_BROADCAST_MSG);
        
        //初始化用户信息
		getUserVo();
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		if(display.getHeight()<600){
			navHeight =display.getHeight()*51/420;
		}else if(display.getHeight()>600){
			navHeight =display.getHeight()*49/520;
		}
		resetUserInfoVo();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		for(int i = 0 ;i<MyApplication.activityList.size();i++){
			LogUtil.d(TAG, "activityList名称="+MyApplication.activityList.get(i).getClass().getName());
			
		}
		LogUtil.d(TAG, "activityList数量="+MyApplication.activityList.size());
	}
	
	private void getUserVo() {
		if(userInfo == null){
		  resetUserInfoVo();
		}
	}
	
	public ArrayList<ModelVo> getModelVo() {
		try {
			SharedPreferences settings =  getSharedPreferences(Def.PREFS_NAME, 0);
			String ujson = settings.getString(Def.SP_MODEL_NAME, "");
			if (ujson != null && !"".equals(ujson)) {
				JSONArray userJson = new JSONArray(ujson);
				mlist = LoginDao.getArrayToModel(userJson);
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.d(TAG, ""+e.getMessage());
		}
		return mlist;
	}
	
	/**
	 * 
	 * @param modelId
	 * @param className 
	 * @param fromClassName
	 * @param mBundle
	 * @return
	 */
	protected boolean isModel(String modelId,String className,String fromClassName,Bundle mBundle){
		boolean result = false;
		ArrayList<ModelVo> mlist= getModelVo();
		if(mlist!=null&&mlist.size()>0){
			for(int i =0 ;i<mlist.size();i++){
				if(modelId.equals(mlist.get(i).getModelcode())){
					String[] popedomList  = mlist.get(i).getPopedom().split(",");
					for(int j =0 ;j<popedomList.length;j++){
						if("0".equals(popedomList[j])){
							result = true;
							LogUtil.d(TAG, "游客资源 ~_~");
							break;
						}
					}
					if(!result){
						if(userInfo!=null){
							for(int j =0 ;j<popedomList.length;j++){
								if(userInfo.getVip().equals(popedomList[j])){
									result = true;
									break;
								}
							}
							LogUtil.d(TAG, "我是vip ： "+userInfo.getVip());
						}
					}
					break;
				}
			}
		
		if(!result){
			if(!fromClassName.endsWith("HomeActivity"))
			     finish();
			Intent mIntent = new Intent(this,LoginActivity.class);  
			if(mBundle==null){
				 mBundle = new Bundle(); 
			}
	        mBundle.putString(Def.TO_CLASS_NAME_TAG, className);
	        mBundle.putString(Def.FROM_CLASS_NAME_TAG, fromClassName);
	        mIntent.putExtra(Def.OBJ_Bundle, mBundle); 
	        mIntent.putExtras(mBundle); 
	        startActivity(mIntent); 
		}
		}else{
			DataAsyncTask	 dat = new DataAsyncTask(this,false); 
				dat.execute("3");
             result = false;
             Toast.makeText(getApplicationContext(), "正在初始化数据，请稍后…… ", Toast.LENGTH_SHORT).show(); 
		}
		return result;
	}
	
	/**
	 * 刷新UserInfo数据（当客户端修改UserInfo后，必须调用此函数）
	 */
	public void resetUserInfoVo() {
		userInfo = null;
		try {
			SharedPreferences settings = getSharedPreferences(Def.PREFS_NAME, 0);
			String ujson = settings.getString(Def.SP_USERINFO_JSON_OBJ, "");
			if (ujson != null && !"".equals(ujson)) {
				JSONObject userJson = new JSONObject(ujson);
				LogUtil.d(TAG, "刷新数据：--" + userJson);
				userInfo = new UserVo();
				String userPass = userJson.getString("password");
				String country = userJson.getString("country");
				String userName = userJson.getString("logname");
				String email = userJson.getString("email");
				String school = userJson.getString("school");
				String city = userJson.getString("city");
				String province = userJson.getString("province");
				String memberid = userJson.getString("memberid");
				String integral = userJson.getString("integral");
				String payment = userJson.getString("payment");
				String provinceid = userJson.getString("provinceid");
				String cityid = userJson.getString("cityid");
				String telphone = userJson.getString("telphone");
				String mobile = userJson.getString("mobile");
				String logo = userJson.getString("logo");
				String nickname = userJson.getString("nickname");
				String truename = userJson.getString("truename");
				String sex = userJson.getString("sex");
				String birthday = userJson.getString("birthday");
				String smis = userJson.getString("smis");
				String vip = userJson.getString("vip");
				String likeinfo = userJson.getString("likeinfo");
				String sid = userJson.getString("sid");
				String intention = userJson.getString("intention");
				userInfo.setIntention(intention);
				userInfo.setSid(sid);
				userInfo.setBirthday(birthday);
				userInfo.setCity(city);
				userInfo.setCityid(cityid);
				userInfo.setCountry(country);
				userInfo.setEmail(email);
				userInfo.setIntegral(integral);
				userInfo.setLikeinfo(likeinfo);
				userInfo.setLogo(logo);
				userInfo.setMemberid(memberid);
				userInfo.setMobile(mobile);
				userInfo.setNickname(nickname);
				userInfo.setPayment(payment);
				userInfo.setProvince(province);
				userInfo.setProvinceid(provinceid);
				userInfo.setUserName(userName);
				userInfo.setUserPass(userPass);
				userInfo.setSchool(school);
				userInfo.setSex(sex);
				userInfo.setSmis(smis);
				userInfo.setTelphone(telphone);
				userInfo.setTruename(truename);
				userInfo.setVip(vip);
			} else {
				//初始化游客userinfo
				//memberid=0,vip=0,logname=visitor,nickname=游客
				userInfo = new UserVo();
				userInfo.setMemberid("0");
				userInfo.setVip("0");
				userInfo.setUserName("visitor");
				userInfo.setNickname("游客");
			}
		} catch (Exception e) {
			LogUtil.d(TAG, e.getMessage());
			userInfo = null;
			e.printStackTrace();
		}
	}
	
	protected void loadAnim(View v) {
		final View thisV = v;
		mAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
		mAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				thisV.clearAnimation();
				thisV.setVisibility(View.INVISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				thisV.setVisibility(View.VISIBLE);
			}
		});
		mLeftAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.nav_left);
		mLeftAnim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				thisV.clearAnimation();
				thisV.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				thisV.setVisibility(View.INVISIBLE);
			}
		});
		thisV.setAnimation(mAnim);
	}
}
