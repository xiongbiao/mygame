package erb.unicomedu.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.QuestionsVo;

/***
 * 考试结果
 * @author xiong
 *
 */
public class ExamingResultActivity extends PublicActivity implements OnClickListener{

	private String TAG = "ExamingResultActivity";
	private Button mSeeWrong;
	private Button mExBack;
	private ImageButton mBack;
	private ArrayList<QuestionsVo> mQuestionList ;
	private ArrayList<QuestionsVo> mWrongQuestionList ;
	private double mSunNum = 0;
	private double mExamSunNum = 0;
	private TextView mScores;
	private TextView mExTime;
	private TextView mExPy;
	private ImageView mExIv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.examing_result);
		LogUtil.d(TAG,"ExamingResultActivity ~ onCreate ");
		init();
		initData();
	}
	public void init(){
		mSeeWrong = (Button)findViewById(R.id.wrong_questions);
		mSeeWrong.setOnClickListener(this);
		mExBack = (Button)findViewById(R.id.ex_back);
		mExBack.setOnClickListener(this);
		mBack = (ImageButton)findViewById(R.id.obj_top_back);
		mBack.setOnClickListener(this);
		mScores = (TextView) findViewById(R.id.obj_info_scores);
		mExTime = (TextView) findViewById(R.id.obj_result_time);
		mExPy = (TextView) findViewById(R.id.obj_info_py);
		mExIv = (ImageView) findViewById(R.id.obj_info_iv);
	}
	public void initData(){
		mQuestionList = (ArrayList<QuestionsVo>) getIntent().getSerializableExtra(Def.OBJ);
		if(mQuestionList!=null&&mQuestionList.size()>0){
			mWrongQuestionList = new ArrayList<QuestionsVo>();
			for(int i = 0 ;i<mQuestionList.size();i++){
				mExamSunNum = mExamSunNum +mQuestionList.get(i).getNumber();
				if(mQuestionList.get(i).getAnswerid().equals(mQuestionList.get(i).getmAnswer())){
					mSunNum = mSunNum +mQuestionList.get(i).getNumber();
				}else{
					mWrongQuestionList.add(mQuestionList.get(i));
				}
			}	
		}
		if(mWrongQuestionList == null||mWrongQuestionList.size()==0){
			mSeeWrong.setVisibility(View.INVISIBLE);
		}
		String  scoresStr="<font color='#73BF17'>考试成绩：</font>";  
		CharSequence charSequence=Html.fromHtml(scoresStr +mSunNum+"分"); 
		mScores.setText(charSequence);
		if(mSunNum/mExamSunNum>0.8){
			mExIv.setBackgroundResource(R.drawable.examing_result_ok);
			mExPy.setText("恭喜您，以优异的成绩通过考试！");
		}
		else if(mSunNum/mExamSunNum<0.6){
			mExIv.setBackgroundResource(R.drawable.examing_result_over);
			mExPy.setText("考试没通过，请加油！");
		}
		else {
			mExIv.setBackgroundResource(R.drawable.examing_result_liang);
			mExPy.setText("恭喜您，考试合格！");
		}
		int ftime =	getIntent().getIntExtra("syTime", 0);
		String timeStr="<font color='#73BF17'>答题时间：</font>";  
	    charSequence=Html.fromHtml(timeStr +ftime+"分钟"); 
		mExTime.setText(charSequence);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wrong_questions:
			mSeeWrong.setBackgroundResource(R.drawable.see_wrong_questions_s);
			mExBack.setBackgroundResource(R.drawable.ex_back);
			if(mWrongQuestionList != null&&mWrongQuestionList.size()>0){
			Intent mIntent = new Intent(this, WrongQuestionsActivity.class);
				Bundle mBundle = new Bundle();
				
					mBundle.putSerializable(Def.OBJ,mWrongQuestionList);  
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
			}
			break;
        case R.id.ex_back:
        	mSeeWrong.setBackgroundResource(R.drawable.see_wrong_questions);
			mExBack.setBackgroundResource(R.drawable.ex_back_s);
			finish();
			break;
        case R.id.obj_top_back:
        	finish();
			break;
		default:
			break;
		}
	}
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				finish();
			}
	    	return super.onKeyDown(keyCode, event);
	    }
}
