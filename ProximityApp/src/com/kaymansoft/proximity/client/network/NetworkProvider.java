package com.kaymansoft.proximity.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.__HttpMultipartMode;
import org.apache.http.entity.mime.__MultipartEntity;
import org.apache.http.entity.mime.content.__InputStreamBody;
import org.apache.http.entity.mime.content.__StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public abstract class NetworkProvider {
	private HttpClient httpClient;
	private HttpParams httpParams;

	// TODO inject
	protected String uri = "http://10.0.2.2:8080/proximity";

	protected class PostFormBuilder {
		__MultipartEntity multipartEntity;

		private PostFormBuilder(__MultipartEntity multipartEntity) {
			this.multipartEntity = multipartEntity;
		}

		protected PostFormBuilder addParam(String name, String... values)
				throws UnsupportedEncodingException {
			for (int i = 0; i < values.length; i++) {
				this.multipartEntity.addPart(name, new __StringBody(values[i]));
			}
			return this;
		}

		protected PostFormBuilder addParam(String name, int... values)
				throws UnsupportedEncodingException {
			for (int i = 0; i < values.length; i++) {
				this.multipartEntity.addPart(name,
						new __StringBody(Integer.toString(values[i])));
			}
			return this;
		}

		protected PostFormBuilder addParam(String name, double... values)
				throws UnsupportedEncodingException {
			for (int i = 0; i < values.length; i++) {
				this.multipartEntity.addPart(name,
						new __StringBody(Double.toString(values[i])));
			}
			return this;
		}

		protected PostFormBuilder addParam(String name, long... values)
				throws UnsupportedEncodingException {
			for (int i = 0; i < values.length; i++) {
				this.multipartEntity.addPart(name,
						new __StringBody(Long.toString(values[i])));
			}
			return this;
		}

		protected PostFormBuilder addParam(String name,
				InputStream... inputStreams) {
			for (int i = 0; i < inputStreams.length; i++) {
				this.multipartEntity.addPart(name, new __InputStreamBody(
						inputStreams[i], "image" + i));
			}
			return this;
		}

		protected HttpResponse getResponse(String uri) throws IOException,
				ClientProtocolException {
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setEntity(multipartEntity);
			return httpClient.execute(httpPost);
		}
	}

	protected HttpResponse MakeGETRequest(String uri) throws IOException,
			ClientProtocolException {
		HttpGet httpGet = new HttpGet(uri);
		return this.httpClient.execute(httpGet);
	}

	public NetworkProvider(String url) {
		uri = url;
		httpParams = new BasicHttpParams(); // TODO set Cookie policy, etc
		httpClient = new DefaultHttpClient(httpParams);
	}

	protected PostFormBuilder createPostForm() {
		return new PostFormBuilder(new __MultipartEntity(
				__HttpMultipartMode.BROWSER_COMPATIBLE));
	}
	
	protected void ensureProperResponse(HttpResponse response) throws Exception{
		int code = response.getStatusLine().getStatusCode();
		if(code < 200 || code > 299){
			throw new Exception("Network error: " + response.getStatusLine().toString());
		}
	}
}
