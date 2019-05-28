package activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import adapter.Favorite_Adapter;
import util.GlobalClass;
import model.FavoriteModel;
import parser.ServiceHandlerReturnString;
import parser.loginParser;
import session.SessionManager;
import util.ConnectionDetector;


public class FavoriteActivity extends Activity implements OnClickListener {

	Button bLogin, bBack;
	ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
			imgBtnSettingsID, imgBtnHomeID;
	TextView tvbasketCount;

	public static List<String> listFav;

	public static View ActivityView;
	public static ListView list;

	public static Context conext;

	public FavoriteActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();

			InitMainUI();

			bLogin.setText(String.format("%s","Profile"));

			ActivityView = this.findViewById(android.R.id.content);
			GlobalClass.globalObj.showCircleCount(getApplicationContext(),
					ActivityView);
		}
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

		listFav = new ArrayList<>();

		bBack.setOnClickListener(this);
		imgBtnBasketID.setOnClickListener(this);
		imgBtnFavouriteID.setOnClickListener(this);
		imgBtnOfferID.setOnClickListener(this);
		imgBtnSettingsID.setOnClickListener(this);
		bLogin.setOnClickListener(this);
		imgBtnHomeID.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnHome_DoneID:
			startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class));
			break;
		case R.id.imgBtnHomeID:
			Intent homeIntent = new Intent(FavoriteActivity.this,
					HomeActivity.class);
			homeIntent.putExtra("status", true);
			startActivity(homeIntent);

			break;
		case R.id.imgBtnSettingsID:
			startActivity(new Intent(FavoriteActivity.this,
					SettingActivity.class));

			break;
		case R.id.imgBtnOfferID:
			startActivity(new Intent(FavoriteActivity.this,
					OffersActivity.class));

		case R.id.imgBtnBasketID:
			startActivity(new Intent(FavoriteActivity.this,
					BasketActivity.class));
			break;
		case R.id.imgBtnFavouriteID:

			break;
		case R.id.btnHome_BackID:
			finish();
			super.onBackPressed();
			break;
		default:
			break;
		}
	}

	public static ArrayList<FavoriteModel> arrlstFavorItem = new ArrayList<>();

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnClickListener {

		// ArrayList<String> arrayList = new ArrayList<String>();
		// public static ListView listAdapter;
		String URL = "http://placeorderonline.com//webservices/GetFavorite.php";

		SharedPreferences pref;
		SessionManager session;
		String customerID;

		String itemName;
		String itemPrice;
		String itemid;
		String itemType;
		String itemDesc;

		ImageView bstar;

		Boolean isInternetPresent = false;
		ConnectionDetector cd;

		FavoriteAsyncTask favTask;

		// ArrayList<FavoriteModel> arrlstFavorItem = new
		// ArrayList<FavoriteModel>();

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			arrlstFavorItem.clear();
			conext = getActivity();
			View rootView = inflater.inflate(
					R.layout.fragment_activity_favorite, container, false);
			list = (ListView) rootView.findViewById(R.id.favlistView);

			session = new SessionManager(getActivity());

			pref = getActivity().getSharedPreferences("fraddys", 0);

			customerID = pref.getString(SessionManager.KEY_ID, "");

			if (customerID.length() == 0) {
				customerID = GlobalClass.customerId;
				Log.v("Customer IDI Global", customerID);
			}
			Log.v("Customer IDI", customerID);
			// if(!GlobalClass.customerId.equals(null)){
			//
			//
			// Log.v("Customer IDI Global", customerID);
			// }

			// if (GlobalClass.arrlstfavoritemodel.size() > 0) {
			// for (int i = 0; i < GlobalClass.arrlstfavoritemodel.size(); i++)
			// {
			//
			// GlobalClass.arrlstfavoritemodel.remove(i);
			// }
			// }

			cd = new ConnectionDetector(getActivity());
			isInternetPresent = cd.isConnectingToInternet();

			if (isInternetPresent) {
				// Internet Connection is Present
				// make HTTP requests
				favTask = new FavoriteAsyncTask();
				try {
					favTask.execute();
				} catch (Exception e) {
                    e.printStackTrace();
				}
				
			} else {
				// Internet connection is not present
				// Ask user to connect to Internet
				ConnectionDetector.showAlertDialog(getActivity(),
						"No Internet Connection",
						"You don't have internet connection.", false);
			}

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					intiAdapterUI(view);
				}

			});

			return rootView;
		}

		private void intiAdapterUI(View view) {
			// TODO Auto-generated method stub

			bstar = (ImageView) view.findViewById(R.id.Btnremovefav);

			bstar.setOnClickListener(this);
		}

		class FavoriteAsyncTask extends AsyncTask<Void, Void, Void> {

			ProgressDialog dialog;
			String URLResponce = "";

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				dialog = new ProgressDialog(getActivity());
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				ServiceHandlerReturnString service = new ServiceHandlerReturnString();
				ArrayList<NameValuePair> arrlst = new ArrayList<>();

				arrlst.add(new BasicNameValuePair("bid",
						BranchDetailActivity.branch_id2));
				arrlst.add(new BasicNameValuePair("cid", customerID));

				Log.e("Customer Id" , customerID +"-");
				Log.e("Branch ID id", BranchDetailActivity.branch_id2 +"-");


				try {
					URLResponce = service.makeHttpRequest(URL, arrlst);
					Log.v("Favorite Responce", URLResponce +"-");
					JSONArray jsonarr = new JSONArray(URLResponce);
					if (jsonarr.length() > 0) {

						for (int i = 0; i < jsonarr.length(); i++) {

							JSONObject obj = jsonarr.getJSONObject(i);
							FavoriteModel bean = new FavoriteModel();
							String compositeKey = obj.getString("compositeKey");

							String name = obj.getString("name");
							String desc = obj.getString("desc");
							String price = obj.getString("price");
							String itemDealId = obj.getString("item_deal_id");
							String id = obj.getString("i_d");
							String customer_id = obj.getString("customer_id");
							String branch_id = obj.getString("branch_id");

							bean.setBranch_id(branch_id);
							bean.setCompositeKey(compositeKey);
							bean.setName(name);
							bean.setDesc(desc);
							bean.setPrice(price);
							bean.setItemDealId(itemDealId);
							bean.setId(id);
							bean.setCustomer_id(customer_id);

							Log.e("abc", " " + compositeKey);

							listFav.add(compositeKey + "");

							arrlstFavorItem.add(bean);

						}
					}
					Log.v("Favorite Responce", URLResponce);
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

				if (!(arrlstFavorItem.get(0).getName().equals(""))
						&& !(arrlstFavorItem.get(0).getPrice().equals("0"))) {

					Favorite_Adapter adapter = new Favorite_Adapter(
							getActivity(), arrlstFavorItem, ActivityView);

					list.setAdapter(adapter);

				}
			}
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.Btnremovefav:
				try {

					int i = (Integer) bstar.getTag();

					itemName = arrlstFavorItem.get(i).getName();
					itemPrice = arrlstFavorItem.get(i).getPrice();
					itemid = arrlstFavorItem.get(i).getItemDealId();
					itemType = arrlstFavorItem.get(i).getId();

					itemDesc = arrlstFavorItem.get(i).getDesc();

					itemName = itemName.replace("\"", "\\");
					itemName = itemName.replace("\'", "\\'");

					itemDesc = itemDesc.replace("\'", "\\'");
					itemDesc = itemDesc.replace("\"", "\\");

					Log.i("FAV LIST SIZE", "" + arrlstFavorItem.size());

					arrlstFavorItem.remove(i);
					try {

						listFav.remove(i);
					} catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
					}

					new favAsyncTask().execute();

					GlobalClass.globalBasketList.get(i).setItemWine("notfav");

				} catch (IndexOutOfBoundsException e) {
					//
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}

			default:
				break;
			}
		}

		class favAsyncTask extends AsyncTask<Void, Void, String> {

			String URL = "http://placeorderonline.com/aminah/webservices/SetFavoriteStatus.php";

			ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				progressDialog = ProgressDialog.show(getActivity(),
						"Processing", "Favorit Item is Being Removed..", true);

			}

			@SuppressWarnings("deprecation")
			@Override
			protected String doInBackground(Void... request21) {
				// TODO Auto-generated method stub
				loginParser parser = new loginParser();

				ArrayList<NameValuePair> arrlst = new ArrayList<>();

				arrlst.add(new BasicNameValuePair("bid",
						BranchDetailActivity.branch_id2));

				arrlst.add(new BasicNameValuePair("itemName", itemName));
				arrlst.add(new BasicNameValuePair("itemPrice", itemPrice));
				arrlst.add(new BasicNameValuePair("cid", customerID));
				arrlst.add(new BasicNameValuePair("i_d", itemType));
				arrlst.add(new BasicNameValuePair("request", "0"));
				arrlst.add(new BasicNameValuePair("I_D_id", itemid));
				arrlst.add(new BasicNameValuePair("itemDesc", itemDesc));
				Log.v("TAG ArrayList", "" + arrlst.toString());

				try {
					parser.makeHttpRequest(URL, arrlst);
				} catch (Exception e) {
					Log.v("TAG Favorite", "" + parser.toString());
				}

				publishProgress();

				return null;
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);

			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub

				super.onPostExecute(result);

				Log.v("FAV LIST SIZE", "" + arrlstFavorItem.size());

				Favorite_Adapter adapter = new Favorite_Adapter(getActivity(),
						arrlstFavorItem, ActivityView);

				list.setAdapter(adapter);

				progressDialog.dismiss();

			}
		}

	}

}
