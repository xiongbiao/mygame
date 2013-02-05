
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
import erb.unicomedu.vo.AnswerVo;
import erb.unicomedu.vo.ExamVo;
import erb.unicomedu.vo.QuestionsVo;

public class ExamDao extends PublicDao{
	private  static String TAG =  "ExamDao";
	public static ArrayList<ExamVo> getExamList(Map<String, Object> param) throws Exception {
		ArrayList<ExamVo> result = new ArrayList<ExamVo>();
		try {
			DefMap(param);
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			LogUtil.d(TAG, HttpUrls.ExamSERVER);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.ExamSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("Examlist");
					JSONObject videoItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							videoItem = jArr.getJSONObject(i);
							ExamVo examVo =new ExamVo();
							String examid = videoItem.getString("examid");
							String type = videoItem.getString("type");
							String info = videoItem.getString("info");
							String school = videoItem.getString("school");
							String subject = videoItem.getString("subject");
							String imgurl = videoItem.getString("imgurl");
							String pubtime = videoItem.getString("pubtime");
							String typeid = videoItem.getString("typeid");
							String examname = videoItem.getString("examname");
							String payment = videoItem.getString("payment");
							String integral = videoItem.getString("integral");
							String teacherid = videoItem.getString("teacherid");
							String teacher = videoItem.getString("teacher");
							int needtime = videoItem.getInt("needtime");
							String clknumber = videoItem.getString("clknumber")==null||"null".equals(videoItem.getString("clknumber")) ?"0":videoItem.getString("clknumber");
							int isbuy =  videoItem.getInt("isbuy");
							examVo.setIsbuy(isbuy);
							examVo.setExamid(examid);
							examVo.setClknumber(clknumber);
							examVo.setInfo(info);
							examVo.setImgurl(imgurl);
							examVo.setIntegral(integral);
							examVo.setPayment(payment);
							examVo.setPubtime(pubtime);
							examVo.setSchool(school);
							examVo.setSubject(subject);
							examVo.setTeacher(teacher);
							examVo.setTeacherid(teacherid);
							examVo.setType(type);
							examVo.setTypeid(typeid);
							examVo.setExamname(examname);
							examVo.setNeedtime(needtime);
							result.add(examVo);
						}
					}  
				}else{
					LogUtil.d(TAG, "server code : "+ code);
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

	public static ArrayList<QuestionsVo> getQuestionsList(Map<String, Object> param) throws Exception {
		ArrayList<QuestionsVo> result = new ArrayList<QuestionsVo>();
		try {
			DefMap(param);
			param.put("subjectid", "1");
			param.put("page", 0);
			param.put("size", 20);
			
			String from =HttpUtil.MapToParam(param) ;
			LogUtil.d(TAG, from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from,HttpUrls.QuestionSERVER, "", ""); 
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				LogUtil.d(TAG, resultJson);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if("200".equals(code)){
					JSONArray jArr = (JSONArray) json.get("Examlist");
					JSONObject questionItem;
					if (jArr.length() > 0) {
						for (int i = 0; i < jArr.length(); i++) {
							questionItem = jArr.getJSONObject(i);
							QuestionsVo examVo =new QuestionsVo();
							String examid = questionItem.getString("examid");
							
							String question = questionItem.getString("question");
							int sort = questionItem.getInt("sort");
							String school = questionItem.getString("school");
							String remark = questionItem.getString("remark");
							String sid = questionItem.getString("sid");
							String questionid = questionItem.getString("questionid");
							String answercode = questionItem.getString("answercode");
							
							String exam = questionItem.getString("exam");
							int number = questionItem.getInt ("number");
							JSONArray alist =(JSONArray) questionItem.get("answerlist");
							ArrayList<AnswerVo> answerlist = new ArrayList<AnswerVo>();
							if (alist != null && alist.length() > 0) {
								JSONObject answerItem;
								for (int j = 0; j < alist.length(); j++) {
									AnswerVo a = new  AnswerVo();
									answerItem = alist.getJSONObject(j);
									String answer_id = answerItem.getString("answerid");
									String itemid = answerItem.getString("answercode");
									String answer = answerItem.getString("answer");
									int mSort = answerItem.getInt("sort");
									a.setAnswer(answer);
									a.setAnswerid(answer_id);
									a.setItemid(itemid);
									a.setSort(mSort);
									answerlist.add(a);
								}
							}
							examVo.setAnswerlist(answerlist);
							examVo.setExamid(examid);
							examVo.setQuestion(question);
							examVo.setAnswerid(answercode);
							examVo.setExam(exam);
							examVo.setQuestionid(questionid);
							examVo.setRemark(remark);
							examVo.setSid(sid);
							examVo.setSort(sort);
							examVo.setSchool(school);
							examVo.setNumber(number);
							
							result.add(examVo);
						}
					}  
				}else{
					LogUtil.d(TAG, "server code : "+ code);
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
	public static String  ExamBuy(Map<String, Object> param) throws Exception {
		String result = "";
		try {
			DefMap(param);
			String from = HttpUtil.MapToParam(param);
			LogUtil.d(TAG, "ExamBuy : "+from);
			HttpURLConnection conn = HttpUtil.getHttpURLConnection(from, HttpUrls.ExamBuySERVER, "", "");
			if (conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				String resultJson = HttpUtil.inputStreamString(in);
				JSONObject json = new JSONObject(resultJson);
				String code = json.getString("code");
				if ("200".equals(code)) {
					result = Def.FAV_OBJ_RESULT_OK;
				}else if("201".equals(code)){
					result = Def.FAV_OBJ_RESULT;
				}else if("202".equals(code)){
					result = Def.NO_OBJ_RESULT;
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
}
