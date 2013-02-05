package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.vo.EduinsVo;

public class EduinsAdapter   extends BaseAdapter {
	   private int[] colors = new int[]{ Color.rgb(254, 251, 244),  Color.rgb(247, 244, 239)};  

		private Context mContext;
		private int mResource;
		private List<EduinsVo> mData = new ArrayList<EduinsVo>();
		
		public EduinsAdapter(Context context , int resource , List<EduinsVo> dataList){
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
			holder.title.setText(mData.get(position).getEduinsName());
			holder.address.setText(mData.get(position).getEduinsAddress());
//			
//			  int colorPos = position%colors.length;
//			  convertView.setBackgroundColor(colors[colorPos]);  
			return convertView;
		}

		class Holder{
			TextView title;
			TextView address;
		}
		
}
