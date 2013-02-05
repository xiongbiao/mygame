package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.vo.SubjectVo;
import erb.unicomedu.vo.TeacherVo;

public class SubjectAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<SubjectVo> mData = new ArrayList<SubjectVo>();
	
	public SubjectAdapter(Context context , int resource , List<SubjectVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
	}
	public void setData(List<SubjectVo> dataList){
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
//			holder.imageView=(ImageView) convertView.findViewById(R.id.item_avatar);
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.favoriteView=(ImageView) convertView.findViewById(R.id.item_favorite);
			holder.level=(TextView) convertView.findViewById(R.id.item_level);
			holder.clknumber=(TextView) convertView.findViewById(R.id.item_tv_browser);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
//			holder.imageView.setImageDrawable(null);
			holder.title.setText("");
//			holder.author.setText("");
			holder.level.setText("");
			holder.clknumber.setText("");
			holder.tag = position;
		}
//		holder.imageView.setImageResource(R.drawable.avatar_icon);
		if(mData.get(position).getSubjectName()!=null&&!"".equals(mData.get(position).getSubjectName())){
		   holder.title.setText(mData.get(position).getSubjectName());
		}
		if(mData.get(position).getFitstudent()!=null&&!"".equals(mData.get(position).getFitstudent())){
		   //holder.author.setText("("+mData.get(position).getFitstudent()+")");
		}
		if(mData.get(position).getInfo()!=null&&!"".equals(mData.get(position).getInfo())){
		   holder.level.setText( mData.get(position).getFitstudent());
		}
		if( !"".equals(mData.get(position).getClknumber())){
			   holder.clknumber.setText(  mData.get(position).getClknumber()+"次");
			}
		if( mData.get(position).getIsarchived()==1){
			holder.favoriteView.setImageResource(R.drawable.favorite_iocn);
		}else{
			holder.favoriteView.setImageResource(R.drawable.favorite_no_icon);
		}
		return convertView;
	}
	
	class Holder{
		int tag;
		ImageView imageView;
		TextView title;
		TextView level;
		TextView clknumber;
		ImageView favoriteView;
	}


}
