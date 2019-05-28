package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.ItemChoiceAdapter;
import dialog.loginDialog;
import model.AddbasketBeanFromHome;
import model.ItemChoiceBean;
import parser.BranchListParser;
import util.ConnectionDetector;
import util.GlobalClass;

public class ItemFromActivty extends Activity implements OnClickListener {

    public static ArrayList<AddbasketBeanFromHome> selectedITem = new ArrayList<>();
    public static ArrayList<AddbasketBeanFromHome> basketModelArrlist = new ArrayList<>();
    TextView tvItemName, tvItemPrice;
    String URL = "http://placeorderonline.com/aminah/webservices/GetItemChoices.php";
    ArrayList<ItemChoiceBean> temparrlist;
    List<List<ItemChoiceBean>> arrlist2;
    ListView listChoice;
    JSONObject nextItem;
    Button bNext, bAddItem, bDiscard, Bback, bLogin;
    int count = 0;
    LinearLayout lineartext;
    List<String> countList = new ArrayList<>();
    TextView tvItem;
    int length;
    String concatednate = "";
    int selectedChoiceRepeat = 0;
    List<String> list;
    AddbasketBeanFromHome model2 = new AddbasketBeanFromHome();
    DecimalFormat df = new DecimalFormat("#.##");
    Double tempchoicePrice = 0.0;
    String ItemName;
    String price;
    String desc;
    String ItemID, branch_ID;
    View ActivityView;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    boolean find = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_form);
        list = new ArrayList<>();
        InitUI();
        Bundle b = getIntent().getExtras();


        if (b != null) {
            ItemName = b.getString("Name");
            price = b.getString("price");
            desc = b.getString("desc");
            branch_ID = b.getString("branch_ID");
            ItemID = b.getString("itemID");

            list.add(desc);    // Add Item Name and Descritption to Final  listAdapter to be display
            countList.add("1"); // Increase Counter for checking Repeating items //

            tvItemName.setText(ItemName);
            tvItemPrice.setText(price);
            tvItem.setText(desc);

            tempchoicePrice = Double.parseDouble(price);

        }
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            new ItemChoicesAsync().execute();
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            ConnectionDetector.showAlertDialog(ItemFromActivty.this,
                    "No Internet Connection",
                    "You don't have internet connection.", false);

        }

        listChoice.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                find = false;
                // additembean = new AddItemInBasketBean();
                AddbasketBeanFromHome bean = new AddbasketBeanFromHome();
                TextView tvItemName2 = (TextView) view.findViewById(R.id.tvItemChoiceName);
                TextView tvprice = (TextView) view.findViewById(R.id.tvItemChoicePrice);
                TextView tvrepeattime = (TextView) view.findViewById(R.id.tvRepeatTime);

                String txt = tvItemName2.getText().toString();

                String text = "";
                if (list.size() > 1)
                    for (int i = 1; i < list.size(); i++) {
                        if (list.get(i).contains(txt)) {
                            find = true;
                            String str = list.get(i);
                            if (countList.get(i).equals("1")) {
                                list.set(i, "2 x " + txt);
                                countList.set(i, "2");
                            } else {

                                int a = Integer.parseInt(countList.get(i));
                                list.set(i, ++a + " x " + txt);
                                countList.set(i, a + "");
                            }
                            break;
                        }

                    }
                if (!find) {
                    list.add("1 x " + txt);
                    countList.add("1");
                }

                for (int i = 0; i < list.size(); i++) {
                    Log.e("List " + i, list.get(i) + ".");
                }

                String price = tvprice.getText().toString();

                bean.setItemName(txt);
                bean.setItemPrice(price);
                selectedITem.add(bean);

                // concatednate = tvItem.getText().toString() + " " + txt;
                concatednate = "";

                for (int a = 0; a < list.size(); a++)
                    concatednate += list.get(a) + "\n";

                tvItem.setText(concatednate);

                Double conPrice = Double.parseDouble(price);
                DecimalFormat df = new DecimalFormat("#.##");

                tempchoicePrice = tempchoicePrice + conPrice;

                tvItemPrice.setText("" + df.format(tempchoicePrice));

                try {
                    String repeattime = arrlist2.get(count).get(0).getRepeattime();
                    Log.v("Repeat TIme", repeattime);
                    int repeat = Integer.parseInt(repeattime);
                    selectedChoiceRepeat++;


                    if (selectedChoiceRepeat >= repeat && repeat != 0) {

                        selectedChoiceRepeat = 0;
                        count++;
                        Log.v("Count", "" + count);
                        Log.v("Repeat Time", "" + repeat);

                        if (arrlist2.size() > count) {
                            listChoice.setAdapter(new ItemChoiceAdapter(
                                    ItemFromActivty.this, arrlist2, count));
                        }

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                }
                if (count >= arrlist2.size()) {
                    Log.v("Count In Visible Item", "" + count);

                    bAddItem.setVisibility(View.VISIBLE);
                    listChoice.setVisibility(View.INVISIBLE);
                    bNext.setVisibility(View.INVISIBLE);

                }

            }

        });

    }

    private void InitUI() {
        // TODO Auto-generated method stub
        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        listChoice = (ListView) findViewById(R.id.lvItemChoice);
        bNext = (Button) findViewById(R.id.bNext);
        bAddItem = (Button) findViewById(R.id.bAdditem);
        bDiscard = (Button) findViewById(R.id.bDiscard);
        Bback = (Button) findViewById(R.id.bBackFromWineChoice);
        bLogin = (Button) findViewById(R.id.bLoginFromWineChoice);

        lineartext = (LinearLayout) findViewById(R.id.linearTextLayout);
        // bNext = (Button) findViewById(R.id.bNext);

        tvItem = (TextView) findViewById(R.id.tvChoices);
        bNext.setOnClickListener(this);
        bDiscard.setOnClickListener(this);
        Bback.setOnClickListener(this);
        bAddItem.setOnClickListener(this);
        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bNext:

                int arrsize = arrlist2.size();
                if (arrsize > count) {

                    listChoice.setAdapter(new ItemChoiceAdapter(ItemFromActivty.this,
                            arrlist2, count));
                    count++;
                } else {
                    listChoice.setVisibility(View.INVISIBLE);
                    bNext.setVisibility(View.INVISIBLE);
                    bAddItem.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.bBackFromWineChoice:
                finish();
                super.onBackPressed();
                break;

            case R.id.bDiscard:
                GlobalClass.itemDiscardStatus = true;
                finish();
                Intent intent2 = new Intent(ItemFromActivty.this, HomeActivity.class);

                startActivity(intent2);
                break;

            case R.id.bLoginFromWineChoice:
                new loginDialog(ItemFromActivty.this);
                break;
            case R.id.bAdditem:

                Intent intent = new Intent(ItemFromActivty.this, HomeActivity.class);
                intent.putExtra("status", true);
                intent.putExtra("itemname", ItemName);
                model2.setItemName(ItemName);
                model2.setDesc(concatednate);
                model2.setID(ItemID);
                model2.setItemPrice(df.format(tempchoicePrice));
                model2.setItemType("i");
                model2.setQuantity("1");

                basketModelArrlist.add(model2);
                GlobalClass.globalBasketList.add(model2);

                startActivity(intent);

            default:
                break;

        }
    }

    void PrepareListData() {

        ArrayList<ItemChoiceBean> arrlistchoice = new ArrayList<>();
        for (int i = 0; i <= arrlist2.size(); i++) {

            for (int j = 0; j < arrlist2.get(i).size() - 1; i++) {

                ItemChoiceBean item = new ItemChoiceBean();
                item.setItemName(arrlist2.get(i).get(j).getItemName());
                item.setItemPrice(arrlist2.get(i).get(j).getItemPrice());
                arrlistchoice.add(item);
            }
            arrlist2.add(arrlistchoice);
            arrlistchoice = new ArrayList<>();

        }
    }

    class ItemChoicesAsync extends AsyncTask<Void, Void, Void> {

        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdialog = new ProgressDialog(ItemFromActivty.this);
            pdialog.show();
            pdialog.setCancelable(false);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            BranchListParser parser = new BranchListParser();
            ArrayList<NameValuePair> arrlst = new ArrayList<>();
            arrlst.add(new BasicNameValuePair("itemID", ItemID));
            arrlst.add(new BasicNameValuePair("bid", branch_ID));
            JSONArray arr = parser.makeHttpRequest(URL, arrlst);

            temparrlist = new ArrayList<>();
            arrlist2 = new ArrayList<>();
            if (arr.length() == 0 || arr.equals(null)) {

                Log.v("TAG", "" + arr);
            } else {
                for (int i = 0; i < arr.length(); i++) {
                    try {
                        JSONObject obj = arr.getJSONObject(i);

                        if (i < arr.length() - 1) {
                            nextItem = arr.getJSONObject(i + 1);
                        }
                        ItemChoiceBean bean = new ItemChoiceBean();

                        bean.setItemName(obj.getString("Name"));
                        bean.setItemPrice(obj.getString("Price"));
                        bean.setItemFree(obj.getString("free"));
                        bean.setM_ID("choiceCategoryID");
                        bean.setRepeattime(obj.getString("repeatTimes"));

                        temparrlist.add(bean);
                        if (i == arr.length() - 1) {
                            arrlist2.add(temparrlist);
                        }
                        if (!obj.getString("choiceCategoryID").equals(
                                nextItem.getString("choiceCategoryID"))) {
                            arrlist2.add(temparrlist);
                            temparrlist = new ArrayList<>();

                        }
                        // Log.i("TAG", name + "->" + price + "->" + free);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block

                    } finally {

                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdialog.dismiss();

            if (arrlist2.size() != 0) {
                listChoice
                        .setAdapter(new ItemChoiceAdapter(ItemFromActivty.this, arrlist2));
            }
        }
    }

}
