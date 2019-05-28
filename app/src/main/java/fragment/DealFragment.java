package fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import activity.DealOfferItemActivity;
import model.AddItemBean;
import model.DealOffersBean;
import parser.DealOffersParser;
import util.ConnectionDetector;


public class DealFragment extends Fragment implements
		View.OnClickListener {

	ListView list;
	String URL = "http://placeorderonline.com/almarrakech/webservices/GetOffersMenu.php";
	String branch_id;
	ArrayList<DealOffersBean> beanArrlist;
	ImageView ivAdd;
	TextView tvOfferName, tvOfferPrice, tvOfferDesc;

	AddItemBean addItembean;
	String offerID;
	String Error_msg;
	
	 Boolean isInternetPresent = false;
	  ConnectionDetector cd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.deal_fragment, container, false);
		addItembean = new AddItemBean();
		Bundle b = getArguments();
		if (b != null) {
			branch_id = b.getString("branch_id");
		}
		InitUI(view);
		
		cd = new ConnectionDetector(getActivity());
		  isInternetPresent = cd.isConnectingToInternet();
		  
		  if (isInternetPresent) {
          // Internet Connection is Present
          // make HTTP requests
			  //calling Services here....!!
			  new GetOffersAsyncTask().execute();
         
      } else {
          // Internet connection is not present
          // Ask user to connect to Internet
         ConnectionDetector.showAlertDialog(getActivity(), "No Internet Connection",
                  "You don't have internet connection.", false);
      }
		
		
		
		

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View adapterview,
					int position, long id) {
				// TODO Auto-generated method stub
				InitAdapterUI(adapterview);
				offerID = beanArrlist.get(position).getOfferID();
			}

		});
		return view;

	}

	private void InitAdapterUI(View adapterview) {
		// TODO Auto-generated method stub
		ivAdd = (ImageView) adapterview.findViewById(R.id.ivAddItem);
		tvOfferName = (TextView) adapterview.findViewById(R.id.tvOfferName);
		tvOfferDesc = (TextView) adapterview.findViewById(R.id.tvOfferDesc);
		tvOfferPrice = (TextView) adapterview.findViewById(R.id.tvOfferPrice);
		ivAdd.setOnClickListener(this);
	}

	private void InitUI(View v) {
		// TODO Auto-generated method stub
		list = (ListView) v.findViewById(R.id.dealListview);
	}

	public Bitmap convertImage(String imageURL) throws IOException {
		URL url = new URL(imageURL);
		InputStream is = url.openConnection().getInputStream();
		Bitmap bitMap = BitmapFactory.decodeStream(is);
		return bitMap;
	}

	class GetOffersAsyncTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.show();
			pDialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			ArrayList<NameValuePair> arrlist = new ArrayList<NameValuePair>();
			arrlist.add(new BasicNameValuePair("bid", branch_id));

			DealOffersParser parser = new DealOffersParser();
			JSONArray jsonArr = parser.makeHttpRequest(URL, arrlist);
			if(jsonArr.length() > 0){
			//	Log.v("Length", ""+jsonArr.length());
			beanArrlist = new ArrayList<DealOffersBean>();
			for (int i = 0; i < jsonArr.length(); i++) {

				try {
					JSONObject obj = jsonArr.getJSONObject(i);
					DealOffersBean bean = new DealOffersBean();

					bean.setOfferID(obj.getString("offerID"));
					bean.setOfferName(obj.getString("offerName"));
					bean.setOfferDesc(obj.getString("offerDesc"));
					bean.setOfferPrice(obj.getString("offerPrice"));

					String image = obj.getString("offerPicURL");
					// bean.setOfferspicURL(obj.getString("offerPicURL"));
					String URL = image.replace("\\", "");
					Log.v("Image URL", URL);
					// Bitmap bitmap = convertImage("http://"+URL);
					bean.setOfferspicURL("http://" + URL);

					beanArrlist.add(bean);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			}else{
				Error_msg =  "Connection Error";
				//Toast.makeText(getActivity(), "Connection Error", 0).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			pDialog.dismiss();
//			if(Error_msg.equals("Connection Error")){
//				Toast.makeText(getActivity(), Error_msg, 0).show();
//				
//			}else{
			//	list.setAdapter(new DealOffersAdapter(getActivity(), beanArrlist,Act));
		//	}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.ivAddItem:
			//gettingDataFromAdapter();
			Intent i = new Intent(this.getActivity(), DealOfferItemActivity.class);
			i.putExtra("branch_ID", branch_id);
			i.putExtra("itemID", offerID);
			i.putExtra("branchName", "");
			startActivity(i);
			break;

		default:
			break;
		}
	}

	private void gettingDataFromAdapter() {
		// TODO Auto-generated method stub
		addItembean.setItemName(tvOfferName.getText().toString());
		addItembean.setItemPrice(tvOfferPrice.getText().toString());
		addItembean.setItemDesc(tvOfferDesc.getText().toString());
	}

}
