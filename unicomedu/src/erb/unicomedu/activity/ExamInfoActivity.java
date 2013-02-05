package erb.unicomedu.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.dao.ExamDao;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.ExamVo;
import erb.unicomedu.vo.VideoVo;

public class ExamInfoActivity extends PublicActivity implements OnClickListener {

	private ExamVo mRv;
	private Button mReading;
	private ImageButton mBack;
	private TextView mObjName;
	private TextView mObjAuthor;
	private TextView mObjLevel;
	private TextView mObjIntroduce;
	private ImageView mObjPicture;
	
//	private ImageButton mRatingButton;
	private Dialog mRatingDialog;
    private RadioGroup ratingGroup;
    private int startCode = 0;
	private Dialog mConfirmDialog;
	private Bundle mObjBundle;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		String className =  this.getClass().getName();
		String fromClassName =  new ExamActivity().getClass().getName();
		isModel(Def.MODEl_EX_INFO, className,fromClassName,null);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_info);
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mObjBundle!=null){
        	mRv = (ExamVo) mObjBundle.getSerializable (Def.OBJ);
        } 
		init();
		initData();
	}
	
	private void initData(){
		if(mRv!=null){
			updateVideoName(mRv.getExamname());	
			updateVideoAuthor(mRv.getTeacher());	
			updateVideoLevel(mRv.getClknumber()+"");	
			mObjIntroduce.setText(mRv.getInfo());	
			 Drawable cachedImage = new AsyncImageLoader().loadDrawable(mRv.getImgurl(), new ImageCallback() {
		            @Override
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		                ImageView imageViewByTag = mObjPicture;
		                if (imageViewByTag != null&&imageDrawable!=null) {
		                     Bitmap bitmap = ((BitmapDrawable)imageDrawable).getBitmap();
		                     imageViewByTag.setImageBitmap(bitmap);
		                }
		            }
		        });
			 if(cachedImage!=null){
			    	Bitmap bitmap = ((BitmapDrawable)cachedImage).getBitmap();
			    	mObjPicture.setImageBitmap(bitmap);
			    }else{
			    	mObjPicture.setImageResource(R.drawable.avatar_icon);
			 }
		}
	}

	private void init() {

		mReading = (Button) findViewById(R.id.obj_info_reading);
		mReading.setOnClickListener(this);
		mBack = (ImageButton) findViewById(R.id.obj_info_back);
		mBack.setOnClickListener(this);
//		mRatingButton = (ImageButton) findViewById(R.id.obj_rating_btn);
//		mRatingButton.setOnClickListener(this);
		mObjName = (TextView) findViewById(R.id.obj_info_name);
		mObjAuthor = (TextView) findViewById(R.id.obj_info_author);
		mObjLevel = (TextView) findViewById(R.id.obj_info_level);
		mObjIntroduce = (TextView) findViewById(R.id.obj_info_introduce);
		mObjPicture = (ImageView) findViewById(R.id.item_avatar);
	}

	/**
	 * 更新名称
	 */
	private void updateVideoName(String content) {
		mObjName.setText(content);
	}

	/**
	 * 更新作者 parame 作者姓名
	 */
	private void updateVideoAuthor(String content) {
		mObjAuthor.setText(String.format(getString(R.string.obj_info_author), content));
	}

	/**
	 * 更新播放数 parame 播放次数
	 */
	private void updateVideoLevel(String content) {
		mObjLevel.setText(String.format(getString(R.string.exam_info_level), content));
	}

	@Override
	public void onClick(View v) {
		String className = "";
		boolean result  = false;
		switch (v.getId()) {
			case R.id.obj_info_reading:
				className =  this.getClass().getName();
				 result = isModel(Def.MODEl_EX_P, className,className,mObjBundle);
				if (mRv!= null&result){
					LogUtil.d("--------------------", mRv.getIsbuy()+"--------"+mRv.getIntegral());
					if(mRv.getIsbuy()==0&&!"0".equals(mRv.getIntegral())){
						createScoreConfirmDialog(Integer.valueOf(mRv.getIntegral()));
					}else{
						startReadVideo();
					}
				}
				break;
			case R.id.obj_info_back:
				finish();
				break;
			case R.id.obj_rating_btn:
				Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
				break;
			default:
				dialogLinstener(v.getId());
				break;

		}
	}
	
	/**
	 * 评级对话框控件监听
	 * @param id
	 */
	private void dialogLinstener(int id){
		switch (id) {
			case R.id.dialog_close:
				if(mRatingDialog != null){
					mRatingDialog.dismiss();
				}
				break;
			case R.id.dialog_ok:
				if (mRatingDialog != null) {
					mRatingDialog.dismiss();
					int checkButtonId = ratingGroup.getCheckedRadioButtonId();
					switch (checkButtonId) {
						case R.id.dialog_star_1:
							startCode = 1;
							break;
						case R.id.dialog_star_2:
							startCode = 2;
							break;
						case R.id.dialog_star_3:
							startCode = 3;
							break;
						case R.id.dialog_star_4:
							startCode = 4;
							break;
						case R.id.dialog_star_5:
							startCode = 5;
							break;
					}
				}
				break;
			case R.id.score_confirm_no:
				if(mConfirmDialog != null){
					mConfirmDialog.dismiss();
				}
				break;
			case R.id.score_confirm_yes:
				if (mConfirmDialog != null&&userInfo!=null) {
					mConfirmDialog.dismiss();
					try {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("memberid", userInfo.getMemberid());
					param.put("examid", mRv.getExamid());
					String res = ExamDao.ExamBuy(param);
					if (Def.FAV_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "已经购买", Toast.LENGTH_SHORT).show();
						mRv.setIsbuy(1);
						startReadVideo();
					} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
						Toast.makeText(this, "购买成功", Toast.LENGTH_SHORT).show();
						mRv.setIsbuy(1);
						startReadVideo();
					}else if (Def.NO_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "积分不够", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "购买失败", Toast.LENGTH_SHORT).show();
					}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(this, "购买失败", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this, "不能购买", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
	
	private void startReadVideo(){
		Intent mIntent = new Intent(this, ExamingActivity.class);
		Bundle mBundle = new Bundle();
		if(mRv != null){
			mBundle.putSerializable(Def.OBJ,mRv);  
		}
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}
	
	/**
	 * 显示评星对话框
	 */
	private void createRatingStarDialog() {
		mRatingDialog = new Dialog(this, R.style.RatingDialog);
		mRatingDialog.setContentView(R.layout.rating_star_dialog);
		mRatingDialog.show();
		Button dialogClose = (Button)mRatingDialog.findViewById(R.id.dialog_close);
		Button dialogOk = (Button) mRatingDialog.findViewById(R.id.dialog_ok);
		dialogClose.setOnClickListener(this);
		dialogOk.setOnClickListener(this);
		ratingGroup = (RadioGroup)mRatingDialog.findViewById(R.id.dialog_rating_group);
	}

	private void createScoreConfirmDialog(int score){
		mConfirmDialog = new Dialog(this, R.style.RatingDialog);
		mConfirmDialog.setContentView(R.layout.score_confirm_dialog);
		mConfirmDialog.show();
		Button dialogClose = (Button)mConfirmDialog.findViewById(R.id.score_confirm_no);
		Button dialogOk = (Button) mConfirmDialog.findViewById(R.id.score_confirm_yes);
		TextView confirmInfo = (TextView)mConfirmDialog.findViewById(R.id.score_confirm_info);
		confirmInfo.setText(String.format(getString(R.string.exam_score_confirm_info), String.valueOf(score)));
		dialogClose.setOnClickListener(this);
		dialogOk.setOnClickListener(this);
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
