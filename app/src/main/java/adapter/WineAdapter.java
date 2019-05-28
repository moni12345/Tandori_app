package adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skytech.aminatandoori.R;

import activity.BasketActivity;
import util.GlobalClass;
import activity.WineChoiceActivity;
import model.AddbasketBeanFromHome;
import model.DealOffersBean;


public class WineAdapter extends ArrayAdapter<DealOffersBean> {

	List<DealOffersBean> itemlst;
	Activity act;
	private LayoutInflater layoutInflater;
	
	int count = 0;
	View ActivityView;
	
	ArrayList<AddbasketBeanFromHome> wineList = new ArrayList<AddbasketBeanFromHome>();

	public WineAdapter(Activity context, List<DealOffersBean> objects) {
		super(context, R.layout.wine_adapter, objects);
		// TODO Auto-generated constructor stub
		act = context;
		itemlst = objects;
	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itemlst.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		layoutInflater = LayoutInflater.from(getContext());
		convertView = layoutInflater.inflate(R.layout.wine_adapter, null);
// for getting the reference of textview and imageview
		final View v2 = convertView;
		
		DealOffersBean bean = itemlst.get(position);
		final TextView tvName = (TextView) convertView
				.findViewById(R.id.tvWineName);
		TextView tvDESC = (TextView) convertView.findViewById(R.id.tvWineDesc);
		TextView tvPrice = (TextView) convertView
				.findViewById(R.id.tvwinePrice);
		TextView tvCounter = (TextView) convertView
				.findViewById(R.id.tvcounterAdd);
		ImageView ivadd = (ImageView) convertView.findViewById(R.id.bWineAdd);
		ImageView ivMinus = (ImageView) convertView
				.findViewById(R.id.bWineMinus);
		ImageView ivGoOnChoice = (ImageView) convertView
				.findViewById(R.id.ivGoOnChoice);

		String offerChoices = bean.getOfferChoices();
		if (offerChoices.equals("0")) {
			ivMinus.setVisibility(View.VISIBLE);
			tvCounter.setVisibility(View.VISIBLE);
			ivadd.setVisibility(View.VISIBLE);
		} else if (offerChoices.equals("1")){
			ivGoOnChoice.setVisibility(View.VISIBLE);
		}
		tvName.setTag(new Integer(position));
		tvName.setText(bean.getOfferName());
		tvDESC.setText(bean.getOfferDesc());
		tvPrice.setText(bean.getOfferPrice());

		// tvDESC.setTag(new Integer(position));
		tvPrice.setTag(new Integer(position));
		// This Conditioon is for Going in Choices....!!!
	

		ivGoOnChoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int i = (int) tvName.getTag();
				Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
				String itemname = itemlst.get(i).getOfferName();
				String itemPrice = itemlst.get(i).getOfferPrice();
				String itemDesc = itemlst.get(i).getOfferDesc();
				String offerID = itemlst.get(i).getOfferID();
				Log.d("TAG", itemname + itemPrice + itemDesc + offerID);

				Intent intent = new Intent(act, WineChoiceActivity.class);
				intent.putExtra("itemID", offerID);
				intent.putExtra("Name", itemname);
				intent.putExtra("price", itemPrice);
				intent.putExtra("desc", itemDesc);

				act.startActivity(intent);
			}
		});
		ivadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int itemTag = (int) tvName.getTag();
				AddbasketBeanFromHome wineBean = new AddbasketBeanFromHome();
				
				String itemID = itemlst.get(itemTag).getOfferID();
				String itemName = itemlst.get(itemTag).getOfferName();
				String itemPrice = itemlst.get(itemTag).getOfferPrice();
				String itemDesc = itemlst.get(itemTag).getOfferDesc();
				
				
				
				TextView tvCounter = (TextView) v2.findViewById(R.id.tvcounterAdd);
				String counter = tvCounter.getText().toString();
				int qty = Integer.parseInt(counter);
				qty++;
				GlobalClass.setCircleCountValuePlus(BasketActivity.viewForSpecialItem);
				tvCounter.setText(""+qty);
				
				for(int i = 0 ; i < wineList.size(); i ++){
					
					if(wineList.get(i).getID().equals(itemID)){
						if(GlobalClass.globalBasketList.get(i).equals(itemID)){
							GlobalClass.globalBasketList.get(i).setQuantity(""+qty);
						}
						wineList.get(i).setQuantity(""+qty);
						return;
						
					}
				}
				
				
				wineBean.setID(itemID);
				wineBean.setItemName(itemName);
				wineBean.setItemPrice(itemPrice);
				wineBean.setDesc(itemDesc);
				wineBean.setItemWine("w");
				wineBean.setItemType("i");
				
//				wineBean.setItemType("d");
//				
				wineBean.setQuantity(""+qty);
				wineList.add(wineBean);
				GlobalClass.globalBasketList.add(wineBean);

				
			}
			
		});
		ivMinus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				TextView tvitemID = (TextView) v2
//						.findViewById(R.id.tvSubItemId);
				int itemTag = (int) tvName.getTag();
				String itemID = itemlst.get(itemTag).getOfferID();
				
			
				TextView itemQuantity = (TextView) v2.findViewById(R.id.tvcounterAdd);
				String counter = itemQuantity.getText().toString();
				if (!counter.equals("0")) {
					count--;
					 GlobalClass.setCircleCountValueMinus(BasketActivity.viewForSpecialItem);
					int qty = Integer.parseInt(counter);

					qty--;

					itemQuantity.setText("" + qty);
					for (int j = 0; j < wineList.size(); j++) {

						if (wineList.get(j).getID()
								.equals(itemID)) {
							wineList.get(j).setQuantity("" + qty);
							if (wineList.get(j).getQuantity().equals("0")) {
								wineList.remove(j);
								GlobalClass.globalBasketList.remove(j);
							}
							return;
						}
					}

				} else {

				}
			}
		});
		return convertView;
	}
}
