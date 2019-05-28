package adapter;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skytech.aminatandoori.R;
import model.PreviosOrderBean;

public class PreviosOrderAdapter extends ArrayAdapter<List<PreviosOrderAdapter>>{

	Activity act;
	List<PreviosOrderBean> item;
	
	public PreviosOrderAdapter(Activity context, 
			List<PreviosOrderBean> previosarrlst) {
		super(context, R.layout.previous_order_adapter);
		
		act = context;
		item = previosarrlst;
		
		// TODO Auto-generated constructor stub
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
		 
	
		PreviosOrderBean bean = item.get(position);

		if (v == null) {
			LayoutInflater li = LayoutInflater.from(getContext());
			v = li.inflate(R.layout.previous_order_adapter, null);
		}

		TextView tvItemName = (TextView) v.findViewById(R.id.tvOrderItem);
		TextView tvPrice = (TextView) v.findViewById(R.id.tvOrderItemPrice);
		TextView tvqty = (TextView) v.findViewById(R.id.tvOrderItemqty);
		TextView tvDesc = (TextView) v.findViewById(R.id.tvPreDesc);

		String price = bean.getPrice() + " " ;
		String name = bean.getName();
		String qty = bean.getQty() ;
		String desc = bean.getDesc() ;
		
		Log.e("bean", price + " " + name + " " + qty +" " +desc +" " +position);
		
		tvPrice.setText(bean.getPrice() + " ");
		tvItemName.setText(bean.getName());
		tvqty.setText("x "+bean.getQty());
		tvDesc.setText(bean.getDesc());

		return v;

	}

}
