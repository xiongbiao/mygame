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
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.OtherReadAdapter;
import erb.unicomedu.dao.ReadDao;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.ReadVo;

/***
 * 阅读介绍
 *
 */
public class ReadInfoActivity  extends PublicActivity implements OnClickListener ,OnItemClickListener{

	private ReadVo mRv;
	private Button mReading;
	private ImageButton mBack;
	private TextView mObjName;
	private TextView mObjAuthor;
	private TextView mObjLevel;
	private TextView mObjIntroduce;
	private ImageView mObjPicture;
	private ImageButton mRatingButton;
	private Dialog mRatingDialog;
	private Dialog mConfirmDialog;
	private LoadingView lv;
	private PullToRefreshListView prlistView;
	private List<ReadVo> data;
	private OtherReadAdapter readBaseAdpter;
	private Bundle mObjBundle;
	
	@Override
	protected void onResume() {
		super.onRestart();
		String className =  this.getClass().getName();
		String fromClassName =  new ReadActivity().getClass().getName();
		isModel(Def.MODEl_RE_INFO, className,fromClassName,null);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_info);
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mObjBundle!=null){
        	mRv = (ReadVo) mObjBundle.getSerializable(Def.OBJ);
        } 
		init();
		initData();
	}
	
	private void initData(){
	
		if(mRv!=null){
			updateVideoName(mRv.getBookname());	
			updateVideoAuthor(mRv.getTeacher());	
			updateVideoLevel(mRv.getClknumber()+"");	
			mObjIntroduce.setText(mRv.getInfo());	
			 Drawable cachedImage = new AsyncImageLoader().loadDrawable(mRv.getImgurl(), new ImageCallback() {
		            @Override
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		                ImageView imageViewByTag = mObjPicture;
		                if (imageViewByTag != null&&imageDrawable!=null) {
		                     Bitmap bitmap = ((BitmapDrawable)imageDrawable).getBitmap();
		                    imageViewByTag.setImageBitmap( bitmap );
		                }
		            }
		        });
		    if(cachedImage!=null){
		    	Bitmap bitmap = ((BitmapDrawable)cachedImage).getBitmap();
		    	mObjPicture.setImageBitmap(  bitmap );
		    }else{
		    	mObjPicture.setImageResource(R.drawable.avatar_icon);
		    }
		    
		    if(mRv.getIsarchived()==1){
				mRatingButton.setBackgroundResource(R.drawable.fav_ed);
			}else{
				mRatingButton.setBackgroundResource(R.drawable.button_background_7);
			}
		}
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
		
		data = new ArrayList<ReadVo>();
		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
	}

	private void init() {

		mReading = (Button) findViewById(R.id.obj_info_reading);
		mReading.setOnClickListener(this);
		mBack = (ImageButton) findViewById(R.id.obj_info_back);
		mBack.setOnClickListener(this);
		mRatingButton = (ImageButton) findViewById(R.id.obj_rating_btn);
		mRatingButton.setOnClickListener(this);
		mObjName = (TextView) findViewById(R.id.obj_info_name);
		mObjAuthor = (TextView) findViewById(R.id.obj_info_author);
		mObjLevel = (TextView) findViewById(R.id.obj_info_level);
		mObjIntroduce = (TextView) findViewById(R.id.obj_info_introduce);
		mObjPicture = (ImageView) findViewById(R.id.item_avatar);
		lv = (LoadingView)findViewById(R.id.data_loading);
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
		
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
		mObjLevel.setText(String.format(getString(R.string.obj_info_level), content));
	}

	@Override
	public void onClick(View v) {
		String className = "";
		boolean result  = false;
		switch (v.getId()) {
			case R.id.obj_info_reading:
				className =  this.getClass().getName();
				result = isModel(Def.MODEl_RE_P, className,className,mObjBundle);
				if (mRv!= null&&result){
					LogUtil.d("--------------------", mRv.getIsbuy()+"--------"+mRv.getIntegral());
					if(mRv.getIsbuy()==0&&!"0".equals(mRv.getIntegral())){
						createScoreConfirmDialog(Integer.valueOf(mRv.getIntegral()));
					}else{
						startReadVideo();
					}
				}
				break;
			case R.id.obj_info_back:
				//按返回键返回列表（临时解决二次循环）
				finish();
				break;
			case R.id.obj_rating_btn:
				  className =  this.getClass().getName();
				  result = isModel(Def.MODEl_RE_FAV, className,className,mObjBundle);
				if(result){
					if(userInfo!=null){
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("memberid", userInfo.getMemberid());
						if (mRv!= null ){
							if (mRv.getIsarchived() == 0) {
								param.put("bookid", mRv.getBookid());
								mRatingButton.setEnabled(false);
								try {
								String res = ReadDao.addFavorites(param);
								faRes(res);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								mRatingButton.setEnabled(true);
							}
							else {
								param.put("id", mRv.getBookid());
								param.put("whichtype", 3);
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
						  Toast.makeText(this, "请登录 才能收藏", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			default:
				dialogLinstener(v.getId());
				break;

		}
	}
	private void faRes(String res){
		if (mRv != null ){
			if ( mRv.getIsarchived() == 0) {
				if (Def.FAV_OBJ_RESULT.equals(res)) {
					Toast.makeText(this, "已经收藏", Toast.LENGTH_SHORT).show();
					mRatingButton.setBackgroundResource(R.drawable.fav_ed);
				} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
					Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
					mRv.setIsarchived(1);
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
						mRv.setIsarchived(0);
						mRatingButton.setBackgroundResource(R.drawable.button_background_7);
					} else {
						Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
					}
				}
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
					param.put("bookid", mRv.getBookid());
					String res = ReadDao.ReadBuy(param);
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
						// TODO: handle exception
						Toast.makeText(this, "购买失败", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this, "不能购买", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
	
	private void startReadVideo(){
		Intent mIntent = new Intent(this, ReadingActivity.class);
		Bundle mBundle = new Bundle();
		if(mRv != null){
			mBundle.putSerializable(Def.OBJ ,mRv);  
			mIntent.putExtras(mBundle);
			startActivity(mIntent);
		}else{
			Toast.makeText(this, "资料不存在", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void createScoreConfirmDialog(int score){
		mConfirmDialog = new Dialog(this, R.style.RatingDialog);
		mConfirmDialog.setContentView(R.layout.score_confirm_dialog);
		mConfirmDialog.show();
		Button dialogClose = (Button)mConfirmDialog.findViewById(R.id.score_confirm_no);
		Button dialogOk = (Button) mConfirmDialog.findViewById(R.id.score_confirm_yes);
		TextView confirmInfo = (TextView)mConfirmDialog.findViewById(R.id.score_confirm_info);
		confirmInfo.setText(String.format(getString(R.string.read_score_confirm_info), String.valueOf(score)));
		dialogClose.setOnClickListener(this);
		dialogOk.setOnClickListener(this);
	}
	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<ReadVo>> {
		int exType = 0;
		String erMsg = "";
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}
		@Override
		protected List<ReadVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				 param.put("bookid", mRv.getBookid());
				 if(userInfo!=null){
					 param.put("memberid", userInfo.getMemberid());
				 }else{
					 param.put("memberid", 0);
				 }
				data = ReadDao.getReadOtherList(param); 
			} catch(EuException ex){ 
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
		protected void onPostExecute(List<ReadVo> data) {
			readBaseAdpter = new OtherReadAdapter(ReadInfoActivity.this, R.layout.other_obj_item, data);
			prlistView.setAdapter(readBaseAdpter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			 prlistView.setShowFooter(false);
			readBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position, long arg3) {
		ReadVo tv = (ReadVo)data.getAdapter().getItem(position);
//		Intent intent = new Intent(this,ReadInfoActivity.class);  
//        Bundle mBundle = new Bundle();  
//        mBundle.putSerializable(Def.OBJ,tv);  
//        intent.putExtra(Def.OBJ_Bundle,mBundle);
//        startActivity(intent);  
        mRv = tv;
        initData();
//        finish();
	}
	
    @Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		mBack.performClick();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
