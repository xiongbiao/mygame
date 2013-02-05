package erb.unicomedu.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import erb.unicomedu.dao.LoginDao;
import erb.unicomedu.util.AsyncImageLoader;
import erb.unicomedu.util.AsyncImageLoader.ImageCallback;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.LogUtil;
import erb.unicomedu.vo.UserVo;

public class UserInfoActivity extends PublicActivity implements OnClickListener {
	private Button mUserEditor;
	private ImageButton mBack;
	private EditText mUserName;
	private EditText mUserPass;
	private EditText mUserNickname;
	private Spinner mUserSex;
	//private EditText mUserGrade;
	private EditText mUserIntegration;
	private EditText mUserAge;
	//private EditText mUserHobby;
	private EditText mUserEmail;
	private EditText mUserCurriculum;
	private boolean isEditor = false;
	private ImageView mAvatar;
//	private Spinner mSex;
	
	private String imagePhotoPath = "/ue/photo/";
	SharedPreferences mSettings;
	SharedPreferences.Editor mEditor;
	
	@Override
	protected void onResume() {
		super.onResume();
		String className = this.getClass().getName();
		String fromClassName = new HomeActivity().getClass().getName();
		resetUserInfoVo();
		isModel(Def.MODEl_USER_INFO, className, fromClassName,null);
		
		initData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		viewInit();
	}
	
	private void viewInit() {
		mUserName = (EditText) findViewById(R.id.user_name);
		mUserPass = (EditText) findViewById(R.id.user_pass);
		mUserNickname = (EditText) findViewById(R.id.user_nickname);
//		mUserSex = (EditText) findViewById(R.id.user_sex);
		mUserSex = (Spinner) findViewById(R.id.user_sex);
		//mUserGrade = (EditText) findViewById(R.id.user_grade);
		mUserIntegration = (EditText) findViewById(R.id.user_integration);
		mUserAge = (EditText) findViewById(R.id.user_age);
		//mUserHobby = (EditText) findViewById(R.id.user_hobby);
		mUserEmail = (EditText) findViewById(R.id.user_email);
		mUserCurriculum = (EditText) findViewById(R.id.user_curriculum);
		mUserEditor = (Button) findViewById(R.id.user_info_editor);
		mUserEditor.setOnClickListener(this);
		mBack = (ImageButton) findViewById(R.id.obj_info_back);
		mBack.setOnClickListener(this);
		idEnabled(false);
		//mUserGrade.setEnabled(false);
		mUserIntegration.setEnabled(false);
		mUserName.setEnabled(false);
		mUserCurriculum.setEnabled(false);
		
		mAvatar = (ImageView)findViewById(R.id.item_avatar);
		mAvatar.setOnClickListener(this);
		mSettings = getSharedPreferences(Def.PREFS_NAME, MODE_PRIVATE);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			System.out.println("down");
//			
//		}
//		return super.onTouchEvent(event);
//	}
	
	private void initData() {
		if (userInfo != null) {
			mUserName.setText(userInfo.getUserName());
			//mUserPhone.setText(uv.getTelphone());
			mUserNickname.setText(userInfo.getNickname());
			String[] setList = new String[]{"男","女"};
			ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, R.drawable.drop_list_hover,setList);
//			categoryAdapter.setDropDownViewResource(R.drawable.drop_list_ys);
			categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mUserSex.setAdapter(categoryAdapter);
			
			
			//0-未知，1-男，2-女
			if ("2".equals(userInfo.getSex()))
				mUserSex.setSelection(1);
			else if ("1".equals(userInfo.getSex()))
				mUserSex.setSelection(0);
			else
				mUserSex.setSelection(0);
			//mUserGrade.setText(uv.getLeve());
			mUserIntegration.setText(userInfo.getIntegral());
			mUserAge.setText(userInfo.getBirthday());
			//mUserHobby.setText(uv.getLikeinfo());
			mUserEmail.setText(userInfo.getEmail());
			mUserCurriculum.setText(userInfo.getIntention());
			String bRestule = userInfo.getLogo();
			LogUtil.d("UserInfo", "-------------图片URL---"+bRestule);
			int fileIndex = bRestule.lastIndexOf("/")+1;
			int fileLast =  bRestule.lastIndexOf(".");
			bRestule = bRestule.substring(fileIndex, fileLast);
			Bitmap photo = getBitmapFromSd(bRestule);
			LogUtil.d("UserInfo", "-------------图片名称---"+bRestule);
			if(photo!=null){
				LogUtil.d("UserInfo", "-------------本地头像---");
				Drawable drawable = new BitmapDrawable(photo);
				mAvatar.setBackgroundDrawable(drawable);
			}else{
				//服务器获取最新头像文件
				 Drawable avatarImage = new AsyncImageLoader().loadDrawable(userInfo.getLogo(), new ImageCallback() {
		            @Override
					public void imageLoaded(Drawable imageDrawable, String imageUrl) {
		                ImageView imageViewByTag = mAvatar;
		                if (imageViewByTag != null&&imageDrawable!=null) {
		                    imageViewByTag.setBackgroundDrawable(imageDrawable);
		                }
		            }
		        });
				 if(avatarImage!=null){
					 try {
				    	Bitmap bitmap = ((BitmapDrawable)avatarImage).getBitmap();
				    	mAvatar.setBackgroundDrawable(avatarImage);
				    	saveMyBitmap(bitmap, bRestule);
					 } catch (Exception e) {
							// TODO: handle exception
						 e.printStackTrace();
						}
				    }else{
				    	mAvatar.setBackgroundResource(R.drawable.mylogo);
				    }
				 LogUtil.d("UserInfo", "-------------服务器头像---");
			}
		}else{
			mUserEditor.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.obj_info_back:
			finish();
			break;
		case R.id.user_info_editor:
			if (userInfo != null) {
				if (!isEditor) {
					mUserEditor.setText("保存");
					idEnabled(true);
					isEditor = true;
				} else {
					String nickname = mUserNickname.getText().toString().trim();
					String sex = mUserSex.getSelectedItemId()+"";
					String age = mUserAge.getText().toString().trim();
					String email = mUserEmail.getText().toString().trim();
					String pass = mUserPass.getText().toString().trim();
					try {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("memberid", userInfo.getMemberid());	//账号
					param.put("nickname", nickname);	//昵称
					param.put("sex",sex);	
					param.put("age", age);	//年龄
					param.put("email",email);//邮箱
					if(pass!=null&&!"".equals(pass))
					   param.put("password",pass);//密码
					
					if (true==LoginDao.updateUser(param))
					{
						Toast.makeText(this, "个人资料更新成功", Toast.LENGTH_SHORT).show();
						mUserEditor.setText("编辑");
						isEditor = false;
						idEnabled(false);
						mUserPass.setText("");
						userInfo.setSex((Integer.valueOf(sex)+1)+"");
						userInfo.setNickname(nickname);
						userInfo.setBirthday(age);
						userInfo.setEmail(email);
						mUserSex.setSelection(Integer.valueOf(sex));
						mEditor = mSettings.edit();
						if(pass!=null&&!"".equals(pass)){
						    userInfo.setUserPass(pass);
						    mEditor.remove(Def.ACCT_USERPWD);
							mEditor.putString(Def.ACCT_USERPWD, pass);	//User密码
							mEditor.commit();
						}
						updateJsonUser(userInfo);
					}
					else {
						Toast.makeText(this, "个人资料更新失败，请稍后再试", Toast.LENGTH_SHORT).show();
					}
					
					} catch (Exception e) {
						Toast.makeText(this, "个人资料更新失败，请稍后再试", Toast.LENGTH_SHORT).show();
					}
				}
			}
			break;
			case R.id.item_avatar:
				ShowDialog();
			break;
		}
	}
	
	private void updateJsonUser(UserVo uInfo){
		try {
			JSONObject j =new JSONObject();
			j.put("smis", uInfo.getSmis());
			j.put("sid", uInfo.getSid());
			j.put("logo", uInfo.getLogo());
			j.put("birthday", uInfo.getBirthday());
			j.put("sex", uInfo.getSex());
			j.put("memberid", uInfo.getMemberid());
			j.put("payment", uInfo.getPayment());
			j.put("nickname", uInfo.getNickname());
			j.put("truename", uInfo.getTruename());
			j.put("likeinfo", uInfo.getLikeinfo());
			j.put("provinceid", uInfo.getProvinceid());
			j.put("password", uInfo.getUserPass());
			j.put("logname", uInfo.getUserName());
			j.put("country", uInfo.getCountry());
			j.put("city", uInfo.getCity());
			j.put("integral", uInfo.getIntegral());
			j.put("cityid", uInfo.getCityid());
			j.put("telphone", uInfo.getTelphone());
			j.put("vip", uInfo.getVip());
			j.put("regtime", uInfo.getRegtime());
			j.put("school", uInfo.getSchool());
			j.put("email", uInfo.getEmail());
			j.put("province", uInfo.getProvince());
			j.put("mobile", uInfo.getMobile());
			j.put("intention", userInfo.getIntention());
		   	LogUtil.d("ssssssssss",  j.toString());
		   	SharedPreferences settings = getSharedPreferences(Def.PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.remove(Def.SP_USERINFO_JSON_OBJ);
			editor.putString(Def.SP_USERINFO_JSON_OBJ, j.toString());
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void ShowDialog() {
		new AlertDialog.Builder(this).setTitle("更换头像")
				.setItems(new String[] { "从相册选择", "拍照" },new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							dialog.dismiss();
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
							startActivityForResult(intent, 1);
							break;
						case 1:
							dialog.dismiss();
							Intent intentC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intentC.putExtra(MediaStore.EXTRA_OUTPUT, Uri
									.fromFile(new File(Environment
											.getExternalStorageDirectory(),
											"temp_image.jpg")));
							startActivityForResult(intentC, 2);
							break;
						}
						 
					}}).show();
	}
	
 
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if(data!=null&&data.getData()!=null)
			  startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case 2:
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/temp_image.jpg");
				
				startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 */
			if(data != null){
				setPicToView(data);
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			 
			String userName = "temp";
			String memberid = "";
			if(userInfo!=null){
				userName = userInfo.getUserName();
				memberid = userInfo.getMemberid();
			}
			try {
				saveMyBitmap(photo, userName);
				
				File temp = new File(Environment.getExternalStorageDirectory()+ "/temp_image.jpg");
				if(temp.exists()){
					temp.delete();
				}
				 Map<String, Object> params = new HashMap<String, Object>();
				 params.put("scode", "xdf");
				 params.put("memberid", memberid);
				 Map<String, File> files = new HashMap<String, File>();
				 String filePath = Environment.getExternalStorageDirectory()+imagePhotoPath +userName+".png";
				 files.put("logofile", new File(filePath));
				 String fileName =  LoginDao.updateUserAvatar(params, files);
				 if(userInfo!=null){
                        userInfo.setLogo(fileName);
                        updateJsonUser(userInfo);
                       
				 }
				 int fileIndex = fileName.lastIndexOf("/")+1;
				 int fileLast =  fileName.lastIndexOf(".");
				 fileName = fileName.substring(fileIndex, fileLast);
				 saveMyBitmap(photo,fileName);
				 LogUtil.d("1----------filename---------：", fileName);
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				LogUtil.d("er::---", e.getClass().getName());
			}
			
			mAvatar.setBackgroundDrawable(drawable);
		}
	}

	private int CopySdcardFile1(String fromFile, String toFile)
    {
        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) 
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex) 
        {
            return -1;
        }
    }
	
	
	private Bitmap getBitmapFromSd(String bitName) {
		String imgFilePath = Environment.getExternalStorageDirectory()+imagePhotoPath + bitName + ".png";
		File file = new File(imgFilePath);
		if(!file.exists()){
			return null;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);// 文件输入流
			Bitmap bmp = BitmapFactory.decodeStream(fis);
			return bmp;
		} catch (Exception e) {
			e.printStackTrace();
           return null;
		}  
	}

	
	private void saveMyBitmap(Bitmap bm,String bitName) throws IOException {
		String imgFilePath = Environment.getExternalStorageDirectory()+imagePhotoPath ;
		File file = new File(imgFilePath);
		if(!file.exists()){
			file.mkdirs();
		}
        File f = new File(imgFilePath + bitName + ".png");
        f.createNewFile();
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
}

	private void idEnabled(boolean isE) {
		mUserPass.setEnabled(isE);
		mUserNickname.setEnabled(isE);
		mUserSex.setEnabled(isE);
		mUserAge.setEnabled(isE);
		//mUserHobby.setEnabled(isE);
		mUserEmail.setEnabled(isE);

	}
	 
    @Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		//按手机返回键则返回首页
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }

}
