package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.vo.BbsVo;

public class BbsAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<BbsVo> mData = new ArrayList<BbsVo>();
	
	public BbsAdapter(Context context , int resource , List<BbsVo> dataList){
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
//			holder.imageView=(ImageView) convertView.findViewById(R.id.item_avatar);
//			holder.loveView=(ImageView) convertView.findViewById(R.id.item_love);
			holder.title=(TextView) convertView.findViewById(R.id.BBS_title);
			holder.author=(TextView) convertView.findViewById(R.id.theme_count);
			holder.level=(TextView) convertView.findViewById(R.id.post_count);
//			holder.number=(TextView) convertView.findViewById(R.id.item_number);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
//			holder.imageView.setImageDrawable(null);
//			holder.loveView.setImageDrawable(null);
			holder.title.setText("");
			holder.author.setText("");
			holder.level.setText("");
//			holder.number.setText("");
			holder.tag = position;
		}
		holder.title.setText("雅思论坛");
		holder.author.setText("主题 ：7999");
		holder.level.setText( "回复：9008");
//		holder.number.setText(mData.get(position).getClknumber()+"次");
		return convertView;
	}
	
	class Holder{
		int tag;
//		ImageView imageView;
//		ImageView loveView;
		TextView title;
		TextView author;
		TextView level;
//		TextView number;
	}


}
