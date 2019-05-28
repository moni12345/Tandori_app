package model;

import java.util.ArrayList;
import java.util.List;

public class MenuCategoryBean {

	public static MenuCategoryBean branchClassObj;
	static {
		branchClassObj = new MenuCategoryBean();
	}

	private String cat_id;
	private String cat_Name;
	private String cat_Description;
	private String item_id;
	private String item_name;
	private String item_description;
	private String itemsPrice;
	private String itemCount;

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getCat_id() {
		return cat_id;
	}

	public void setCat_id(String cat_id) {
		this.cat_id = cat_id;
	}

	public String getCat_Name() {
		return cat_Name;
	}

	public void setCat_Name(String cat_Name) {
		this.cat_Name = cat_Name;
	}

	public String getCat_Description() {
		return cat_Description;
	}

	public void setCat_Description(String cat_Description) {
		this.cat_Description = cat_Description;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_description() {
		return item_description;
	}

	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}

	public String getItemsPrice() {
		return itemsPrice;
	}

	public void setItemsPrice(String itemsPrice) {
		this.itemsPrice = itemsPrice;
	}

	public ArrayList<List<MenuCategoryBean>> branches = new ArrayList<List<MenuCategoryBean>>();
}
