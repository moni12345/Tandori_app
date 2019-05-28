package model;

import java.io.Serializable;



public class AddItemInBasketBean implements Serializable {

	String itemPrice;
	String itemName;

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
