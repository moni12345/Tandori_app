package adapter;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.util.Log;

import interfaces.GeneralCommunicator;
import parser.ServiceHandlerReturnString;

public class GlobalAsyncTask extends AsyncTask<String, Void, String> {

	Activity act;
	String URL;
	GeneralCommunicator com;
	@SuppressWarnings("deprecation")
	ArrayList<NameValuePair> arrlst;
	ProgressDialog dialog;
	
	public GlobalAsyncTask(Activity context , String URL, ArrayList<NameValuePair> arrlst ) {
		// TODO Auto-generated constructor stub
		act = context;
		com = (GeneralCommunicator) context;
		this.URL = URL;
		this.arrlst = arrlst;
	}
	public GlobalAsyncTask(Activity act){
		this.act = act;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog = new ProgressDialog(act);
		dialog.show();
		dialog.setCancelable(false);
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ServiceHandlerReturnString service = new ServiceHandlerReturnString();
		String str = service.makeHttpRequest(URL, arrlst, act);
		Log.i("TAG Global Async TASk", str);
		return str;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		if(result != null){
			com.urlStuff(result);
		}
	Log.i("TAG Global Async TASk", result);
	}



	
	

}
