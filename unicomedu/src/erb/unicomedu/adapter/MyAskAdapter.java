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
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.BbsAskVo;

public class MyAskAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<BbsAskVo> mData = new ArrayList<BbsAskVo>();
	
	public MyAskAdapter(Context context , int resource , List<BbsAskVo> dataList){
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
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.bbs_myask_title);
			holder.title.setTextColor(Color.rgb(255, 149, 1));
			holder.author=(TextView) convertView.findViewById(R.id.master_name);
			holder.author.setTextColor(Color.rgb(208, 207, 203));
			holder.replyCount=(TextView) convertView.findViewById(R.id.reply_count);
			holder.replyCount.setTextColor(Color.rgb(208, 207, 203));
			holder.pubtime=(TextView) convertView.findViewById(R.id.date_count);
			holder.pubtime.setTextColor(Color.rgb(208, 207, 203));
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
			holder.title.setText("");
			holder.author.setText("");
			holder.replyCount.setText("");
			holder.pubtime.setText("");
			holder.tag = position;
		}
		LogUtil.i("TUDE==", "HOLDER:"+holder);
		LogUtil.i("TUDE==", "mdata:"+mData);
		holder.title.setText(mData.get(position).getTopic());
		holder.author.setText(mData.get(position).getMember());
		holder.replyCount.setText("回复 ："+mData.get(position).getReplynumber());
		holder.pubtime.setText(isexpires(mData.get(position).getPubtime())+"天前");
		return convertView;
	}
	
	private int isexpires(String time){
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date createDate = null;
		try {
			createDate = format2.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}

		if(createDate == null) return 0;
		System.out.println("new Date()="+new Date());
		long ss=((new Date()).getTime()-createDate.getTime())/1000/3600/24;
		System.out.println("ss="+ss);
		return (int)ss;
	}
	
	class Holder{
		int tag;
		TextView title;
		TextView author;
		TextView replyCount;
		TextView pubtime;
		@Override
		public String toString() {
			return "Holder [tag=" + tag + ", title=" + title + ", author="
					+ author + ", replyCount=" + replyCount + ", pubtime="
					+ pubtime + "]";
		}
	}


}
