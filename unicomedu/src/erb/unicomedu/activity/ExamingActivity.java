package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.ExamDao;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.AnswerVo;
import erb.unicomedu.vo.ExamVo;
import erb.unicomedu.vo.QuestionsVo;

public class ExamingActivity extends PublicActivity implements OnClickListener {
	private TextView mTopName;
	private ImageButton mBack;
	private ExamVo mRv;
	private String TAG = "ExamingActivity";
	private ArrayList<QuestionsVo> mQuestionList;
	private TextView mQuestionInfo;
	private int mQuestionNum = 0;
//	private RelativeLayout mRlA;
//	private RelativeLayout mRlB;
//	private RelativeLayout mRlC;
//	private RelativeLayout mRlD;
//	private ImageView mExamA;
//	private ImageView mExamB;
//	private ImageView mExamC;
//	private ImageView mExamD;

//	private TextView mExamAInfo;
//	private TextView mExamBInfo;
//	private TextView mExamCInfo;
//	private TextView mExamDInfo;

	private TextView mExamTime;

	private Button mExamNext;
	private Button mExamPrevious;
	private Button mExamOK;

	private TimeCount time;
	private int mNowTime;
	private int mSTime;
	LinearLayout ll;
	RelativeLayout rl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.examing);
		LogUtil.d(TAG, "ExamingActivity ~ onCreate ");
		init();
		initData();

	}

	private void init() {
		mTopName = (TextView) findViewById(R.id.obj_top_name);
		mQuestionInfo = (TextView) findViewById(R.id.obj_info_introduce);
		mBack = (ImageButton) findViewById(R.id.obj_top_back);
		mBack.setOnClickListener(this);
		mExamTime = (TextView) findViewById(R.id.obj_info_time);
		mExamPrevious = (Button) findViewById(R.id.filter_latest);
		mExamOK = (Button) findViewById(R.id.filter_popularity);
		mExamNext = (Button) findViewById(R.id.filter_price);
		mExamPrevious.setOnClickListener(this);
		mExamOK.setOnClickListener(this);
		mExamNext.setOnClickListener(this);
		ll = (LinearLayout) findViewById(R.id.exam_subject_content);
	}

	public static int dipToPixels(Context context, float dip) {
		return (int) (context.getResources().getDisplayMetrics().density * dip);
	}

	private void initData() {
		mRv = (ExamVo) getIntent().getSerializableExtra(Def.OBJ);
		if (mRv != null) {
			// mTopName.setText(mRv.getExamname());
			try {
				String examid = mRv.getExamid();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("examid", examid);
				if (userInfo != null) {
					param.put("memberid", userInfo.getMemberid());
				} else {
					param.put("memberid", 0);
				}
				mQuestionList = ExamDao.getQuestionsList(param);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (mQuestionList != null && mQuestionList.size() > 0) {
				updateQuestion(mQuestionList.get(0));
				time = new TimeCount(60000 * mRv.getNeedtime(), 1000);// 构造CountDownTimer对象
				time.start();// 开始计时
			} else {
				Toast.makeText(this, "没有试题 ", Toast.LENGTH_SHORT).show();
				finish();
			}

		}

	}

	/* 定义一个倒计时的内部类 */
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			mExamTime.setText("考试结束");
			mExamTime.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			mExamTime.setClickable(false);
			mSTime = Integer.valueOf(millisUntilFinished % 60000 / 1000 + "");
			mNowTime = Integer.valueOf(millisUntilFinished / 60000 + "");
			mExamTime.setText("剩余时间 ：" + mNowTime + "分" + mSTime + "秒");
			if(mNowTime == 0 && mSTime == 0){
				Toast.makeText(ExamingActivity.this, "时间到 考试结束  ",Toast.LENGTH_SHORT ).show();
				Intent mIntent = new Intent(ExamingActivity.this, ExamingResultActivity.class);
				Bundle mBundle = new Bundle();
				if (mQuestionList != null) {
					mBundle.putSerializable(Def.OBJ, mQuestionList);
				}
				mBundle.putInt("syTime", (mRv.getNeedtime() - mNowTime));
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
				time.cancel();
				time.onFinish();
				finish();
			}
		}
	}

	private void updateQuestion(QuestionsVo qv) {
		if (qv != null) {
			mQuestionInfo.setText((mQuestionNum + 1) + " : " + qv.getQuestion());
			updateAnswer(qv.getAnswerlist());
		} else {
			LogUtil.d(TAG, "1-------QuestionsVo");
		}
	}



	private void updateAnswer(ArrayList<AnswerVo> answerlist) {
		if (answerlist != null && answerlist.size() > 0) {
			ll.removeAllViews();
		    int size = answerlist.size();
			for (int i = 0; i <size; i++) {
				rl = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.exam_subject_item, null, false);
				TextView mContent = (TextView) rl.findViewById(R.id.tv_exam_a);
				final ImageView mImage = (ImageView) rl.findViewById(R.id.iv_icon_a);
				mContent.setText(answerlist.get(i).getAnswer());
				rl.setId(-(i + 10));
				mImage.setId(i + 10);
				if(getSelect(getToQuestion(-(i+10)))){
				  mImage.setImageResource(R.drawable.exam_ti_dui);
				}
				ll.addView(rl);
                
				final LinearLayout ls = ll;
				rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						boolean isC = false;
						if (mImage.getTag() == null || !"true".equals(mImage.getTag())) {
							mImage.setImageResource(R.drawable.exam_ti_dui);
							mImage.setTag("true");
							mQuestionList.get(mQuestionNum).setmAnswer(getToQuestion(v.getId()));
							isC = true;
						} else {
							mImage.setImageResource(R.drawable.exam_ti);
							mImage.setTag("false");
							isC = false;
						}
						LogUtil.d(TAG, ls.getChildCount() + " --------");
						if (isC) {
							// 做单选题
							for (int j = 0; j < ls.getChildCount(); j++) {
								View vItem = ls.getChildAt(j);
								if (vItem instanceof RelativeLayout) {
									if (vItem.getId() != v.getId()) {
										for (int k = 0; k < ((RelativeLayout) vItem)
												.getChildCount(); k++) {
											View vvItem = ((RelativeLayout) vItem)
													.getChildAt(k);
											if (vvItem instanceof ImageView) {
												if ("true".equals(vvItem.getTag())) {
													((ImageView) vvItem).setImageResource(R.drawable.exam_ti);
													vvItem.setTag("false");
												}
											}
										}
									}  
								}
							}
						}
					}
				});

			}

		} else {
			LogUtil.d(TAG, "问题------------is  null");
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.obj_top_back:
			finish();
			break;
		case R.id.filter_latest:
			if (mQuestionList != null && mQuestionList.size() > 0) {
				if (mQuestionList.size() >= mQuestionNum && mQuestionNum > 0) {
					mQuestionNum--;
					updateQuestion(mQuestionList.get(mQuestionNum));
				}
			}
			break;
		case R.id.filter_price:
			if (mQuestionList != null && mQuestionList.size() > 0) {
				if (mQuestionList.size() - 1 > mQuestionNum) {
					mQuestionNum++;
					updateQuestion(mQuestionList.get(mQuestionNum));
				}else{
					Toast.makeText(this, "已经是最后一道题了", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.filter_popularity:
			Intent mIntent = new Intent(this, ExamingResultActivity.class);
			Bundle mBundle = new Bundle();
			if (mQuestionList != null) {
				mBundle.putSerializable(Def.OBJ, mQuestionList);
			}
			mBundle.putInt("syTime", (mRv.getNeedtime() - mNowTime));
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
			time.cancel();
			time.onFinish();
			finish();
			break;
		default:
			break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
