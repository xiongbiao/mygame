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
import erb.unicomedu.adapter.ReadAdapter.Holder;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.ImageUtil;
import erb.unicomedu.vo.VideoVo;

public class VideoAdapter extends BaseAdapter {


	private Context mContext;
	private int mResource;
	private List<VideoVo> mData = new ArrayList<VideoVo>();
	private AsyncImageLoader asyncImageLoader;
	public VideoAdapter(Context context , int resource , List<VideoVo> dataList){
		mContext = context;
		mResource = resource ;
		mData = dataList;
		asyncImageLoader = new AsyncImageLoader();
	}

	public void setData(List<VideoVo> dataList){
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
		final Holder holder  ;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(mResource, null);
			holder=new Holder();
			holder.imageView=(ImageView) convertView.findViewById(R.id.item_avatar);
			holder.buyView=(ImageView) convertView.findViewById(R.id.item_buy);
			holder.favoriteView=(ImageView) convertView.findViewById(R.id.item_favorite);
			holder.title=(TextView) convertView.findViewById(R.id.item_title);
			holder.author=(TextView) convertView.findViewById(R.id.item_author);
			holder.level=(TextView) convertView.findViewById(R.id.item_level);
			holder.browser=(TextView) convertView.findViewById(R.id.item_tv_browser);
			holder.integral=(TextView) convertView.findViewById(R.id.item_tv_integral);
			holder.tag = position;
			convertView.setTag(holder);
		}else
		{
			holder=(Holder) convertView.getTag();
			holder.imageView.setImageDrawable(null);
			holder.buyView.setImageDrawable(null);
			holder.favoriteView.setImageDrawable(null);
			holder.title.setText("");
			holder.author.setText("");
			holder.level.setText("");
			holder.browser.setText("");
			holder.integral.setText("");
			holder.tag = position;
		}
		
		
		Drawable cachedImage = asyncImageLoader.loadDrawable(mData
				.get(position).getImgurl(), new ImageCallback() {
			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				ImageView imageViewByTag = holder.imageView;
				if (imageViewByTag != null && imageDrawable != null) {
					Bitmap bitmap = ((BitmapDrawable) imageDrawable).getBitmap();
					// 根据容器需要进行锁定比例的图片裁剪（横中，竖中）
//					bitmap = ImageUtil.clippingImage(bitmap,imageViewByTag.getWidth(),imageViewByTag.getHeight(),ImageUtil.CLIP_W_CENTER, ImageUtil.CLIP_H_CENTER);
					imageViewByTag.setImageBitmap(bitmap);
				}
			}
		});
	    if(cachedImage!=null){
	    	Bitmap bitmap = ((BitmapDrawable)cachedImage).getBitmap();
	    	// 根据容器需要进行锁定比例的图片裁剪（横中，竖中）
//			bitmap = ImageUtil.clippingImage(bitmap,holder.imageView.getWidth(),holder.imageView.getHeight(),ImageUtil.CLIP_W_CENTER, ImageUtil.CLIP_H_CENTER);
	 		holder.imageView.setImageBitmap( bitmap );
	    }else{
		    holder.imageView.setImageResource(R.drawable.avatar_icon);
	    }
		
		holder.title.setText( (mData.get(position).getMediaName() ));
		holder.author.setText("作者:"+mData.get(position).getTeacher());
		holder.level.setText( mData.get(position).getSubject());
		holder.browser.setText(mData.get(position).getClknumber()+"次");
		if("0".equals(mData.get(position).getIntegral()))
			holder.integral.setText("免费");
		else	
			holder.integral.setText(mData.get(position).getIntegral()+"分");
		LogUtil.d("read-----------",  mData.get(position).getIsbuy()+"-------------"+mData.get(position).getIsarchived());
		if(mData.get(position).getIsbuy()==1){
			holder.buyView.setImageResource (R.drawable.buy_icon);
		}else{
			holder.buyView.setImageResource(R.drawable.buy_no_icon);
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
		ImageView favoriteView;
		ImageView buyView;
		TextView integral;
		TextView browser;
		
		TextView title;
		
		TextView author;
		TextView level;
	}


}
