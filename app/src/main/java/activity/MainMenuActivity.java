package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dialog.AlertDialogManager;
import dialog.loginDialog;
import parser.ServiceHandlerReturnString;
import session.SessionManager;
import util.GlobalClass;

public class MainMenuActivity extends Activity implements OnClickListener, loginDialog.Communicator {

    public static TextView tvLogin;
    public static ImageView ivlogin;
    SessionManager session;
    private ImageView ivMenu, ivOffers, ivFav, ivContact, ivExit;
    private boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main_menu_activty);

        initUI();
        isLogin = session.isLoggedIn();
        if (isLogin || GlobalClass.status == 1) {
            tvLogin.setText("Logout");
        } else {
            tvLogin.setText("Login");
        }

    }

    private void initUI() {
        // TODO Auto-generated method stub
        session = new SessionManager(MainMenuActivity.this);

        Animation fadeIn = new AlphaAnimation(0, 5);
        fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
        fadeIn.setDuration(4000);

        ivMenu = (ImageView) findViewById(R.id.ivHome);
        ivOffers = (ImageView) findViewById(R.id.ivOffers);
        ivFav = (ImageView) findViewById(R.id.ivfavorite);
        ivlogin = (ImageView) findViewById(R.id.ivlogin);
        ivContact = (ImageView) findViewById(R.id.ivContact);
        ivExit = (ImageView) findViewById(R.id.ivExit);
        tvLogin = (TextView) findViewById(R.id.tvLoginInMainMenu);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);

        ivMenu.setAnimation(animation);
        ivOffers.setAnimation(animation);
        ivlogin.setAnimation(animation);
        ivContact.setAnimation(animation);
        ivExit.setAnimation(animation);
        tvLogin.setAnimation(animation);


        ivMenu.setOnClickListener(this);
        ivOffers.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        ivlogin.setOnClickListener(this);
        ivContact.setOnClickListener(this);
        ivExit.setOnClickListener(this);

        new BranchTimingInformationTask().execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.ivHome:
                startActivity(new Intent(MainMenuActivity.this, HomeActivity.class));
                break;
            case R.id.ivOffers:
                startActivity(new Intent(MainMenuActivity.this, OffersActivity.class));
                break;
            case R.id.ivfavorite:
                if (GlobalClass.status == 1 || isLogin) {
                    startActivity(new Intent(MainMenuActivity.this, FavoriteActivity.class));
                } else {
                    new loginDialog(MainMenuActivity.this, "");
                }
                break;

            case R.id.ivlogin:
                if (GlobalClass.status == 1 || isLogin) {

                    session.logoutUser();
                    GlobalClass.status = 0;
                    isLogin = false;
                    ivlogin.setImageResource(R.drawable.loginicon2);

                    Toast.makeText(getApplication(), "Logged out", Toast.LENGTH_SHORT).show();
                    //	startActivity(new Intent(MainMenuActivity.this, ProfileActivity.class));
                } else {
                    new loginDialog(MainMenuActivity.this, "MainMenuActivity");

                }
                break;
            case R.id.ivContact:
                startActivity(new Intent(MainMenuActivity.this, FindusActivity.class));
                break;
            case R.id.ivExit:
                alertForExits();
                break;

            default:
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void alertForExits() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainMenuActivity.this);


        // set title
        alertDialogBuilder.setTitle("Exit Dialog");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure want to exit?")
                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                GlobalClass.status = 0;
                                GlobalClass.globalBasketList.clear();


                                dialog.dismiss();
                                Intent intent = new Intent(MainMenuActivity.this,
                                        MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finishAffinity();
                                finish();
                            }
                        });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void loginResponce(String data) {
        tvLogin.setText(data);
    }

    //	specialItems.php
    class BranchTimingInformationTask extends AsyncTask<String, Void, String> {

        private final String URL = GlobalClass.APPLICATION_URL + "branchTimings.php";
        ProgressDialog dialog;
        private String status, message;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(MainMenuActivity.this);
            dialog.setCancelable(false);
            dialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            ServiceHandlerReturnString service = new ServiceHandlerReturnString();
            ArrayList<NameValuePair> arrlst = new ArrayList<>();
            arrlst.add(new BasicNameValuePair("bid", GlobalClass.BRANCH_ID));

            String gotdata = service
                    .makeHttpRequest(URL, arrlst, MainMenuActivity.this);
            try {
                JSONArray branchStatusArr = new JSONArray(gotdata);
                JSONObject branchStatusObj = branchStatusArr.getJSONObject(0);
                status = branchStatusObj.getString("Status");
                message = branchStatusObj.getString("Message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();
            try {
                if (!result.isEmpty()) {
                    GlobalClass.branchTimeing = result;
                }

                if (result.equals("OK")) {

                } else if (result.equals("Warning")) {
                    AlertDialogManager.showAlertDialog(MainMenuActivity.this, result, message, false);
                } else if (result.equals("Closed")) {
                    AlertDialogManager.showAlertDialog(MainMenuActivity.this, result, message, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

/*	@Override
    public void changeLoginImage() {
		ivlogin.setImageResource(R.drawable.log_out);
		
		
	}*/
}
