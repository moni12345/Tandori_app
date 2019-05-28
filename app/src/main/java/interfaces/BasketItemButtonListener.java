package interfaces;

import android.view.View;
import android.widget.TextView;

public interface BasketItemButtonListener {
	
	void onMinusButtonClick(int position, TextView tv);

	void onAddButtonClick(int position, TextView tv);
}
