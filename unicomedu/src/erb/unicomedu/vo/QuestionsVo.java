package erb.unicomedu.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 试卷
 * @author xiong
 *
 */
public class QuestionsVo implements Serializable {

	private String question;
	private int sort;
	private String remark;
	private String school;
	private String sid;
	private String examid;
	private String questionid;
	private String answerid;
	private String exam;
	private String mAnswer;
	private int number;
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getmAnswer() {
		return mAnswer;
	}

	public void setmAnswer(String mAnswer) {
		this.mAnswer = mAnswer;
	}

	private ArrayList<AnswerVo> answerlist;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getExamid() {
		return examid;
	}

	public void setExamid(String examid) {
		this.examid = examid;
	}

	public String getQuestionid() {
		return questionid;
	}

	public void setQuestionid(String questionid) {
		this.questionid = questionid;
	}

	public String getAnswerid() {
		return answerid;
	}

	public void setAnswerid(String answerid) {
		this.answerid = answerid;
	}

	public String getExam() {
		return exam;
	}

	public void setExam(String exam) {
		this.exam = exam;
	}

	public ArrayList<AnswerVo> getAnswerlist() {
		return answerlist;
	}

	public void setAnswerlist(ArrayList<AnswerVo> answerlist) {
		this.answerlist = answerlist;
	}

	 

 
}
