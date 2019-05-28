package activity;

import java.util.ArrayList;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import dialog.AlertDialogManager;
import dialog.loginDialog;
import adapter.WineAdapter;
import util.GlobalClass;
import interfaces.GeneralCommunicator;
import model.DealOffersBean;
import parser.ServiceHandlerReturnString;
import session.SessionManager;

public class WineActivity extends Activity implements GeneralCommunicator, OnClickListener, loginDialog.Communicator {

	private static final String URL = GlobalClass.APPLICATION_URL + "specialItems.php";

	ArrayList<DealOffersBean> wineArrayList = new ArrayList<DealOffersBean>();
	
	Button bLogin, bBack, bGoHome, bGoFav, bGoOffer, bGoBasket, bSetting;
	ListView lvWine;
	TextView tvCounter;
	View ActivityView;
	SessionManager session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wine_activity);
		intiUI();
		AlertDialogManager.showAlertDialog(WineActivity.this, "Warning", "You must be atleast 18 year old to Order this Item ", true, 1);
		

	}

	private void intiUI() {
		session = new SessionManager(WineActivity.this);
	//	bLogin = (Button) findViewById(R.id.bLoginInWine);
		bBack = (Button) findViewById(R.id.bBackInWine);
		lvWine = (ListView) findViewById(R.id.WineList); 
	
		ActivityView = this.findViewById(android.R.id.content);
		
		
		bBack.setOnClickListener(this);
	//	bLogin.setOnClickListener(this);
		
		
	}
	
	@Override
	public void getTimeResponce(String time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gettingAgeResponce(String age) {
		// TODO Auto-generated method stub
		if(age.equals("NO")){
		super.onBackPressed();
		}else if(age.equals("YES")){
			new gettingWineTask().execute();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
//		case R.id.bLoginInWine:
//			boolean isLogin = session.isLoggedIn();
//			if(GlobalClass.status == 1 || isLogin == true){
//				startActivity(new Intent(WineActivity.this, ProfileActivity.class));
//			}else{
//				new loginDialog(WineActivity.this);
//			}
			
		//	break;
		case R.id.bBackInWine:
			super.finish();
			
			startActivity(new Intent(WineActivity.this, BasketActivity.class));
			break;
		default:
			break;
		}
	}

	class gettingWineTask extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(WineActivity.this);
			dialog.setCancelable(false);
			dialog.show();
		
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			
			ServiceHandlerReturnString service = new ServiceHandlerReturnString();
			ArrayList<NameValuePair> arrlst = new ArrayList<>();
			arrlst.add(new BasicNameValuePair("bid", GlobalClass.BRANCH_ID));

			String gotDataFromURL = service.makeHttpRequest(URL, arrlst,
					WineActivity.this);
			
			if(gotDataFromURL.equals(null)|| gotDataFromURL == null){
				return null;
			}else{
			try {
				JSONArray jsonArray = new JSONArray(gotDataFromURL);
				
				for(int i = 0 ; i < jsonArray.length(); i ++){
					DealOffersBean bean = new DealOffersBean();
					JSONObject obj = jsonArray.getJSONObject(i);
					String wineID = obj.getString("offerID");
					String wineName = obj.getString("offerName");
					String wineDesc = obj.getString("offerDesc");
					String winePrice = obj.getString("offerPrice");
					String wineChoice = obj.getString("offerChoices");
					String wineGoLarge = obj.getString("goLarge");
					String wineAddPrice = obj.getString("addPrice");
					String wineURL = obj.getString("offerPicURL");
					
					bean.setOfferID(wineID);
					bean.setOfferName(wineName);
					bean.setOfferPrice(winePrice);
					bean.setOfferDesc(wineDesc);
					bean.setOfferChoices(wineChoice);
					
					wineArrayList.add(bean);
					
					
					
					
				}
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
			try{
			lvWine.setAdapter(new WineAdapter(WineActivity.this, wineArrayList));
			}catch(Exception e){
				
			}
		}

	}

	@Override
	public void loginResponce(String data) {
		// TODO Auto-generated method stub
		bLogin.setText(data);
	}

	@Override
	public void urlStuff(String urlData) {
		// TODO Auto-generated method stub
		
	}

	


}
