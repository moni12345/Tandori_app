package adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skytech.aminatandoori.R;
import model.ItemChoiceBean;

public class ItemChoiceAdapter extends ArrayAdapter<List<ItemChoiceBean>> {

	private List<List<ItemChoiceBean>> item;
	int _count = 0;

	public ItemChoiceAdapter(Context context, List<List<ItemChoiceBean>> items,
			int count) {
		super(context, R.layout.item_choice_adapter, items);
		this.item = items;
		_count = count;
	}

	public ItemChoiceAdapter(Context context, List<List<ItemChoiceBean>> items) {
		super(context, R.layout.item_choice_adapter, items);
		this.item = items;

	}
	
	
	

	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return item.get(_count).size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;

		if (v == null) {
			LayoutInflater li = LayoutInflater.from(getContext());
			v = li.inflate(R.layout.item_choice_adapter, null);
		}
		ItemChoiceBean bean;

		bean = item.get(_count).get(position);

		TextView tvItemChoiceName = (TextView) v
				.findViewById(R.id.tvItemChoiceName);

		TextView tvItemChoicePrice = (TextView) v
				.findViewById(R.id.tvItemChoicePrice);
		TextView tvrepeat = (TextView) v.findViewById(R.id.tvRepeatTime);

		tvItemChoiceName.setText(bean.getItemName());
		tvItemChoicePrice.setText(bean.getItemPrice());
		tvrepeat.setText(bean.getRepeattime());

		return v;
	}

}
