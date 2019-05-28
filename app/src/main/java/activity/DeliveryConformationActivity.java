package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import adapter.ListExpandableAdapter;
import util.GlobalClass;

public class DeliveryConformationActivity extends Activity implements
		OnClickListener {
	Button bLogin, bBack;
	ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
			imgBtnSettingsID, imgBtnHomeID;
	TextView tvbasketCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);
		GlobalClass.wineSelection = true;
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
			InitMainUI();
			bLogin.setText("Exit");
			GlobalClass.wineSelection = true ;
			for (int i = 0; i < GlobalClass.globalBasketList.size(); i++) {
				GlobalClass.globalBasketList.remove(i);
			}
			GlobalClass.counterValue = "0";
		}
	}

	void InitMainUI() {

		tvbasketCount = (TextView) findViewById(R.id.basketCount);
		tvbasketCount.setVisibility(View.INVISIBLE);
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
			alertForExit();
			// Toast.makeText(getApplicationContext(), "this is Exit",
			// Toast.LENGTH_SHORT ).show();

			break;
		case R.id.imgBtnHomeID:
			Intent homeIntent = new Intent(DeliveryConformationActivity.this,
					HomeActivity.class);
			homeIntent.putExtra("status", true);
			startActivity(homeIntent);

			break;
		case R.id.imgBtnSettingsID:
			startActivity(new Intent(DeliveryConformationActivity.this,
					SettingActivity.class));

			break;
		case R.id.imgBtnOfferID:
			startActivity(new Intent(DeliveryConformationActivity.this,
					OffersActivity.class));

		case R.id.imgBtnBasketID:
			startActivity(new Intent(DeliveryConformationActivity.this,
					BasketActivity.class));
			break;
		case R.id.imgBtnFavouriteID:

			startActivity(new Intent(DeliveryConformationActivity.this,
					FavoriteActivity.class));

			break;
		case R.id.btnHome_BackID:
			finish();
			startActivity(new Intent(DeliveryConformationActivity.this,
					HomeActivity.class));
			break;

		default:
			break;
		}
	}

	// Alert for Exit
	public void alertForExit() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				DeliveryConformationActivity.this);

		// set title
		alertDialogBuilder.setTitle("Exit ");
		alertDialogBuilder.setMessage("Do you want to exit");

		// set dialog message
		alertDialogBuilder.setNegativeButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity

						dialog.dismiss();
						Intent intent = new Intent(
								DeliveryConformationActivity.this,
								MainActivity.class);
						
						
						GlobalClass.status = 0;
//						GlobalClass.statusLogin = "";
//						GlobalClass.statusLogin2 = "";
						GlobalClass.globalBasketList.clear();
						
						
						// intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("EXIT", true);
						DeliveryConformationActivity.this.finishAffinity();
						startActivity(intent);
						DeliveryConformationActivity.this.finish();

					}
				});

		alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
//		alertDialogBuilder.setNegativeButton("", new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//
//			}
//		});

		// show it
		alertDialogBuilder.show();

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_delivery__conformation, container, false);

			GlobalClass.globalBasketList.clear();
			GlobalClass.counterValue = "0";
			GlobalClass.GloballistDataChild.clear();
			ListExpandableAdapter.basketarr.clear();
			return rootView;

		}
	}
}
