package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

public class SettingListAdapter extends ArrayAdapter<String> {

	Activity _context;
	String[] _itemname;

	public SettingListAdapter(Activity context, String[] arr) {
		super(context, R.layout.simple_setting_list_adapter, arr);
		// TODO Auto-generated constructor stub
		_context = context;
		_itemname = arr;
	}
	
	
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = _context.getLayoutInflater();
		View v = inflater.inflate(R.layout.simple_setting_list_adapter, null,
				true);

		TextView pizza_name = (TextView) v.findViewById(R.id.tvSettingRow);
		// ImageView star_filled = (ImageView)
		// v.findViewById(R.id.imgBtnSelector);
		// TextView count_pizza = (TextView)
		// v.findViewById(R.id.txtViewPizzaCountID);

		pizza_name.setText(_itemname[position]);
		// star_filled.setImageResource(imgid[position]);
		// extratxt.setText(itemname[position]);
		return v;

	}
}
