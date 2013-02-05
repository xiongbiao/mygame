package erb.unicomedu.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import erb.unicomedu.activity.FavoriteActivity;
import erb.unicomedu.activity.R;
import erb.unicomedu.adapter.BbsInfoAdapter;
import erb.unicomedu.adapter.ReadAdapter;
import erb.unicomedu.adapter.SubjectAdapter;
import erb.unicomedu.adapter.TeacherAdapter;
import erb.unicomedu.adapter.VideoAdapter;
import erb.unicomedu.ui.PullToRefreshListView;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;
import erb.unicomedu.vo.ReadVo;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TeacherVo;
import erb.unicomedu.vo.VideoVo;

/** 
 * 刷新数据
 * @author xiong
 */
public class GetDataTask extends AsyncTask<String, Void, List<Object>> {
	
	private final int TOOLBAR_ITEM_TEACHER = 0;
	private final int TOOLBAR_ITEM_SUBJECT = 1;
	private final int TOOLBAR_ITEM_READ = 2;
	private final int TOOLBAR_ITEM_VIDEO = 3;
	private final int TOOLBAR_ITEM_BBS = 4;
	private List<Object> mObjectData;
	private PullToRefreshListView prlistView;
	int exType = 0;
	String erMsg = "";
	boolean isFoot = false;
	private TeacherAdapter teacherBaseAdpter;
	List<TeacherVo> mTeacherData;
	
	List<SubjectVo> mSubjectData;
	private SubjectAdapter subjectBaseAdpter;
	
	private List<ReadVo> mReadData;
	private ReadAdapter readBaseAdpter;
	
	
	private List<VideoVo> mVideoData;
	private VideoAdapter videoBaseAdpter;
	
	private List<BbsAskVo>   mBbsData;
	private BbsInfoAdapter askAdapter;
	int mTypeId ;
	@Override
	protected void onPreExecute() {
		prlistView.setLoading();
	}
	@Override
	protected List<Object> doInBackground(String... params) {
		try {
			 mObjectData.clear();
			 mTypeId = Integer.valueOf(params[0]);
			 String memberid = params[1];
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
		} catch ( Exception e) {
			 e.printStackTrace();
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
//				teacherBaseAdpter = new TeacherAdapter(FavoriteActivity.this, R.layout.list_item, mTeacherTempData);
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
//				subjectBaseAdpter = new SubjectAdapter(FavoriteActivity.this, R.layout.subject_list_item, mSubjectTempData);
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
//				readBaseAdpter = new ReadAdapter(FavoriteActivity.this, R.layout.obj_item, mReadTempData);
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
//				videoBaseAdpter = new VideoAdapter(FavoriteActivity.this, R.layout.obj_item, mVideoTempData);
				prlistView.setAdapter(videoBaseAdpter);
				prlistView.onRefreshComplete();
				videoBaseAdpter.notifyDataSetChanged();
				super.onPostExecute(data);
				break;
			case TOOLBAR_ITEM_BBS:
				List<BbsAskVo> mBbsTempData =new ArrayList<BbsAskVo>();
				if(data!=null){
				for(int i = 0; i<data.size();i++){
					if(data.get(i) instanceof BbsAskVo ){
						mBbsTempData.add((BbsAskVo)data.get(i));
					}
				}}
//				askAdapter = new BbsInfoAdapter(FavoriteActivity.this, R.layout.list_bbs_askitem, mBbsTempData);
				prlistView.setAdapter(askAdapter);
				prlistView.onRefreshComplete();
				askAdapter.notifyDataSetChanged();
				super.onPostExecute(data);
				break;
			}
		
		
		 prlistView.setShowFooter(isFoot);
	}
}
