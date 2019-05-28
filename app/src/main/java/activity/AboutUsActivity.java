package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
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
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import dialog.loginDialog;
import util.GlobalClass;

public class AboutUsActivity extends Activity implements OnClickListener {

    TextView tvbasketCount;
    View ActivityView;

    Button bLogin, bBack;
    ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
            imgBtnSettingsID, imgBtnHomeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new AboutUSFragment()).commit();
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
        //  bLogin.setOnClickListener(this);
        imgBtnHomeID.setOnClickListener(this);
        bLogin.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnHome_DoneID:
                if (GlobalClass.status == 1) {
                } else {
                    loginDialog dialog = new loginDialog(AboutUsActivity.this);
                }
                break;
            case R.id.imgBtnHomeID:
                Intent homeIntent = new Intent(AboutUsActivity.this,
                        HomeActivity.class);
                startActivity(homeIntent);

                break;
            case R.id.imgBtnSettingsID:
                startActivity(new Intent(AboutUsActivity.this,
                        SettingActivity.class));

                break;
            case R.id.imgBtnOfferID:
                startActivity(new Intent(AboutUsActivity.this,
                        OffersActivity.class));

                break;

            case R.id.imgBtnBasketID:
                startActivity(new Intent(AboutUsActivity.this,
                        BasketActivity.class));

                break;
            case R.id.imgBtnFavouriteID:
                if (GlobalClass.status == 1) {
                    startActivity(new Intent(AboutUsActivity.this,
                            FavoriteActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Please Login First",
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

    @SuppressLint("ValidFragment")
    private class AboutUSFragment extends Fragment {

        public AboutUSFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            View view = inflater.inflate(R.layout.about_us, container, false);
            return view;
        }
    }
}
