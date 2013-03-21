package erb.unicomedu.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import erb.unicomedu.util.Def;
import erb.unicomedu.util.EuException;
import erb.unicomedu.util.LogUtil;

public final class HttpPostDataTask extends AsyncTask<Void, Void, Boolean> {
	private static String TAG = LogUtil.makeLogTag(HttpPostDataTask.class);
	private final AsyncTaskCompleteListener<String> callback;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";
	private List<NameValuePair> valuePairs;
	private final String URL_TO_CALL;
	private String responseString;

	public HttpPostDataTask( List<NameValuePair> values,String api_url,
			AsyncTaskCompleteListener<String> asyncTaskCompleteListener) {
		LogUtil.i(TAG, "Calling URL:> " + api_url);
		this.valuePairs = values;
		this.URL_TO_CALL = api_url;
		this.callback = asyncTaskCompleteListener;
	}

	protected synchronized Boolean doInBackground(Void[] params) {
		try {
			LogUtil.d(TAG, "Calling url : " + this.URL_TO_CALL);
			LogUtil.d(TAG, "params Values: " + valuePairs);
			HttpPost httpPost = new HttpPost(this.URL_TO_CALL);
			httpPost.setEntity(new UrlEncodedFormEntity(this.valuePairs,"UTF-8"));

			BasicHttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 7000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			int timeoutSocket = 7000;

			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(HttpRequest request, HttpContext context) {
					if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
						request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
					}
				}
			});
			httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
				public void process(HttpResponse response, HttpContext context) {
					HttpEntity entity = response.getEntity();
					Header encoding = entity.getContentEncoding();
					if (encoding != null) {
						HeaderElement[] arrayOfHeaderElement;
						int j = (arrayOfHeaderElement = encoding.getElements()).length;
						int i = 0;

						for (; i < j; i++) {
							HeaderElement element = arrayOfHeaderElement[i];
							if (!element.getName().equalsIgnoreCase(
									ENCODING_GZIP)) {
								continue;
							}
							response.setEntity(new HttpPostDataTask.InflatingEntity(
									response.getEntity()));
							break;
						}
					}
				}
			});
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpClient.execute(httpPost);
			LogUtil.i(TAG, "Status Code: " + httpResponse.getStatusLine().getStatusCode());
			int code =  httpResponse.getStatusLine().getStatusCode();
	            if(code == 200){   
				this.responseString = EntityUtils.toString( httpResponse.getEntity(), "UTF-8");
				LogUtil.i(TAG, "Response String:" + this.responseString);
				if ((this.responseString != null) && (!this.responseString.equals(""))) {
					return Boolean.TRUE;
				}
			}
			else {
				LogUtil.d(TAG, "code : " + code);
				throw new EuException(Def.getServiceMsg(code));
			}
			
//		} catch (SocketTimeoutException e) {
//			LogUtil.e(TAG, "SocketTimeoutException Thrown" + e.toString());
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			LogUtil.e(TAG, "ClientProtocolException Thrown" + e.toString());
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			LogUtil.e(TAG, "MalformedURLException Thrown" + e.toString());
//			e.printStackTrace();
//		} catch (IOException e) {
//			LogUtil.e(TAG, "IOException Thrown" + e.toString());
//			e.printStackTrace();
		} catch (Exception e) {
			try {
				throw new EuException(e);
			} catch (EuException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return Boolean.FALSE;
	}

	protected synchronized void onPostExecute(Boolean result) {
		try {
			if (result.booleanValue()) {
				this.callback.onTaskComplete(this.responseString);
			} else {
				this.callback.onTaskComplete(this.responseString);
				LogUtil.e(TAG, "Call Failed due to Network error. ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class InflatingEntity extends HttpEntityWrapper {

		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		public InputStream getContent() throws IOException {
			return new GZIPInputStream(this.wrappedEntity.getContent());
		}

		public long getContentLength() {
			return -1L;
		}

	}

}
