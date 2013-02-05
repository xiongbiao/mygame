package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.EduInsInfoActivity;
import erb.unicomedu.activity.OnlineRegistrationActivity;
import erb.unicomedu.activity.R;
import erb.unicomedu.vo.PlaceVo;
import erb.unicomedu.vo.SubjectVo;

public class PlaceAdpter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private SubjectVo mSubjectVo;
	public void setmSubjectVo(SubjectVo mSubjectVo) {
		this.mSubjectVo = mSubjectVo;
	}

	private List<PlaceVo> mData = new ArrayList<PlaceVo>();
	
	public PlaceAdpter(Context context , int resource , List<PlaceVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		final int item = position;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.number=(TextView) convertView.findViewById(R.id.item_author);
			holder.amount=(TextView) convertView.findViewById(R.id.item_level);
			holder.mTime=(TextView) convertView.findViewById(R.id.item_time);
			holder.address=(TextView) convertView.findViewById(R.id.item_address);
			holder.mCoordinate = (ImageView)convertView.findViewById(R.id.item_gps);
			holder.onlineReg =(Button) convertView.findViewById(R.id.online_reg_filter);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
			holder.title.setText("");
			holder.number.setText("");
			holder.amount.setText("");
			holder.mTime.setText("");
			holder.address.setText("");
			holder.tag = position;
		}
		if(mData.get(position).getSchoollocation()!=null&&!"".equals(mData.get(position).getSchoollocation())){
		   holder.title.setText(mData.get(position).getSchoollocation());
		}
		if(mData.get(position).getNumber()!=null&&!"".equals(mData.get(position).getNumber())){
		   holder.number.setText("(限"+mData.get(position).getNumber()+"人)");
		}
		if(mData.get(position).getAmount()!=null&&!"".equals(mData.get(position).getAmount())){
		   holder.amount.setText("价格："+ mData.get(position).getAmount());
		}
		if(mData.get(position).getAddress()!=null&&!"".equals(mData.get(position).getAddress())){
			   holder.address.setText("地址："+ mData.get(position).getAddress());
		}
		holder.mTime.setText("时间："+"9:00-12:00");
		holder.mCoordinate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mContext,EduInsInfoActivity.class);  
				Bundle mBundle = new Bundle();  
		        mBundle.putSerializable("place_info",mData.get(item));  
		        mIntent.putExtras(mBundle); 
				mContext.startActivity(mIntent);  
			}
		});
		holder.onlineReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(mContext,OnlineRegistrationActivity.class); 
				String[] id = null;
				String[] name = null;
				if(mData!=null&&mData.size()>0){
					  id = new String[mData.size()];
					  name = new String[mData.size()];
					for(int i =0 ;i<mData.size();i++){
						name[i] = mData.get(i).getSchoollocation();
						id[i] = mData.get(i).getItemid();
					}
				}
				Bundle mBundle = new Bundle();  
		        mBundle.putSerializable("place_info",mData.get(item));  
		        mBundle.putSerializable("subject_info",mSubjectVo);  
		        mBundle.putStringArray("data_name", name);
		        mBundle.putStringArray("data_id", id);
		        mIntent.putExtras(mBundle); 
				mContext.startActivity(mIntent);  
			}
		});
		return convertView;
	}
	
	class Holder{
		int tag;
		ImageView mCoordinate;
		TextView title;
		TextView number;
		TextView amount;
		TextView mTime;
		TextView address;
		Button  onlineReg;
	}


}
