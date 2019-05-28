package activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import adapter.PreviosOrderAdapter;
import util.GlobalClass;
import model.PreviosOrderBean;
import parser.FindusParser;
import parser.loginParser;
import session.SessionManager;
import util.ConnectionDetector;

public class PreviousOrderActivity extends Activity implements OnClickListener {

	String URL = "http://placeorderonline.com/aminah/webservices/PreviousOrder.php";

	SessionManager session;
	SharedPreferences pref;
	String Userid, userName;
	TextView tvOrderNow, tvOrderDate, tvName, tvAddress, tvCity, tvContact,
			tvOrderstatus, tvOrderType, tvTotalPrice;

	String totalPrice;
	String orderType;
	String paymentType;
	String orderDate;
	String orderID;
	String contact;
	String id;
	String city;
	String order_tracking;
	String address;
	String OrderStatus;
	String personname;

	View ActivityView;

	Button bLogin, bBack;
	ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
			imgBtnSettingsID, imgBtnHomeID;
	TextView tvbasketCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new previousOrderFragment()).commit();
		}
		InitMainUI();

		ActivityView = this.findViewById(android.R.id.content);
		GlobalClass.globalObj.showCircleCount(getApplicationContext(),
				ActivityView);

	}

	void InitMainUI() {

		tvbasketCount = (TextView) findViewById(R.id.basketCount);
		imgBtnBasketID = (ImageButton) findViewById(R.id.imgBtnBasketID);
		imgBtnFavouriteID = (ImageButton) findViewById(R.id.imgBtnFavouriteID);
		imgBtnOfferID = (ImageButton) findViewById(R.id.imgBtnOfferID);
		imgBtnSettingsID = (ImageButton) findViewById(R.id.imgBtnSettingsID);
		bLogin = (Button) findViewById(R.id.btnHome_DoneID);
		imgBtnHomeID = (ImageButton) findViewById(R.id.imgBtnHomeID);
		bBack = (Button) findViewById(R.id.btnHome_BackID);

		bBack.setOnClickListener(this);
		imgBtnBasketID.setOnClickListener(this);
		imgBtnFavouriteID.setOnClickListener(this);
		imgBtnOfferID.setOnClickListener(this);
		imgBtnSettingsID.setOnClickListener(this);
		bLogin.setOnClickListener(this);
		imgBtnHomeID.setOnClickListener(this);

		bLogin.setText("Profile");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnHome_DoneID:
			// loginDialog dialog = new loginDialog(PreviousOrderActivity.this);
			Intent profileIntent = new Intent(PreviousOrderActivity.this,
					ProfileActivity.class);
			startActivity(profileIntent);

			break;
		case R.id.imgBtnHomeID:
			Intent homeIntent = new Intent(PreviousOrderActivity.this,
					HomeActivity.class);
			startActivity(homeIntent);

			break;
		case R.id.imgBtnSettingsID:
			startActivity(new Intent(PreviousOrderActivity.this, SettingActivity.class));

			break;
		case R.id.imgBtnOfferID:
			startActivity(new Intent(PreviousOrderActivity.this, OffersActivity.class));

			break;

		case R.id.imgBtnBasketID:
			startActivity(new Intent(PreviousOrderActivity.this, BasketActivity.class));

			break;
		case R.id.imgBtnFavouriteID:
			startActivity(new Intent(PreviousOrderActivity.this,
					FavoriteActivity.class));

			break;
		case R.id.btnHome_BackID:
			finish();
			super.onBackPressed();
			break;
		default:
			break;
		}
	}

	class previousOrderFragment extends Fragment {

		ListView lv;
		Boolean isInternetPresent = false;
		ConnectionDetector cd;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.activity_previous_order,
					container, false);
			intiUI(view);
			session = new SessionManager(PreviousOrderActivity.this);
			pref = getSharedPreferences("fraddys", 0);
			if (GlobalClass.status == 1) {
				Userid = GlobalClass.customerId;
			} else {
				Userid = pref.getString(SessionManager.KEY_ID, "");
			}
			userName = pref.getString(SessionManager.FIRST_NAME, "");
			city = pref.getString(SessionManager.CITY, "");

			cd = new ConnectionDetector(getApplicationContext());
			isInternetPresent = cd.isConnectingToInternet();

			if (isInternetPresent) {
				// Internet Connection is Present
				// make HTTP requests
				new PreviosOrderAsyncTask().execute();
			} else {
				// Internet connection is not present
				// Ask user to connect to Internet
				ConnectionDetector.showAlertDialog(getActivity(),
						"No Internet Connection",
						"You don't have internet connection.", false);

			}

			return view;

		}

		private void intiUI(View v) {
			// TODO Auto-generated method stub

			lv = (ListView) v.findViewById(R.id.preList);
			tvOrderNow = (TextView) v.findViewById(R.id.tvOrderNow);
			tvOrderDate = (TextView) v.findViewById(R.id.tvOrderDate);
			tvName = (TextView) v.findViewById(R.id.tvCustomerName);
			tvAddress = (TextView) v.findViewById(R.id.tvAddress);
			tvCity = (TextView) v.findViewById(R.id.tvCity);
			tvContact = (TextView) v.findViewById(R.id.tvContact);
			tvOrderstatus = (TextView) v.findViewById(R.id.tvOrderStatus);
			tvOrderType = (TextView) v.findViewById(R.id.tvOrderType);
			tvTotalPrice = (TextView) v.findViewById(R.id.tvTotolPrice);

		}

		class PreviosOrderAsyncTask extends AsyncTask<Void, Void, Void> {
			ProgressDialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				dialog = new ProgressDialog(PreviousOrderActivity.this);
				dialog.show();
				dialog.setCancelable(false);

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				FindusParser parser = new FindusParser();
				ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();
				arrlst.add(new BasicNameValuePair("cid", Userid));
				arrlst.add(new BasicNameValuePair("bid",
						BranchDetailActivity.branch_id2));
				JSONArray jsonArr = parser.makeHttpRequest(URL, arrlst);
				Log.i("Array Previos Order", "" + jsonArr);
				for (int i = 0; i < jsonArr.length(); i++) {
					try {
						JSONObject jsonObj = jsonArr.getJSONObject(i);

						totalPrice = jsonObj.getString("totalprice");
						orderType = jsonObj.getString("orderType");
						
						String s = orderType;
						
						paymentType = jsonObj.getString("paymentType");
						orderDate = jsonObj.getString("orderDate");
						contact = jsonObj.getString("contact1") + " / "
								+ jsonObj.getString("contact2");
						id = jsonObj.getString("id");
						address = jsonObj.getString("address1") + " / "
								+ jsonObj.getString("address1");
						city = jsonObj.getString("town_city");
						OrderStatus = jsonObj.getString("orderStatus");
						order_tracking = jsonObj.getString("orderTracking");
						personname = jsonObj.getString("name");

						Log.v("Status true", totalPrice + " " + orderType + " "
								+ paymentType + " " + orderDate + " " + contact
								+ " " + id + " " + city + " " + order_tracking);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return null;
			}

			// PreviousOrderDetails.php

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				dialog.dismiss();

				tvTotalPrice.setText(totalPrice);
				tvOrderNow.setText(id);
				tvOrderDate.setText(orderDate);
				tvCity.setText(city);
				tvContact.setText(contact);
				tvAddress.setText(address);
				tvName.setText(personname);
				tvOrderstatus.setText(OrderStatus);
				tvCity.setText(city);

				if (orderType.equals("d")) {
					tvOrderType.setText("Devlivery");
				}else if(orderType.equals("c")){
					tvOrderType.setText("Collection");
				}

				new PreviousOrderDetailAsyncTask().execute();

			}

		}

		class PreviousOrderDetailAsyncTask extends AsyncTask<Void, Void, Void> {

			List<PreviosOrderBean> previosarrlst = new ArrayList<PreviosOrderBean>();
			String previosOrderURL = "http://placeorderonline.com//aminah/webservices/PreviousOrderDetails.php";

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				loginParser parser = new loginParser();
				ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();
				arrlst.add(new BasicNameValuePair("oid", id));
				try {
					JSONArray arr = parser.makeHttpRequest(previosOrderURL,
							arrlst);

					for (int i = 0; i < arr.length(); i++) {

						JSONObject obj = arr.getJSONObject(i);
						PreviosOrderBean bean = new PreviosOrderBean();

						String item = obj.getString("Name");
						String itemPrice = obj.getString("unitPrice");
						String itemqty = obj.getString("qty");
						String desc = obj.getString("description");

						bean.setName(item);
						bean.setPrice(itemPrice);
						bean.setQty(itemqty);
						bean.setDesc(desc);

						previosarrlst.add(bean);
						int size = previosarrlst.size();

					}
					Log.i("Detail", "" + arr);
				} catch (Exception e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				lv.setAdapter(new PreviosOrderAdapter(getActivity(),
						previosarrlst));
			}

		}

	}

}
