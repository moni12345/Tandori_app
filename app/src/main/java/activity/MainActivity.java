package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.EditText;

import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import parser.ServiceHandlerReturnString;
import util.ConnectionDetector;
import util.GlobalClass;

public class MainActivity extends Activity implements OnClickListener {

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    private EditText etPostalCode;
    private Button bEnter;
    private ProgressDialog dialog;
    private String URL = "http://placeorderonline.com/aminah/webservices/DeliveryPostcode.php";
    private String postcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        InitUI();
        cd = new ConnectionDetector(getApplicationContext());



        // new getLongitudeLatitudeFromURL().execute();
    }

    private void CheckTextField() {
        // TODO Auto-generated method stub
        isInternetPresent = cd.isConnectingToInternet();
        if (etPostalCode.equals(null) || etPostalCode.length() == 0) {
            etPostalCode.setError("Please Enter Post Code");
        } else if (isInternetPresent) {
            postcode = etPostalCode.getText().toString();
            new deliveryPostcodeAsync().execute();

        } else {

            ConnectionDetector.showAlertDialog(MainActivity.this,
                    "No Internet Connection",
                    "You don't have internet connection.", false);
        }

    }


    private void InitUI() {
        // TODO Auto-generated method stub
        etPostalCode = (EditText) findViewById(R.id.etPostalCode);

        Animation fadeIn = new AlphaAnimation(0, 5);
        fadeIn.setInterpolator(new AccelerateInterpolator()); // add this
        fadeIn.setDuration(4000);

        bEnter = (Button) findViewById(R.id.button_Enter);

        AnimationSet animation = new AnimationSet(false); // change to false
        animation.addAnimation(fadeIn);
        bEnter.setAnimation(animation);
        etPostalCode.setAnimation(animation);

        bEnter.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button_Enter:

                CheckTextField();
                break;

            default:
                break;
        }
    }

    public void showAlertDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Check Post Code");

        // set dialog message
        alertDialogBuilder
                .setMessage("You Entered wrong post code. Would you like to Continue? ")
                .setCancelable(false)
                .setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                startActivity(new Intent(MainActivity.this,
                                        MainMenuActivity.class));

                            }
                        });
        alertDialogBuilder.setPositiveButton("No",
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
        // alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(R.color.alertbuttonblue);
        // alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(R.color.alertbuttonblue);

    }

    class deliveryPostcodeAsync extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        private String status;
        private String message;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            ServiceHandlerReturnString parser = new ServiceHandlerReturnString();
            @SuppressWarnings("deprecation")
            ArrayList<NameValuePair> urlParam = new ArrayList<>();
            urlParam.add(new BasicNameValuePair("bid", GlobalClass.BRANCH_ID));
            urlParam.add(new BasicNameValuePair("pcode", postcode.toString().trim()));
            try {
                String str = parser.makeHttpRequest(URL, urlParam);
                Log.e("Response", str + "-");
                JSONArray jsonarr = new JSONArray(str);
                JSONObject obj = jsonarr.getJSONObject(0);
                status = obj.getString("Status");
                message = obj.getString("Message");
                Log.e("TAG", str);
            } catch (Exception e) {
                Log.e("TAG Connection Error", e.toString());
            }
            return status;
        }

        @Override
        protected void onPostExecute(String status) {
            // TODO Auto-generated method stub
            super.onPostExecute(status);
            dialog.dismiss();
            try {
                if (status.equals("Warning")) {
                    showAlertDialog(message);
                } else {
                    startActivity(new Intent(MainActivity.this,
                            MainMenuActivity.class));
                }
            } catch (NullPointerException e) {
            }

        }

    }

}
