package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.AppVo;
import erb.unicomedu.vo.PushCofigVo;

/**
 *   dao
 * @author xiongbiao
 *
 */
public class LoadDao extends PublicDao{
	private  static String TAG =  "LoadDao";
	/**
	 * 
	 * @param param
	 * @return
	 */
	public static AppVo  getApp(Map<String, Object> param)throws Exception{
		AppVo  bRestule = null;
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.UpdateAppSERVER, "", "");        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					bRestule = new AppVo();
					JSONObject appJson = json.getJSONObject("VersionInfo");
					String info = appJson.getString("info");
					String version = appJson.getString("version");
					String appurl = appJson.getString("appurl");
					String filesize = appJson.getString("filesize");
					bRestule.setAppurl(appurl);
					bRestule.setFilesize(filesize);
					bRestule.setInfo(info);
					bRestule.setVersion(version);
				}else{
					bRestule = null;
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return bRestule;
	}
	
	public static String getPushCofig() throws Exception{
		String restule = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.PushConfigSERVER, "", "");        
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					// 把数据写到本地
					//10-15 16:51:29.528: D/LoadDao(13544): {"port":"8080","method":"pushlip","code":200,"success":true,"apikey":"1234567890","ip":"113.105.153.221"}
					restule = resultJson ;
				}else{
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return restule;
	}
	
	public static PushCofigVo getCofigVo(String jsonStr){
		PushCofigVo pv = new PushCofigVo();
		try {
			JSONObject json = new JSONObject(jsonStr);
			pv.setApikey(json.getString("apikey"));
			pv.setIp(json.getString("ip"));
			pv.setPort(json.getString("port"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
			 
		}
		return pv;
	}
}
