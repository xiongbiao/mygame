package erb.unicomedu.dao;

import java.util.Map;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import erb.unicomedu.activity.MyApplication;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.TokenUtil;

public class PublicDao {
	/**
	 * def 通用 的
	 * @param param
	 */
	public  static void DefMap(Map<String, Object> param){
		param.put("token", TokenUtil.generateToken(Def.TOKEN_NAME,Def.TOKEN_PASS));
		param.put("sid", Def.SID);
		param.put("os", Def.SO);
		param.put("version", Def.VERSION);
		param.put("smis", MyApplication.mSIMCard.getImsi());
	}
	public  static void DefMapNoSmis(Map<String, Object> param){
		param.put("token", TokenUtil.generateToken(Def.TOKEN_NAME,Def.TOKEN_PASS));
		param.put("sid", Def.SID);
		param.put("os", Def.SO);
		param.put("version", Def.VERSION);
	}
	 
}
