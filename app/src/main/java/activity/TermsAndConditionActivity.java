package activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import util.GlobalClass;

public class TermsAndConditionActivity extends Activity implements
		OnClickListener {

	static Button bLogin, bBack;
	ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
			imgBtnSettingsID, imgBtnHomeID;
	TextView basketCountView ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_activity);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		InitMainUI();
	}

	void InitMainUI() {
		basketCountView = (TextView)findViewById(R.id.basketCount);
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

		bLogin.setVisibility(View.GONE);
		basketCountView.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.imgBtnHomeID:
			Intent homeIntent = new Intent(TermsAndConditionActivity.this,
					HomeActivity.class);
			homeIntent.putExtra("status", true);
			startActivity(homeIntent);

			break;
		case R.id.imgBtnSettingsID:
			startActivity(new Intent(TermsAndConditionActivity.this,
					SettingActivity.class));

			break;
		case R.id.imgBtnOfferID:
			startActivity(new Intent(TermsAndConditionActivity.this,
					OffersActivity.class));

		case R.id.imgBtnBasketID:
			startActivity(new Intent(TermsAndConditionActivity.this,
					BasketActivity.class));
			break;
		case R.id.imgBtnFavouriteID:
			if (GlobalClass.status == 1) {
				startActivity(new Intent(TermsAndConditionActivity.this,
						FavoriteActivity.class));
			} else {
				Toast.makeText(getApplication(), "Please Login First",
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

	public static class PlaceholderFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View v = inflater.inflate(
					R.layout.terms_and_conidtion_fragment_layout, container,
					false);

			return v;
		}
	}

}
