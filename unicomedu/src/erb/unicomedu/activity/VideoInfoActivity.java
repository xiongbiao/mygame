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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.OtherVideoAdapter;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.dao.VideoDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.VideoVo;

/**
 * 视频详情
 * 
 */
public class VideoInfoActivity extends PublicActivity implements
		OnClickListener, OnItemClickListener {

	private VideoVo mVv;
	private String mediaurl_key = "CHOOSE_URL";
	private Button mPlay;
	private ImageButton mBack;
	private TextView mVideoName;
	private TextView mVideoAuthor;
	private TextView mVideoLevel;
	private TextView mVideoIntroduce;
	private ImageView mObjPicture;
	private ImageButton mRatingButton;
	private Dialog mRatingDialog;
	private Dialog mConfirmDialog;
	private RadioGroup ratingGroup;
	private int startCode = 0;
	 private LoadingView lv;
	private List<VideoVo> data;
	private PullToRefreshListView prlistView;
	private OtherVideoAdapter videoBaseAdpter;
	private Bundle mObjBundle;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onRestart();
		String className = this.getClass().getName();
		String fromClassName = new VideoActivity().getClass().getName();
		isModel(Def.MODEl_VI_INFO, className, fromClassName, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_info);
		mObjBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
		if (mObjBundle != null) {
			mVv = (VideoVo) mObjBundle.getSerializable(Def.OBJ);
		}
		init();
		initData();
	}

	private void initData() {
		if (mVv != null) {
			updateVideoName(mVv.getMediaName());
			updateVideoAuthor(mVv.getTeacher());
			updateVideoLevel(mVv.getClknumber() + "");
			mVideoIntroduce.setText(mVv.getInfo());
			Drawable cachedImage = new AsyncImageLoader().loadDrawable(
					mVv.getImgurl(), new ImageCallback() {
						@Override
						public void imageLoaded(Drawable imageDrawable,
								String imageUrl) {
							ImageView imageViewByTag = mObjPicture;
							if (imageViewByTag != null && imageDrawable != null) {
								Bitmap bitmap = ((BitmapDrawable) imageDrawable).getBitmap();
//								bitmap = ImageUtil.clippingImage(bitmap,imageViewByTag.getWidth(),imageViewByTag.getHeight(),ImageUtil.CLIP_W_CENTER, ImageUtil.CLIP_H_CENTER);
								imageViewByTag.setImageBitmap(bitmap);
							}
						}
					});
			if (cachedImage != null) {
				Bitmap bitmap = ((BitmapDrawable) cachedImage).getBitmap();
//				bitmap = ImageUtil.clippingImage(bitmap,mObjPicture.getWidth(),mObjPicture.getHeight(),ImageUtil.CLIP_W_CENTER, ImageUtil.CLIP_H_CENTER);
				mObjPicture.setImageBitmap(bitmap);
			} else {
				mObjPicture.setImageResource(R.drawable.avatar_icon);
			}
			if (mVv.getIsarchived() == 1) {
				mRatingButton.setBackgroundResource(R.drawable.fav_ed);
			}else{
				mRatingButton.setBackgroundResource(R.drawable.button_background_7);
			}
		}
		data = new ArrayList<VideoVo>();
		new GetDataTask().execute();
	}

	private void init() {
		lv = (LoadingView)findViewById(R.id.data_loading);
		mPlay = (Button) findViewById(R.id.video_info_play);
		mPlay.setOnClickListener(this);
		mBack = (ImageButton) findViewById(R.id.video_info_back);
		mBack.setOnClickListener(this);
		mRatingButton = (ImageButton) findViewById(R.id.video_rating_btn);
		mRatingButton.setOnClickListener(this);
		mVideoName = (TextView) findViewById(R.id.video_info_name);
		mVideoAuthor = (TextView) findViewById(R.id.video_info_author);
		mVideoLevel = (TextView) findViewById(R.id.video_info_level);
		mVideoIntroduce = (TextView) findViewById(R.id.vidio_info_introduce);
		mObjPicture = (ImageView) findViewById(R.id.item_avatar);

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

		
		prlistView.setOnItemClickListener(this);
	}

	/**
	 * 更新名称
	 */
	private void updateVideoName(String content) {
		mVideoName.setText(content);
	}

	/**
	 * 更新作者 parame 作者姓名
	 */
	private void updateVideoAuthor(String content) {
		mVideoAuthor.setText(String.format(
				getString(R.string.video_info_author), content));
	}

	/**
	 * 更新播放数 parame 播放次数
	 */
	private void updateVideoLevel(String content) {
		mVideoLevel.setText(String.format(getString(R.string.video_info_level), content));
	}

	@Override
	public void onClick(View v) {
		String className = "";
		boolean result = false;
		switch (v.getId()) {
		case R.id.video_info_play:
			className = this.getClass().getName();
			result = isModel(Def.MODEl_VI_P, className, className, mObjBundle);
			if (result && mVv != null) {
				LogUtil.d("--------------------", mVv.getIsbuy() + "--------"
						+ mVv.getIntegral());
				if (mVv.getIsbuy() == 0 && !"0".equals(mVv.getIntegral())) {
					createScoreConfirmDialog(Integer.valueOf(mVv.getIntegral()));
				} else {
					startPlayVideo();
				}
			}
			break;
		case R.id.video_info_back:
			finish();
			break;
		case R.id.video_rating_btn:
			className = this.getClass().getName();
			result = isModel(Def.MODEl_VI_FAV, className, className, mObjBundle);
			if (result) {
				if (userInfo != null) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("memberid", userInfo.getMemberid());
					if (mVv != null) {
						try {

							if (mVv.getIsarchived() == 0) {
								param.put("mediaid", mVv.getMediaid());
								mRatingButton.setEnabled(false);
								String res = VideoDao.addFavorites(param);
								faRes(res);
								mRatingButton.setEnabled(true);
							} else {
								param.put("id", mVv.getMediaid());
								param.put("whichtype", 4);
								mRatingButton.setEnabled(false);
								try {
									String res = TeacherDao
											.DeleteFavorites(param);
									faRes(res);
								} catch (EuException ex) {
									Toast.makeText(this, ex.getMessage(),
											Toast.LENGTH_SHORT).show();
								} catch (Exception e) {
									e.printStackTrace();
								}
								mRatingButton.setEnabled(true);
							}
						} catch (EuException ex) {
							Toast.makeText(this, ex.getMessage(),
									Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					Toast.makeText(this, "请登录 才能收藏", Toast.LENGTH_SHORT).show();
				}

			}
			break;
		default:
			dialogLinstener(v.getId());
			break;

		}
	}

	private void faRes(String res) {
		if (mVv != null) {
			if (mVv.getIsarchived() == 0) {
				if (Def.FAV_OBJ_RESULT.equals(res)) {
					Toast.makeText(this, "已经收藏", Toast.LENGTH_SHORT).show();
					mRatingButton.setBackgroundResource(R.drawable.fav_ed);
				} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
					Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
					mVv.setIsarchived(1);
					mRatingButton.setBackgroundResource(R.drawable.fav_ed);
				} else {
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
			} else {
				if (Def.FAV_OBJ_RESULT.equals(res)) {
					Toast.makeText(this, "已经取消收藏", Toast.LENGTH_SHORT).show();
					mRatingButton
							.setBackgroundResource(R.drawable.button_background_7);
				} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
					Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show();
					mVv.setIsarchived(0);
					mRatingButton
							.setBackgroundResource(R.drawable.button_background_7);
				} else {
					Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	private void startPlayVideo() {
		Intent mIntent = new Intent(this, VideoPlayerActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(mediaurl_key, mVv.getMediaUrl());
		mBundle.putString("MediaName", mVv.getMediaName());
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
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
			}
			break;
		case R.id.score_confirm_no:
			if (mConfirmDialog != null) {
				mConfirmDialog.dismiss();
			}
			break;
		case R.id.score_confirm_yes:
			if (mConfirmDialog != null && userInfo != null) {
				mConfirmDialog.dismiss();
				try {

					Map<String, Object> param = new HashMap<String, Object>();
					param.put("memberid", userInfo.getMemberid());
					param.put("mediaid", mVv.getMediaid());
					String res = VideoDao.videoBuy(param);
					if (Def.FAV_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "已经购买", Toast.LENGTH_SHORT).show();
						mVv.setIsbuy(1);
						startPlayVideo();
					} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
						Toast.makeText(this, "购买成功", Toast.LENGTH_SHORT).show();
						mVv.setIsbuy(1);
						startPlayVideo();
					} else if (Def.NO_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "积分不够", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, "购买失败", Toast.LENGTH_SHORT).show();
					}
				} catch (EuException ex) {
					Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(this, "不能购买", Toast.LENGTH_SHORT).show();
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

	private void createScoreConfirmDialog(int score) {
		mConfirmDialog = new Dialog(this, R.style.RatingDialog);
		mConfirmDialog.setContentView(R.layout.score_confirm_dialog);
		mConfirmDialog.show();
		Button dialogClose = (Button) mConfirmDialog
				.findViewById(R.id.score_confirm_no);
		Button dialogOk = (Button) mConfirmDialog
				.findViewById(R.id.score_confirm_yes);
		TextView confirmInfo = (TextView) mConfirmDialog
				.findViewById(R.id.score_confirm_info);
		confirmInfo.setText(String.format(
				getString(R.string.score_confirm_info), String.valueOf(score)));
		dialogClose.setOnClickListener(this);
		dialogOk.setOnClickListener(this);
	}

	/**
	 * 刷新数据
	 * 
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<VideoVo>> {
		int exType = 0;
		String erMsg = "";
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}
		@Override
		protected List<VideoVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("mediaid", mVv.getMediaid());
				if (userInfo != null) {
					param.put("memberid", userInfo.getMemberid());
				} else {
					param.put("memberid", 0);
				}
				data = VideoDao.getVideoOtherList(param);
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
		protected void onPostExecute(List<VideoVo> data) {
			videoBaseAdpter = new OtherVideoAdapter(VideoInfoActivity.this, R.layout.other_obj_item, data);
			prlistView.setAdapter(videoBaseAdpter);
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			 prlistView.setShowFooter(false);
			videoBaseAdpter.notifyDataSetChanged();
			super.onPostExecute(data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> data, View view, int position,
			long arg3) {
		VideoVo tv = (VideoVo) data.getAdapter().getItem(position);
//		Intent mIntent = new Intent(this, VideoInfoActivity.class);
//		Bundle mBundle = new Bundle();
//		mBundle.putSerializable(Def.OBJ, tv);
//		mIntent.putExtra(Def.OBJ_Bundle, mBundle);
//		startActivity(mIntent);
//		finish();
		mVv = tv;
		initData();
	}
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    };
}
