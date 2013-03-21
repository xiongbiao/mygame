package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import erb.unicomedu.adapter.BbsCheckAdapter;
import erb.unicomedu.dao.BbsCheckDao;
import erb.unicomedu.dao.BbsDao;
import erb.unicomedu.dao.BbsInfoAskDao;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;
import erb.unicomedu.vo.BbsCheckVo;

/**
 * 具体详情
 * @author xiongbiao
 *
 */
public class BbsCheckAllActivity extends PublicActivity implements OnClickListener,OnItemClickListener{
	private String TAG = "BbsCheckAllActivity"; 
	
	private Button button_TopReply;
	private Button button_TopBack;
	private TextView textView_ReadTime;
	private TextView textView_PostTitle;
	private TextView textView_PostNum;
	private RelativeLayout linear_Line;
	private List<BbsCheckVo> data = new ArrayList<BbsCheckVo>();
	private BbsAskVo bbsAskVo;
	private boolean isMybbs = false;
	private BbsCheckVo bbsCheckVo = new BbsCheckVo();
	private PullToRefreshListView prlistView;
	private BbsCheckAdapter bbsCheckAdapter;
	private LoadingView lv;
	private int mCanvasW; 

	private Bundle mRBundle;
	LayoutInflater inflater;
	ImageView ivSc;
	@Override
	protected void onResume() {
		super.onResume();
		new GetDataTask().execute();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DisplayMetrics DM = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(DM);
		mCanvasW = DM.widthPixels;
		//mCanvasH = DM.heightPixels;
		
		linear_Line = (RelativeLayout)this.getLayoutInflater().inflate(R.layout.bbs_check_list, null, false);
	    LinearLayout l = (LinearLayout)linear_Line.findViewById(R.bbs.linear_line);
	    myLine line = new myLine(BbsCheckAllActivity.this); 
	    LinearLayout.LayoutParams lp_line = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    lp_line.height = 2;
	    line.setLayoutParams(lp_line);
	    l.addView(line);
	    setContentView(linear_Line);
	    
		inflater = this.getLayoutInflater();
		lv = (LoadingView)findViewById(R.id.data_loading);
		prlistView = (PullToRefreshListView)linear_Line.findViewById(R.id.obj_list);
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
		
//		bbsAskVo = (BbsAskVo)getIntent().getSerializableExtra(Def.OBJ);
		
		isMybbs =  getIntent().getBooleanExtra(Def.OBJ_bbs, false);
		
		mRBundle = getIntent().getBundleExtra(Def.OBJ_Bundle);
        if(mRBundle!=null){
        	bbsAskVo = (BbsAskVo)mRBundle.getSerializable(Def.OBJ);
        } 
		
		prlistView.setOnItemClickListener(this);
		
		button_TopBack = (Button)linear_Line.findViewById(R.id.bbs_check_back);
		button_TopBack.setFocusable(true);
		button_TopBack.setClickable(true);
		button_TopBack.setOnClickListener(this);
		
		button_TopReply = (Button)linear_Line.findViewById(R.id.bbs_check_reply);
		button_TopReply.setFocusable(true);
		button_TopReply.setClickable(true);
		button_TopReply.setOnClickListener(this); 
		textView_ReadTime = (TextView)linear_Line.findViewById(R.id.bbs_check_readtime);
		textView_PostNum = (TextView)linear_Line.findViewById(R.id.bbs_check_postnum);
		textView_PostTitle = (TextView)linear_Line.findViewById(R.id.bbs_check_posttitle);
		textView_PostTitle.setTextColor(Color.rgb(255, 149, 1));
		if(bbsAskVo != null) {
			textView_ReadTime.setText("浏览:" + bbsAskVo.getReadnumber() );
			textView_PostNum.setText("回复:" + bbsAskVo.getReplynumber() );
			if(isMybbs){
				textView_PostTitle.setText(bbsAskVo.getTitle());
			}
			else{
				textView_PostTitle.setText(bbsAskVo.getTopic());
			}
		} 
		ivSc  = (ImageView)linear_Line.findViewById(R.id.bbs_check_collection);
		ivSc.setOnClickListener(this);
	}
	
	
	  
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<BbsCheckVo>> {
		int exType = 0;
		String erMsg = "";
		boolean isFoot = false;
		@Override
		protected void onPreExecute() {
			prlistView.setLoading();
			lv.setVisibility(View.VISIBLE);
			lv.show(0);
		}
		@Override
		protected List<BbsCheckVo> doInBackground(Void... params) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				BbsCheckVo vo = new BbsCheckVo();
				if(bbsAskVo != null) {
					param.put("askid", bbsAskVo.getAskid());
					vo.setAnswer(bbsAskVo.getMember());
					vo.setPubtime(bbsAskVo.getPubtime());
					vo.setAskid(bbsAskVo.getAskid());
					vo.setAnswerid(0);
					vo.setMemberid(bbsAskVo.getMemberid());
					vo.setMember(bbsAskVo.getMember());
					vo.setContext(bbsAskVo.getContext());
					vo.setBbsCheckVoList(new ArrayList<BbsCheckVo>());
					if(userInfo==null){
						param.put("memberid", 0);
					}else{
						param.put("memberid", userInfo.getMemberid());
					}
					bbsAskVo = BbsInfoAskDao.getAskShow(param);
				}
//				if(bbsMyAskVo != null) {
//					param.put("askid", bbsMyAskVo.getAskid());
//					vo.setAnswer(bbsMyAskVo.getMember());
//					vo.setPubtime(bbsMyAskVo.getPubtime());
//					vo.setAskid(bbsMyAskVo.getAskid());
//					vo.setAnswerid(0);
//					vo.setMemberid(bbsMyAskVo.getMemberid());
//					vo.setMember(bbsMyAskVo.getMember());
//					vo.setContext(bbsMyAskVo.getContext());
//					vo.setBbsCheckVoList(new ArrayList<BbsCheckVo>());
//				}
				bbsCheckVo.setAskid(vo.getAskid());
				bbsCheckVo.setAnswerid(vo.getAnswerid());
				bbsCheckVo.setMemberid(vo.getMemberid());
				bbsCheckVo.setMember(vo.getMember());
				data = BbsCheckDao.getBbsCheckList(param, vo);
			} catch(EuException ex){ 
				ex.printStackTrace();
				exType = 1;	
				erMsg = ex.getMessage();
				data = null;
				isFoot = false;
				LogUtil.d(TAG, ""+ex.getMessage());
			}catch(Exception e) {
				 e.printStackTrace();
				 data = null;
				 isFoot = false;
			}
			return data;
		}

		@Override
		protected void onPostExecute(List<BbsCheckVo> data) {
//			InitializeData(data);
			bbsCheckAdapter = new BbsCheckAdapter(BbsCheckAllActivity.this, inflater, data);
			bbsCheckAdapter.setAskVo(bbsAskVo);
			prlistView.setAdapter(bbsCheckAdapter);
			prlistView.setCacheColorHint(0);
			prlistView.postInvalidate();
			prlistView.onRefreshComplete();
			lv.onPost(data, lv, prlistView, exType, erMsg);
			prlistView.setShowFooter(false);
			bbsCheckAdapter.notifyDataSetChanged();
			if (bbsAskVo != null) {
				if (bbsAskVo.getIsarchived() == 1) {
					ivSc.setBackgroundResource(R.drawable.fav_ed);
				} else {
					ivSc.setBackgroundResource(R.drawable.button_background_7);
				}
			}
			super.onPostExecute(data);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bbs_check_back:
				finish(); 
				break;
			case R.id.bbs_check_reply:
				String fromClassName =  this.getClass().getName();
				String className =  new BbsReplyActivity().getClass().getName();
//				Bundle bundle = new Bundle();
				mRBundle.putSerializable(Def.OBJ_bbs_ask,bbsCheckVo);  
//				mRBundle.putSerializable(Def.OBJ_bbs_ask,bbsAskVo);  
		        
				boolean result = isModel(Def.MODEl_BBS_H_ASK, className,fromClassName,mRBundle);
				if(result){
					LogUtil.d(TAG,"onClick---回复楼主（跟帖）----");
					//回复楼主（跟帖）
					Intent intent = new Intent(this, BbsReplyActivity.class);
					intent.putExtra(Def.OBJ_Bundle,mRBundle); 
					startActivity(intent);
				}
				break;
			case R.id.bbs_check_collection:
				String className1 =  this.getClass().getName();
				boolean result1 = isModel(Def.MODEl_BBS_list, className1,className1,null);
				if(result1){
					if(userInfo!=null){
						Map<String, Object> param = new HashMap<String, Object>();
						param.put("memberid", userInfo.getMemberid());
						if (bbsAskVo!= null ){
							if (bbsAskVo.getIsarchived() == 0) {
								param.put("askid", bbsAskVo.getAskid());
								ivSc.setEnabled(false);
								try {
									String res = BbsDao.addFavorites(param);
									faRes(res);	
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
								ivSc.setEnabled(true);
							}
							else {
								param.put("id", bbsAskVo.getAskid());
								param.put("whichtype", 5);
								ivSc.setEnabled(false);
								try {
									String res = TeacherDao.DeleteFavorites(param);
									faRes(res);
									}catch(EuException ex){ 
										Toast.makeText(this, ex.getMessage() , Toast.LENGTH_SHORT).show();
									}catch (Exception e) {
										 e.printStackTrace();
									}
								ivSc.setEnabled(true);
							}
						}
					}else{
						  Toast.makeText(this, "请登录 才能收藏", Toast.LENGTH_SHORT).show();
					}
				}
				
				break;
			case R.id.bbs_check_down:
				
				break;
		}
	}
	
	
	
	private void faRes(String res){
		if (bbsAskVo != null ){
			if ( bbsAskVo.getIsarchived() == 0) {
				if (Def.FAV_OBJ_RESULT.equals(res)) {
					Toast.makeText(this, "已经收藏", Toast.LENGTH_SHORT).show();
					ivSc.setBackgroundResource(R.drawable.fav_ed);
				} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
					Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
					bbsAskVo.setIsarchived(1);
					ivSc.setBackgroundResource(R.drawable.fav_ed);
				} else {
					Toast.makeText(this, "收藏失败", Toast.LENGTH_SHORT).show();
				}
				}else {
					if (Def.FAV_OBJ_RESULT.equals(res)) {
						Toast.makeText(this, "已经取消收藏", Toast.LENGTH_SHORT).show();
						ivSc.setBackgroundResource(R.drawable.button_background_7);
					} else if (Def.FAV_OBJ_RESULT_OK.equals(res)) {
						Toast.makeText(this, "取消收藏成功", Toast.LENGTH_SHORT).show();
						bbsAskVo.setIsarchived(0);
						ivSc.setBackgroundResource(R.drawable.button_background_7);
					} else {
						Toast.makeText(this, "取消收藏失败", Toast.LENGTH_SHORT).show();
					}
				}
		}
		
	}
	
	class myLine extends View {
		private Paint paint = new Paint();
		
		public myLine(Context context) {
			super(context);
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.argb(255, 188, 184, 181));
			DashPathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);  
			paint.setPathEffect(effects);
		}

		@Override
		public void onDraw(Canvas c) {
			super.onDraw(c);
			c.drawLine(20, 0, mCanvasW-20, 0, paint);
		}

	} 
	
	 

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	};
	
	public static int dipToPixels(Context context, float dip)
	{
		return (int)(context.getResources().getDisplayMetrics().density * dip);
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
