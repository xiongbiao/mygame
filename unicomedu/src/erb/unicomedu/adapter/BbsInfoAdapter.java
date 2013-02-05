package erb.unicomedu.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;

public class BbsInfoAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<BbsAskVo> mData = new ArrayList<BbsAskVo>();
	
	public BbsInfoAdapter(Context context , int resource , List<BbsAskVo> data){
		mContext = context;
		mResource = resource ;
		mData = data;
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
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.isrecommend=(ImageView) convertView.findViewById(R.id.isrecommend);
			holder.isimage=(ImageView) convertView.findViewById(R.id.isimage);
			holder.title=(TextView) convertView.findViewById(R.id.bbs_ask_title);
			holder.replyCount=(TextView) convertView.findViewById(R.id.reply_count);
			holder.level=(TextView) convertView.findViewById(R.id.date_count);
			holder.masterName=(TextView) convertView.findViewById(R.id.master_name);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
			holder.isrecommend.setImageDrawable(null);
			holder.isimage.setImageDrawable(null);
			holder.title.setText("");
			holder.replyCount.setText("");
			holder.level.setText("");
			holder.masterName.setText("");
			holder.tag = position;
		}
		
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = null;    
         String str = mData.get(position).getPubtime();
         String time = "";
         try {    
        	     date = format.parse(str);  // Thu Jan 18 00:00:00 CST 2007   
//        	     Date dNow = new Date();  
        	     int fttime = (int)date.getTime();
        	     int nowTime =(int)System.currentTimeMillis();
        	     LogUtil.d("tiem : ",fttime + " + " + nowTime);
        	     int rTime = (nowTime-fttime)/(1000*60*60);
        	     LogUtil.d("tiem : ",rTime+"-------"+fttime +"-------"+ str+" + " + nowTime);
        	     if(rTime > 24&& rTime<=48){
        	    	 time = "一天前";
        	     }else if(rTime > 1&&rTime <=24){
        	    	 time = rTime+"小时前";
        	     } else if(rTime < 1){
        	    	 time = (nowTime-fttime)/(1000*60)+"分钟前";
        	     }else if(rTime>48){
        	    	 time = "很久前";
        	     }
        	     
        } catch (ParseException e) {    
        	     e.printStackTrace();    
        } 

		holder.title.setText(mData.get(position).getTitle());
		holder.replyCount.setText("回复："+mData.get(position).getReplynumber());
		holder.level.setText( ""+time);
		holder.masterName.setText(mData.get(position).getMember());
		if(mData.get(position).getIsrecommend()==1){
			holder.isrecommend.setVisibility(View.VISIBLE);
		}else{
			holder.isrecommend.setVisibility(View.INVISIBLE);
		}
        if(mData.get(position).getIsimgcontext()==1){
        	holder.isimage.setVisibility(View.VISIBLE);
		}else{
			holder.isimage.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}
	
	class Holder{
		int tag;
		ImageView isrecommend;
		ImageView isimage;
		TextView title;
		TextView replyCount;
		TextView level;
		TextView masterName;
	}

}
