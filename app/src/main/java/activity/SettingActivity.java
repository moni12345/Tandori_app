package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import dialog.loginDialog;
import fragment.SettingFragment;
import session.SessionManager;
import util.GlobalClass;

public class SettingActivity extends Activity implements OnClickListener,
        loginDialog.Communicator, SettingFragment.TextListener {

    public static Button bLogin, bBack;

    ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
            imgBtnSettingsID, imgBtnHomeID;

    TextView tvbasketCount;
    SessionManager session;

    View ActivityView;

    public SettingActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);

        if (savedInstanceState == null) {

            if (savedInstanceState == null) {

                getFragmentManager().beginTransaction()
                        .add(R.id.container, new SettingFragment(SettingActivity.this)).commit();

            }

            InitMainUI();

            session = new SessionManager(SettingActivity.this);
            boolean status2 = session.isLoggedIn();

            if (status2 || GlobalClass.status == 1) {

                bLogin.setText("Profile");

            } else {
                bLogin.setText("Login");
            }
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

        bBack.setOnClickListener(this);
        imgBtnBasketID.setOnClickListener(this);
        imgBtnFavouriteID.setOnClickListener(this);
        imgBtnOfferID.setOnClickListener(this);
        imgBtnSettingsID.setOnClickListener(this);
        bLogin.setOnClickListener(this);
        imgBtnHomeID.setOnClickListener(this);
        if (GlobalClass.status == 1) {
            bLogin.setText("Profile");
        } else {
            bLogin.setText("Login");
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btnHome_DoneID:

                boolean status2 = session.isLoggedIn();

                if (status2) {

                    startActivity(new Intent(SettingActivity.this, ProfileActivity.class));

                } else if (GlobalClass.status == 1) {

                    startActivity(new Intent(SettingActivity.this, ProfileActivity.class));

                } else {

                    new loginDialog(SettingActivity.this);

                }
                break;

            case R.id.imgBtnHomeID:

                Intent homeIntent = new Intent(SettingActivity.this,
                        HomeActivity.class);

                startActivity(homeIntent);

            case R.id.imgBtnSettingsID:

                break;
            case R.id.imgBtnOfferID:

                startActivity(new Intent(SettingActivity.this,
                        OffersActivity.class));

                break;

            case R.id.imgBtnBasketID:

                Intent i = new Intent(SettingActivity.this, BasketActivity.class);
                startActivity(i);

                break;
            case R.id.imgBtnFavouriteID:

                if (session.isLoggedIn() || GlobalClass.status == 1) {

                    startActivity(new Intent(SettingActivity.this,
                            FavoriteActivity.class));

                } else {

                    //
                    new loginDialog(SettingActivity.this);
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

    @Override
    public void loginResponce(String data) {
        // TODO Auto-generated method stub
        bLogin.setText(data);
    }

    @Override
    public void onLoginResponse(String loginButtonText) {
        bLogin.setText(loginButtonText);
    }
}
