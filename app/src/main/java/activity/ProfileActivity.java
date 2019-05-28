package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import parser.ServiceHandlerReturnString;
import session.SessionManager;
import util.GlobalClass;

public class ProfileActivity extends Activity implements OnClickListener {

    static Button bLogin, bBack;
    static EditText etFirstname;
    static EditText etLastname;
    static EditText etEmail;
    static EditText etAddress;
    static EditText etAddress2;
    static EditText etContact;
    static EditText etContact2;
    static EditText etCity;
    static EditText etPostcode;
    static String prefID;
    static String firstName;
    static String lastname;
    static String email;
    static String address1;
    static String address2;
    static String contact1;
    static String contact2;
    static String city;
    static String postcode;
    ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
            imgBtnSettingsID, imgBtnHomeID;
    TextView tvbasketCount;
    String URL = "http://placeorderonline.com/aminah/webservices/RegisterCustApp.php";
    View ActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

        ActivityView = this.findViewById(android.R.id.content);
        GlobalClass.globalObj.showCircleCount(getApplicationContext(),
                ActivityView);

        InitMainUI();
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
        bLogin.setText("Done");

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnHome_DoneID:
                new ProfileAsyncTask().execute();
                finish();
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                break;
            case R.id.imgBtnHomeID:
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                homeIntent.putExtra("status", true);
                startActivity(homeIntent);

                break;
            case R.id.imgBtnSettingsID:
                startActivity(new Intent(ProfileActivity.this, SettingActivity.class));

                break;
            case R.id.imgBtnOfferID:
                startActivity(new Intent(ProfileActivity.this, OffersActivity.class));

            case R.id.imgBtnBasketID:

                break;
            case R.id.imgBtnFavouriteID:
                startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class));

                break;
            case R.id.btnHome_BackID:

                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    public void gettingDateFromTextField() {
        firstName = etFirstname.getText().toString();
        lastname = etLastname.getText().toString();
        email = etEmail.getText().toString();
        address1 = etAddress.getText().toString();
        address2 = etAddress2.getText().toString();
        contact1 = etContact.getText().toString();
        contact2 = etContact2.getText().toString();
        city = etCity.getText().toString();
        postcode = etPostcode.getText().toString();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements
            OnClickListener {


        Button bSubmit, bClear;
        SharedPreferences pref;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile,
                    container, false);
            initUI(rootView);
            pref = getActivity().getSharedPreferences("fraddys", 0);

            if (GlobalClass.status == 1) {
                prefID = GlobalClass.customerId;
                SettingDataFromGlobalClasstoTextField();
            } else {
                SettingDataFromSharedPreftoTextField();
                prefID = pref.getString(SessionManager.KEY_ID, "");
            }

            //	gettingDateFromTextField();

            return rootView;
        }

        private void SettingDataFromSharedPreftoTextField() {
            // TODO Auto-generated method stub
            etFirstname.setText(pref.getString(SessionManager.FIRST_NAME, ""));
            etLastname.setText(pref.getString(SessionManager.LAST_NAME, ""));
            etEmail.setText(pref.getString(SessionManager.KEY_EMAIL, ""));
            etAddress.setText(pref.getString(SessionManager.ADDRESS, ""));
            etAddress2.setText(pref.getString(SessionManager.ADDRESS2, ""));
            etContact.setText(pref.getString(SessionManager.CONTACT1, ""));
            etContact2.setText(pref.getString(SessionManager.CONTACT2, ""));
            etCity.setText(pref.getString(SessionManager.CITY, ""));
            etPostcode.setText(pref.getString(SessionManager.POSTCODE, ""));

        }

        private void SettingDataFromGlobalClasstoTextField() {
            // TODO Auto-generated method stub
            etFirstname.setText(GlobalClass.firstName);
            etLastname.setText(GlobalClass.lastName);
            etEmail.setText(GlobalClass.email);
            etAddress.setText(GlobalClass.address);
            etAddress2.setText(GlobalClass.address2);
            etContact.setText(GlobalClass.contact1);
            etContact2.setText(GlobalClass.contact2);
            etCity.setText(GlobalClass.city);
            etPostcode.setText(GlobalClass.postcode);

        }

        private void clearAllFields() {
            // TODO Auto-generated method stub
            etFirstname.setText("");
            etLastname.setText("");
            etEmail.setText("");
            etAddress.setText("");
            etAddress2.setText("");
            etContact.setText("");
            etContact2.setText("");
            etCity.setText("");
            etPostcode.setText("");

        }

        private void initUI(View v) {
            // TODO Auto-generated method stub
            //bSubmit = (Button) v.findViewById(R.id.bSubmitInProfile);
            etFirstname = (EditText) v.findViewById(R.id.etName);
            etLastname = (EditText) v.findViewById(R.id.etLastName);
            etEmail = (EditText) v.findViewById(R.id.etEmailInProfile);
            etEmail.setEnabled(false);
            etAddress = (EditText) v.findViewById(R.id.etAddress1);
            etAddress2 = (EditText) v.findViewById(R.id.etAddress2);
            etContact = (EditText) v.findViewById(R.id.etContact1);
            etContact2 = (EditText) v.findViewById(R.id.etContact2);
            etCity = (EditText) v.findViewById(R.id.etCity);
            etPostcode = (EditText) v.findViewById(R.id.etPostalCodeInProfile);
            //	bClear = (Button) v.findViewById(R.id.bClearAllFields);

//			bSubmit.setOnClickListener(this);
//			bClear.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.bSubmitInProfile:
                    // gettingDateFromTextField();

                    break;
                case R.id.bClearAllFields:
                    clearAllFields();
                    break;

                default:
                    break;
            }
        }

        @SuppressWarnings("deprecation")
        void AlertDialogshow(String msg) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());

            // set title
            alertDialogBuilder.setTitle("ProfileActivity Update Dialog");

            // set dialog message
            alertDialogBuilder
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    dialog.dismiss();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

    }

    class ProfileAsyncTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHandlerReturnString service = new ServiceHandlerReturnString();
            gettingDateFromTextField();
            ArrayList<NameValuePair> arrlst = new ArrayList<NameValuePair>();
            arrlst.add(new BasicNameValuePair("cid", prefID));
            arrlst.add(new BasicNameValuePair("fname", firstName));
            arrlst.add(new BasicNameValuePair("lname", lastname));
            arrlst.add(new BasicNameValuePair("email", email));
            arrlst.add(new BasicNameValuePair("address1", address1));
            arrlst.add(new BasicNameValuePair("address2", address2));
            arrlst.add(new BasicNameValuePair("contact1", contact1));
            arrlst.add(new BasicNameValuePair("contact2", contact2));
            arrlst.add(new BasicNameValuePair("TownCity", city));
            arrlst.add(new BasicNameValuePair("PostCode", postcode));
            String responce = service.makeHttpRequest(URL, arrlst);
            Log.v("responce", "" + responce);
            return responce;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub


            GlobalClass.firstName = firstName;
            GlobalClass.lastName = lastname;
            GlobalClass.email = email;
            GlobalClass.address = address1;
            GlobalClass.address2 = address2;
            GlobalClass.contact1 = contact1;
            GlobalClass.contact2 = contact2;
            GlobalClass.city = city;
            GlobalClass.postcode = postcode;

            super.onPostExecute(result);


            //	AlertDialogshow(result);
        }

    }

}
