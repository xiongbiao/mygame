package erb.unicomedu.util;



public class Def {
	/** 记录当前APP激活的Activity对象，以便退出APP时finish掉  */
	//public static List<Activity> activityList = new ArrayList<Activity>();
	//public static final String APP_EXIT_BROADCAST_MSG = "unicomedu.AppExit";//退出APP的广播标识
	
	
	/*版本更新的路径*/
	public final static String DOWNLOAD_PATH 	= "/unicomedu/";
	public final static String APP_FILE_NAME	= "unicomedu.apk";
	public static String VERSION = "1.0";
	
	public final static String TOKEN_NAME = "xiongbiao";
	public final static String TOKEN_PASS = "xioangbiao";
	public final static String SIGN_DATA = "SIGN_DATA";
	public final static String SIGN_NUM = "SIGN_NUM";
	public final static String SID = "1";
	public final static String SO = "1";
	
	public final static String WHERE_KEY = "com.sin.object.key";
	public final static String OBJ = "erb.unicomedu.obj";
	/** 回复评论对象标记 */
	public final static String BBS_REPLY_PL = "erb.unicomedu.Reply2PingLun";
	
	public final static String OBJ_bbs_ask = "erb.unicomedu.obj.bbs.ask";
	public final static String OBJ_Bundle = "erb.unicomedu.obj.bundle";
	public final static String OBJ_bbs = "erb.unicomedu.obj.bbs";
	public final static int Teacher_KEY = 0;
	public final static int Video_KEY = 1;
	public final static int Read_KEY = 2;
	public final static int Exam_KEY = 3;
	public final static int Subject_KEY = 4;
	public final static int Intention_KEY = 5;
	public final static int KEY_NUM = 9;
	public final static int KEY_MAX_SIZE = 100;
	
	
	public final static String REG_RESULT_REPEAT = "Repeat" ; 
	
	public final static String PREFS_NAME = "erb.unicomedu"; 
	public final static String SP_NAV_NAME = "navJson"; 
	public final static String SP_MODEL_NAME = "modelJson"; 
	public final static String SP_Recommend_NAME = "RecommendJson"; 
	public final static String SP_USERINFO_JSON_OBJ = "UserInfoObjJson"; 
	public final static String  USER_LOGIN_RESULT = "NOUSER"; 
	public final static String TO_CLASS_NAME_TAG = "ClassName";
	public final static String FROM_CLASS_NAME_TAG = "FROM_ClassName";
	public final static String  FAV_OBJ_RESULT = "FAVED"; 
	public final static String  NO_OBJ_RESULT = "NO_JIEFEN"; 
	public final static String  FAV_OBJ_RESULT_OK = "FAVOK"; 
	public final static String READ_URL = HttpUrls.RemoteSERVER+"/book/view?bookid=%s &tonken= %s &memberid= %s";

	public final static String MSGINFO_URL = HttpUrls.RemoteSERVER+"/msg/view?msgid=%s&tonken=%s";
	
	public final static String EMAP_URL = HttpUrls.RemoteSERVER+"/location/map?locationid=%s&mylatitude=%s&mylongitude=%s&width=%s&height=%s";
//	public final static String EMAP_URL = HttpUrls.RemoteSERVER+"/location/map?locationid=%s&mylatitude=%s&mylongitude=%s";
	
//	public final static String USERID_FOR_NULL = "0";	//游客默认ID为0 
	public final static String ACCT_USERID = "userID";
	public final static String ACCT_USERPWD = "userPWD";
	public final static String ACCT_USERPWD_SAVE = "autoSave";
	public final static String ACCT_AUTOLOGIN = "autoLOGIN";
	public final static String ACCT_USERPWD_SAVE_KEYWORD = "900";
	public final static String ACCT_AUTOLOGIN_KEYWORD = "901";
	
	
	public  static String isSavePassword = "false"; 
	public  static String isAutoLogin = "false"; 
	public  static String UserPassword = "UserPassword"; 
	public  static String UserName = "UserName"; 
	
	
	
	
	public  static String MODEl_USER_INFO = "1001"; 
	public  static String MODEl_TE_LIST = "0201"; 
	public  static String MODEl_TE_INFO = "0202"; 
	public  static String MODEl_TE_FAV = "0203"; 
	public  static String MODEl_TE_PF = "0204"; 
	
	public  static String MODEl_SU_LIST = "0301"; 
	public  static String MODEl_SU_INFO = "0302"; 
	public  static String MODEl_SU_FAV = "0303"; 
	public  static String MODEl_SU_BM = "0304"; 
	
	public  static String MODEl_RE_LIST = "0401"; 
	public  static String MODEl_RE_INFO = "0402"; 
	public  static String MODEl_RE_FAV = "0403"; 
	public  static String MODEl_RE_P = "0404"; 
	
	public  static String MODEl_VI_LIST = "0501"; 
	public  static String MODEl_VI_INFO = "0502"; 
	public  static String MODEl_VI_FAV = "0503"; 
	public  static String MODEl_VI_P = "0504"; 
	
	public  static String MODEl_EX_LIST = "0601"; 
	public  static String MODEl_EX_INFO = "0602"; 
	public  static String MODEl_EX_FAV = "0603"; 
	public  static String MODEl_EX_P = "0604"; 
	
	public  static String MODEl_BBS_list = "0701"; 
	public  static String MODEl_BBS_ASK = "0702"; 
	public  static String MODEl_BBS_H_ASK = "0703"; 
	public  static String MODEl_BBS_MY = "0704"; 
	
	public  static String MODEl_FA_List = "0801"; 
	
	public static String MODEl_SIGN = "1101";
	public static String MODEl_SIGN_POST = "1102";

	
	
	public static int M_LIST_SIZE = 20;
	
	
	//错误提示
	
	public static String MSG_SERVICE_404 = "服务器资源存在";
	public static String MSG_SERVICE_500 = "服务器错误";
	public static String MSG_SERVICE_NO = "服务器 未知错误";
	public static String MSG_NO = "请求 未知错误";
	public static String MSG_Connect_NO = "网络异常";
	public static String MSG_YM_NO = "服务器解析失败 检查网络";
	public static String MSG_TimeOut = "请求超时";
	
	public static String getServiceMsg(int code){
		String msg = "";
		switch (code) {
		case 404:
			msg = MSG_SERVICE_404;
			break;
		case 500:
			msg = MSG_SERVICE_500;
			break;
		case 501:
			msg = MSG_Connect_NO;
			break;
		case 502:
			msg = MSG_YM_NO;
			break;
		case 503:
			msg = MSG_TimeOut;
			break;
		case -1:
			msg = MSG_NO;
			break;
		default:
			msg = MSG_SERVICE_NO;
			break;
		}
		return msg;
	}
	
	
	
	 
	
	
}
