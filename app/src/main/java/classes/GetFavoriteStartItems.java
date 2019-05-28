package classes;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import activity.BranchDetailActivity;
import parser.ServiceHandlerReturnString;

class GetFavoriteStartItems extends AsyncTask<Void, Void, Void> {

	ProgressDialog dialog;
	String URLResponce = "";
	String URL = "http://placeorderonline.com/aminah/webservices/GetFavorite.php";
	String customerID;
	String compositeKey;
	public static String[] ccKey; 
	 
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		// dialog = new ProgressDialog(BasketActivity);
		// dialog.setCancelable(false);
		// dialog.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		ServiceHandlerReturnString service = new ServiceHandlerReturnString();
		ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();

		arrlst.add(new BasicNameValuePair("bid",
				BranchDetailActivity.branch_id2));

		arrlst.add(new BasicNameValuePair("cid", customerID));

		Log.v("Branch ID id", BranchDetailActivity.branch_id2);

		try {
			URLResponce = service.makeHttpRequest(URL, arrlst);
			Log.v("Favorite Responce", URLResponce);
			JSONArray jsonarr = new JSONArray(URLResponce);
			if (jsonarr.length() > 0) {

				for (int i = 0; i < jsonarr.length(); i++) {

					JSONObject obj = jsonarr.getJSONObject(i);
					compositeKey = obj.getString("compositeKey");
					Log.e("ccccc" ,compositeKey+"");					
					ccKey[i] = compositeKey ;
					Log.e("array",ccKey.toString()+"");
				
				}
			}
		} catch (Exception e) {
			Log.i("Favorite Responce", e.toString());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		// if(GlobalClass.arrlstfavoritemodel.size() == 0){}else{

	}
}
