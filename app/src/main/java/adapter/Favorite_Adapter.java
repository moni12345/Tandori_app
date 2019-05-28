package adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import util.GlobalClass;
import model.AddbasketBeanFromHome;
import model.FavoriteModel;
import session.SessionManager;

public class Favorite_Adapter extends ArrayAdapter<ArrayList<FavoriteModel>> {

	ArrayList<FavoriteModel> item;
	String itemName;
	String itemPrice;
	String itemqty;
	String itemid;
	String itemType;
	String itemDesc;
	String customer_id;
	String customerID;

	Activity context;

	SessionManager session;
	View activityView;

	SharedPreferences pref;

	public Favorite_Adapter(Activity _context,
			ArrayList<FavoriteModel> list, View v) {
		super(_context, R.layout.list_itemn);
		// TODO Auto-generated constructor stub
		
		item = list;
		activityView = v;
		context = _context;

		session = new SessionManager(_context);
		pref = context.getSharedPreferences("fraddys", 0);
		boolean isLogin = session.isLoggedIn();

		if (GlobalClass.status == 1) {
			customer_id = GlobalClass.customerId;
		} else if (isLogin == true) {
			customer_id = pref.getString(SessionManager.KEY_ID, "");
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = convertView;

		FavoriteModel bean = item.get(position);
		// basketModel bean = item.get(position);

		if (v == null) {
			LayoutInflater li = LayoutInflater.from(getContext());
			v = li.inflate(R.layout.list_itemn, null);
		}

		final TextView tvItemName = (TextView) v
				.findViewById(R.id.tvremoveItem);
		final TextView tvPrice = (TextView) v.findViewById(R.id.tvPriceIDitem);
		final TextView tvDesc = (TextView) v.findViewById(R.id.tvRemoveDesc);
		TextView tvID = (TextView) v.findViewById(R.id.tvItemIDInBasketAdapter);

		final ImageView ivAdd = (ImageView) v.findViewById(R.id.imgfavadd);
		final ImageView ivMinus = (ImageView) v.findViewById(R.id.BtnMinusfac);
		final TextView tvQty = (TextView) v.findViewById(R.id.tvQtyfav);

		final ImageView bstar = (ImageView) v.findViewById(R.id.Btnremovefav);

		bstar.setTag(new Integer(position));
		ivAdd.setTag(new Integer(position));
		ivMinus.setTag(new Integer(position));

		ivAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int j = (Integer) ivAdd.getTag();

				AddbasketBeanFromHome bean = new AddbasketBeanFromHome();

				String str = tvQty.getText().toString();

				int qty = Integer.parseInt(str);

				qty++;
				tvQty.setText("" + qty);
				// GlobalClass.arrlstfavoritemodel.get(j).setItemqty(""+qty);

				for (int a = 0; a < GlobalClass.globalBasketList.size(); a++) {
					if (tvItemName
							.getText()
							.toString()
							.equals(GlobalClass.globalBasketList.get(a)
									.getItemName())
							&& tvDesc
									.getText()
									.toString()
									.equals(GlobalClass.globalBasketList.get(a)
											.getDesc())) {

						GlobalClass.globalBasketList.get(a).setQuantity(
								"" + qty);
						GlobalClass.setCircleCountValuePlus(activityView);
						return;
					}

				}
				GlobalClass.setCircleCountValuePlus(activityView);
				bean.setItemName(tvItemName.getText().toString());
				bean.setItemPrice(tvPrice.getText().toString());
				bean.setDesc("" + tvDesc.getText().toString());
				bean.setQuantity(tvQty.getText().toString());
				bean.setItemWine("fav");
				
				GlobalClass.globalBasketList.add(bean);

				Log.i("Favorite Bean", GlobalClass.globalBasketList.toString());

			}
		});
		ivMinus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int k = (Integer) ivMinus.getTag();
				String counter = tvQty.getText().toString();
				if (!counter.equals("0")) {

					GlobalClass.setCircleCountValueMinus(activityView);
					int qtyMinus = Integer.parseInt(counter);

					
					
					qtyMinus--;

					int s = qtyMinus ;
					
					tvQty.setText("" + qtyMinus);
					for (int m = 0; m < GlobalClass.globalBasketList.size(); m++) {

						if (GlobalClass.globalBasketList.get(m).getItemName()
								.equals(tvItemName.getText().toString())
								&& GlobalClass.globalBasketList.get(m)
										.getDesc()
										.equals(tvDesc.getText().toString())) {
							GlobalClass.globalBasketList.get(m).setQuantity(
									"" + qtyMinus);
							if (GlobalClass.globalBasketList.get(m)
									.getQuantity().equals("0")) { //

								GlobalClass.globalBasketList.remove(m);
							}
							return;
						}
					}

				} else {

				}

			}
		});

		bstar.setImageResource(R.drawable.star_fill);

		tvPrice.setText(bean.getPrice());
		tvItemName.setText(bean.getName());
		tvDesc.setText(bean.getDesc());
		tvID.setText(bean.getItemDealId());

		return v;

	}

	}

