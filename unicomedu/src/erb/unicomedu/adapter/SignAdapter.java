package erb.unicomedu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.SignRankingVo;

public class SignAdapter extends   BaseAdapter {
	   private int[] colors = new int[]{ Color.rgb(254, 251, 244),  Color.rgb(247, 244, 239)};  

		private Context mContext;
		private int mResource;
		private boolean mIsMy = true;
		private List<SignRankingVo> mData = new ArrayList<SignRankingVo>();
		public SignAdapter(Context context , int resource , List<SignRankingVo> dataList){
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

		public void isMy(boolean isMy){
			mIsMy = isMy;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = null;
			if(convertView==null)
			{
				convertView=LayoutInflater.from(mContext).inflate(mResource, null);
				holder=new Holder();
				holder.address=(TextView) convertView.findViewById(R.id.sign_address);
				holder.name=(TextView) convertView.findViewById(R.id.sign_name);
				holder.tvTime=(TextView) convertView.findViewById(R.id.sign_time);
				convertView.setTag(holder);
			}else
			{
				holder=(Holder) convertView.getTag();
			}
			
			 Date date = null;    
			 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         String str = mData.get(position).getSignuptime();
	         String time = "";
	         try {    
	        	     date = format.parse(str);  
	        	     int fttime = (int)date.getTime();
	        	     int nowTime =(int)System.currentTimeMillis();
	        	     LogUtil.d("tiem : ",fttime + " + " + nowTime);
	        	     int rTime = (nowTime-fttime)/(1000*60*60);
	        	     LogUtil.d("tiem : ",rTime+"-------"+fttime +"-------"+ str+" + " + nowTime);
	        	     if(rTime >= 1&&rTime <=24){
	        	    	 time = rTime+"小时前";
	        	     } else if(rTime < 1){
	        	    	 time = (nowTime-fttime)/(1000*60)+"分钟前";
	        	     }else if(rTime>24){
	        	    	 time =  mData.get(0).getSignuptime();
	        	     }
	        	     
	        } catch (ParseException e) {    
	        	     e.printStackTrace();    
	        } 
			
			holder.address.setText(mData.get(position).getLocationname()+"附近");
			if(!mIsMy){
			   holder.name.setVisibility(View.VISIBLE);
			   holder.name.setText(mData.get(position).getNickname());
			}else{
			   holder.name.setVisibility(View.GONE);
			}
			holder.tvTime.setText(time);
			  int colorPos = position%colors.length;
			  convertView.setBackgroundColor(colors[colorPos]);  
			return convertView;
		}

		class Holder{
			TextView address;
			TextView name;
			TextView tvTime;
		}
		
}
