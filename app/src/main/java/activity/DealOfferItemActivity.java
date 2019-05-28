package activity;

import java.text.DecimalFormat;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import dialog.loginDialog;
import adapter.DealOffersAdapter;
import adapter.ItemChoiceAdapter;
import adapter.ListExpandableAdapter;
import util.GlobalClass;
import model.AddbasketBeanFromHome;
import model.ItemChoiceBean;
import parser.BranchListParser;
import util.ConnectionDetector;

public class DealOfferItemActivity extends Activity implements OnClickListener , loginDialog.Communicator {

	TextView tvItemName, tvItemPrice;
	String URL = "http://placeorderonline.com/aminah/webservices/GetDealChoices.php";
	ArrayList<ItemChoiceBean> temparrlist;
	List<List<ItemChoiceBean>> arrlist2;

	ListView listChoice;
	JSONObject nextItem;
	Button bNext, bAddItem, bDiscard, bback, bLogin;
	int count = 0;
	LinearLayout lineartext;
	String ItemID, branch_ID;
	TextView tvChoices;

	int length;
	int selectedChoiceRepeat = 0;

	String ItemName;
	String choiceConcatenate = "";
	Double tempchoicePrice = 0.0;

	String Itemprice;
	View ActivityView;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;

	AddbasketBeanFromHome model2 = new AddbasketBeanFromHome();

	// int

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.deal_offer_item);

		InitUI();
		
		bLogin.setText(GlobalClass.statusLogin);
		Bundle b = getIntent().getExtras();
		if (b != null) {
			ItemName = b.getString("Name");
			Itemprice = b.getString("price");
			branch_ID = b.getString("branch_ID");
			ItemID = b.getString("itemID");

			tvItemName.setText(ItemName);
			tvItemPrice.setText(Itemprice);
			tvChoices.setText(b.getString("desc"));
		}

		cd = new ConnectionDetector(getApplicationContext());
		isInternetPresent = cd.isConnectingToInternet();

		if (isInternetPresent) {
			// Internet Connection is Present
			// make HTTP requests
			new ItemChoicesAsync().execute();

		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			ConnectionDetector.showAlertDialog(DealOfferItemActivity.this,
					"No Internet Connection",
					"You don't have internet connection.", false);
		}

		listChoice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// additembean = new AddItemInBasketBean();

				TextView tvOfferchoiceName = (TextView) view
						.findViewById(R.id.tvItemChoiceName);
				TextView tvprice = (TextView) view
						.findViewById(R.id.tvItemChoicePrice);
			//	AddbasketBeanFromHome model = new AddbasketBeanFromHome();

				String OfferChoice = tvOfferchoiceName.getText().toString();
			//	String offerPrice = tvprice.getText().toString();

				String tvChoiceposition = tvChoices.getText().toString();
				choiceConcatenate = tvChoiceposition + ", " + OfferChoice;
				tvChoices.setText(choiceConcatenate);

				String tvchoicePricePosition = tvprice.getText().toString();

				Double price = Double.parseDouble(tvchoicePricePosition);

				tempchoicePrice = tempchoicePrice + price;
				try {
					String repeattime = arrlist2.get(count).get(0)
							.getRepeattime();

					int repeat = Integer.parseInt(repeattime);

					selectedChoiceRepeat++;

					if (selectedChoiceRepeat >= repeat && repeat != 0) {

						selectedChoiceRepeat = 0;
						count++;
						Log.v("Count", "" + count);
						Log.v("Repeat Time", "" + repeat);

						if (arrlist2.size() > count) {
							listChoice.setAdapter(new ItemChoiceAdapter(
									DealOfferItemActivity.this, arrlist2, count));
						}

					}

					if (count >= arrlist2.size()) {
						Log.v("Count In Visible Item", "" + count);

						bAddItem.setVisibility(View.VISIBLE);
						listChoice.setVisibility(View.INVISIBLE);
						bNext.setVisibility(View.INVISIBLE);
						// bSkip.setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
					Log.e("Exception", e.toString());
				}
			}

		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ListExpandableAdapter.count = 0;
	}

	private void InitUI() {
		// TODO Auto-generated method stub
		tvItemName = (TextView) findViewById(R.id.tvItemName);
		tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
		listChoice = (ListView) findViewById(R.id.lvItemChoice);
		bNext = (Button) findViewById(R.id.bNext);
		bAddItem = (Button) findViewById(R.id.bAdditem);
		bDiscard = (Button) findViewById(R.id.bDiscard);
		bLogin = (Button) findViewById(R.id.bLoginFromWineChoice);
		bback = (Button) findViewById(R.id.bBackFromWineChoice);
		
		bback.setOnClickListener(this);
		bLogin.setOnClickListener(this);

		lineartext = (LinearLayout) findViewById(R.id.linearTextLayout);
		// bNext = (Button) findViewById(R.id.bNext);

		tvChoices = (TextView) findViewById(R.id.tvChoices);
		bNext.setOnClickListener(this);
		bDiscard.setOnClickListener(this);

		bAddItem.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bNext:
			try {

				int arrsize = arrlist2.size();
				if (arrsize > count) {

					listChoice.setAdapter(new ItemChoiceAdapter(
							DealOfferItemActivity.this, arrlist2, count));
					count++;
				} else {
					listChoice.setVisibility(View.INVISIBLE);
					bNext.setVisibility(View.INVISIBLE);
					bAddItem.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				Log.v("TAG", e.toString());
				// TODO: handle exception
			}
			break;

		case R.id.bDiscard:
			// GlobalClass.setCircleCountValueMinus(ActivityView);
			GlobalClass.OfferDiscardStatus = true;
			finish();
			startActivity(new Intent(DealOfferItemActivity.this, OffersActivity.class));
			// super.onBackPressed();

			break;
		case R.id.bAdditem:
			Intent intent = new Intent(DealOfferItemActivity.this,
					OffersActivity.class);
			intent.putExtra("status", true);
			intent.putExtra("itemname", ItemName);
			
			GlobalClass.setCircleCountValuePlus(DealOffersAdapter.ActivityView);
			
			
			// choiceConcatenate = choiceConcatenate.replace(",", "\");
			model2.setItemName(ItemName);
			model2.setDesc(choiceConcatenate);
			Double tempPrice = Double.parseDouble(Itemprice);
			tempPrice = tempchoicePrice + tempPrice;
			DecimalFormat df = new DecimalFormat("#.##");
			String stringPrice = df.format(tempPrice);
			model2.setID(ItemID);
			model2.setItemPrice(stringPrice);
			model2.setItemType("d");
			model2.setQuantity("1");
			model2.setItemWine("NotWine");
			// basketModelArrlist.add(model2);

			GlobalClass.globalBasketList.add(model2);
		//	GlobalClass.setCircleCountValuePlus(ActivityView);
		

//			int itemlength = GlobalClass.globalBasketList.size();
//			Log.v("Deal item Length", "" + itemlength);
			startActivity(intent);
			
			break;
		case R.id.bLoginFromWineChoice:
			String loginstatus = bLogin.getText().toString();
			if(loginstatus.equals("Login")){
				new loginDialog(DealOfferItemActivity.this);
			}else if(loginstatus.equals("Profile")){
				startActivity(new Intent(DealOfferItemActivity.this, ProfileActivity.class));
			}
			break;
		case R.id.bBackFromWineChoice:
			finish();
			onBackPressed();
		default:
			break;

		}
	}

	/*void PrepareListData() {

		ArrayList<ItemChoiceBean> arrlistchoice = new ArrayList<ItemChoiceBean>();
		for (int i = 0; i <= arrlist2.size(); i++) {

			for (int j = 0; j < arrlist2.get(i).size() - 1; i++) {

				ItemChoiceBean item = new ItemChoiceBean();
				item.setItemName(arrlist2.get(i).get(j).getItemName());
				item.setItemPrice(arrlist2.get(i).get(j).getItemPrice());
				arrlistchoice.add(item);
			}
			arrlist2.add(arrlistchoice);
			arrlistchoice = new ArrayList<>();

		}
	}*/

	class ItemChoicesAsync extends AsyncTask<Void, Void, Void> {

		ProgressDialog pdialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = new ProgressDialog(DealOfferItemActivity.this);
			pdialog.show();
			pdialog.setCancelable(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			BranchListParser parser = new BranchListParser();
			ArrayList<NameValuePair> arrlst = new ArrayList<>();
			arrlst.add(new BasicNameValuePair("itemID", ItemID));
			arrlst.add(new BasicNameValuePair("bid", branch_ID));

			try {
				JSONArray arr = parser.makeHttpRequest(URL, arrlst);

				Log.v("hello", "" + arr);
				temparrlist = new ArrayList<>();
				arrlist2 = new ArrayList<>();
				if (arr.length() == 0 ) {

					Log.v("TAG", "" + arr);
				} else {
					for (int i = 0; i < arr.length(); i++) {
						try {
							JSONObject obj = arr.getJSONObject(i);
							if (i < arr.length() - 1) {
								nextItem = arr.getJSONObject(i + 1);
							}
							ItemChoiceBean bean = new ItemChoiceBean();

							bean.setItemName(obj.getString("Name"));
							bean.setItemPrice(obj.getString("Price"));
							bean.setItemFree(obj.getString("free"));
							bean.setM_ID("Mid");
							bean.setRepeattime(obj.getString("repeatTimes"));

							temparrlist.add(bean);
							if (i == arr.length() - 1) {
								arrlist2.add(temparrlist);
							}
							if (!obj.getString("Mid").equals(
									nextItem.getString("Mid"))) {
								arrlist2.add(temparrlist);
								temparrlist = new ArrayList<>();
							}
							// Log.i("TAG", name + "->" + price + "->" + free);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pdialog.dismiss();
			try {
				
				listChoice.setAdapter(new ItemChoiceAdapter(DealOfferItemActivity.this,
						arrlist2));
			
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void loginResponce(String data) {
		// TODO Auto-generated method stub
	bLogin.setText( String.format("%s","Profile"));
	}

}
