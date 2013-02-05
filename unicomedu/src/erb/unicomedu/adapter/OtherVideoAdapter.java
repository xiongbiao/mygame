package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.vo.VideoVo;

public class OtherVideoAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<VideoVo> mData = new ArrayList<VideoVo>();
//	private AsyncImageLoader asyncImageLoader;
	public OtherVideoAdapter(Context context , int resource , List<VideoVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
//		asyncImageLoader = new AsyncImageLoader();
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
		
//		holder.title.setText( (mData.get(position).getMediaName().length()>20?mData.get(position).getMediaName().substring(0, 20)+"…":mData.get(position).getMediaName()));
		holder.title.setText( (mData.get(position).getMediaName()) );
		return convertView;
	}
	
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if(bitmap==null)
			return null;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) width / w, (float) height / h);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}
	
	class Holder{
		int tag;
		TextView title;
	}


}
