package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dialog.AlertDialogManager;
import parser.ServiceHandlerReturnString;
import session.SessionManager;
import util.ConnectionDetector;
import util.GlobalClass;

public class DeliveryInformationActivity extends Activity implements
        OnClickListener {

    static String URL = "http://placeorderonline.com//webservices/printOrder.php";
    static String firstName, lastname, address1, address2, contact1,
            contact2, city, postcode;
    static double totalPrice;
    static double delieverycharge;
    static double discont;
    static String OrderType;
    static String specialComment = "";
    static EditText etFirstname, etLastname, etAddress, etAddress2, etContact,etContact2, etCity, etPostcode;
    static RadioButton rbtn;
    String couponCode;
    Button bLogin, bBack;
    ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
            imgBtnSettingsID, imgBtnHomeID;
    TextView tvbasketCount;
    double subTotalPrice;
    SharedPreferences pref;
    String prefID;
    String Status;
    ArrayList<JSONObject> lst;
    String regID = null;
    // Dictionary<String, String> dictionary;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    JSONObject dictionary;
    String date;
    View ActivityView;
    private GoogleCloudMessaging gcm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
        registerInBackground();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
        InitMainUI();

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {

            // Internet connection is not present
            // Ask user to connect to Internet
            ConnectionDetector.showAlertDialog(
                    DeliveryInformationActivity.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        ActivityView = this.findViewById(android.R.id.content);
        GlobalClass.globalObj.showCircleCount(getApplicationContext(),
                ActivityView);

        bLogin.setText(String.format("%s", "Done"));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            OrderType = bundle.getString("orderType");
            totalPrice = bundle.getDouble("totalPrice");
            subTotalPrice = bundle.getDouble("ItemsubPrice");
            discont = bundle.getDouble("discount");
            delieverycharge = bundle.getDouble("deliveryCharge");
            specialComment = bundle.getString("Special_Comments");
            couponCode = bundle.getString("couponCode");

            Log.e("Discount", discont + "-");
            Log.e("OrderType", "" + couponCode + "-");
            Log.e("OrderType", "" + OrderType + "-");
            Log.e("totalPrice", "" + totalPrice + "-");
            Log.e("ItemsubPrice", "" + subTotalPrice + "-");
            Log.e("delieverycharge", "" + delieverycharge + "-");

        }
        gettingSystemDate();

        if (GlobalClass.globalBasketList.size() > 0) {
            // jsonArray = new JSONArray(GlobalClass.globalBasketList);
            lst = new ArrayList<>();
            for (int i = 0; i < GlobalClass.globalBasketList.size(); i++) {


                String itemType = GlobalClass.globalBasketList.get(i)
                        .getItemType();

                String itemID = GlobalClass.globalBasketList.get(i).getID();
                String itemDesc = GlobalClass.globalBasketList.get(i).getDesc();
                String itemqty = GlobalClass.globalBasketList.get(i)
                        .getQuantity();
                String itemPrice = GlobalClass.globalBasketList.get(i)
                        .getItemPrice();
                try {
                    dictionary = new JSONObject();
                    // dictionary.put("name", itemName);
                    dictionary.put("qty", itemqty);
                    dictionary.put("description", itemDesc);
                    dictionary.put("I_S_D", itemType);
                    dictionary.put("item_subi_dealID", itemID);
                    dictionary.put("unitPrice", itemPrice);
                    lst.add(dictionary);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.v("TAG", e.toString());
                }

            }
            gettingSystemDate();
        }
        if (GlobalClass.status == 1) {
            prefID = GlobalClass.customerId;
        } else {
            pref = getSharedPreferences("fraddys", 0);
            prefID = pref.getString(SessionManager.KEY_ID, "");
        }

    }

    @SuppressLint("SimpleDateFormat")
    private void gettingSystemDate() {
        // TODO Auto-generated method stub

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();

        date = dateFormat.format(cal.getTime());

        Log.v("TAG mDATE", date);

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
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnHome_DoneID:

                boolean b = checkTextField();
                if (b) {
                    if (OrderType.equals("d")) {
                        new GetMileAsyncTask().execute();
                    } else {
                        new DeliveryAsyncTask().execute();
                        // SettingPushotification();
                    }
                }

                break;
            case R.id.imgBtnHomeID:
                Intent homeIntent = new Intent(DeliveryInformationActivity.this,
                        HomeActivity.class);
                homeIntent.putExtra("status", true);
                startActivity(homeIntent);
                break;
            case R.id.imgBtnSettingsID:
                startActivity(new Intent(DeliveryInformationActivity.this,
                        SettingActivity.class));

                break;
            case R.id.imgBtnOfferID:
                startActivity(new Intent(DeliveryInformationActivity.this,
                        OffersActivity.class));

            case R.id.imgBtnBasketID:
                break;

            case R.id.imgBtnFavouriteID:
                startActivity(new Intent(DeliveryInformationActivity.this,
                        FavoriteActivity.class));
                break;

            case R.id.btnHome_BackID:
                finish();
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public void gettingDateFromTextField() {
        firstName = etFirstname.getText().toString();
        lastname = etLastname.getText().toString();
        // email = etEmail.getText().toString();
        address1 = etAddress.getText().toString();
        address2 = etAddress2.getText().toString();
        contact1 = etContact.getText().toString();
        contact2 = etContact2.getText().toString();
        city = etCity.getText().toString();
        postcode = etPostcode.getText().toString();
    }

    public boolean checkTextField() {

        if (etFirstname.length() == 0
                || etFirstname.getText().toString().equals("")) {
            etFirstname.setError("Please Enter FirstName");
            return false;
        } else if (etLastname.length() == 0
                || etLastname.getText().toString().equals("")) {
            etLastname.setError("Please Enter LastName");
            return false;

        } else if (etAddress.length() == 0
                || etAddress.getText().toString().equals("")) {
            etAddress.setError("Please Enter Address");
            return false;
        } else if (etAddress2.getText().toString().equals("")
                || etAddress2.length() == 0) {
            etAddress2.setError("Please Enter 2nd Address ");
            return false;
        } else if (etPostcode.getText().toString().equals("") || etPostcode.length() < 5
                || etPostcode.length() > 9) {
            etPostcode.setError("Please check Postal Code"
                    + etPostcode.length());
            return false;
        } else if (etContact.getText().toString().equals("")
                || etContact.length() == 0) {
            etContact.setError("Please Enter Contact Number");
            return false;
            // } else if (isValidPhoneNumber(etContact.getText().toString())==
            // false) {
            // etContact.setError("Please Enter Valid Contact Number");
            // return false;
        } else if (etContact2.getText().toString().equals("")
                || etContact2.length() == 0) {
            etContact2.setError("Please Enter 2nd Contact Number");
            return false;
            // } else if(MunchBoxValidator.isPhoneNumber(etContact2, false)){
            // etContact2.setError("Please Enter Valid Contact Number");
            // return false;
        } else if (etCity.length() == 0

                || etCity.getText().toString().equals("")) {
            etCity.setError("Please Enter City Name");
            return false;
        }
        return true;
    }

    private void conformationDialog(String msg) {


        final Double discount = subTotalPrice * 0.1;
        totalPrice = subTotalPrice - discount;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                DeliveryInformationActivity.this);

        // set title
        alertDialogBuilder.setTitle("Warning Dialog");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg + "\nyour Total Amount is "+ totalPrice)
                .setCancelable(false)
                .setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity

                                // delieverycharge = dlv;
                                // totalPrice = tempPrice;
                                OrderType = "c";
                                // SettingPushotification();


                                discont = discount;
                                Log.e("Collection", "+" + totalPrice);

                                new DeliveryAsyncTask().execute();
                                dialog.dismiss();

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
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(
                getApplicationContext(), R.color.alertbuttonblue));

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(
                getApplicationContext(), R.color.alertbuttonblue));
    }

    private void registerInBackground() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
                    regID = instanceID.getToken("230633287836", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.e("Regid", regID);

                } catch (IOException ex) {
                    Log.e("Failed", ex.toString());
                }
                return regID;
            }

            @Override
            protected void onPostExecute(String regId) {
                Log.e("Regis", regId + "-");
            }
        }.execute(null, null, null);
    }


    public static class PlaceholderFragment extends Fragment {

        SharedPreferences pref2;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_order,
                    container, false);
            initUI(rootView);
            pref2 = getActivity().getSharedPreferences("fraddys", 0);
            if (GlobalClass.status == 1) {
                SettingDataFromGlobalClasstoTextField();
            } else {
                SettingDataFromSharedPreftoTextField();
            }

            rbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    // TODO Auto-generated method stub

                    if (isChecked) {
                        etFirstname.setText("");
                        etLastname.setText("");
                        // etEmail.setText("");
                        etAddress.setText("");
                        etAddress2.setText("");
                        etContact.setText("");
                        etContact2.setText("");
                        etCity.setText("");
                        etPostcode.setText("");

                    }
                }
            });

            return rootView;
        }

        private void initUI(View v) {
            // TODO Auto-generated method stub

            etFirstname = (EditText) v.findViewById(R.id.etName);
            etLastname = (EditText) v.findViewById(R.id.etLastName);
            // etEmail = (EditText) v.findViewById(R.id.etEmailInProfile);
            etAddress = (EditText) v.findViewById(R.id.etAddress1);
            etAddress2 = (EditText) v.findViewById(R.id.etAddress2);
            etContact = (EditText) v.findViewById(R.id.etContact1);
            etContact2 = (EditText) v.findViewById(R.id.etContact2);
            etCity = (EditText) v.findViewById(R.id.etCity);
            etPostcode = (EditText) v.findViewById(R.id.etPostalCodeInProfile);
            rbtn = (RadioButton) v.findViewById(R.id.radioButtonforClearFields);
        }

        private void SettingDataFromSharedPreftoTextField() {
            // TODO Auto-generated method stub
            etFirstname.setText(pref2.getString(SessionManager.FIRST_NAME, ""));
            etLastname.setText(pref2.getString(SessionManager.LAST_NAME, ""));
            // etEmail.setText(pref2.getString(SessionManager.KEY_EMAIL, ""));
            etAddress.setText(pref2.getString(SessionManager.ADDRESS, ""));
            etAddress2.setText(pref2.getString(SessionManager.ADDRESS2, ""));
            etContact.setText(pref2.getString(SessionManager.CONTACT1, ""));
            etContact2.setText(pref2.getString(SessionManager.CONTACT2, ""));
            etCity.setText(pref2.getString(SessionManager.CITY, ""));
            etPostcode.setText(pref2.getString(SessionManager.POSTCODE, ""));

        }

        private void SettingDataFromGlobalClasstoTextField() {
            // TODO Auto-generated method stub
            etFirstname.setText(GlobalClass.firstName);
            etLastname.setText(GlobalClass.lastName);
            // etEmail.setText(GlobalClass.email);
            etAddress.setText(GlobalClass.address);
            etAddress2.setText(GlobalClass.address2);
            etContact.setText(GlobalClass.contact1);
            etContact2.setText(GlobalClass.contact2);
            etCity.setText(GlobalClass.city);
            etPostcode.setText(GlobalClass.postcode);

        }

    }

    class DeliveryAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(DeliveryInformationActivity.this);
            dialog.show();
            dialog.setCancelable(false);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHandlerReturnString service = new ServiceHandlerReturnString();
            ArrayList<NameValuePair> arrlst = new ArrayList<>();
            gettingDateFromTextField();

            arrlst.add(new BasicNameValuePair("cid", prefID));

            // arrlst.add(new BasicNameValuePair("email", email));
            arrlst.add(new BasicNameValuePair("address1", address1));
            arrlst.add(new BasicNameValuePair("address2", address2));
            arrlst.add(new BasicNameValuePair("contact1", contact1));
            arrlst.add(new BasicNameValuePair("contact2", contact2));
            arrlst.add(new BasicNameValuePair("town_city", city));
            arrlst.add(new BasicNameValuePair("postcode", postcode));
            arrlst.add(new BasicNameValuePair("extrafee", "0.0"));
            arrlst.add(new BasicNameValuePair("type", "Android"));
            arrlst.add(new BasicNameValuePair("ID", regID));


            String convertedDiscount = Double.toString(discont);
            String ctotalPrice = Double.toString(totalPrice);
            String cDeliveryCharge = Double.toString(delieverycharge);


            Log.e("FinalDiscount", convertedDiscount);
            Log.e("FinalTotalPrice", ctotalPrice);
            Log.e("FinalDeliveryCharge", cDeliveryCharge);

            arrlst.add(new BasicNameValuePair("orderDate", date));
            arrlst.add(new BasicNameValuePair("totaldiscount", convertedDiscount));
            arrlst.add(new BasicNameValuePair("totalprice", ctotalPrice));
            arrlst.add(new BasicNameValuePair("comments", specialComment));
            arrlst.add(new BasicNameValuePair("deliverycost", cDeliveryCharge));
            arrlst.add(new BasicNameValuePair("bid", BranchDetailActivity.branch_id2));
            arrlst.add(new BasicNameValuePair("paymentType", "c"));
            arrlst.add(new BasicNameValuePair("orderType", OrderType));
            arrlst.add(new BasicNameValuePair("itemsArray", "" + lst));
            arrlst.add(new BasicNameValuePair("couponCode", couponCode));
            Log.v("Tag Arrlsit", arrlst.toString());
            try {
                String responce = service.makeHttpRequest(URL, arrlst);
                Log.v("Responce", responce);
            } catch (Exception e) {
                Log.v("String retun Null", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();


            Log.e("totalprice", totalPrice + "-");
            Log.e("deliverycost", delieverycharge + "-");


            startActivity(new Intent(DeliveryInformationActivity.this,
                    DeliveryConformationActivity.class));
            finish();
        }
    }

    class GetMileAsyncTask extends AsyncTask<Void, Void, Void> {

        String URL = "http://placeorderonline.com/aminah/webservices/DeliveryPostcode.php";
        String urlResponce = "";
        ProgressDialog pDialog;
        String status, Message;
        String deliveryChargeFromService;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(DeliveryInformationActivity.this);
            pDialog.setCancelable(false);
            pDialog.show();
            gettingDateFromTextField();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            ServiceHandlerReturnString service = new ServiceHandlerReturnString();

            ArrayList<NameValuePair> arrlst = new ArrayList<>();
            arrlst.add(new BasicNameValuePair("pcode", postcode));
            arrlst.add(new BasicNameValuePair("bid", BranchDetailActivity.branch_id2));
            Log.v("TAG Branch", arrlst.toString());

            urlResponce = service.makeHttpRequest(URL, arrlst);

            try {
                JSONArray jsonArr = new JSONArray(urlResponce);
                JSONObject jsonObject = jsonArr.getJSONObject(0);

                Log.e("Response", jsonArr.toString());
                Status = jsonObject.getString("Status");
                Message = jsonObject.getString("Message");

                deliveryChargeFromService = jsonObject.getString("deliveryCharge");

                Log.e("deliveryChargeFromSer", deliveryChargeFromService + "-");

            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.v("TAG DeliveryPostcode", urlResponce);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            try {
                if (Status.equals("OK") && deliveryChargeFromService.equals("1.50")) {

                    if (subTotalPrice < 10) {
                        AlertDialogManager.showAlertDialog(
                                DeliveryInformationActivity.this,
                                "Delivery not Available",
                                "Delivery not Available in your area under £10",
                                false);

                        //     Toast.makeText(getApplicationContext(), "Delivery not Available in your area under £10", Toast.LENGTH_SHORT).show();
                    } else if (subTotalPrice > 10) {

                        delieverycharge = 1.5;
                   /* totalPrice = subTotalPrice + delieverycharge - discont;
                    new DeliveryAsyncTask().execute();*/

                        alertForCheckDelivery();
                    }


                } else if (Status.equals("OK") && deliveryChargeFromService.equals("0")) {
                    if (subTotalPrice > 10) {
                        delieverycharge = 0;
                        alertForCheckDelivery();
                    } else {
                        delieverycharge = 1.5;
                    /*totalPrice = subTotalPrice + delieverycharge - discont;
                    new DeliveryAsyncTask().execute();*/
                        alertForCheckDelivery();
                    }
                } else if (Status.equals("collectionOnly")) {
                    conformationDialog("Delivery not available in your area. would you like to collect your order with 10% discount? ");

                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Post Code", Toast.LENGTH_SHORT).show();

                }
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Network Error ", Toast.LENGTH_SHORT).show();
            }
        }

        private void alertForCheckDelivery() {

            totalPrice = subTotalPrice + delieverycharge - discont;

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    DeliveryInformationActivity.this);

            // set title
            alertDialogBuilder.setTitle("");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Your Delivery Charge is " + delieverycharge
                            + " Your new Total amount is" + (totalPrice))
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.dismiss();
                            new DeliveryAsyncTask().execute();
                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // if this button is clicked, close
                    // current activity
                    dialog.dismiss();

                }
            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // show it
            alertDialog.show();
        }
    }
}
