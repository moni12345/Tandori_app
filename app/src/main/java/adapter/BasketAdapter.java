package adapter;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.skytech.aminatandoori.R;

import dialog.loginDialog;
import activity.BasketActivity;
import activity.FavoriteActivity;
import util.GlobalClass;
import interfaces.BasketItemButtonListener;
import model.AddbasketBeanFromHome;
import model.FavoriteModel;
import parser.ServiceHandlerReturnString;
import session.SessionManager;

public class BasketAdapter extends ArrayAdapter<AddbasketBeanFromHome> {

	Activity context;
	ArrayList<AddbasketBeanFromHome> items;

	String itemName;
	String itemPrice;
	String itemqty;
	String itemid;
	String itemType;
	String itemDesc;
	String customer_id;
	BasketActivity backset_activity;

	SharedPreferences pref;
	View view;

	BasketActivity.BasketFragment frag;

	String request = "";
	BasketItemButtonListener itemButtonListener;
	boolean isClick = true;

	String req;
	int count = 0;

	SessionManager session;

	public BasketAdapter(Activity _context,
			ArrayList<AddbasketBeanFromHome> _items, BasketActivity.BasketFragment fragment) {
		super(_context, R.layout.basket_adapter_activity, _items);
		// TODO Auto-generated constructor stub
		this.context = _context;
		this.items = _items;
		itemButtonListener = fragment;
		session = new SessionManager(_context);
		pref = context.getSharedPreferences("fraddys", 0);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub

		// View v = convertView;
		final View v21 = v;
		int a = position;
		AddbasketBeanFromHome bean = items.get(position);

		LayoutInflater li = LayoutInflater.from(getContext());
		v = li.inflate(R.layout.basket_adapter_activity, null);

		TextView tvItemName = (TextView) v.findViewById(R.id.tvItem);
		TextView tvPrice = (TextView) v.findViewById(R.id.tvPriceIDitem);
		TextView tvDesc = (TextView) v.findViewById(R.id.tvDesc);
		final TextView tvitemQuantity = (TextView) v.findViewById(R.id.tvQty);
		TextView tvid = (TextView) v.findViewById(R.id.tvItemIDInBasketAdapter);

		final ImageView bstar = (ImageView) v.findViewById(R.id.BtnSelectfav);
		ImageView badd = (ImageView) v.findViewById(R.id.imgadd);
		ImageView bMinus = (ImageView) v.findViewById(R.id.BtnMinus);

		Log.e("abcdef", bean.getID() + "");
		Log.e("abcdef", bean.getItemName() + "");
		Log.e("abcdef", bean.getDesc() + "");

		bstar.setTag(new Integer(position));
		badd.setTag(new Integer(position));
		bMinus.setTag(new Integer(position));

		bstar.setImageResource(R.drawable.star_nill);

		String sss = bean.getID() + "_" + bean.getItemName() + "_"
				+ bean.getDesc();

		try {
			if (!FavoriteActivity.listFav.isEmpty()) {
				for (int i = 0; i < FavoriteActivity.listFav.size(); i++) {

					Log.e("oo", FavoriteActivity.listFav.get(i) + "");
					Log.e("ee", sss + "");
					if (FavoriteActivity.listFav.get(i).equals(sss)) {
						bstar.setImageResource(R.drawable.star_fill);

					}

				}
			}

		} catch (NullPointerException e) {
		}
		tvDesc.setText(bean.getDesc());
		tvItemName.setText(bean.getItemName());
		tvPrice.setText(bean.getItemPrice());
		tvid.setText(bean.getID());
		String item = bean.getQuantity();

		Log.v("Quantity", "" + item);
		tvitemQuantity.setText(bean.getQuantity());

	/*	if (bean.getItemWine().equals("w")) {
			bstar.setVisibility(View.INVISIBLE);
		} else if (bean.getItemWine().equals("fav")) {
			// bstar.setImageResource(R.drawable.star_fill);
		} else {
			// bstar.setImageResource(R.drawable.star_nill);
		}*/

		bstar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v2) {

				int i = (Integer) v2.getTag();

				FavoriteModel model = new FavoriteModel();
				itemName = GlobalClass.globalBasketList.get(i).getItemName();
				itemPrice = GlobalClass.globalBasketList.get(i).getItemPrice();
				itemqty = GlobalClass.globalBasketList.get(i).getQuantity();
				itemid = GlobalClass.globalBasketList.get(i).getID();
				itemType = GlobalClass.globalBasketList.get(i).getItemType();
				itemDesc = GlobalClass.globalBasketList.get(i).getDesc();

				itemName = itemName.replace("\"", "\\");
				itemName = itemName.replace("\'", "\\'");

				itemDesc = itemDesc.replace("\'", "\\'");
				itemDesc = itemDesc.replace("\"", "\\");

				boolean isLogin = session.isLoggedIn();

				if (GlobalClass.status == 1 || isLogin == true) {
					if (GlobalClass.status == 1) {
						customer_id = GlobalClass.customerId;
					} else {
						// if(GlobalClass.status == 1){
						customer_id = pref.getString("id", "");
					}

					Log.e("Customer ID", customer_id);

					if (isClick == true) {
						GlobalClass.favStarStatus = "f";
						GlobalClass.favPosition = i;
						bstar.setImageResource(R.drawable.star_fill);
						request = "1";
						isClick = false;
						new favAsyncTask().execute();

					} else if (isClick == false) {
						GlobalClass.favStarStatus = "nf";
						bstar.setImageResource(R.drawable.star_nill);
						request = "0";
						isClick = true;
						GlobalClass.globalBasketList.get(i).setItemWine("fav");
						new favAsyncTask().execute();

					}
				} else {
					new loginDialog(context);
				}
			}
		});

		badd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = (Integer) v.getTag();
				itemButtonListener.onAddButtonClick(i, tvitemQuantity);

			}
		});

		bMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int i = (Integer) v.getTag();
				itemButtonListener.onMinusButtonClick(i, tvitemQuantity);

			}
		});
		return v;
	}

	class favAsyncTask extends AsyncTask<Void, Void, String> {

		String URL = "http://placeorderonline.com/aminah/webservices/SetFavoriteStatus.php";

		@SuppressWarnings("deprecation")
		@Override
		protected String doInBackground(Void... request21) {
			// TODO Auto-generated method stub
			ServiceHandlerReturnString parser = new ServiceHandlerReturnString();
			ArrayList<NameValuePair> arrlst = new ArrayList<>();
			arrlst.add(new BasicNameValuePair("bid", GlobalClass.BRANCH_ID));
			arrlst.add(new BasicNameValuePair("itemName", itemName));
			arrlst.add(new BasicNameValuePair("itemPrice", itemPrice));
			arrlst.add(new BasicNameValuePair("cid", customer_id));
			arrlst.add(new BasicNameValuePair("i_d", itemType));
			arrlst.add(new BasicNameValuePair("request", request));
			arrlst.add(new BasicNameValuePair("I_D_id", itemid));
			arrlst.add(new BasicNameValuePair("itemDesc", itemDesc));
			try {
				String s = parser.makeHttpRequest(URL, arrlst);
				Log.v("TAG", s +"-");

			} catch (Exception e) {
				Log.v("TAG Favorite", "" + parser.toString());
			}

			return null;
		}

	}

}
