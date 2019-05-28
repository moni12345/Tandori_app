package util;

import java.net.URI;
import java.util.ArrayList;

import org.json.JSONObject;

public class WineCheck {

	static URI URL;
	Boolean isInternetPresent = false;
	static ArrayList<JSONObject> lst;

	public static boolean isNormalItemSelected() {

		boolean isNormalItem = false;
		if (GlobalClass.globalBasketList.size() > 0) {
			// jsonArray = new JSONArray(GlobalClass.globalBasketList);
			lst = new ArrayList<JSONObject>();
			
			for (int i = 0; i < GlobalClass.globalBasketList.size(); i++) {

				String itemType = GlobalClass.globalBasketList.get(i)
						.getItemType();
				String itemWine = GlobalClass.globalBasketList.get(i).getItemWine();


				if (itemWine == "hitem" || itemWine == "NotWine" || itemWine =="fav") {
					isNormalItem = true;

				}

			}


		}
		return isNormalItem;

	}
}
