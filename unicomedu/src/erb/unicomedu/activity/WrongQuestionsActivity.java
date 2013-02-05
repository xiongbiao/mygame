package erb.unicomedu.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.AnswerVo;
import erb.unicomedu.vo.QuestionsVo;

/***
 * 错误的题
 */
public class WrongQuestionsActivity extends PublicActivity  implements OnClickListener{
	private String TAG = "WrongQuestionsActivity";
	private ImageButton mBack;
	private Button mExamNext;
	private Button mExamPrevious;
	private int mQuestionNum = 0;
	private TextView mQuestionInfo;
	
//	private ImageView mExamA;
//	private ImageView mExamB;
//	private ImageView mExamC;
//	private ImageView mExamD;
	
//	private TextView mExamAInfo;
//	private TextView mExamBInfo;
//	private TextView mExamCInfo;
//	private TextView mExamDInfo;
	private TextView mExamRemark;
	private TextView mExamDA;
	
	LinearLayout ll;
	RelativeLayout rl;
	private ArrayList<QuestionsVo> mQuestionList ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_wrong_questions);
		LogUtil.d(TAG,"WrongQuestionsActivity ~ onCreate ");
		init();
		initData();
	}
	public void init(){
		mQuestionInfo = (TextView) findViewById(R.id.obj_info_introduce);
		
		mBack = (ImageButton)findViewById(R.id.obj_top_back);
		mBack.setOnClickListener(this);
		mExamNext = (Button)findViewById(R.id.ex_w_n);
		mExamNext.setOnClickListener(this);
		mExamPrevious = (Button)findViewById(R.id.ex_w_p);
		mExamPrevious.setOnClickListener(this);
		mExamRemark = (TextView) findViewById(R.id.ex_sm_remark);
		mExamDA = (TextView) findViewById(R.id.ex_sm_da);
		
		ll = (LinearLayout) findViewById(R.id.exam_subject_content);

	}
	
	@SuppressWarnings("unchecked")
	public void initData(){
		mQuestionList = (ArrayList<QuestionsVo>) getIntent().getSerializableExtra(Def.OBJ);
		if(mQuestionList!=null&&mQuestionList.size()>0){
			updateQuestion(mQuestionList.get(0) );
		}
	}
	

	private String getToQuestion(int id){
		String question = "";
		switch (id) {
		case -10:
			question = "A";
			break;
		case -11:
			question = "B";
			break;
		case -12:
			question = "C";
			break;
		case -13:
			question = "D";
			break;
		case -14:
			question = "E";
			break;
		case -15:
			question = "F";
			break;
		case -16:
			question = "J";
			break;
		case -17:
			question = "H";
			break;
		case -18:
			question = "I";
			break;
		case -19:
			question = "K";
			break;
		}
        return question;  
	}
	
	
	private boolean getSelect(String question){
		return question.equals(mQuestionList.get(mQuestionNum).getmAnswer()); 
	}
	
	private void updateAnswer(ArrayList<AnswerVo> answerlist) {
		if (answerlist != null && answerlist.size() > 0) {
			ll.removeAllViews();
		    int size = answerlist.size();
			for (int i = 0; i <size; i++) {
				rl = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.exam_subject_item, null, false);
				TextView mContent = (TextView) rl.findViewById(R.id.tv_exam_a);
			    ImageView mImage = (ImageView) rl.findViewById(R.id.iv_icon_a);
				mContent.setText(answerlist.get(i).getAnswer());
				rl.setId(-(i + 10));
				if(getSelect(getToQuestion(-(i+10)))){
				  mImage.setImageResource(R.drawable.exam_ti_dui);
				}
				ll.addView(rl);
			}

		} else {
			LogUtil.d(TAG, "问题------------is  null");
		}
	}
	
	private void updateQuestion(QuestionsVo qv){
		if(qv!=null){
			mQuestionInfo.setText((mQuestionNum + 1) + " : " + qv.getQuestion());
			mExamRemark.setText(qv.getRemark());
			String addressStr="<font color='#6D6B6B'>正确答案："+qv.getAnswerid()+"</font> <br><br>";  
			CharSequence charSequence=Html.fromHtml(addressStr +"问题解析说明 : "); 
			mExamDA.setText(charSequence);
			updateAnswer(qv.getAnswerlist());
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		 case R.id.obj_top_back:
			  finish();
				break;
		 case R.id.ex_w_n:
			 mExamNext.setBackgroundResource(R.drawable.ex_w_q_n_s);
			 mExamPrevious.setBackgroundResource(R.drawable.ex_w_q_p);
			 if(mQuestionList!=null&&mQuestionList.size()>0){
				 if(mQuestionList.size()-1>mQuestionNum){
					 mQuestionNum++;
					 updateQuestion(mQuestionList.get(mQuestionNum) );
				 }
			 }
				break;
		 case R.id.ex_w_p:
			 mExamNext.setBackgroundResource(R.drawable.ex_w_q_n);
			 mExamPrevious.setBackgroundResource(R.drawable.ex_w_q_p_s);
			 if(mQuestionList!=null&&mQuestionList.size()>0){
				 if(mQuestionList.size()>=mQuestionNum&&mQuestionNum>0){
					 mQuestionNum--;
					 updateQuestion(mQuestionList.get(mQuestionNum) );
				 }
			 }
				break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
