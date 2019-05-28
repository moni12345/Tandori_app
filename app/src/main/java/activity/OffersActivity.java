package activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import dialog.loginDialog;
import adapter.DealOffersAdapter;
import util.GlobalClass;
import model.AddItemBean;
import model.DealOffersBean;
import parser.DealOffersParser;
import session.SessionManager;

public class OffersActivity extends Activity implements OnClickListener,
		loginDialog.Communicator {

	Button bLogin, bBack;
	ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
			imgBtnSettingsID, imgBtnHomeID;
	TextView tvbasketCount;
	SessionManager session;
	View ActivityView;
	public OffersActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new OfferFragment()).commit();
		}
		InitMainUI();
		session = new SessionManager(OffersActivity.this);
		boolean status2 = session.isLoggedIn();
		// Toast.makeText(getApplicationContext(), ""+status2, 0).show();
		if ( status2 || GlobalClass.status == 1) {
			bLogin.setText("Profile");
		}

		ActivityView = this.findViewById(android.R.id.content);
		GlobalClass.globalObj.showCircleCount(getApplicationContext(),
				ActivityView);
//
//		if (GlobalClass.OfferDiscardStatus == true) {
//			GlobalClass.setCircleCountValueMinus(ActivityView);
//		}
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
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnHome_DoneID:
			boolean status2 = session.isLoggedIn();
			if ( status2 || GlobalClass.status == 1) {
				startActivity(new Intent(OffersActivity.this, ProfileActivity.class));
			} else {
				new loginDialog(OffersActivity.this);
			}
			break;
		case R.id.imgBtnHomeID:
			Intent homeIntent = new Intent(OffersActivity.this,
					HomeActivity.class);
			startActivity(homeIntent);

			break;
		case R.id.imgBtnSettingsID:
			startActivity(new Intent(OffersActivity.this,
					SettingActivity.class));

			break;
		case R.id.imgBtnOfferID:

			break;

		case R.id.imgBtnBasketID:
			// GlobalClass.globalBasketList.add(ListExpandable.basketarr);
			Intent i = new Intent(OffersActivity.this, BasketActivity.class);
			startActivity(i);

			break;
		case R.id.imgBtnFavouriteID:
			if (GlobalClass.status != 0) {
				startActivity(new Intent(OffersActivity.this,
						FavoriteActivity.class));
			} else {
				Toast.makeText(getBaseContext(), "Please Login First",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.btnHome_BackID:
			finish();
			super.onBackPressed();
			break;
		default:
			break;
		}
	}

	 class OfferFragment extends Fragment implements
			OnClickListener {

		ListView list;
		String URL = "http://placeorderonline.com/aminah/webservices/GetOffersMenu.php";
		String branch_id = BranchDetailActivity.branch_id2;
		ArrayList<DealOffersBean> beanArrlist;
		AddItemBean addItembean;
		String offerID;
		String Error_msg;
		String offername;
		String offerPrice;
		String offerDesc;
		public OfferFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub

			View view = inflater.inflate(R.layout.deal_fragment, container,
					false);
			addItembean = new AddItemBean();

			InitUI(view);
			new GetOffersAsyncTask().execute();

			// listAdapter.setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> parent,
			// View adapterview, int position, long id) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// });

			if (GlobalClass.status == 1) {
				bLogin.setText("Profile");
			}
			return view;

		}

		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
			new GetOffersAsyncTask().isCancelled();
		}

		private void InitUI(View v) {
			// TODO Auto-generated method stub
			list = (ListView) v.findViewById(R.id.dealListview);
		}

		/*public Bitmap convertImage(String imageURL) throws IOException {
			URL url = new URL(imageURL);
			InputStream is = url.openConnection().getInputStream();
			Bitmap bitMap = BitmapFactory.decodeStream(is);
			return bitMap;
		}*/

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

				ArrayList<NameValuePair> arrlist = new ArrayList<>();
				arrlist.add(new BasicNameValuePair("bid",
						BranchDetailActivity.branch_id2));

				DealOffersParser parser = new DealOffersParser();
				JSONArray jsonArr = parser.makeHttpRequest(URL, arrlist);
				if (jsonArr.length() > 0) {
					// Log.v("Length", ""+jsonArr.length());
					beanArrlist = new ArrayList<>();
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
							// bean.setOfferspicURL("http://" + URL);
							bean.setOfferspicURL("http://" + image);

							beanArrlist.add(bean);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				} else {
					Error_msg = "Connection Error";
					// Toast.makeText(getActivity(), "Connection Error",
					// 0).show();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				pDialog.dismiss();
				try {
					list.setAdapter(new DealOffersAdapter(getActivity(),
							beanArrlist, ActivityView));
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.ivAddItem:
				// gettingDataFromAdapter();
				Intent i = new Intent(this.getActivity(), DealOfferItemActivity.class);
				i.putExtra("branch_ID", branch_id);
				i.putExtra("itemID", offerID);
				i.putExtra("Name", offername);
				i.putExtra("price", offerPrice);
				i.putExtra("desc", offerDesc);
				// int counter = 1;
				// GlobalClass.setCircleCountValuePlus(ActivityView);

				startActivity(i);
				break;

			default:
				break;
			}
		}

	}

	@Override
	public void loginResponce(String data) {
		// TODO Auto-generated method stub
		bLogin.setText("Profile");
		//
		// bLogin.setText(data);
	}
}
