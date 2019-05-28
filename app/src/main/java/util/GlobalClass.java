package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skytech.aminatandoori.R;

import model.AddbasketBeanFromHome;

public class GlobalClass extends Application {

	// Login Status Checking

	public static String APPLICATION_URL = "http://placeorderonline.com/aminah/webservices/";

	public static final String BRANCH_ID = "2";
	public static String pre_Order_Timing = null;
	public static String firstName;
	public static String lastName;
	public static String email = "";
	public static String contact1 = "";
	public static String contact2 = "";
	public static String address = "";
	public static String address2 = "";
	public static String customerId = "";
	public static String postcode = "";
	public static String city = "";
	// checking the login status
	public static int status = 0;

	public static String branchTimeing = "H";
	public static String statusLogin = "Login";
	public static String favStarStatus = "";
	public static int favPosition;

	public static boolean OfferDiscardStatus = false;
	public static boolean itemDiscardStatus = false;
	public static boolean wineSelection = true;

	public static ArrayList<AddbasketBeanFromHome> globalBasketList = new ArrayList<>();

	public static GlobalClass globalObj;
	static {
		globalObj = new GlobalClass();
	}

	public static String counterValue = "0";
	public static HashMap<String, List<String>> GloballistDataChild = new HashMap<>();

	// public static ArrayList<FavoriteModel> arrlstfavoritemodel = new
	// ArrayList<FavoriteModel>();

	public void showCircleCount(Context context, View v) {
		// Display display = getWindowManager().getDefaultDisplay();

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final TextView txtBasketCount = (TextView) v
				.findViewById(R.id.basketCount);

		// int width = display.getWidth();
		// int height = display.getHeight();
		// float widthfloat= (float) width;
		// double widthdouble= widthfloat*0.72;

		// float floatHeight= (float) height;
		// double heightdouble= floatHeight*0.88;

		// System.out.println("width"+width);
		// System.out.println("Height"+height);

		// if (width == 768){
		// circle on basket
		// params.setMargins(left, top, right, bottom);
		// bottom fixed
		// top bhi fixed rhy ga
		// params.setMargins((int) widthdouble, (int)heightdouble, 100, 24);
		// params.setMargins(100, 1042, 100, 24);
		// params.setMargins(560, 1042, 100, 24);
		// txtBasketCount.setLayoutParams(params);

		txtBasketCount.setTextSize(11);
		final float scale = context.getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale

		int pixels = (int) (15 * scale + 0.5f);
		// txtBasketCount.setTextSize(pixels);
		// txtBasketCount.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
		// counterValue = "0";
		counterValue = counterValue.trim();
		int counterValueI = Integer.parseInt(counterValue);

		if (counterValueI == 0 && counterValueI < 10) {

			txtBasketCount.setText(counterValue);
			txtBasketCount.setGravity(Gravity.CENTER);

		} else {
			txtBasketCount.setGravity(Gravity.CENTER);
			txtBasketCount.setText(counterValue);

		}
		txtBasketCount.setHeight(pixels);
		txtBasketCount.setWidth(pixels);

	}

	public static void setCircleCountValuePlus(View _ActivityView) {

		final TextView txtBasketCount = (TextView) _ActivityView
				.findViewById(R.id.basketCount);
		// valuePlus = valuePlus+Integer.parseInt(counterValue);
		int counterValueInt;
		// String valuePlusString =String.valueOf(valuePlus);

		counterValueInt = Integer.parseInt(counterValue);
		counterValueInt++;
		counterValue = String.valueOf(counterValueInt);

		if (counterValueInt > 0 && counterValueInt < 10) {
			txtBasketCount.setText(" " + counterValue);
			txtBasketCount.setGravity(Gravity.CENTER);
		} else {
			txtBasketCount.setText(counterValue);
			txtBasketCount.setGravity(Gravity.CENTER);

		}
	}

	public static void setCircleCountValueMinus(View _ActivityView) {
		final TextView txtBasketCount = (TextView) _ActivityView
				.findViewById(R.id.basketCount);

		int counterValueInt = Integer.parseInt(counterValue);

		counterValueInt--;
		counterValue = String.valueOf(counterValueInt);

		if (counterValueInt > 0 && counterValueInt < 10) {
			txtBasketCount.setText(counterValue);
			txtBasketCount.setGravity(Gravity.CENTER);
		} else {
			txtBasketCount.setText(counterValue);
			txtBasketCount.setGravity(Gravity.CENTER);
		}
	}

}
