package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.OnlineRegistrationActivity;
import erb.unicomedu.activity.R;
import erb.unicomedu.dao.SubjectDao;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.PlaceVo;
import erb.unicomedu.vo.SubjectVo;

public class TeacherSubjectAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<SubjectVo> mData = new ArrayList<SubjectVo>();
	private List<PlaceVo> data;
	
	public TeacherSubjectAdapter(Context context , int resource , List<SubjectVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
		data = new ArrayList<PlaceVo>();
	}

	@Override
	public int getCount() {
		if(mData == null)
			return 0;
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		if(mData == null || mData.size() == 0 || mData.get(position) == null)
			return null;
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	  int item = 0;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.onlineReg =(Button) convertView.findViewById(R.id.online_reg_filter);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
//			holder.imageView.setImageDrawable(null);
			holder.title.setText("");
			holder.tag = position;
		}
//		holder.imageView.setImageResource(R.drawable.avatar_icon);
		if(mData.get(position).getSubjectName()!=null&&!"".equals(mData.get(position).getSubjectName())){
		   holder.title.setText(mData.get(position).getSubjectName());
		}
		item = position;
		final int index = position;
		holder.onlineReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new GetDataTask().execute(mData.get(index).getSubjectId());
			}
		});
		return convertView;
	}
	
	private class GetDataTask extends AsyncTask<String, Void, List<PlaceVo>> {
		
		@Override
		protected void onPostExecute(List<PlaceVo> data) {
			 
			Intent mIntent = new Intent(mContext,OnlineRegistrationActivity.class); 
			String[] id = null;
			String[] name = null;
			if(data!=null&&data.size()>0){
				  id = new String[data.size()];
				  name = new String[data.size()];
				for(int i =0 ;i<data.size();i++){
					name[i] = data.get(i).getSchoollocation();
					id[i] = data.get(i).getItemid();
				}
			}
			LogUtil.d(TeacherSubjectAdapter.this.getClass().getName(), "size " +data.size());
			Bundle mBundle = new Bundle();  
	        mBundle.putSerializable("subject_info",mData.get(item));  
	        mBundle.putStringArray("data_name", name);
	        mBundle.putStringArray("data_id", id);
	        mIntent.putExtras(mBundle); 
			mContext.startActivity(mIntent);  
			
			super.onPostExecute(data);
		}

		@Override
		protected List<PlaceVo> doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				LogUtil.d("*********", "getSubjectId : "+params[0]);
				param.put("subjectid", params[0]);
				data = SubjectDao.getSubjectClassList(param);
			} catch ( Exception e) {
				 e.printStackTrace();
			}
			return data;
		}
	}
	
	class Holder{
		int tag;
		ImageView imageView;
		TextView title;
		TextView author;
		TextView level;
		TextView clknumber;
		Button  onlineReg;
	}

}
