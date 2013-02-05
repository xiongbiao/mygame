package erb.unicomedu.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 图片裁剪等处理工具类
 *
 */
public class ImageUtil {
	private final static String TAG = "ImageUtil";
	
	public static final int CLIP_W_LEFT  	= -1;
	public static final int CLIP_W_CENTER 	= 0;
	public static final int CLIP_W_RIGTH 	= 1;
	public static final int CLIP_H_TOP 		= -1;
	public static final int CLIP_H_CENTER 	= 0;
	public static final int CLIP_H_BOTTOM 	= 1;

	/**
	 * 图片裁剪处理 
	 * @param bitmap	源图
	 * @param outWidth	输出图宽(>0)
	 * @param outHeight 使出图高(>0)
	 * @param baseW  	特横版的图片裁剪基准（-1：左,0:中,1:右）
	 * @param baseH		特高版的图片裁剪基准（-1：上,0:中,1:下）
	 * @return	返回新的bitmap对象
	 */
	public   Bitmap clippingImage(Bitmap bitmap, int outWidth, int outHeight, int baseW,int baseH) {
		
		if(bitmap==null)
			return null;
		if(true){
		if (outWidth<=0||outHeight<=0)
		{
			LogUtil.e(TAG, "非法请求，必须指定输出图的宽高尺寸");
			return bitmap;
		}
		
		int srcW = bitmap.getWidth();	//源图宽
		int srcH = bitmap.getHeight();	//源图高
		Matrix matrix = new Matrix();
		
		//先分析源图采用何种裁剪方式
		boolean cutStyle2Width = ( ((float)srcW/srcH-(float)outWidth/outHeight)>=0 );	//true:需要横向裁剪，false：需要竖向裁剪
		
		//横向裁剪时
		if (cutStyle2Width) {
			LogUtil.i(TAG,"横向裁剪");
			//此时以图片的高度H为准，计算裁剪后的图片宽度W
			int newSrcW = srcH*outWidth/outHeight;
			
			int offsetW = 0;
			if (baseW==CLIP_W_CENTER)
				offsetW = (int)(srcW-newSrcW)/2;
			else if (baseW>=CLIP_W_RIGTH)
				offsetW = srcW-newSrcW;
			
			//LogUtil.d(TAG,"源图W:H="+srcW+":"+srcH+ " 裁剪后W:H="+newSrcW+":"+srcH);
			//缩放比例
			matrix.postScale((float)  outWidth/ newSrcW, (float) outHeight / srcH);
			bitmap = Bitmap.createBitmap(bitmap, offsetW,0,newSrcW,srcH, matrix,true);
		}
		else {
			LogUtil.i(TAG,"竖向裁剪");
			//此时以图片的宽度W为准，计算裁剪后的图片高度H
			int newSrcH = srcW*outHeight/outWidth;
			
			int offsetH = 0;
			if (baseH==CLIP_H_CENTER)
				offsetH = (int)(srcH-newSrcH)/2;
			else if (baseH>=CLIP_H_BOTTOM)
				offsetH = srcH-newSrcH;
			
			//LogUtil.d(TAG,"源图W:H="+srcW+":"+srcH+ " 裁剪后W:H="+srcW+":"+newSrcH);
			//缩放比例
			matrix.postScale((float)  outWidth/ srcW, (float) outHeight / newSrcH);
			bitmap = Bitmap.createBitmap(bitmap, 0,offsetH,srcW,newSrcH,matrix,true );
		}
		//LogUtil.d(TAG,"缩放后W:H="+bitmap.getWidth()+":"+bitmap.getHeight());
		}
		return bitmap;
	}
}
