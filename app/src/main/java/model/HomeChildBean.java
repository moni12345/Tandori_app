package model;

import java.util.ArrayList;
import java.util.List;



public class HomeChildBean {

	private String subitems_id;
	private String subitemname;
	// private String subitemName;
	private String subItems_Desc;
	private String subItems_price;
	private String subItems_CategoreyName;
	private String subItems_counts;
	private String itemhasSubitem;
	private int correctIndex;
	  
     
	  public String getSubItems_counts() {
	        return subItems_counts;
	    }

	
	public static HomeChildBean subItemsClassObj1;
	static {
		subItemsClassObj1 = new HomeChildBean();
	}

	public void setItemhasSubitem(String itemhasSubitem) {
		this.itemhasSubitem = itemhasSubitem;
	}

	public void setCorrectIndex(int correctIndex) {
		this.correctIndex = correctIndex;
	}

	public String getSubitems_id() {
		return subitems_id;
	}

	public void setSubitems_id(String subitems_id) {
		this.subitems_id = subitems_id;
	}

	public String getSubitemname() {
		return subitemname;
	}

	public void setSubitemname(String subitemname) {
		this.subitemname = subitemname;
	}

	public String getSubItems_Desc() {
		return subItems_Desc;
	}

	public void setSubItems_Desc(String subItems_Desc) {
		this.subItems_Desc = subItems_Desc;
	}

	public String getSubItems_price() {
		return subItems_price;
	}

	public void setSubItems_price(String subItems_price) {
		this.subItems_price = subItems_price;
	}

	public String getSubItems_CategoreyName() {
		return subItems_CategoreyName;
	}

	

	public String getFavoriteItemPresence() {
		return favouritePresence;
	}

	public void setFavoriteItemPresence(String favoriteItemEnd) {
		this.favouritePresence = favoriteItemEnd;
	}

	private String favouritePresence;
	static HomeChildBean subItemsClassObj;
	static {
		subItemsClassObj1 = new HomeChildBean();
	}

	public String getItems_idSub() {
		return Items_idSub;
	}

	public void setItems_idSub(String items_idSub) {
		Items_idSub = items_idSub;
	}
	  public void setSubItems_CategoreyName(String subItems_CategoreyName) {
	        this.subItems_CategoreyName = subItems_CategoreyName;
	    }
	private String Items_idSub;

	private String subCategory;

	public String getSubCategory() {
		return subCategory;
	}
	
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public void setSubItems_counts(String subItems_counts) {
		this.subItems_counts = subItems_counts;
	}

	public ArrayList<List<HomeChildBean>> subItemClassArrayList = new ArrayList<List<HomeChildBean>>();


	public String getItemhasSubitem() {
		// TODO Auto-generated method stub
		return null;
	}
}
