package erb.unicomedu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import erb.unicomedu.activity.R;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.ImageUtil;
import erb.unicomedu.vo.TeacherVo;

public class TeacherAdapter  extends BaseAdapter {
	//private final String TAG = "TeacherAdapter";
	
	private Context mContext;
	private int mResource;
	private List<TeacherVo> mData = new ArrayList<TeacherVo>();
	private AsyncImageLoader asyncImageLoader;
	private ImageUtil iu;
	 Drawable cachedImage;
	class Holder{
		ImageView imageView;
		TextView title;
		TextView content;
		TextView info;
		
		ImageView favoriteView;
		TextView browser;
	}
	
	public TeacherAdapter(Context context , int resource , List<TeacherVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
		asyncImageLoader = new AsyncImageLoader();
		iu = new ImageUtil();
	}
	
	public void setData(List<TeacherVo> dataList){
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
		View view ;
		
		final Holder holder  ;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder = new Holder();
			holder.imageView=(ImageView) convertView.findViewById(R.id.item_avatar);
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.content=(TextView) convertView.findViewById(R.id.item_author);
			holder.info=(TextView) convertView.findViewById(R.id.item_level);
			holder.favoriteView=(ImageView) convertView.findViewById(R.id.item_favorite);
			holder.browser=(TextView) convertView.findViewById(R.id.item_tv_browser);
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
		}
		cachedImage = asyncImageLoader.loadDrawable(mData.get(position).getPicture(), new ImageCallback() {
	            @Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
	                ImageView imageViewByTag = holder.imageView;
					if (imageViewByTag != null && imageDrawable != null) {
						Bitmap bitmap = ((BitmapDrawable) imageDrawable).getBitmap();
						//根据容器需要进行锁定比例的图片裁剪（横中，竖顶）
						bitmap = iu.clippingImage( bitmap,imageViewByTag.getWidth(),imageViewByTag.getHeight(),ImageUtil.CLIP_W_CENTER,ImageUtil.CLIP_H_TOP);
						imageViewByTag.setImageBitmap( bitmap );
					}
	            }
	        });
	    if(cachedImage!=null){
	    	Bitmap bitmap = ((BitmapDrawable)cachedImage).getBitmap();
	    	//根据容器需要进行锁定比例的图片裁剪（横中，竖顶）
	    	bitmap = iu.clippingImage( bitmap,holder.imageView.getWidth(),holder.imageView.getHeight(),ImageUtil.CLIP_W_CENTER,ImageUtil.CLIP_H_TOP);
	 		holder.imageView.setImageBitmap( bitmap );
	    }else{
		    holder.imageView.setImageResource(R.drawable.avatar_icon);
	    }
	    String cnName = mData.get(position).getEnname()==null||"".equals(mData.get(position).getEnname().trim()) ?"":"( " + mData.get(position).getEnname()+ " )";
		holder.title.setText(mData.get(position).getCnname()+ cnName);
		holder.content.setText(mData.get(position).getZhicheng());
		holder.info.setText(mData.get(position).getTechclass()== null ? "" : mData.get(position).getTechclass()); 
		if( mData.get(position).getIsarchived()==1){
			holder.favoriteView.setImageResource(R.drawable.favorite_iocn);
		}else{
			holder.favoriteView.setImageResource(R.drawable.favorite_no_icon);
		}
		holder.browser.setText(mData.get(position).getClknumber()+"次");
		return convertView;
	}
	
}
