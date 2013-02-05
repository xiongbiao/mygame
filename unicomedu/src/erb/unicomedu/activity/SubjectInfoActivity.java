package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.PlaceAdpter;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.PlaceVo;
import erb.unicomedu.vo.SubjectVo;

/***
 * 课程详情
 *
 */
public class SubjectInfoActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	private Button mFilterLatest;
	private Button mFilterPopularity;
	private Button mFilterStar;
	private SubjectVo mSubjectVo;
	private TextView mSubjectName;
	private TextView mSubjectfit;
	private TextView mSubjectIntroduction;
	
	private ImageButton mRatingButton;
//	private Dialog mDialog;
	private Bundle mObjBundle;
	private Button mEInfo;

	List<PlaceVo> data;
	private PullToRefreshListView prlistView;
	private PlaceAdpter mPlaceBaseAdpter;
	private ImageButton mBack;
	private Dialog mRatingDialog;
    private RadioGroup ratingGroup;
    private int startCode = 0;
    private LoadingView lv;
	private String TAG = "SubjectInfoActivity";
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		String className =  this.getClass().getName();
		String fromClassName =  new SubjectActivity().getClass().getName();
		isModel(Def.MODEl_SU_INFO, className,fromClassName,null);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_info);
//		mSubjectVo = (SubjectVo) getIntent().getSerializableExtra(Def.OBJ);
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mObjBundle!=null){
        	mSubjectVo = (SubjectVo)mObjBundle.getSerializable(Def.OBJ);
        } 
		init();
		initData();
	}

	private void init() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		mFilterLatest = (Button) findViewById(R.id.filter_instructional_videos);
		mFilterPopularity = (Button) findViewById(R.id.filter_read_online);
		mFilterStar = (Button) findViewById(R.id.filter_online_test);
		mFilterLatest.setOnClickListener(this);
		mFilterPopularity.setOnClickListener(this);
		mFilterStar.setOnClickListener(this);

		mSubjectName = (TextView) findViewById(R.id.subject_name);
		mSubjectfit = (TextView) findViewById(R.id.subject_fit_info);
		mSubjectIntroduction = (TextView) findViewById(R.id.subject_introduction_info);

		mRatingButton = (ImageButton) findViewById(R.id.subject_rating_btn);
		mRatingButton.setOnClickListener(this);
		
		mEInfo = (Button) findViewById(R.id.filter_address);
		mBack = (ImageButton) findViewById(R.id.subject_search_back);
		mBack.setOnClickListener(this);
		
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

		data = new ArrayList<PlaceVo>();
		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
		
	}

	private void initData() {
		if (mSubjectVo != null) {
			mSubjectName.setText(mSubjectVo.getSubjectName());
			mSubjectfit.setText(mSubjectVo.getFitstudent());
			mSubjectIntroduction.setText(mSubjectVo.getInfo());
			if(mSubjectVo.getIsarchived()==1){
				mRatingButton.setBackgroundResource(R.drawable.fav_ed);
			}else{
				mRatingButton.setBackgroundResource(R.drawable.button_background_7);
			}
		}
	}
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<PlaceVo>> {
		int exType = 0;
		String erMsg = "";
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}
		@Override
		protected List<PlaceVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				LogUtil.d("*********", "getSubjectId : "+mSubjectVo.getSubjectId());
				param.put("subjectid", mSubjectVo.getSubjectId());
				data = SubjectDao.getSubjectClassList(param);
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
		protected void onPostExecute(List<PlaceVo> data) {
			mPlaceBaseAdpter = new PlaceAdpter(SubjectInfoActivity.this, R.layout.place_item, data);
			mPlaceBaseAdpter.setmSubjectVo(mSubjectVo);
			prlistView.setAdapter(mPlaceBaseAdpter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(false);
			mPlaceBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}
	
	@Override
	public void onClick(View v) {
		String fromClassName =  this.getClass().getName();
		String className = "";
		boolean isOK = false;
		switch (v.getId()) {
		case R.id.filter_instructional_videos:
			mFilterLatest.setBackgroundResource(R.drawable.filter_but_l_s);
			mFilterLatest.setTextColor(getResources().getColor(R.color.white));
			mFilterPopularity.setTextColor(getResources().getColor(R.color.black));
			mFilterStar.setTextColor(getResources().getColor(R.color.black));
			mFilterPopularity.setBackgroundResource(R.drawable.filter_but_c);
			mFilterStar.setBackgroundResource(R.drawable.filter_but_r);
			className =  new VideoResultActivity().getClass().getName();
//			Bundle mVideoBundle = new Bundle(); 
//			mVideoBundle.putSerializable (Def.OBJ, mSubjectVo );
		    isOK = isModel(Def.MODEl_VI_LIST, className,fromClassName,mObjBundle);
		    if(isOK){
			Intent mVideoIntent = new Intent(this, VideoResultActivity.class);
			mVideoIntent.putExtras(mObjBundle);
			startActivity(mVideoIntent);
		    }
			break;
		case R.id.filter_read_online:
			mFilterLatest.setBackgroundResource(R.drawable.filter_but_l);
			mFilterPopularity.setBackgroundResource(R.drawable.filter_but_c_s);
			mFilterStar.setBackgroundResource(R.drawable.filter_but_r);
			mFilterLatest.setTextColor(getResources().getColor(R.color.black));
			mFilterPopularity.setTextColor(getResources().getColor(R.color.white));
			mFilterStar.setTextColor(getResources().getColor(R.color.black));
			className =  new ReadResultActivity().getClass().getName();
//			Bundle mReadBundle = new Bundle();
//			mReadBundle.putSerializable(Def.OBJ, mSubjectVo );
			isOK = isModel(Def.MODEl_RE_LIST, className,fromClassName,mObjBundle);
			if(isOK){
					Intent mReadIntent = new Intent(this, ReadResultActivity.class);
					
					mReadIntent.putExtras(mObjBundle);
					startActivity(mReadIntent);
			}
			break;
		case R.id.filter_online_test:
			mFilterLatest.setBackgroundResource(R.drawable.filter_but_l);
			mFilterPopularity.setBackgroundResource(R.drawable.filter_but_c);
			mFilterStar.setBackgroundResource(R.drawable.filter_but_r_s);
			mFilterLatest.setTextColor(getResources().getColor(R.color.black));
			mFilterPopularity.setTextColor(getResources().getColor(R.color.black));
			mFilterStar.setTextColor(getResources().getColor(R.color.white));
			className =  new ExamResultActivity().getClass().getName();
//			Bundle mExamBundle = new Bundle();
//			mExamBundle.putSerializable(Def.OBJ, mSubjectVo);
			isOK = isModel(Def.MODEl_EX_LIST, className,fromClassName,mObjBundle);
			if(isOK){
				Intent mExamIntent = new Intent(this, ExamResultActivity.class);
				mExamIntent.putExtras(mObjBundle);
				startActivity(mExamIntent);
			}
			break;
		case R.id.subject_rating_btn:
			className = this.getClass().getName();
			boolean result = isModel(Def.MODEl_SU_FAV, className,className,mObjBundle);
			if(result){
				if(userInfo!=null){
					Map<String, Object> param = new HashMap<String, Object>();
					LogUtil.d(TAG, userInfo.getMemberid()+" : id");
					param.put("memberid", userInfo.getMemberid());
					
					if (mSubjectVo!= null ){
						if (mSubjectVo.getIsarchived() == 0) {
							param.put("subjectid", mSubjectVo.getSubjectId());
							mRatingButton.setEnabled(false);
							try {
								String res = SubjectDao.addFavorites(param);
								faRes(res);
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							mRatingButton.setEnabled(true);
						}
						else {
							param.put("id", mSubjectVo.getSubjectId());
							param.put("whichtype", 2);
							mRatingButton.setEnabled(false);
							try {
							String res = TeacherDao.DeleteFavorites(param);
							faRes(res);
							}catch(EuException ex){ 
								Toast.makeText(this, ex.getMessage() , Toast.LENGTH_SHORT).show();
							}catch (Exception e) {
								 e.printStackTrace();
							}
							mRatingButton.setEnabled(true);
						}
					}
				}else{
					  Toast.makeText(this, "请登录才能收藏", Toast.LENGTH_SHORT).show();
				}
			}
			
			break;
	    case R.id.subject_search_back:
			finish();
			break;
	 
		default:
		    dialogLinstener(v.getId());
			break;
		}
	}
	
	private void faRes(String res){
		if (mSubjectVo != null ){
			if ( mSubjectVo.getIsarchived() == 0) {
				if (Def.FAV_OBJ_RESULT.equals(res)) {
					Toast.makeText(this, "已经收藏", Toast.LENGTH_SHORT).show();
					mRatingButton.setBackgroundResource(R.drawable.fav_ed);
				} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
					Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
					mSubjectVo.setIsarchived(1);
					mRatingButton.setBackgroundResource(R.drawable.fav_ed);
				} else {
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
				}else {
					if (Def.FAV_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "已经取消收藏", Toast.LENGTH_SHORT).show();
						mRatingButton.setBackgroundResource(R.drawable.button_background_7);
					} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
						Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show();
						mSubjectVo.setIsarchived(0);
						mRatingButton.setBackgroundResource(R.drawable.button_background_7);
					} else {
						Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
					}
				}
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
//		ImageView iv = (ImageView)view.findViewById(R.id.item_gps);
//		iv.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
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
		}
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
	
	/**
	 * 在线报名
	 */
	private void startOnlineRegistration(){
		startActivity(new Intent(this, OnlineRegistrationActivity.class));
		ArrayList<String> id = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		if(data!=null&&data.size()>0){
			for(int i =0 ;i<data.size();i++){
				name.add( data.get(i).getSchoollocation());
				id.add( data.get(i).getItemid());
			}
		}
		Intent mExamIntent = new Intent(this, OnlineRegistrationActivity.class);
		Bundle mExamBundle = new Bundle();
		mExamBundle.putStringArrayList(Def.OBJ, name);
		mExamIntent.putExtras(mExamBundle);
		startActivity(mExamIntent);
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
