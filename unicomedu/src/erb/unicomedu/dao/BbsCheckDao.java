package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsCheckVo;

public class BbsCheckDao extends PublicDao{
	private static String TAG = "BbsCheckDao";
	
	public static ArrayList<BbsCheckVo> getBbsCheckList(Map<String, Object> param, BbsCheckVo vo)throws Exception {
		ArrayList<BbsCheckVo> result = new ArrayList<BbsCheckVo>();
		try {
			result.add(vo);
			DefMap(param);
			param.put("page", 0);
			param.put("size", 20);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.BbsCheckSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("replylist");
					LogUtil.i("TUDE==", "JSONArray-jArr.LENGTH==" + jArr.length());
					JSONObject bbsItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							bbsItem = jArr.getJSONObject(i);
							BbsCheckVo bbsCheckVo = new BbsCheckVo();
							
							String context = bbsItem.getString("context");
							LogUtil.i("TUDE==", "NNNNN===1");
							String replyto = bbsItem.getString("replyto");
							LogUtil.i("TUDE==", "NNNNN===2");
							String title = bbsItem.getString("title");
							int askid = bbsItem.getInt("askid");
							int replyid = bbsItem.getInt("replyid");
							String pubtime = bbsItem.getString("pubtime");
							String asktitle = bbsItem.getString("asktitle");
							int answerid = bbsItem.getInt("answerid");
							String answer = bbsItem.getString("answer");
							String replytolist = bbsItem.getString("replytolist");
							String floor = bbsItem.getString("floor");
							
							bbsCheckVo.setAnswer(answer);
							bbsCheckVo.setAnswerid(answerid);
							bbsCheckVo.setAskid(askid);
							bbsCheckVo.setAsktitle(asktitle);
							bbsCheckVo.setContext(context);
							bbsCheckVo.setPubtime(pubtime);
							bbsCheckVo.setReplyid(replyid);
							bbsCheckVo.setReplyto(replyto);
							bbsCheckVo.setReplytolist(replytolist);
							bbsCheckVo.setTitle(title);
							bbsCheckVo.setFloor(floor); 
							LogUtil.d("%%%%%%%", replytolist );
							if(replytolist!=null&&!replytolist.equals("")&&!"null".equals(replytolist)) {
								JSONArray array = bbsItem.getJSONArray("replytolist");
								ArrayList<BbsCheckVo> reply = new ArrayList<BbsCheckVo>();
								run(array,reply);
								LogUtil.i("TUDE==", "SIZE" + reply.size());
								bbsCheckVo.setBbsCheckVoList(reply);
							}
							LogUtil.i("TUDE==", "I="+bbsCheckVo.getAnswerid());
							result.add(bbsCheckVo);
						}
					}
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			}else {
				LogUtil.d(TAG, "code : " + conn.getResponseCode());
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
	public static void run(JSONArray array, ArrayList<BbsCheckVo> reply) {
		JSONObject bbsItem;
		if (array.length() > 0) {
			try {
				for (int i = 0; i < array.length(); i++) {
					bbsItem = array.getJSONObject(i);
					BbsCheckVo bbsCheckVo = new BbsCheckVo();
					
					String context = bbsItem.getString("context");
					String title = bbsItem.getString("title");
					int askid = bbsItem.getInt("askid");
					int replyid = bbsItem.getInt("replyid");
					String pubtime = bbsItem.getString("pubtime");
					String asktitle = bbsItem.getString("asktitle");
					int answerid = bbsItem.getInt("answerid");
					String answer = bbsItem.getString("answer");
					if(i==0){
						String floor = bbsItem.getString("floor");
						bbsCheckVo.setFloor(floor);
					}
					bbsCheckVo.setAnswer(answer);
					bbsCheckVo.setAnswerid(answerid);
					bbsCheckVo.setAskid(askid);
					bbsCheckVo.setAsktitle(asktitle);
					bbsCheckVo.setContext(context);
					bbsCheckVo.setPubtime(pubtime);
					bbsCheckVo.setReplyid(replyid);
					bbsCheckVo.setTitle(title);
					reply.add(bbsCheckVo);
				}
			} catch (JSONException e) {
				LogUtil.i("TUDE==", "reply-here1=6");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
