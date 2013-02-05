package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.TeacherSubjectAdapter;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TeacherVo;

/**
 * 名师详情介绍
 */
public class TeacherInfoActivity extends PublicActivity implements OnClickListener, OnItemClickListener {
	private String TAG = "TeacherInfoActivity";
	private TextView mTeaName;
	private TextView mTeaEnName;
	private TextView mTeaSex;
	private TextView mTeaHometown;
	private TextView mTitler;
	private TextView mExperience;
	private TextView mTeaInfo;
	private ImageView mTeaPicture;
	private TeacherVo teacherVo;
	private ImageButton mBack;

	private Button mEInfo;
	private Button mExqcx;
	private LinearLayout mLinfo;
	private LinearLayout mLExqcx;

	private RatingBar mRatingBar;
	private ImageButton mFavoriteButton;
	private Button mRatingButton;

	private Dialog mRatingDialog;
	private RadioGroup ratingGroup;
	private int startCode = 0;
	private LoadingView lv;
	private PullToRefreshListView prlistView;
	private TeacherSubjectAdapter subjectBaseAdpter;
	List<SubjectVo> data;
	private AsyncImageLoader asyncImageLoader;
	private Bundle mObjBundle;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		String className = this.getClass().getName();
		String fromClassName = new TeacherActivity().getClass().getName();
		isModel(Def.MODEl_TE_INFO, className, fromClassName,null);
		new GetDataTask().execute();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacherinfo);
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
		if(mObjBundle!=null)
		   teacherVo = (TeacherVo) mObjBundle.getSerializable(Def.OBJ);
		init();

	}

	private void init() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		mTeaName = (TextView) findViewById(R.id.teacherinfo_name);
		mTeaEnName = (TextView) findViewById(R.id.teacherinfo_en_name);
		mTeaSex = (TextView) findViewById(R.id.teacherinfo_sex);
		mTeaHometown = (TextView) findViewById(R.id.teacherinfo_jg);
		mTitler = (TextView) findViewById(R.id.teacherinfo_titler);
		mExperience = (TextView) findViewById(R.id.teacherinfo_experience);
		mTeaInfo = (TextView) findViewById(R.id.teacherinfo_info);
		mTeaPicture = (ImageView) findViewById(R.id.teacherinfo_picture);

		mEInfo = (Button) findViewById(R.id.filter_info);
		mExqcx = (Button) findViewById(R.id.filter_xqcx);
		mBack = (ImageButton) findViewById(R.id.teacher_search_back);
		mBack.setOnClickListener(this);
		mEInfo.setOnClickListener(this);
		mExqcx.setOnClickListener(this);
		mLinfo = (LinearLayout) findViewById(R.id.teacherinfo_ll_info);
		mLExqcx = (LinearLayout) findViewById(R.id.teacherinfo_ll_xqjs);

		mRatingBar = (RatingBar) findViewById(R.id.teacherinfo_rating);
		mFavoriteButton = (ImageButton) findViewById(R.id.teacher_favorite_btn);
		mFavoriteButton.setOnClickListener(this);
		mRatingButton = (Button) findViewById(R.id.teacherinfo_pf);
		mRatingButton.setOnClickListener(this);

		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});

		data = new ArrayList<SubjectVo>();
		
		prlistView.setOnItemClickListener(this);
		
		asyncImageLoader = new AsyncImageLoader();
		if (teacherVo != null) {
			mTeaName.setText(String.format(
					getResources().getString(R.string.teacherinfo_name),
					teacherVo.getCnname()));
			mTeaEnName.setText(String.format(
					getResources().getString(R.string.teacherinfo_en_name),
					teacherVo.getEnname()));
			mTeaSex.setText(String.format(
					getResources().getString(R.string.teacherinfo_sex),
					teacherVo.getSex() == null ? "" : teacherVo.getSex()));
			mTeaHometown.setText(String.format(
					getResources().getString(R.string.teacherinfo_jg),
					teacherVo.getCountry()));
			mTitler.setText(String.format(
					getResources().getString(R.string.teacherinfo_titler),
					teacherVo.getZhicheng()));
			mExperience.setText(String.format(
					getResources().getString(R.string.teacherinfo_experience),
					teacherVo.getJingyan()));
			mTeaInfo.setText(teacherVo.getInfo());
			 Drawable cachedImage = asyncImageLoader.loadDrawable(teacherVo.getPicture(), new ImageCallback() {
		            @Override
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		                ImageView imageViewByTag = mTeaPicture;
		                if (imageViewByTag != null&&imageDrawable!=null) {
		                     Bitmap bitmap = ((BitmapDrawable)imageDrawable).getBitmap();
		                    imageViewByTag.setImageBitmap( bitmap );
		                }
		            }
		        });
		    if(cachedImage!=null){
		    	Bitmap bitmap = ((BitmapDrawable)cachedImage).getBitmap();
		    	mTeaPicture.setImageBitmap(  bitmap );
		    }else{
		    	mTeaPicture.setImageResource(R.drawable.avatar_icon);
		    }
			mRatingBar.setNumStars(teacherVo.getStar());
			if(teacherVo.getIsarchived()==1){
			   mFavoriteButton.setBackgroundResource(R.drawable.fav_ed);
			}else{
			  mFavoriteButton.setBackgroundResource(R.drawable.button_background_7);
			}
			 LogUtil.d(TAG, "Isarchived : " + teacherVo.getIsarchived());
		}
	}

	/**
	 * 刷新数据
	 * 
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<SubjectVo>> {
		int exType = 0;
		String erMsg = "";
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}
		@Override
		protected List<SubjectVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("teacherid", teacherVo.getTeacherid());
				if(userInfo!=null)
				param.put("memberid", userInfo.getMemberid());
				data = TeacherDao.getSubjectList(param);
			}catch(EuException ex){ 
				ex.printStackTrace();
				exType = 1;	
				erMsg = ex.getMessage();
				data = null;
				LogUtil.d("XB", ""+ex.getMessage());
			}catch(Exception e) {
				 e.printStackTrace();
				 data = null;
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<SubjectVo> data) {
			subjectBaseAdpter = new TeacherSubjectAdapter( TeacherInfoActivity.this, R.layout.teacher_subject_item,data);
			prlistView.setAdapter(subjectBaseAdpter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			 prlistView.setShowFooter(false);
			subjectBaseAdpter.notifyDataSetChanged();
			
			super.onPostExecute(data);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.teacher_search_back:
			finish();
			break;
		case R.id.filter_info:
			mEInfo.setBackgroundResource(R.drawable.button_4);
			mEInfo.setTextColor(getResources().getColor(
					R.color.text_color_orange));
			mExqcx.setBackgroundResource(R.drawable.button_4_uncheck);
			mExqcx.setTextColor(getResources().getColor(R.color.black));
			mLinfo.setVisibility(View.VISIBLE);
			mLExqcx.setVisibility(View.INVISIBLE);
			break;
		case R.id.filter_xqcx:
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mEInfo.setBackgroundResource(R.drawable.button_4_uncheck);
			mEInfo.setTextColor(getResources().getColor(R.color.black));
			mExqcx.setBackgroundResource(R.drawable.button_4);
			mExqcx.setTextColor(getResources().getColor(
					R.color.text_color_orange));
			mLinfo.setVisibility(View.GONE);
			mLExqcx.setVisibility(View.VISIBLE);
			break;
		case R.id.teacher_favorite_btn:
			String className = this.getClass().getName();
			boolean result = isModel(Def.MODEl_TE_FAV, className, className,mObjBundle);
			if (result) {
				if (userInfo != null) {
					Map<String, Object> param = new HashMap<String, Object>();
					
					LogUtil.d(TAG, userInfo.getMemberid() + " : id");
					param.put("memberid", userInfo.getMemberid());
					if (teacherVo != null ){
						try {
						
						if (teacherVo.getIsarchived() == 0) {
							param.put("teacherid", teacherVo.getTeacherid());
							mFavoriteButton.setEnabled(false);
							String res = TeacherDao.addFavorites(param);
							faRes(res);
							mFavoriteButton.setEnabled(true);
						}
						else {
							param.put("id", teacherVo.getTeacherid());
							param.put("whichtype", 1);
							mFavoriteButton.setEnabled(false);
							String res = TeacherDao.DeleteFavorites(param);
							faRes(res);
							mFavoriteButton.setEnabled(true);
						}
						} catch(EuException ex){ 
							Toast.makeText(this, ex.getMessage() , Toast.LENGTH_SHORT).show();
						}catch (Exception e) {
							 e.printStackTrace();
							 Toast.makeText(this, e.getMessage() , Toast.LENGTH_SHORT).show();
						}
						
					}
					
				} else {
					Toast.makeText(this, "请登录 才能收藏", Toast.LENGTH_SHORT).show();
				}
			} 
			break;
		case R.id.teacherinfo_pf:
			  className = this.getClass().getName();
			  result = isModel(Def.MODEl_TE_PF, className, className,mObjBundle);
			  if(result){
				  createRatingStarDialog();
			  }
			break;
		default:
			dialogLinstener(v.getId());
			break;
		}
	}

	
	private void faRes(String res){
		if (teacherVo != null ){
			if ( teacherVo.getIsarchived() == 0) {
				if (Def.FAV_OBJ_RESULT.equals(res)) {
					Toast.makeText(this, "已经收藏", Toast.LENGTH_SHORT).show();
					mFavoriteButton.setBackgroundResource(R.drawable.fav_ed);
				} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
					Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
					teacherVo.setIsarchived(1);
					mFavoriteButton.setBackgroundResource(R.drawable.fav_ed);
				} else {
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
				}else {
					if (Def.FAV_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "已经取消收藏", Toast.LENGTH_SHORT).show();
						mFavoriteButton.setBackgroundResource(R.drawable.button_background_7);
					} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
						Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show();
						teacherVo.setIsarchived(0);
						mFavoriteButton.setBackgroundResource(R.drawable.button_background_7);
					} else {
						Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
					}
				}
		}
		
	}
	/**
	 * 评级对话框控件监听
	 * 
	 * @param id
	 */
	private void dialogLinstener(int id) {
		switch (id) {
		case R.id.dialog_close:
			if (mRatingDialog != null) {
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
				String className = this.getClass().getName();
				boolean b = isModel(Def.MODEl_TE_PF, className, className,mObjBundle);
				if (b) {
					try {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("star", startCode);
					param.put("teacherid", teacherVo.getTeacherid());
					param.put("memberid", userInfo.getMemberid());
					int result = TeacherDao.UserScore(param);
					LogUtil.d(TAG, "评分 结果  ：" + result);
					mRatingBar.setNumStars(result);
					Toast.makeText(this, "评分成功", Toast.LENGTH_SHORT).show();
					mRatingButton.setClickable(false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Toast.makeText(this, "权限不够", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
	}

	/**
	 * 显示评星对话框
	 */
	private void createRatingStarDialog() {
		mRatingDialog = new Dialog(this, R.style.RatingDialog);
		mRatingDialog.setContentView(R.layout.rating_star_dialog);
		mRatingDialog.show();
		Button dialogClose = (Button) mRatingDialog
				.findViewById(R.id.dialog_close);
		Button dialogOk = (Button) mRatingDialog.findViewById(R.id.dialog_ok);
		dialogClose.setOnClickListener(this);
		dialogOk.setOnClickListener(this);
		ratingGroup = (RadioGroup) mRatingDialog
				.findViewById(R.id.dialog_rating_group);
	}

	@Override
	public void onItemClick(AdapterView<?> data, View arg1, int position,long arg3) {
		SubjectVo tv = (SubjectVo) data.getAdapter().getItem(position);
		Intent mIntent = new Intent(this, SubjectInfoActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Def.OBJ, tv);
		mIntent.putExtra(Def.OBJ_Bundle,mBundle);
		startActivity(mIntent);
		LogUtil.d(TAG, "课程------------");
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
