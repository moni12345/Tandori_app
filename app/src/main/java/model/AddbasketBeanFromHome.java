package model;

public class AddbasketBeanFromHome {

	String ID;
	String itemName;
	String itemPrice;
	String Desc;
	String quantity;
	String itemType;
	String itemWine;
	boolean isFavoriteStarFill ;
	String wineSelected;

	public void setWineSelected(String wineSelected) {
		this.wineSelected = wineSelected;
	}

	public String getItemWine() {
		return itemWine;
	}
	public void setItemWine(String itemWine) {
		this.itemWine = itemWine;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}

}
