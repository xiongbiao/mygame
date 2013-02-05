package erb.unicomedu.dao;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.HttpUrls;
import erb.unicomedu.util.HttpUtil;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.PlaceVo;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TreeVo;

public class SubjectDao extends PublicDao {
	private static final String TAG = "SubjectDao";

	public static ArrayList<SubjectVo> getSubjectList(Map<String, Object> param)
			throws Exception {
		ArrayList<SubjectVo> result = new ArrayList<SubjectVo>();
		try {
			DefMap(param);

			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.SubjectSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("Subjectlist");
					result = getTList(jArr);
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

	public static String addFavorites(Map<String, Object> param) throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "addFavorites : " + from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.SubjectAddFavoritesSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = Def.FAV_OBJ_RESULT_OK;
				} else if ("201".equals(code)) {
					result = Def.FAV_OBJ_RESULT;
				}
				LogUtil.d(TAG, resultJson);
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

	public static ArrayList<SubjectVo> getSubjectFavoritesList(
			Map<String, Object> param) throws Exception {
		ArrayList<SubjectVo> result = new ArrayList<SubjectVo>();
		try {
			DefMap(param);
			param.put("size", 20);
			param.put("page", 0);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.SubjectFavoritesSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("subjectlist");
					result = getTList(jArr);
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

	private static ArrayList<SubjectVo> getTList(JSONArray jArr) {
		ArrayList<SubjectVo> result = new ArrayList<SubjectVo>();
		try {
			JSONObject videoItem;
			if (jArr.length() > 0) {
				for (int i = 0; i < jArr.length(); i++) {
					videoItem = jArr.getJSONObject(i);
					SubjectVo subjectVo = new SubjectVo();
					String subjectid = videoItem.getString("subjectid");
					String subject = videoItem.getString("subject");
					String info = videoItem.getString("info");
					String type = videoItem.getString("type");
					String fitstudent = videoItem.getString("fitstudent");
					String pubtime = videoItem.getString("pubtime");
					int clknumber = videoItem.getString("clknumber") == null
							|| "null".equals(videoItem.getString("clknumber")) ? 0
							: videoItem.getInt("clknumber");
					int isarchived = videoItem.getInt("isarchived");
					// int isbuy = videoItem.getInt("isbuy");
					// int integral = videoItem.getInt("integral");
					subjectVo.setIsarchived(isarchived);
					// subjectVo.setIsbuy(isbuy);
					// subjectVo.setIntegral(integral);
					subjectVo.setInfo(info);
					subjectVo.setFitstudent(fitstudent);
					subjectVo.setSubjectName(subject);
					subjectVo.setPubtime(pubtime);
					subjectVo.setClknumber(clknumber);
					subjectVo.setSubjectId(subjectid);
					subjectVo.setType(type);
					result.add(subjectVo);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}

	/***
	 * 获得上课地点
	 * 
	 * @param param
	 * @return
	 */
	public static ArrayList<PlaceVo> getSubjectClassList(
			Map<String, Object> param) throws Exception {
		ArrayList<PlaceVo> result = new ArrayList<PlaceVo>();
		try {
			DefMap(param);

			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.SubjectClassSERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					JSONArray jArr = (JSONArray) json.get("classlist");
					JSONObject videoItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							videoItem = jArr.getJSONObject(i);
							PlaceVo pv = new PlaceVo();
							String address = videoItem.getString("address");
							String number = videoItem.getString("number");
							String info = videoItem.getString("info");
							String subjectid = videoItem.getString("subjectid");
							String itemid = videoItem.getString("classid");
							String classno = videoItem.getString("classno");
							String schoollocation = videoItem
									.getString("schoollocation");
							String amount = videoItem.getString("amount");
							String mapurl = videoItem.getString("mapurl");
							String locationid = videoItem
									.getString("locationid");
							pv.setLocationid(locationid);
							pv.setInfo(info);
							pv.setAddress(address);
							pv.setAmount(amount);
							pv.setClassno(classno);
							pv.setItemid(itemid);
							pv.setMapurl(mapurl);
							pv.setNumber(number);
							pv.setSchoollocation(schoollocation);
							pv.setSubjectid(subjectid);
							result.add(pv);
						}
					}
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			} else {
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}

	public static ArrayList<TreeVo> getNavTypeList(Map<String, Object> param)
			throws Exception {
		ArrayList<TreeVo> result = new ArrayList<TreeVo>();
		try {
			DefMap(param);
			param.put("typeid", 1);
			param.put("subjectid", 1);
			param.put("page", 0);
			param.put("size", 200);
			param.put("orderby", 1);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.SubjectSERVERTYPE, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {

					JSONArray jArr = (JSONArray) json.get("Typelist");
					LogUtil.d(TAG, "" + jArr.toString());

					JSONObject videoItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							videoItem = jArr.getJSONObject(i);
							TreeVo treeVo = new TreeVo();
							String parentid = videoItem.getString("parentid");
							String treeId = videoItem.getString("typeid");
							String treeName = videoItem.getString("typename");
							int subjectnumber = videoItem
									.getInt("subjectnumber");
							int booknumber = videoItem.getInt("booknumber");
							int medianumber = videoItem.getInt("medianumber");
							int examnumber = videoItem.getInt("examnumber");
							treeVo.setBooknumber(booknumber);
							treeVo.setExamnumber(examnumber);
							treeVo.setMedianumber(medianumber);
							treeVo.setParentid(parentid);
							treeVo.setSubjectnumber(subjectnumber);
							treeVo.setTreeId(treeId);
							treeVo.setTreeName(treeName);
							result.add(treeVo);
						}
					}
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			} else {
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}

	public static ArrayList<TreeVo> getArrayToTree(JSONArray jArr) {
		ArrayList<TreeVo> result = new ArrayList<TreeVo>();
		try {
			LogUtil.d(TAG, "" + jArr.toString());
			JSONObject videoItem;
			if (jArr.length() > 0) {
				for (int i = 0; i < jArr.length(); i++) {
					videoItem = jArr.getJSONObject(i);
					TreeVo treeVo = new TreeVo();
					String parentid = videoItem.getString("parentid");
					String treeId = videoItem.getString("typeid");
					String treeName = videoItem.getString("typename");
					int subjectnumber = videoItem.getInt("subjectnumber");
					int booknumber = videoItem.getInt("booknumber");
					int medianumber = videoItem.getInt("medianumber");
					int examnumber = videoItem.getInt("examnumber");
					treeVo.setBooknumber(booknumber);
					treeVo.setExamnumber(examnumber);
					treeVo.setMedianumber(medianumber);
					treeVo.setParentid(parentid);
					treeVo.setSubjectnumber(subjectnumber);
					treeVo.setTreeId(treeId);
					treeVo.setTreeName(treeName);
					result.add(treeVo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String getNavTypeString(Map<String, Object> param)
			throws Exception {
		String result = "";
		try {
			DefMap(param);
			param.put("typeid", 1);
			param.put("subjectid", 1);
			param.put("page", 0);
			param.put("size", 200);
			param.put("orderby", 1);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.SubjectSERVERTYPE, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = json.getString("Typelist");
					LogUtil.d(TAG, "" + result);
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}

			} else {
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}

	public static String OnlineRegistration(Map<String, Object> param)
			throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,
					HttpUrls.OnlineRegistrationSERVERTYPE, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				boolean success = json.getBoolean("success");
				if ("200".equals(code) && success) {
					result = "success";
				} else {
					LogUtil.d(TAG, "server code : " + code);
				}
			} else {
				throw new EuException(Def.getServiceMsg(conn.getResponseCode()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new EuException(e);
		}
		return result;
	}
}
