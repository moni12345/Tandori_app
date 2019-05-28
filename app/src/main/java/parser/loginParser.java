package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class loginParser {

	// new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") +
	// 1));
	String TAG = "com.aib.freddychickenandpizza.HomeParser";

	// Response from the HTTP Request
	static InputStream httpResponseStream = null;
	// JSON Response String to create JSON Object
	static String jsonString = "";
	JSONArray jarr;
	JSONObject jObj;
	//fname , 

	// Method to issue HTTP request, parse JSON result and return JSON Object
	public JSONArray makeHttpRequest(String url, List<NameValuePair> params) {

		@SuppressWarnings("deprecation")
		DefaultHttpClient httpClient = new DefaultHttpClient();
		@SuppressWarnings("deprecation")
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse responce = httpClient.execute(post);
			HttpEntity httpEntity = responce.getEntity();
			httpResponseStream = httpEntity.getContent();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			// Create buffered reader for the httpResponceStream
			BufferedReader httpResponseReader = new BufferedReader(
					new InputStreamReader(httpResponseStream, "iso-8859-1"), 8);
			// String to hold current line from httpResponseReader
			String line = null;
			// Clear jsonString
			jsonString = "";
			// While there is still more response to read
			while ((line = httpResponseReader.readLine()) != null) {
				// Add line to jsonString
				jsonString += (line + "\n");
			}
			// Close Response Stream
			httpResponseStream.close();
		} catch (Exception e){
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		try {
			Log.i("String Data", jsonString);
			JSONArray jarr = new JSONArray(jsonString);
			return jarr;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Log.v(TAG, jsonString);
		return new JSONArray();
		

	}
}