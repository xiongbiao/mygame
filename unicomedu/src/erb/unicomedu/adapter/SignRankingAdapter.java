package erb.unicomedu.adapter;

import java.util.ArrayList;
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
import erb.unicomedu.vo.EduinsVo;
import erb.unicomedu.vo.SignRankingVo;

public class SignRankingAdapter   extends BaseAdapter {
	   private int[] colors = new int[]{ Color.rgb(254, 251, 244),  Color.rgb(247, 244, 239)};  

		private Context mContext;
		private int mResource;
		private List<SignRankingVo> mData = new ArrayList<SignRankingVo>();
		
		public SignRankingAdapter(Context context , int resource , List<SignRankingVo> dataList){
			mContext = context;
			mResource = resource ;
			mData = dataList;
		}
		public void setData(List<SignRankingVo> dataList){
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
				holder.tvId=(TextView) convertView.findViewById(R.id.sign_ranking_id_tv);
				holder.ivId=(ImageView) convertView.findViewById(R.id.sign_ranking_id_iv);
				holder.name=(TextView) convertView.findViewById(R.id.sign_ranking_name);
				holder.rank=(TextView) convertView.findViewById(R.id.sign_ranking_rank);
				convertView.setTag(holder);
			}else
			{
				holder=(Holder) convertView.getTag();
			}
			
			switch (position) {
			case 0:
				holder.ivId.setVisibility(View.VISIBLE);
				holder.tvId.setVisibility(View.GONE);
				holder.ivId.setBackgroundResource(R.drawable.ranking_first);	
				break;
			case 1:
				holder.ivId.setVisibility(View.VISIBLE);
				holder.tvId.setVisibility(View.GONE);
				holder.ivId.setBackgroundResource(R.drawable.ranking_second);	
				break;
			case 2:
				holder.ivId.setVisibility(View.VISIBLE);
				holder.tvId.setVisibility(View.GONE);
				holder.ivId.setBackgroundResource(R.drawable.ranking_third);	
				break;
			default:
				holder.ivId.setVisibility(View.GONE);
				holder.tvId.setVisibility(View.VISIBLE);
				holder.tvId.setText((position+1)+"");
				break;
			}
			
			
			holder.name.setText(mData.get(position).getNickname());
			holder.rank.setText(mData.get(position).getRank()+"次");
//			
			  int colorPos = position%colors.length;
			  convertView.setBackgroundColor(colors[colorPos]);  
			return convertView;
		}

		class Holder{
			TextView tvId;
			TextView name;
			ImageView ivId;
			TextView rank;
		}
		
}
