package erb.unicomedu.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AppVo implements Serializable  {
	private String filesize;
	private String info;
	private String version;
	private String appurl;
	public String getFilesize() {
		return filesize;
	}
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppurl() {
		return appurl;
	}
	public void setAppurl(String appurl) {
		this.appurl = appurl;
	}
}
