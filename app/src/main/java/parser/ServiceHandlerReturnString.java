package parser;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import dialog.AlertDialogManager;

public class ServiceHandlerReturnString {
    // Response from the HTTP Request
    static InputStream httpResponseStream = null;
    // JSON Response String to create JSON Object
    static String jsonString = "";
    String TAG = "com.aib.freddychickenandpizza.ServiceHandlerReturnObject";
    JSONArray jarr;
    JSONObject jObj;


    public String makeHttpRequest(String url, List<NameValuePair> params, Activity act) {
        @SuppressWarnings("deprecation")
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse responce = httpClient.execute(post);
            HttpEntity httpEntity = responce.getEntity();
            httpResponseStream = httpEntity.getContent();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            Log.v("Connection1", e1.toString());
            AlertDialogManager.showAlertDialog(act, "Please Check your Internet Connection...!!");
        } finally {
            //	AlertDialogManager.showAlertDialog(act, "Please Check your Internet Connection...!!");
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

//		Log.d("TAG String", jsonString);
            httpResponseStream.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        return jsonString;

    }


    @SuppressWarnings("deprecation")

    public String makeHttpRequest(String url, List<NameValuePair> params) {

        @SuppressWarnings("deprecation")
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse responce = httpClient.execute(post);
            HttpEntity httpEntity = responce.getEntity();
            httpResponseStream = httpEntity.getContent();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            Log.v("Connection1", e1.toString());
            //	AlertDialogManager dialog = new AlertDialogManager();
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
            Log.d("TAG String", jsonString);
            httpResponseStream.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        return jsonString;
    }
}
