package model;

public class ItemChoiceBean {

	String itemName;
	String itemPrice;
	String itemFree;
	String catgory_ID;
	String repeattime;
	String Mid;

	public String getMid() {
		return Mid;
	}

	public void setMid(String mid) {
		Mid = mid;
	}

	public String getRepeattime() {
		return repeattime;
	}

	public void setRepeattime(String repeattime) {
		this.repeattime = repeattime;
	}

	public String getM_ID() {
		return Mid;
	}

	public void setM_ID(String m_id) {
		this.Mid = m_id;
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

	public String getItemFree() {
		return itemFree;
	}

	public void setItemFree(String itemFree) {
		this.itemFree = itemFree;
	}
}
