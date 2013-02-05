package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.vo.ReadVo;

public class OtherReadAdapter extends BaseAdapter {
	
	private Context mContext;
	private int mResource;
	private List<ReadVo> mData = new ArrayList<ReadVo>();
	private AsyncImageLoader asyncImageLoader;
	public OtherReadAdapter(Context context , int resource , List<ReadVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
		asyncImageLoader = new AsyncImageLoader();
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
		final Holder holder  ;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
			holder.title.setText("");
			holder.tag = position;
		}
		
		holder.title.setText(mData.get(position).getBookname());
		return convertView;
	}
	class Holder{
		int tag;
		TextView title;
	}

}
