package activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skytech.aminatandoori.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.BasketAdapter;
import adapter.ListExpandableAdapter;
import dialog.AlertDialogManager;
import dialog.loginDialog;
import interfaces.BasketItemButtonListener;
import interfaces.GeneralCommunicator;
import model.AddbasketBeanFromHome;
import session.SessionManager;
import util.ConnectionDetector;
import util.GlobalClass;

public class BasketActivity extends Activity implements OnClickListener,
        loginDialog.Communicator, GeneralCommunicator {
    //perm=com.aibglobal.freddychickenandpizza.permission.C2D_MESSAGE pkg=com.skytech.munchbox
    public static View viewForSpecialItem;
    public static boolean isDeliveryCharge;
    static boolean isAskForPrice;

    Double deliveryDiscount = 0.0;
    Double collectionDiscount = 0.0;

    Double ddiscount = 0.0;
    String couponCode = "";
    String strCouponStatus;

    private View ActivityView;
    private boolean isOrderTypeRadioGroupChecked;
    // Fragment Veriable...
    private double subtotalPrice;
    private double discount = 0.0;
    private double deliveryCharge = 0.0;
    private double deliveryTotalPrice;
    private String orderType;
    private Double alltotalPrice = 0.0;
    private DecimalFormat df = new DecimalFormat("#.##");
    private Double deliveryOnCharge = 0.00;
    private Button bLogin, bBack;
    private ImageButton imgBtnFavouriteID;
    private ImageButton imgBtnOfferID;
    private ImageButton imgBtnBasketID;
    private ImageButton imgBtnSettingsID;
    private ImageButton imgBtnHomeID;
    private TextView tvbasketCount;
    private SessionManager session;
    private TextView tvSubTotlaPrice, tvDiscount, tvTotalPrice, tvDeliveryCharge;
    private EditText etSpecialComment;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    private ArrayList<AddbasketBeanFromHome> homebasketArray = new ArrayList<>();
    private BasketFragment basketFragment;

    public BasketActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_activity);

        basketFragment = new BasketFragment();
        if (savedInstanceState != null) {
            Log.i("TAG", "Saved Instance state");
        } else {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, basketFragment).commit();
        }
        InitMainUI();

        session = new SessionManager(BasketActivity.this);
        bLogin.setText("Done");

        bracnhTimingStatus();

        ActivityView = this.findViewById(android.R.id.content);
        GlobalClass.globalObj.showCircleCount(getApplicationContext(),
                ActivityView);
        viewForSpecialItem = ActivityView;
    }

    // Checking the Branch Timing if it is close OR Pre Order than show dialog
    // box

    private void bracnhTimingStatus() {

        if (GlobalClass.branchTimeing.equals("Closed")) {
            bLogin.setVisibility(View.INVISIBLE);
            AlertDialogManager
                    .showAlertDialog(
                            BasketActivity.this,
                            "Sorry",
                            "We can not proceed your order,We are closed today!",
                            false);

        } else if (GlobalClass.branchTimeing.equals("Warning")) {
            AlertDialogManager.timePickerDialog(BasketActivity.this);
        }
    }

    // Checking the Internet Connection if the Internet Connection is not
    // Present Show an Error
    void checkInternetConnection() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {

            // Internet connection is not present
            // Ask user to connect to Internet
            ConnectionDetector.showAlertDialog(BasketActivity.this,
                    "No Internet Connection",
                    "You don't have internet connection.", false);
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnHome_DoneID:
                boolean status2 = session.isLoggedIn();

                if (GlobalClass.globalBasketList.size() == 0) {

                    AlertDialogManager.showAlertDialog(BasketActivity.this,
                            "Warning", "You have entered no item", false);

                } else {

                    if (status2 || GlobalClass.status == 1) {

                        if (isOrderTypeRadioGroupChecked) {

                            Intent intent = new Intent(BasketActivity.this, DeliveryInformationActivity.class);

                            intent.putExtra("couponCode", couponCode);
                            intent.putExtra("ItemsubPrice", alltotalPrice);
                            intent.putExtra("totalPrice", deliveryTotalPrice);
                            intent.putExtra("discount", ddiscount);
                            intent.putExtra("orderType", orderType);
                            intent.putExtra("deliveryCharge", deliveryCharge);
                            intent.putExtra("Special_Comments", etSpecialComment.getText().toString());

                            Log.e("Discount", ddiscount + "-");


                            startActivity(intent);

                        } else {
                            alertForDeliveryOrderType();
                        }
                    } else if (GlobalClass.status == 1) {
                        if (isOrderTypeRadioGroupChecked) {

                            Intent intent = new Intent(BasketActivity.this, DeliveryInformationActivity.class);
                            intent.putExtra("couponCode", couponCode);
                            intent.putExtra("ItemsubPrice", alltotalPrice);
                            intent.putExtra("totalPrice", deliveryTotalPrice);
                            intent.putExtra("discount", ddiscount);
                            intent.putExtra("orderType", orderType);
                            Log.e("Discount", ddiscount + "-");
                            intent.putExtra("deliveryCharge", deliveryCharge);
                            intent.putExtra("Special_Comments", etSpecialComment.getText().toString());
                            startActivity(intent);

                        } else {
                            alertForDeliveryOrderType();
                        }
                    } else {
                        new loginDialog(BasketActivity.this, "Basket");
                    }
                }
                break;
            case R.id.imgBtnHomeID:
                Intent homeIntent = new Intent(BasketActivity.this,
                        HomeActivity.class);
                homeIntent.putExtra("status", true);
                startActivity(homeIntent);

                break;
            case R.id.imgBtnSettingsID:
                startActivity(new Intent(BasketActivity.this,
                        SettingActivity.class));

                break;
            case R.id.imgBtnOfferID:
                startActivity(new Intent(BasketActivity.this,
                        OffersActivity.class));

            case R.id.imgBtnBasketID:

                break;
            case R.id.imgBtnFavouriteID:
                if (session.isLoggedIn() || GlobalClass.status == 1) {
                    startActivity(new Intent(BasketActivity.this,
                            FavoriteActivity.class));
                } else {
                    new loginDialog(BasketActivity.this);
                    changeLoginText();
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

    public void changeLoginText() {
        bLogin.setText("Profile");
    }


    public void setTotalPrice() {

        ddiscount = 0.0;
        ddiscount = alltotalPrice * discount;
        deliveryTotalPrice = alltotalPrice + deliveryCharge - ddiscount;
        String total = df.format(deliveryTotalPrice);


        tvDiscount.setText("£ " + df.format(ddiscount));
        tvTotalPrice.setText("£" + total);
    }

    private void alertForDeliveryOrderType() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BasketActivity.this);

        // set title
        alertDialogBuilder.setTitle("Order Type Dialog");

        // set dialog message
        alertDialogBuilder.setMessage("Please Select Order Type")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                        dialog.dismiss();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    /* private void alertForCheckDeliver() {
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                 BasketActivity.this);

         // set title
         alertDialogBuilder.setTitle("");

         // set dialog message
         alertDialogBuilder
                 .setMessage(
                         "Please Select Collection Delivery is not available under £ 10 ")
                 .setCancelable(false)
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
 */
    @Override
    public void loginResponce(String data) {
        bLogin.setText("Done");
    }

    @Override
    public void getTimeResponce(String time) {
        etSpecialComment.setText(time);
        GlobalClass.pre_Order_Timing = time;
    }

    @Override
    public void gettingAgeResponce(String age) {

    }

    @Override
    public void urlStuff(String urlData) {


    }

    public class BasketFragment extends Fragment implements
            BasketItemButtonListener {

        ListView basketListview;
        BasketAdapter adapter;
        Button couponButton;
        RadioGroup mOrderTypeRadioGroup;
        RadioButton rbDelivery, rbCollection;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_basket_layout, container,
                    false);
            initUI(view);

            basketListview = (ListView) view.findViewById(R.id.basketListview);

            if (GlobalClass.globalBasketList.size() > 0) {
                //
                adapter = new BasketAdapter(getActivity(),
                        GlobalClass.globalBasketList, basketFragment);

                basketListview.setAdapter(adapter);
            }
            for (int basketitem = 0; basketitem < GlobalClass.globalBasketList
                    .size(); basketitem++) {

                AddbasketBeanFromHome homeItemBean = new AddbasketBeanFromHome();
                homeItemBean.setItemName(GlobalClass.globalBasketList.get(
                        basketitem).getItemName());
                homeItemBean.setQuantity(GlobalClass.globalBasketList.get(
                        basketitem).getQuantity());
                homebasketArray.add(homeItemBean);

            }

            Log.v("Basket home item", homebasketArray.toString());

            setSubTotalPrice();
            setTotalPrice();

            basketListview.setOnItemClickListener(null);

            mOrderTypeRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    RadioButton checkedRadioButton = (RadioButton) mOrderTypeRadioGroup.findViewById(checkedId);

                    isOrderTypeRadioGroupChecked = checkedRadioButton.isChecked();
                    if (isOrderTypeRadioGroupChecked) {
                        orderType = checkedRadioButton.getText().toString();

                        if (orderType.equals("Delivery")) {

                            orderType = "d";

                            if (alltotalPrice < deliveryOnCharge)
                                deliveryCharge = 1.5;
                            else
                                deliveryCharge = 0;

                            tvDeliveryCharge.setText("£" + deliveryCharge);

                            discount = deliveryDiscount;

                            setTotalPrice();

                        } else if (orderType.equals("Collection")) {
                            orderType = "c";
                            // if (deliveryCharge != 0.00) {
                            deliveryCharge = 0;
                            tvDeliveryCharge.setText("£" + deliveryCharge);

                            discount = collectionDiscount;

                            setTotalPrice();
                            // }
                        }
                    }
                }
            });
            deliverCharge();
            //   tvDeliveryCharge.setText("£" + deliveryCharge);
            return view;

        }

        private void deliverCharge() {
            // TODO Auto-generated method stub

            if (alltotalPrice < deliveryOnCharge) {
                deliveryCharge = 1.50;
                isDeliveryCharge = true;
                tvDeliveryCharge.setText("£" + deliveryCharge);
            } else {
                deliveryCharge = 0.00;
                isDeliveryCharge = false;
                tvDeliveryCharge.setText("£" + deliveryCharge);
            }

            deliveryTotalPrice = alltotalPrice + deliveryCharge;
            tvTotalPrice.setText("£" + df.format(deliveryTotalPrice));
        }

        private void addButtonPLus() {

            int blength = GlobalClass.globalBasketList.size();
            Double t = 0.0;
            Log.v("blength", "" + blength);
            for (int k = 0; k <= blength - 1; k++) {

                String itemqty = GlobalClass.globalBasketList.get(k)
                        .getQuantity();
                String itemprice = GlobalClass.globalBasketList.get(k)
                        .getItemPrice();
                Log.i("TAG TOTAL", itemqty + " - " + itemprice);
                int qty = Integer.parseInt(itemqty);

                try {
                    Double prce = Double.parseDouble(itemprice);
                    Double mul = prce * qty;
                    t = t + mul;
                    isAskForPrice = false;
                } catch (NumberFormatException e) {
                    isAskForPrice = true;
                    Toast.makeText(getActivity(), "" + itemprice,
                            Toast.LENGTH_LONG).show();
                }

                Log.v("TOTAL", "" + t);
            }

            String total = df.format(t);
            tvSubTotlaPrice.setText("£" + total);
            alltotalPrice = t;
            deliverCharge();
            tvTotalPrice.setText("£" + df.format(alltotalPrice));

        }

        private void setSubTotalPrice() {
            // TODO Auto-generated method stub
            int blength = GlobalClass.globalBasketList.size();
            Log.v("blength", "" + blength);
            for (int k = 0; k <= blength - 1; k++) {

                String itemqty = GlobalClass.globalBasketList.get(k)
                        .getQuantity();

                String itemprice = GlobalClass.globalBasketList.get(k)
                        .getItemPrice();

                Log.i("TAG TOTAL", itemqty + " - " + itemprice);
                int qty = Integer.parseInt(itemqty);

                try {
                    Double prce = Double.parseDouble(itemprice);
                    subtotalPrice = prce * qty;
                    alltotalPrice = alltotalPrice + subtotalPrice;
                    isAskForPrice = false;
                } catch (RuntimeException e) {
                    isAskForPrice = true;
                    Toast.makeText(getActivity(), "" + itemprice,
                            Toast.LENGTH_LONG).show();
                }
                Log.v("TOTAL", "" + alltotalPrice);
            }
            String total = df.format(alltotalPrice);
            tvSubTotlaPrice.setText("£" + total);

        }

        public void initUI(View view) {

            tvDiscount = (TextView) view.findViewById(R.id.tvDiscountPrice);
            couponButton = (Button) view.findViewById(R.id.bCoupin);
            tvSubTotlaPrice = (TextView) view.findViewById(R.id.tvSubTotalPrice);
            tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);
            mOrderTypeRadioGroup = (RadioGroup) view.findViewById(R.id.order_type_radio_group);
            rbDelivery = (RadioButton) view.findViewById(R.id.rbDelivery);
            rbCollection = (RadioButton) view.findViewById(R.id.rbCollection);
            tvDeliveryCharge = (TextView) view.findViewById(R.id.tvDeliveryCharge);
            etSpecialComment = (EditText) view.findViewById(R.id.etSpecialComment);


            couponButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isOrderTypeRadioGroupChecked) {
                        final Dialog dialog = new Dialog(BasketActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        // Include dialog.xml file
                        dialog.setContentView(R.layout.coupon_dialog_layout);
                        //

                        final EditText editText = (EditText) dialog
                                .findViewById(R.id.couponText);
                        Button button = (Button) dialog
                                .findViewById(R.id.btnEnterCoupon);

                        dialog.show();
                        final RequestQueue requestQueue = Volley
                                .newRequestQueue(BasketActivity.this);

                        button.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Close dialog
                                final String couponString = editText.getText()
                                        .toString();
                                if (couponString.equals("")) {
                                    editText.setError("Enter Coupon Here");
                                } else {

                                    dialog.dismiss();
                                    String URL = "http://placeorderonline.com/aminah/webservices/discountCoupon.php";


                                    final ProgressDialog progressDialog = new ProgressDialog(
                                            BasketActivity.this);
                                    progressDialog.show();

                                    StringRequest strRequest = new StringRequest(
                                            Request.Method.POST, URL,
                                            new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(
                                                        String response) {
                                                    Log.e("Coupon", response + ".");
                                                    progressDialog.dismiss();

                                                    try {
                                                        JSONArray jsonArray = new JSONArray(
                                                                response);
                                                        JSONObject jsonObject = (JSONObject) jsonArray
                                                                .get(0);

                                                        Log.e("Coupon Response", "-" + jsonObject.toString());

                                                        strCouponStatus = jsonObject
                                                                .getString("status");

                                                        String delivery = jsonObject.getString("delivery");
                                                        String collection = jsonObject.getString("collection");

                                                        couponCode = couponString;
                                                        deliveryDiscount = Double.parseDouble(delivery) / 100;
                                                        collectionDiscount = Double.parseDouble(collection) / 100;


                                                        if (orderType.equals("c")) {
                                                            discount = collectionDiscount;
                                                        } else if (orderType.equals("d")) {
                                                            discount = deliveryDiscount;
                                                        }
                                                /*    strDiscount = jsonObject
                                                            .getString("discount");


                                                    discount = Double
                                                            .parseDouble("0."
                                                                    + strDiscount);
                                                    Log.e("Double", discount
                                                            + ".");*/

                                                        setTotalPrice();

                                                    } catch (JSONException e) {
                                                        AlertDialogManager
                                                                .showAlertDialog(
                                                                        BasketActivity.this,
                                                                        "Not Found",
                                                                        "Invalid Coupon",
                                                                        false);
                                                        e.printStackTrace();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(
                                                VolleyError error) {
                                            Log.e("Coupon",
                                                    error.toString() + ".");
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Network Error",
                                                    Toast.LENGTH_SHORT)
                                                    .show();

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams()
                                                throws AuthFailureError {
                                            Map<String, String> map = new HashMap<>();
                                            map.put("cno", couponString);
                                            return map;
                                        }
                                    };
                                    requestQueue.add(strRequest);
                                    dialog.dismiss();

                                }
                            }
                        });

                    } else {
                        AlertDialogManager.showAlertDialog(
                                BasketActivity.this,
                                "Select Order Type",
                                "Please Select Order Type Delivery or Collection",
                                false);

                    }
                }

            });


        }


        @Override
        public void onMinusButtonClick(int position, TextView tv) {
            String itemqtyM = GlobalClass.globalBasketList.get(position)
                    .getQuantity();
            if (itemqtyM.equals("1")) {

                GlobalClass.globalBasketList.get(position).setQuantity("0");

                GlobalClass.globalBasketList.remove(position);

                try {
                    ListExpandableAdapter.basketarr.remove(position);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                adapter = new BasketAdapter(getActivity(),
                        GlobalClass.globalBasketList, basketFragment);
                adapter.notifyDataSetChanged();
                basketListview.setAdapter(adapter);
                addButtonPLus();
                GlobalClass.setCircleCountValueMinus(ActivityView);
                setTotalPrice();

            } else {
                int qty2 = Integer.parseInt(itemqtyM);

                qty2--;
                tv.setText("" + qty2);
                GlobalClass.globalBasketList.get(position).setQuantity(
                        "" + qty2);
                addButtonPLus();
                // deliverCharge();
                GlobalClass.setCircleCountValueMinus(ActivityView);
                setTotalPrice();
            }
        }

        @Override
        public void onAddButtonClick(int position, TextView tv) {
            String itemqty = GlobalClass.globalBasketList.get(position)
                    .getQuantity();
            int qty = Integer.parseInt(itemqty);
            qty++;

            tv.setText("" + qty);

            GlobalClass.globalBasketList.get(position).setQuantity("" + qty);

            addButtonPLus();

            try {
                if (!orderType.equals("c"))
                    deliverCharge();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            GlobalClass.setCircleCountValuePlus(ActivityView);

            setTotalPrice();
        }
    }
}
