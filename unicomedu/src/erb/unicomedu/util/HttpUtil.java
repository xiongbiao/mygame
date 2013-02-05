package erb.unicomedu.util;

/**
 * http 请求常用类
 * @author Administrator
 *
 */

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {

	public static final String REQUEST_METHOD_POST = "POST";
	public static final String REQUEST_METHOD_GET = "GET";
	public static final String D_CHARSET = "UTF-8";

	/**
	 * 创建服务端 交互 并 返回响应
	 * 
	 * @param form
	 *            参数 格式 "?xx=x1&bb=b1"
	 * @param toUrl
	 *            访问 地址
	 * @param method
	 *            访问方式 默认POST
	 * @param charset
	 *            编码方式 默认UTF-8
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getHttpURLConnection(String form,
			String toUrl, String method, String charset) throws Exception {

		URL url = new URL(toUrl);
		charset = charset.equals("") ? D_CHARSET : charset;
		byte[] data = form.getBytes(charset);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		try {
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(8000);
			conn.setUseCaches(false);
			conn.setDoOutput(true);
			conn.setRequestMethod(method.equals("") ? REQUEST_METHOD_POST
					: REQUEST_METHOD_GET);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", charset);
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			DataOutputStream outStream = new DataOutputStream(
					conn.getOutputStream());
			outStream.write(data);
			outStream.flush();
		} catch (ConnectException e) {
			e.getClass();
		}
		return conn;
	}

	/**
	 * 文件传送
	 * 
	 * @param form
	 * @param toUrl
	 * @param method
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static HttpURLConnection getHttpURLConnectionFromData(String form,
			String toUrl, String method, String charset) throws Exception {
		String boundary = "*****";
		URL url = new URL(toUrl);
		charset = charset.equals("") ? D_CHARSET : charset;
		byte[] data = form.getBytes(charset);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(20000);
		conn.setUseCaches(false);
		conn.setDoOutput(true);
		conn.setRequestMethod(method.equals("") ? REQUEST_METHOD_POST
				: REQUEST_METHOD_GET);
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", charset);
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(data);
		outStream.flush();
		return conn;
	}

	 
	
	
	public static HttpURLConnection postFiles(String actionUrl, Map<String, Object> params, Map<String, File> files)  throws  Exception {
		String boundary = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		try {
			URL url = new URL(actionUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
			DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
			/**
			 *params
			 */
			StringBuilder strParams = new StringBuilder();
			StringBuilder strFiles ;
		    if(params!=null){
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					strParams.append(PREFIX);
					strParams.append(boundary);
					strParams.append(LINEND);
					strParams.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\""+LINEND+LINEND);
					strParams.append(entry.getValue());
					strParams.append(LINEND);
				}
				outStream.write(strParams.toString().getBytes());
			}
		    /**
		     * files
		     */
		    if(files!=null){
		    	for (Map.Entry<String, File> file : files.entrySet()) {
		    		strFiles = new StringBuilder();
		    		strFiles.append(PREFIX);
		    		strFiles.append(boundary);
		    		strFiles.append(LINEND);
		    		strFiles.append("Content-Disposition: form-data; name=\""+file.getKey()+"\"; filename=\""
							+ file.getValue().getName() 
							+ "\""
							+ LINEND+"Content-Type: image/jpeg" + LINEND + 1 + "\"" + LINEND);
		    		strFiles.append(LINEND);
					byte[] content = readFile(file.getValue()+"");
					outStream.write(strFiles.toString().getBytes());
					outStream.write(content, 0, content.length);
					outStream.write(LINEND.getBytes());
		    	}
		    }

			byte[] end_data = (PREFIX + boundary +PREFIX+LINEND).getBytes();// 数据结束标志
			outStream.write(end_data);
			outStream.flush();
           return conn;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public static byte[] readFile(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	/**
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String inputStreamString(InputStream in) throws IOException {
		StringBuffer sb = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(in, D_CHARSET);
		char[] buff = new char[1024];
		int len;
		while ((len = reader.read(buff)) > 0) {
			sb.append(buff, 0, len);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public static String MapToParam(Map<String, Object> param) {
		StringBuffer paramStrB = new StringBuffer();
		if (param == null)
			return "";
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			paramStrB.append(entry.getKey() + "=" + entry.getValue() + "&");
		}
		String paramStr = paramStrB.toString();
		if ("".equals(paramStr))
			return "";
		paramStr = paramStr.substring(0, paramStr.length() - 1);
		return paramStr;
	}

}
