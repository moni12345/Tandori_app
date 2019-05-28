package model;

import android.graphics.Bitmap;

public class DealOffersBean {

	String offerID;
	String offerName;
	String offerDesc;
	String offerPrice;
	String offerspicURL;
	String offerChoices;

	public String getOfferChoices() {
		return offerChoices;
	}

	public void setOfferChoices(String offerChoices) {
		this.offerChoices = offerChoices;
	}

	public String getOfferID() {
		return offerID;
	}

	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getOfferDesc() {
		return offerDesc;
	}

	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}

	public String getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getOfferspicURL() {
		return offerspicURL;
	}

	public void setOfferspicURL(String offerspicURL) {
		this.offerspicURL = offerspicURL;
	}
}
