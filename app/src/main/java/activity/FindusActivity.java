package activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import parser.FindusParser;
import util.GlobalClass;

public class FindusActivity extends Activity implements OnClickListener {

    TextView tvbasketCount;

    Button bLogin, bBack;
    ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
            imgBtnSettingsID, imgBtnHomeID;
    View ActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new FindUsFragment()).commit();
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

        tvbasketCount.setVisibility(View.GONE);

        bBack.setOnClickListener(this);
        imgBtnBasketID.setOnClickListener(this);
        imgBtnFavouriteID.setOnClickListener(this);
        imgBtnOfferID.setOnClickListener(this);
        imgBtnSettingsID.setOnClickListener(this);
        //bLogin.setOnClickListener(this);
        imgBtnHomeID.setOnClickListener(this);
        bLogin.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.imgBtnHomeID:
                Intent homeIntent = new Intent(FindusActivity.this,
                        HomeActivity.class);
                startActivity(homeIntent);

                break;

            case R.id.imgBtnSettingsID:
                startActivity(new Intent(FindusActivity.this, SettingActivity.class));
                break;

            case R.id.imgBtnOfferID:
                startActivity(new Intent(FindusActivity.this, OffersActivity.class));

                break;

            case R.id.imgBtnBasketID:
                startActivity(new Intent(FindusActivity.this, BasketActivity.class));

                break;
            case R.id.imgBtnFavouriteID:
                if (GlobalClass.status == 1) {
                    startActivity(new Intent(FindusActivity.this,
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

    class FindUsFragment extends Fragment {
        String URL = "http://placeorderonline.com/aminah/webservices/FindUs.php";
        String branchname, contact2, branchaddress1, postCode, branchaddress2, contact1, townCity;
        String mondaycl, tuesdaycl, wedensdaycl, thursdaycl, fridaycl,
                saturdaycl, sundaycl;
        String mondayOT, tuesdayOT, wedensdayOT, thursdayOT, fridayOT,
                saturdayOT, sundayOT;

        TextView tvAddress, tvContact, tvEmail, tvMonday, tvTuesday,
                tvWedensday, tvThursday, tvFriday, tvSaturday, tvSunday;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub\
            View view = inflater.inflate(R.layout.find_us_activity, container,
                    false);
            InitUI(view);

            return view;
        }

        private void InitUI(View v) {
            // TODO Auto-generated method stub
            tvAddress = (TextView) v.findViewById(R.id.tvAddress);
            tvContact = (TextView) v.findViewById(R.id.tvContact);
            tvEmail = (TextView) v.findViewById(R.id.tvEmail);
            tvMonday = (TextView) v.findViewById(R.id.tvMonday);
            tvTuesday = (TextView) v.findViewById(R.id.tvThuesday);
            tvWedensday = (TextView) v.findViewById(R.id.tvWedensday);
            tvThursday = (TextView) v.findViewById(R.id.tvThurday);
            tvFriday = (TextView) v.findViewById(R.id.tvFriday);
            tvSaturday = (TextView) v.findViewById(R.id.tvsaturday);
            tvSunday = (TextView) v.findViewById(R.id.tvSunday);
            new GetFindusAsyncTask().execute();

        }

        void prepareClosingAndOpeningTime() {
            if (mondaycl.equals("1")) {
                tvMonday.setText(String.format("%s", "Close"));
            } else if (!mondaycl.equals("1")) {
                tvMonday.setText(mondayOT);
            }
            if (tuesdaycl.equals("1")) {
                tvTuesday.setText(String.format("%s", "Close"));
            } else if (!tuesdaycl.equals("1")) {
                tvTuesday.setText(tuesdayOT);
            }
            if (wedensdaycl.equals("1")) {
                tvWedensday.setText(String.format("%s", "Close"));
            } else {
                tvWedensday.setText(wedensdayOT);
            }
            if (thursdaycl.equals("1")) {
                tvThursday.setText(String.format("%s", "Close"));
            } else {
                tvThursday.setText(thursdayOT);
            }
            if (fridaycl.equals("1")) {
                tvFriday.setText(String.format("%s", "Close"));
            } else {
                tvFriday.setText(fridayOT);
            }
            if (saturdaycl.equals("1")) {
                tvSaturday.setText(String.format("%s", "Close"));
            } else {
                tvSaturday.setText(saturdayOT);
            }
            if (sundaycl.equals("1")) {
                tvSunday.setText(String.format("%s", "Close"));
            } else {
                tvSunday.setText(sundayOT);
            }

        }

        class GetFindusAsyncTask extends AsyncTask<Void, Void, Void> {

            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                dialog = new ProgressDialog(FindusActivity.this);
                dialog.show();
                dialog.setCancelable(false);
            }

            @SuppressWarnings("deprecation")
            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                FindusParser parser = new FindusParser();
                ArrayList<NameValuePair> arrlst = new ArrayList<>();
                arrlst.add(new BasicNameValuePair("bid",
                        BranchDetailActivity.branch_id2));
                try {
                    JSONArray jsonArr = parser.makeHttpRequest(URL, arrlst);
                    Log.i("Findus lengtj", "" + jsonArr.length());
                    if (jsonArr.length() != 0) {

                        for (int i = 0; i < jsonArr.length(); i++) {
                            try {
                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                /*
       [{"fridayCT":"23:59:00","tuesdayCl":"0","contact1":"0161 941 1234","address1":"2 Peter Street","contact2":"0161 941 3222","address2":"-","fridayOT":"17:00:00","wednesdayOT":"17:00:00","longi":"-2.3519962","id":"2","thursdayCl":"0","tuesdayOT":"17:00:00","name":"Aminah Tandoori","sundayOT":"17:00:00","saturdayCl":"0","status":"1","mondayCT":"23:00:00","saturdayOT":"17:00:00","postcode":"WA14 2DS","sundayCl":"0","thursdayOT":"17:00:00","mondayOT":"17:00:00","wednesdayCT":"23:00:00","tuesdayCT":"23:00:00","saturdayCT":"23:59:00","mondayCl":"0","sundayCT":"23:00:00","email":"support@skytechenterprise.co.uk","wednesdayCl":"0","fridayCl":"0","thursdayCT":"23:00:00","lati":"53.3835113","townCity":"Altrincham"}]

                                */
                                contact2 = jsonObj.getString("contact2");
                                postCode = jsonObj.getString("postcode");
                                branchname = jsonObj.getString("name");
                                branchaddress1 = jsonObj.getString("address1");
                                branchaddress2 = jsonObj.getString("address2");
                                contact1 = jsonObj.getString("contact1");
                                // email = jsonObj.getString("email");
                                townCity = jsonObj.getString("townCity");
                                mondaycl = jsonObj.getString("mondayCl");
                                tuesdaycl = jsonObj.getString("tuesdayCl");
                                wedensdaycl = jsonObj.getString("wednesdayCl");
                                thursdaycl = jsonObj.getString("thursdayCl");
                                fridaycl = jsonObj.getString("fridayCl");
                                saturdaycl = jsonObj.getString("saturdayCl");
                                sundaycl = jsonObj.getString("sundayCl");
                                //
                                mondayOT = jsonObj.getString("mondayOT") + "-"
                                        + jsonObj.getString("mondayCT");
                                tuesdayOT = jsonObj.getString("tuesdayOT")
                                        + " - "
                                        + jsonObj.getString("tuesdayCT");
                                wedensdayOT = jsonObj.getString("wednesdayOT")
                                        + " - "
                                        + jsonObj.getString("wednesdayCT");
                                thursdayOT = jsonObj.getString("thursdayOT")
                                        + " - "
                                        + jsonObj.getString("thursdayCT");
                                fridayOT = jsonObj.getString("fridayOT")
                                        + " - " + jsonObj.getString("fridayCT");
                                saturdayOT = jsonObj.getString("saturdayOT")
                                        + " - "
                                        + jsonObj.getString("saturdayCT");
                                sundayOT = jsonObj.getString("sundayOT")
                                        + " - " + jsonObj.getString("sundayCT");

                                Log.v("Timing", mondayOT);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                dialog.dismiss();
                tvAddress.setText( branchaddress1+ " \n" +  townCity + "\n" + postCode);
                tvContact.setText(contact1 +"\n" +contact2);
                // tvEmail.setText("E"+email);
                // tvTuesday.setText(tuesdayOT);
                try {
                    prepareClosingAndOpeningTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
