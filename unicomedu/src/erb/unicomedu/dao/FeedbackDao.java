package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.FeedbackTypeVo;

public class FeedbackDao extends PublicDao {

	private static String TAG  = "FeedbackDao";
	public static ArrayList<FeedbackTypeVo> getFeedbackType(Map<String, Object> param)throws Exception {
		ArrayList<FeedbackTypeVo> result = new ArrayList<FeedbackTypeVo>();
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.FeedbackTypeSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("typelist");
					LogUtil.d(TAG, "" + jArr.toString());
					JSONObject objItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							objItem = jArr.getJSONObject(i);
							FeedbackTypeVo objVo = new FeedbackTypeVo();
							String type = objItem.getString("type");
							String typeid = objItem.getString("typeid");
							objVo.setType(type);
							objVo.setTypeid(typeid);
							result.add(objVo);
						}
					}
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			} else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	
	public static boolean submitFeedback(Map<String, Object> param) throws Exception{
		boolean  bRestule = false;
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.FeedbackSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					bRestule = true;
				}else{
					LogUtil.d(TAG, " ------ code : "+code);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return bRestule;
	}
	
	
	
}
