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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import adapter.GlobalAsyncTask;
import adapter.ItemChoiceAdapter;
import util.GlobalClass;
import interfaces.GeneralCommunicator;
import model.AddbasketBeanFromHome;
import model.ItemChoiceBean;

public class WineChoiceActivity extends Activity implements
		GeneralCommunicator, OnItemClickListener, OnClickListener {

	private String getWineID;
	private TextView tvItemName, tvItemPrice, tvChoices;
	private ListView listChice;

	private ArrayList<NameValuePair> arrlst;
	private ArrayList<ItemChoiceBean> temparrlist;
	private List<List<ItemChoiceBean>> arrlist2;
	private JSONObject nextItem;
	private String choiceConcatenate = "";
	private Button bAdd, bDiscard, bBackFromWineChoice;

	String itemName, itemPrice, itemId;
	
	AddbasketBeanFromHome model2 = new AddbasketBeanFromHome();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_choice_activity);

		initUI();
		gettingWineInfo();

		boolean checkData = gettingWineInfo();
		if (checkData == true) {
			RequestURL();
		}
	}

	// Setting the URL Parameter here..!!
	@SuppressWarnings("deprecation")
	void RequestURL() {
		arrlst = new ArrayList<NameValuePair>();
		arrlst.add(new BasicNameValuePair("itemID", getWineID));
		arrlst.add(new BasicNameValuePair("bid", GlobalClass.BRANCH_ID));
		if (arrlst != null) {

			new GlobalAsyncTask(WineChoiceActivity.this,
					GlobalClass.APPLICATION_URL + "GetItemChoices.php", arrlst)
					.execute();

		}
	}

	private void initUI() {
		// TODO Auto-generated method stub
		tvItemName = (TextView) findViewById(R.id.tvItemName);
		tvItemPrice = (TextView) findViewById(R.id.tvItemPriceInWineChoice);
		listChice = (ListView) findViewById(R.id.lvItemChoiceInWhine);
		tvChoices = (TextView) findViewById(R.id.tvChoices);
		bAdd = (Button) findViewById(R.id.bAdditem);
		bDiscard = (Button) findViewById(R.id.bDiscard);
		bBackFromWineChoice = (Button) findViewById(R.id.bBackFromWineChoice);

		bAdd.setOnClickListener(this);
		bDiscard.setOnClickListener(this);
		bBackFromWineChoice.setOnClickListener(this);
		listChice.setOnItemClickListener(this);
	}

	boolean gettingWineInfo() {

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			itemName = bundle.getString("Name");
			getWineID = bundle.getString("itemID");
			itemPrice = bundle.getString("price");

			// Log.v("TAG STRING DATA", itemName + "->" + getWineID + "->" +
			// itemPrice);

			tvItemName.setText(itemName);
			tvItemPrice.setText(itemPrice);
			return true;
		}
		return false;

	}

	@Override
	public void getTimeResponce(String time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gettingAgeResponce(String age) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		new GlobalAsyncTask(WineChoiceActivity.this).cancel(true);
	}

	@Override
	public void urlStuff(String urlData) {
		// TODO Auto-generated method stub
		if (urlData != null) {
			try {
				JSONArray jsonArr = new JSONArray(urlData);

				temparrlist = new ArrayList<ItemChoiceBean>();
				arrlist2 = new ArrayList<List<ItemChoiceBean>>();
				for (int i = 0; i < jsonArr.length(); i++) {

					try {
						JSONObject jobj = jsonArr.getJSONObject(i);

						if (i < jsonArr.length() - 1) {
							nextItem = jsonArr.getJSONObject(i + 1);
						}
						ItemChoiceBean bean = new ItemChoiceBean();

						bean.setItemName(jobj.getString("Name"));
						bean.setItemPrice(jobj.getString("Price"));
						bean.setRepeattime(jobj.getString("repeatTimes"));
						bean.setM_ID(jobj.getString("Mid"));
						bean.setItemFree(jobj.getString("free"));
						bean.setMid(jobj.getString("mainItemID"));

						temparrlist.add(bean);
						if (i == jsonArr.length() - 1) {
							arrlist2.add(temparrlist);
						}
						if (!jobj.getString("Mid").equals(
								nextItem.getString("Mid"))) {
							arrlist2.add(temparrlist);
							temparrlist = new ArrayList<ItemChoiceBean>();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				Log.d("ArrayList size", "" + arrlist2.size());
				listChice.setAdapter(new ItemChoiceAdapter(
						WineChoiceActivity.this, arrlist2));
			} catch (Exception e) {

			} finally {

			}
		}

	}

	Double tempchoicePrice = 0.0;
	int selectedChoiceRepeat = 0;
	int count = 0;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		TextView tvOfferchoiceName = (TextView) view
				.findViewById(R.id.tvItemChoiceName);
		TextView tvprice = (TextView) view.findViewById(R.id.tvItemChoicePrice);
		// AddbasketBeanFromHome model = new AddbasketBeanFromHome();

		String OfferChoice = tvOfferchoiceName.getText().toString();
		// String offerPrice = tvprice.getText().toString();

		String tvChoiceposition = tvChoices.getText().toString();
		choiceConcatenate = tvChoiceposition + ", " + OfferChoice;
		tvChoices.setText(choiceConcatenate);

		String tvchoicePricePosition = tvprice.getText().toString();

		Double price = Double.parseDouble(tvchoicePricePosition);

		tempchoicePrice = tempchoicePrice + price;
		try {
			String repeattime = arrlist2.get(count).get(0).getRepeattime();

			int repeat = Integer.parseInt(repeattime);

			selectedChoiceRepeat++;
			Log.v("Repeat", "" + repeat);

			if (selectedChoiceRepeat >= repeat && repeat != 0) {

				selectedChoiceRepeat = 0;
				count++;
				Log.v("Count", "" + count);
				Log.v("Repeat Time", "" + repeat);

				if (arrlist2.size() > count) {
					listChice.setAdapter(new ItemChoiceAdapter(
							WineChoiceActivity.this, arrlist2, count));
				}

			}

			if (count >= arrlist2.size()) {
				Log.v("Count In Visible Item", "" + count);

				bAdd.setVisibility(View.VISIBLE);
				listChice.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.bDiscard:
			// GlobalClass.setCircleCountValueMinus(ActivityView);
			GlobalClass.OfferDiscardStatus = true;
			finish();
			startActivity(new Intent(WineChoiceActivity.this,
					OffersActivity.class));
			// super.onBackPressed();

			break;
		case R.id.bAdditem:
			// Intent intent = new Intent(WineChoiceActivity.this,
			// WineActivity.class);
			// intent.putExtra("status", true);
			// intent.putExtra("itemname", itemName);

			// choiceConcatenate = choiceConcatenate.replace(",", "\");
			model2.setItemName(itemName);
			model2.setDesc(choiceConcatenate);
			Double tempPrice = Double.parseDouble(itemPrice);
			tempPrice = tempchoicePrice + tempPrice;
			DecimalFormat df = new DecimalFormat("#.##");
			String stringPrice = df.format(tempPrice);
			model2.setID(getWineID);
			model2.setItemPrice(stringPrice);
			model2.setItemType("i");
			
		
			model2.setWineSelected("wine");   // Updation for check only wine can not be sent // 
			
			model2.setQuantity("1");
			model2.setItemWine("w");
			// basketModelArrlist.add(model2);

			GlobalClass
					.setCircleCountValuePlus(BasketActivity.viewForSpecialItem);
			GlobalClass.globalBasketList.add(model2);

			// startActivity(intent);
			finish();
			break;
		case R.id.bBackFromWineChoice:
			finish();
			super.onBackPressed();
			break;
		}
	}
}
