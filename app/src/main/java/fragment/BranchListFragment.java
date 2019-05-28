package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skytech.aminatandoori.R;


public class BranchListFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		 View view = inflater.inflate(R.layout.item_choice_fragment_layout, container, false);
		 
		 
		 return view;
	}
}
