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
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ListExpandableAdapter;
import dialog.loginDialog;
import model.Items;
import parser.ServiceHandlerReturnString;
import session.SessionManager;
import util.GlobalClass;


public class HomeActivity extends Activity implements OnClickListener,
        loginDialog.Communicator {

    static Button btnLogin, btnBack;
    ProgressDialog pDialog;
    String URL = "http://placeorderonline.com/aminah/webservices/GetMainMenu.php";
    List<List<Items>> mainlist;
    JSONObject nextobj;
    List<Items> listDataHeader;
    JSONArray array;
    JSONObject obj;
    ListExpandableAdapter listAdapter;
    ExpandableListView listview;
    HashMap<String, List<Items>> hashlist;
    TextView tvbasketCount;
    SessionManager session;
    boolean status = false;
    String branch_ID = BranchDetailActivity.branch_id2;
    View ActivityView;
    ImageButton imgBtnFavouriteID, imgBtnOfferID, imgBtnBasketID,
            imgBtnSettingsID, imgBtnHomeID;

    private String gotDatFromUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new ItemChoiceFragment()).commit();

            mainlist = new ArrayList<>();

            InitMainUI();
            Bundle bundle = getIntent().getExtras();

            ActivityView = this.findViewById(android.R.id.content);
            GlobalClass.globalObj.showCircleCount(getApplicationContext(), ActivityView);

            if (bundle != null) {
                status = bundle.getBoolean("status");
            }


            session = new SessionManager(HomeActivity.this);

            if (GlobalClass.status == 1) {
                btnLogin.setText("Profile");
            }

            if (GlobalClass.itemDiscardStatus) {
                GlobalClass.setCircleCountValueMinus(ActivityView);
            }
        } else {
            Log.i("DATA", savedInstanceState.getString("data", "Jello"));
            Toast.makeText(getApplicationContext(), "Instance Restore", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(getApplicationContext(),
                "Instance Restore in On Restore", Toast.LENGTH_SHORT).show();
    }

    void InitMainUI() {

        tvbasketCount = (TextView) findViewById(R.id.basketCount);
        imgBtnBasketID = (ImageButton) findViewById(R.id.imgBtnBasketID);
        imgBtnFavouriteID = (ImageButton) findViewById(R.id.imgBtnFavouriteID);
        imgBtnOfferID = (ImageButton) findViewById(R.id.imgBtnOfferID);
        imgBtnSettingsID = (ImageButton) findViewById(R.id.imgBtnSettingsID);
        btnLogin = (Button) findViewById(R.id.btnHome_DoneID);
        imgBtnHomeID = (ImageButton) findViewById(R.id.imgBtnHomeID);
        btnBack = (Button) findViewById(R.id.btnHome_BackID);

        btnBack.setOnClickListener(this);
        imgBtnBasketID.setOnClickListener(this);
        imgBtnFavouriteID.setOnClickListener(this);
        imgBtnOfferID.setOnClickListener(this);
        imgBtnSettingsID.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        imgBtnHomeID.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnHome_DoneID:

                // String loginstatus = bLogin.getText().toString();
                boolean loginSessionStatus = session.isLoggedIn();
                if (loginSessionStatus) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                } else if (GlobalClass.status == 1) {
                    startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                } else {
                    new loginDialog(HomeActivity.this);
                }
                break;
            case R.id.imgBtnHomeID:

                break;
            case R.id.imgBtnSettingsID:
                startActivity(new Intent(HomeActivity.this, SettingActivity.class));

                break;
            case R.id.imgBtnOfferID:
                Intent homeIntent = new Intent(HomeActivity.this,
                        OffersActivity.class);
                startActivity(homeIntent);

                break;

            case R.id.imgBtnBasketID:
                // GlobalClass.globalBasketList.add(ListExpandable.basketarr);
                Intent i = new Intent(HomeActivity.this, BasketActivity.class);
                startActivity(i);

                break;
            case R.id.imgBtnFavouriteID:
                if (session.isLoggedIn() || GlobalClass.status == 1) {
                    startActivity(new Intent(HomeActivity.this,
                            FavoriteActivity.class));
                } else {
                    new loginDialog(HomeActivity.this);
                }
                break;
            case R.id.btnHome_BackID:
                GlobalClass.globalBasketList.clear();

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
        if (GlobalClass.status == 1) {
            btnLogin.setText("Profile");
        }
    }

    class getMenu extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected Void doInBackground(Void... params) {

            ServiceHandlerReturnString urlResponce = new ServiceHandlerReturnString();
            @SuppressWarnings("deprecation")
            ArrayList<NameValuePair> arr = new ArrayList<>();
            arr.add(new BasicNameValuePair("bid", branch_ID));
            try {
                gotDatFromUrl = urlResponce.makeHttpRequest(URL, arr, HomeActivity.this);

                Log.v("TAG URL Data ", gotDatFromUrl);
                array = new JSONArray(gotDatFromUrl);

                List<Items> temparrlist = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {

                    obj = array.getJSONObject(i);

                    if (i < array.length() - 1) {
                        nextobj = array.getJSONObject(i + 1);
                    }

                    // Log.v("JSON", "" + obj);
                    Items item = new Items();

                    item.setCat_ID(obj.getString("Cat_id"));
                    item.setCat_name(obj.getString("categoryName"));
                    item.setCat_desc(obj.getString("Cat_desc"));
                    item.setItemID(obj.getString("itemID"));
                    item.setItem_Desc(obj.getString("Item_Desc"));

                    item.setItemName(obj.getString("ItemName"));
                    item.setItemPrice(obj.getString("itemsPrice"));
                    item.setOfferChoices(obj.getString("offerChoices"));

                    Log.i("ITEM ID", "" + item.getItemID());
                    temparrlist.add(item);

                    if (i == array.length() - 1) {
                        mainlist.add(temparrlist);
                        break;
                    }

                    if (!obj.getString("categoryName").equals(
                            nextobj.get("categoryName"))) {
                        mainlist.add(temparrlist);
                        temparrlist = new ArrayList<>();
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            PrepareList();
            listAdapter = new ListExpandableAdapter(HomeActivity.this, listDataHeader,
                    hashlist, branch_ID, ActivityView);
            listview.setAdapter(listAdapter);

        }

        private void PrepareList() {
            // TODO Auto-generated method stub
            listDataHeader = new ArrayList<>();

            List<Items> subItem2 = new ArrayList<>();
            hashlist = new HashMap<>();

            for (int i = 0; i < mainlist.size(); i++) {
                Items headerItems = new Items();
                headerItems.setCat_name(mainlist.get(i).get(0).getCat_name());
                headerItems.setCat_desc(mainlist.get(i).get(0).getCat_desc());
                listDataHeader.add(headerItems);


                for (int j = 0; j < mainlist.get(i).size(); j++) {
                    try {
                        Items item = new Items();
                        item.setItemName(mainlist.get(i).get(j).getItemName());
                        item.setItemPrice(mainlist.get(i).get(j).getItemPrice());
                        item.setItem_Desc(mainlist.get(i).get(j).getItem_Desc());
                        item.setItemID(mainlist.get(i).get(j).getItemID());
                        item.setCat_ID(mainlist.get(i).get(j).getCat_ID());
                        item.setOfferChoices(mainlist.get(i).get(j)
                                .getOfferChoices());
                        item.setQuantity("0");

                        for (int k = 0; k < GlobalClass.globalBasketList.size(); k++) {
                            if (item.getItemID()
                                    .equals(GlobalClass.globalBasketList.get(k)
                                            .getID())) {
                                item.setQuantity(GlobalClass.globalBasketList
                                        .get(k).getQuantity());
                            }
                        }
                        subItem2.add(item);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                hashlist.put(listDataHeader.get(i).getCat_name(), subItem2);
                subItem2 = new ArrayList<>();
            }

        }
    }

    public class ItemChoiceFragment extends Fragment {
        View view;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onActivityCreated(savedInstanceState);
            listview = (ExpandableListView) view.findViewById(R.id.lvExp);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            view = inflater.inflate(R.layout.item_choice_fragment_layout,
                    container, false);
            if (GlobalClass.status == 1) {
                btnLogin.setText("Profile");
            }
            new getMenu().execute();
            return view;

        }

    }
}
