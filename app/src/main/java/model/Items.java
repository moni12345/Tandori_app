package model;

public class Items {

	String cat_name ;
	String cat_ID  ;
	String cat_desc;
	
	String Item_Desc;
	String ItemID  ;
	String ItemPrice ;
	String ItemName ;
	String OfferChoices;
	String Quantity;

	
	
	public String getQuantity() {
		return Quantity;
	}
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getCat_ID() {
		return cat_ID;
	}
	public void setCat_ID(String cat_ID) {
		this.cat_ID = cat_ID;
	}
	public String getItem_Desc() {
		return Item_Desc;
	}
	public void setItem_Desc(String item_Desc) {
		Item_Desc = item_Desc;
	}
	public String getItemID() {
		return ItemID;
	}
	public void setItemID(String itemID) {
		ItemID = itemID;
	}
	public String getItemPrice() {
		return ItemPrice;
	}
	public void setItemPrice(String itemPrice) {
		ItemPrice = itemPrice;
	}
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public String getCat_desc() {
		return cat_desc;
	}
	public void setCat_desc(String cat_desc) {
		this.cat_desc = cat_desc;
	}
	public String getOfferChoices() {
		return OfferChoices;
	}
	public void setOfferChoices(String offerChoices) {
		OfferChoices = offerChoices;
	}
	
}
