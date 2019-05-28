package activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import adapter.BranchesAdapter;
import model.BranchesBean;
import parser.BranchListParser;
import util.Application;
import util.ConnectionDetector;

public class BranchDetailActivity extends Activity {

	String postCode;
	ProgressDialog dialog;
	TextView tvShowPostalCode;

	List<BranchesBean> branchesArraylist;
	BranchesBean bean;
	String URL ="http://placeorderonline.com/aminah/webservices/sortedBranches.php";
	BranchListParser objbranchList;
	public static String branch_id2 = "2";

	// "http://vdesignandprint.co.uk/webservices/RetriveBranch.php";

	ListView list;
	BranchesBean brach_id;
	
	 Boolean isInternetPresent = false;
	  ConnectionDetector cd;


	public BranchDetailActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.branchdetail_activity);

		InitUI();

		Bundle b = getIntent().getExtras();
		if (b != null) {
			postCode = b.getString("PostalCode");
			// getLongitudeLatitudeFromPostCode(postCode);
			tvShowPostalCode.setText(postCode);
			// Toast.makeText(getApplicationContext(), postCode, 0).show();

		}
		cd = new ConnectionDetector(getApplicationContext());
		  isInternetPresent = cd.isConnectingToInternet();
		  
		  if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
			  new BranchAsyncTask().execute();
           
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
           ConnectionDetector.showAlertDialog(BranchDetailActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

		// Intent intent = getIntent().getExtras().getString("");
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					brach_id = branchesArraylist.get(position);
					branch_id2 = brach_id.getTotlalID();

					startActivity(new Intent(BranchDetailActivity.this,
							HomeActivity.class));
					// putExtra("id", branch_id2));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/*public Bitmap convertImage(String imageURL) throws IOException {
		URL url = new URL(imageURL);
		InputStream is = url.openConnection().getInputStream();
		Bitmap bitMap = BitmapFactory.decodeStream(is);
		return bitMap;
	}*/

	private void InitUI() {
		// TODO Auto-generated method stub
		list = (ListView) findViewById(R.id.listViewBranch);
		tvShowPostalCode = (TextView) findViewById(R.id.tvShowPostalCode);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		new BranchAsyncTask().isCancelled();
	}

	class BranchAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(BranchDetailActivity.this);
			dialog.setCancelable(false);
			dialog.show();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			objbranchList = new BranchListParser();
			// ServiceHandlerReturnObject service= new
			// ServiceHandlerReturnObject();
			List<NameValuePair> nameValuePair = new ArrayList<>();

			nameValuePair.add(new BasicNameValuePair("appID",
					Application.Application_ID));
			nameValuePair.add(new BasicNameValuePair("postCode", postCode));


			JSONArray jsonarr = objbranchList.makeHttpRequest(URL,
					nameValuePair);

			branchesArraylist = new ArrayList<>();

			Log.i("Object", "" + jsonarr);

			for (int i = 0; i < jsonarr.length(); i++) {
				try {
					JSONObject jsonObj = jsonarr.getJSONObject(i);

					Log.v("TAG", "" + jsonObj);
					bean = new BranchesBean();
					String id = jsonObj.getString("bid");
					String branches = jsonObj.getString("name");
					String address = jsonObj.getString("address1")
							+ jsonObj.getString("address2")
							+ jsonObj.getString("cityTown") +" "+ jsonObj.getString("postcode");
					// bean.setIcon(jsonObj.getString("blogo"));
				//	String icon = jsonObj.getString("branchLogo");
					String postcode = jsonObj.getString("postcode");
					double distance = jsonObj.getDouble("Distance");
					String ImageURL = jsonObj.getString("branchLogo");
					Log.v("Distance", "" + distance);

					bean.setTotlalID(id);
					bean.setBranches(branches);
					bean.setAddress(address);
					bean.setPostcode(postcode);
					bean.setDistance(distance);
					bean.setImageURL(ImageURL);

					branchesArraylist.add(bean);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if(branchesArraylist.size() != 0){
			BranchesAdapter adapter = new BranchesAdapter(
					BranchDetailActivity.this, branchesArraylist);
			list.setAdapter(adapter);
			}
		}

	}

}
