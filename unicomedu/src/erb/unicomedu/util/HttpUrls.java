package erb.unicomedu.util;

public class HttpUrls { 
	public static final String RemoteSERVER = "http://edusrv.100le.cn:8080"; // 服务端IP

	
	public static final String RecommendSERVER = RemoteSERVER+"/recommend/search";  //获取推荐应用. 
	public static final String UpdateAppSERVER = RemoteSERVER+"/version/get";  //获取推荐应用. 
	
	public static final String TeacherSERVER = RemoteSERVER+"/teacher/search";  //教师列表
	public static final String TeacherClassListSERVER = RemoteSERVER+"/teacher/techclasslist";  //教授课程列表
	public static final String TeacherKEYSERVER = RemoteSERVER+"/teacher/keyword";  //教师搜索关键字
	public static final String TeacherFavoritesSERVER = RemoteSERVER+"/favorites/teacherlist";  //教师收藏
	public static final String TeacherAddFavoritesSERVER = RemoteSERVER+"/teacher/addfavorites";  //教师收藏、

	
	public static final String DeleteFavoritesSERVER = RemoteSERVER+"/favorites/deletefavorites";  //删除收藏
	
	public static final String SetstarSERVER = RemoteSERVER+"/teacher/setstar";  //评定星级
	
	public static final String VideoSERVER = RemoteSERVER+"/media/search";  //视频列表
	public static final String VideoFavoritesSERVER = RemoteSERVER+"/favorites/medialist";  //视频列表
	public static final String VideoKEYSERVER = RemoteSERVER+"/media/keyword";  //视频搜索关键字
	public static final String VideoOtherKEYSERVER = RemoteSERVER+"/media/linklist";  //视频相关
	public static final String VideoAddFavoritesSERVER = RemoteSERVER+"/media/addfavorites";  //视频收藏
	public static final String VideoBuySERVER = RemoteSERVER+"/media/buy";  //视频购买
	
	public static final String ReadOtherSERVER = RemoteSERVER+"/book/linklist";  //相关阅读列表
	public static final String ReadSERVER = RemoteSERVER+"/book/search";  //阅读列表
	public static final String  ReadFavoritesSERVER= RemoteSERVER+"/favorites/booklist";  //阅读列表
	public static final String ReadKEYSERVER = RemoteSERVER+"/book/keyword";  //阅读搜索关键字
	public static final String ReadAddFavoritesSERVER = RemoteSERVER+"/book/addfavorites";  //阅读收藏
	public static final String ReadBuySERVER = RemoteSERVER+"/book/buy";  //阅读购买
	
	public static final String SubjectFavoritesSERVER = RemoteSERVER+"/favorites/subjectlist";  //课程列表
	public static final String SubjectSERVER = RemoteSERVER+"/subject/search";  //课程列表
	public static final String SubjectKEYSERVER = RemoteSERVER+"/subject/keyword";  //课程列表
	public static final String SubjectAddFavoritesSERVER = RemoteSERVER+"/subject/addfavorites";  //课程收藏
	public static final String IntentionSERVER = RemoteSERVER+"/subject/intention";  //意向课程
	
	public static final String SubjectClassSERVER = RemoteSERVER+"/subject/classlist";  //班级列表
	public static final String SubjectSERVERTYPE = RemoteSERVER+"/subjecttype/search";  //课程导航数据列表
	public static final String OnlineRegistrationSERVERTYPE = RemoteSERVER+"/subject/signup";  //在线报名
	public static final String EduinsSERVER = RemoteSERVER+"/location/search";  //校区列表
	public static final String EduinsShowSERVER = RemoteSERVER+"/location/show";  //校区列表
	public static final String EduinsAreaSERVER = RemoteSERVER+"/location/districtlist";  //校区列表
	
	public static final String LoginSERVER = RemoteSERVER+"/member/login";  //登录user
	public static final String RegSERVER = RemoteSERVER+"/member/register";  //注册user
	public static final String ExistSERVER = RemoteSERVER+"/member/chklogname";  //ExistUser
	public static final String ModelpopedomSERVER = RemoteSERVER+"/modelpopedom/list";  //权限接口
	public static final String UpdateUserSERVER = RemoteSERVER+"/member/update";  //修改个人资料
	public static final String UpdateUserAvatarSERVER = RemoteSERVER+"/member/updatelogo";  //修改头像
	
	
	public static final String ExamSERVER = RemoteSERVER+"/exam/search";  //在线考试
	public static final String ExamKEYSERVER = RemoteSERVER+"/exam/keyword";  //在线考试搜索关键字
	public static final String QuestionSERVER = RemoteSERVER+"/exam/questionlist";  //在线考试
	public static final String ExamBuySERVER = RemoteSERVER+"/exam/buy";  //在线考试购买
	
	
	public static final String BbsFavoritesSERVER = RemoteSERVER+"/favorites/asklist";  //bbs专题列表
	public static final String BbsSERVER = RemoteSERVER+"/bbs/topiclist";  //bbs专题列表
	public static final String BbsMyThemeSERVER = RemoteSERVER+"/bbs/myasklist";  //bbs我的主题列表
	public static final String BbsAskSERVER = RemoteSERVER+"/bbs/asklist";  //bbs我的主题列表
	public static final String BbsAskShowSERVER = RemoteSERVER+"/bbs/show";  //bbs我的主题列表
	public static final String BbsCheckSERVER = RemoteSERVER+"/bbs/replylist";  //bbs问题回复列表
	public static final String BbsTakeAskSERVER = RemoteSERVER+"/bbs/ask";  //bbs发布问题
	public static final String BbsTakeReplySERVER = RemoteSERVER+"/bbs/reply";  //bbs发布回复
	public static final String BbsAddFavoritesSERVER = RemoteSERVER+"/bbs/addfavorites";  //bbs添加收藏
	
	
	public static final String SignRankingSERVER = RemoteSERVER+"/location/signuptoplist";  //签到排行榜
	public static final String OtherSignSERVER = RemoteSERVER+"/location/othersignuplist";  //附近签到
	public static final String MySignSERVER = RemoteSERVER+"/location/mysignuplist";  //我的签到
	public static final String SignSERVER = RemoteSERVER+"/location/signup";  //签到
	public static final String MyLocationSERVER = RemoteSERVER+"/location/mylocation";  //签到

	public static final String  FeedbackTypeSERVER = RemoteSERVER+"/feedback/typelist";  //反馈类型
	public static final String  FeedbackSERVER = RemoteSERVER+"/feedback/submit";  //反馈

	
	public static final String  MsgListSERVER = RemoteSERVER+"/msg/msglist";  //消息列表
	
	public static final String  PushConfigSERVER = RemoteSERVER+"/msg/puship";  //服务器地址
	public static final String  PushOKSERVER = RemoteSERVER+"/msg/recieve";  //上报信息
	public static final String  PushInfoSERVER = RemoteSERVER+"/msg/show";  //信息info
	
	
	
	
	
}
