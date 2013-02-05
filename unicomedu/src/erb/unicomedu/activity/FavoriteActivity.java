package erb.unicomedu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import erb.unicomedu.adapter.BbsInfoAdapter;
import erb.unicomedu.adapter.ReadAdapter;
import erb.unicomedu.adapter.SubjectAdapter;
import erb.unicomedu.adapter.TeacherAdapter;
import erb.unicomedu.adapter.VideoAdapter;
import erb.unicomedu.dao.BbsDao;
import erb.unicomedu.dao.ReadDao;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.dao.TeacherDao;
import erb.unicomedu.dao.VideoDao;
import erb.unicomedu.ui.LoadingView;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.ui.PullToRefreshListView.OnRefreshListener;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;
import erb.unicomedu.vo.BbsVo;
import erb.unicomedu.vo.ReadVo;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TeacherVo;
import erb.unicomedu.vo.VideoVo;


/**
 * 收藏
 *
 */
public class FavoriteActivity  extends PublicActivity implements OnClickListener ,OnItemClickListener {
	private ImageButton mBack;
	private String memberid;
	
	private GridView tbGrid;
	private final int TOOLBAR_ITEM_TEACHER = 0;
	private final int TOOLBAR_ITEM_SUBJECT = 1;
	private final int TOOLBAR_ITEM_READ = 2;
	private final int TOOLBAR_ITEM_VIDEO = 3;
	private final int TOOLBAR_ITEM_BBS = 4;
	
	
	private TeacherAdapter teacherBaseAdpter;
	List<TeacherVo> mTeacherData;
	private PullToRefreshListView prlistView;
	
	List<SubjectVo> mSubjectData;
	private SubjectAdapter subjectBaseAdpter;
	
	private List<ReadVo> mReadData;
	private ReadAdapter readBaseAdpter;
	
	
	private List<VideoVo> mVideoData;
	private VideoAdapter videoBaseAdpter;
	
	private List<BbsAskVo>   mBbsData;
	private BbsInfoAdapter askAdapter;
	
	List<Object> mObjectData;
	private LoadingView lv;
	int mTypeId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.favorite);
		init();
	}
	private void init(){
		tbGrid = (GridView) findViewById(R.id.GridView_toolbar);
		initMenu();
		lv = (LoadingView)findViewById(R.id.data_loading);
		prlistView = (PullToRefreshListView) findViewById(R.id.obj_list);
		prlistView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
			}
		});
		mObjectData = new ArrayList<Object>();
		mTeacherData = new ArrayList<TeacherVo>();
		mSubjectData = new ArrayList<SubjectVo>();
		mReadData = new ArrayList<ReadVo>();
		mVideoData = new ArrayList<VideoVo>();
		mBbsData = new ArrayList<BbsAskVo>();
		if(userInfo!=null){
		     memberid = userInfo.getMemberid();
		}
		new GetDataTask().execute();
		prlistView.setOnItemClickListener(this);
		
		
		
		mBack = (ImageButton) findViewById(R.id.obj_back);
		mBack.setOnClickListener(this);
	}
	
	
	private void initMenu() {
		/** 底部菜单图片 **/
		String[] menu_toolbar_name_array = { "名师", "课程", "阅读", "视频", "帖子" };
		// 设置点击后的效果
		tbGrid.setSelector(R.drawable.bg_alibuymenu_states);
//		toolbarGrid.setBackgroundResource(R.drawable.dock);// 设置背景
		
		tbGrid.setNumColumns(5);// 设置每行列数
		tbGrid.setGravity(Gravity.CENTER);// 位置居中
		tbGrid.setVerticalSpacing(20);// 垂直间隔
		tbGrid.setHorizontalSpacing(10);// 水平间隔
		tbGrid.setAdapter(getMenuAdapter(menu_toolbar_name_array));// 设置菜单Adapter
		/** 监听底部菜单选项 **/
		tbGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> data, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case TOOLBAR_ITEM_TEACHER:
					mTypeId = TOOLBAR_ITEM_TEACHER ;
					new GetDataTask().execute();
//					arg1.setBackgroundResource(R.drawable.button_background_3);
//					tbGrid.r;
					  for(int i=0;i<data.getCount();i++){
				            View v=data.getChildAt(i);
				            if(v!=null){
				            if (arg2 == i) {
				                v.setBackgroundResource(R.drawable.button_background_3);
				            } else {
				                v.setBackgroundDrawable(null); 
				            }}
				         }
					break;
				case TOOLBAR_ITEM_SUBJECT:
					mTypeId = TOOLBAR_ITEM_SUBJECT ;
					new GetDataTask().execute();
					for(int i=0;i<data.getCount();i++){
			            View v=data.getChildAt(i);
			            if(v!=null){
			            if (arg2 == i) {
			                v.setBackgroundResource(R.drawable.button_background_3);
			            } else {
			            	v.setBackgroundDrawable(null); 
			            }}
			         }
					break;
				case TOOLBAR_ITEM_READ:
					mTypeId = TOOLBAR_ITEM_READ ;
					new GetDataTask().execute();
					for(int i=0;i<data.getCount();i++){
			            View v=data.getChildAt(i);
			            if(v!=null){
			            if (arg2 == i) {
			                v.setBackgroundResource(R.drawable.button_background_3);
			            } else {
			            	v.setBackgroundDrawable(null); 
			            }}
			         }
					break;
				case TOOLBAR_ITEM_VIDEO:
					mTypeId = TOOLBAR_ITEM_VIDEO ;
					new GetDataTask().execute();
					for(int i=0;i<data.getCount();i++){
			            View v=data.getChildAt(i);
			            if(v!=null){
			            if (arg2 == i) {
			                v.setBackgroundResource(R.drawable.button_background_3);
			            } else {
			            	v.setBackgroundDrawable(null); 
			            }}
			         }
					break;
				case TOOLBAR_ITEM_BBS:
					mTypeId = TOOLBAR_ITEM_BBS ;
					new GetDataTask().execute();
					for(int i=0;i<data.getCount();i++){
			            View v=data.getChildAt(i);
			            if(v!=null){
			            if (arg2 == i) {
			                v.setBackgroundResource(R.drawable.button_background_3);
			            } else {
			            	v.setBackgroundDrawable(null); 
			            }}
			         }
					break;
				}
			}
		});
	}
	
	/** 
	 * 刷新数据
	 * @author xiong
	 */
	private class GetDataTask extends AsyncTask<Void, Void, List<Object>> {
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
		protected List<Object> doInBackground(Void... params) {
			try {
				if(mObjectData!=null)
				   mObjectData.clear();
				else
				  mObjectData = new ArrayList<Object>();
				Map<String, Object> param = new HashMap<String, Object>();
				 param.put("memberid", memberid);
				 switch (mTypeId) {
					case TOOLBAR_ITEM_TEACHER:
						mTeacherData =   TeacherDao.getTeacherFavoritesList(param);
						for(int i = 0; i<mTeacherData.size();i++){
							mObjectData.add(mTeacherData.get(i));
							LogUtil.d("sssssss", mTeacherData.get(i).getEnname() );
						}
						break;
					case TOOLBAR_ITEM_SUBJECT:
						mSubjectData =   SubjectDao.getSubjectFavoritesList(param);
						for(int i = 0; i<mSubjectData.size();i++){
							mObjectData.add(mSubjectData.get(i));
						}
						break;
					case TOOLBAR_ITEM_READ:
						mReadData =   ReadDao.getReadFavoritesList(param);
						for(int i = 0; i<mReadData.size();i++){
							mObjectData.add(mReadData.get(i));
						}
						break;
					case TOOLBAR_ITEM_VIDEO:
						mVideoData =   VideoDao.getVideoFavoritesList(param);
						for(int i = 0; i<mVideoData.size();i++){
							mObjectData.add(mVideoData.get(i));
						}
						break;
					case TOOLBAR_ITEM_BBS:
						mBbsData =   BbsDao.getBbsFavoritesList(param);
						for(int i = 0; i<mBbsData.size();i++){
							mObjectData.add(mBbsData.get(i));
						}
						break;
				default:
					break;
				}
			} catch(EuException ex){ 
				ex.printStackTrace();
				exType = 1;	
				erMsg = ex.getMessage();
				mObjectData = null;
				isFoot = false;
				LogUtil.d("XB", ""+ex.getMessage());
			}catch(Exception e) {
				 e.printStackTrace();
				 mObjectData = null;
				 isFoot = false;
			}
			return mObjectData;
		}
		@Override
		protected void onPostExecute(List<Object> data) {
		
			 switch (mTypeId) {
				case 0:
					List<TeacherVo> mTeacherTempData =new ArrayList<TeacherVo>();
					if(data!=null){
					for(int i = 0; i<data.size();i++){
						if(data.get(i) instanceof TeacherVo ){
						mTeacherTempData.add((TeacherVo)data.get(i));
						}
					}}
					teacherBaseAdpter = new TeacherAdapter(FavoriteActivity.this, R.layout.list_item, mTeacherTempData);
					prlistView.setAdapter(teacherBaseAdpter);
					prlistView.onRefreshComplete();
					teacherBaseAdpter.notifyDataSetChanged();
					super.onPostExecute(data);
					break;
				case 1:
					List<SubjectVo> mSubjectTempData =new ArrayList<SubjectVo>();
					if(data!=null){
					for(int i = 0; i<data.size();i++){
						if(data.get(i) instanceof SubjectVo ){
							mSubjectTempData.add((SubjectVo)data.get(i));
						}
					}}
					subjectBaseAdpter = new SubjectAdapter(FavoriteActivity.this, R.layout.subject_list_item, mSubjectTempData);
					prlistView.setAdapter(subjectBaseAdpter);
					prlistView.onRefreshComplete();
					subjectBaseAdpter.notifyDataSetChanged();
					super.onPostExecute(data);
					break;
				case 2:
					List<ReadVo> mReadTempData =new ArrayList<ReadVo>();
					if(data!=null){
					for(int i = 0; i<data.size();i++){
						if(data.get(i) instanceof ReadVo ){
							mReadTempData.add((ReadVo)data.get(i));
						}
					}}
					readBaseAdpter = new ReadAdapter(FavoriteActivity.this, R.layout.obj_item, mReadTempData);
					prlistView.setAdapter(readBaseAdpter);
					prlistView.onRefreshComplete();
					readBaseAdpter.notifyDataSetChanged();
					super.onPostExecute(data);
					break;
				case TOOLBAR_ITEM_VIDEO:
					LogUtil.d("^&%%%%%%%%%%%", "sssssss");
					List<VideoVo> mVideoTempData =new ArrayList<VideoVo>();
					if(data!=null){
					for(int i = 0; i<data.size();i++){
						if(data.get(i) instanceof VideoVo ){
							mVideoTempData.add((VideoVo)data.get(i));
						}
					}}
					videoBaseAdpter = new VideoAdapter(FavoriteActivity.this, R.layout.obj_item, mVideoTempData);
					prlistView.setAdapter(videoBaseAdpter);
					prlistView.onRefreshComplete();
					videoBaseAdpter.notifyDataSetChanged();
					super.onPostExecute(data);
					break;
				case TOOLBAR_ITEM_BBS:
					LogUtil.d("^&%%%%%%%%%%%", "");
					List<BbsAskVo> mBbsTempData =new ArrayList<BbsAskVo>();
					if(data!=null){
					for(int i = 0; i<data.size();i++){
						if(data.get(i) instanceof BbsAskVo ){
							mBbsTempData.add((BbsAskVo)data.get(i));
						}
					}}
					askAdapter = new BbsInfoAdapter(FavoriteActivity.this, R.layout.list_bbs_askitem, mBbsTempData);
					prlistView.setAdapter(askAdapter);
					prlistView.onRefreshComplete();
					askAdapter.notifyDataSetChanged();
					super.onPostExecute(data);
					break;
				}
			
			 lv.onPost(data, lv, prlistView, exType, erMsg);
			 prlistView.setShowFooter(isFoot);
		}
	}
	 
	
	/**
	 * 构造菜单Adapter
	 * 
	 * @param textResourceArray
	 *            图片
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter( String[] textResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < textResourceArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemText", textResourceArray[i]);
			data.add(map);
		}
		FootBarAdapter simperAdapter = new FootBarAdapter(this, data,
				R.layout.favorite_top_item, new String[] { "itemText" },
				new int[] { R.id.item_text  });
		return simperAdapter;
	}
	
	public class FootBarAdapter extends SimpleAdapter {
		public FootBarAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v =  super.getView(position, convertView, parent);
			if (position == 0) {
				v.setBackgroundResource(R.drawable.button_3);
			}
			return v;
		}
		}
	
	@Override
	protected void onResume() {
		super.onResume();
		String className =  new HomeActivity().getClass().getName();
		String fromClassName =  new HomeActivity().getClass().getName();
		isModel(Def.MODEl_FA_List, className,fromClassName,null);
	}

	@Override
	public void onClick(View v) {
		 
		switch (v.getId()) {
		case R.id.obj_back:
			finish();
		}
	}
	 
	
	@Override
	public void onItemClick(AdapterView<?> data, View arg1, int position, long arg3) {
		if(data.getAdapter().getItem(position) instanceof TeacherVo ){
			TeacherVo tv = (TeacherVo)data.getAdapter().getItem(position);
			Intent mIntent = new Intent(this,TeacherInfoActivity.class);  
	        Bundle mBundle = new Bundle();  
	        mBundle.putSerializable(Def.OBJ,tv);  
	        mIntent.putExtra(Def.OBJ_Bundle,mBundle);
	        startActivity(mIntent);  
		}else if(data.getAdapter().getItem(position) instanceof SubjectVo ){
			SubjectVo tv = (SubjectVo)data.getAdapter().getItem(position);
			Intent mIntent = new Intent(this,SubjectInfoActivity.class);  
	        Bundle mBundle = new Bundle();  
	        mBundle.putSerializable(Def.OBJ,tv);  
	        mIntent.putExtra(Def.OBJ_Bundle,mBundle);
	        startActivity(mIntent);  
		}else if(data.getAdapter().getItem(position) instanceof ReadVo ){
			ReadVo tv = (ReadVo)data.getAdapter().getItem(position);
			Intent mIntent = new Intent(this,ReadInfoActivity.class);  
	        Bundle mBundle = new Bundle();  
	        mBundle.putSerializable(Def.OBJ,tv);  
	        mIntent.putExtra(Def.OBJ_Bundle,mBundle);
	        startActivity(mIntent);  
		}else if(data.getAdapter().getItem(position) instanceof VideoVo ){
			VideoVo tv = (VideoVo)data.getAdapter().getItem(position);
			Intent mIntent = new Intent(this,VideoInfoActivity.class);  
	        Bundle mBundle = new Bundle();  
	        mBundle.putSerializable(Def.OBJ,tv);  
	        mIntent.putExtra(Def.OBJ_Bundle,mBundle);
	        startActivity(mIntent);  
		}else if(data.getAdapter().getItem(position) instanceof BbsAskVo ){
			BbsAskVo bv = (BbsAskVo)data.getAdapter().getItem(position);
			Intent mIntent = new Intent(this,BbsCheckAllActivity.class);  
	        Bundle mBundle = new Bundle();
	        mBundle.putSerializable(Def.OBJ,bv);
	        mIntent.putExtra(Def.OBJ_Bundle,mBundle);
	        startActivity(mIntent);  
		}
		
		
		
		
	}
 

	class AreaAdapter   extends BaseAdapter {
		   private int[] colors = new int[]{ Color.rgb(254, 251, 244),  Color.rgb(247, 244, 239)};  

			private Context mContext;
			private int mResource;
			private List<BbsVo> mData = new ArrayList<BbsVo>();
			
			public AreaAdapter(Context context , int resource , List<BbsVo> dataList){
				mContext = context;
				mResource = resource ;
				mData = dataList;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mData==null?0:mData.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return mData.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				
				return arg0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				Holder holder = null;
				if(convertView==null)
				{
					convertView=LayoutInflater.from(mContext).inflate(mResource, null);
					holder=new Holder();
					holder.title=(TextView) convertView.findViewById(R.id.eduins_item_title);
					holder.address=(TextView) convertView.findViewById(R.id.eduins_item_address);
					convertView.setTag(holder);
				}else
				{
					holder=(Holder) convertView.getTag();
				}
				holder.title.setText(mData.get(position).getInfo());
				holder.address.setVisibility(View.GONE);
				  int colorPos = position%colors.length;
				  convertView.setBackgroundColor(colors[colorPos]);  
				return convertView;
			}

			class Holder{
				TextView title;
				TextView address;
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

