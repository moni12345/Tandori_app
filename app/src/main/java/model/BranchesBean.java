package model;

import java.io.Serializable;

import android.graphics.Bitmap;



public class BranchesBean implements Serializable{

	String id;
	String branches;
	String address;
	String totlalID ;
	String ImageURL;
	Bitmap iconURL;

	String postcode;
	double distance;
	String citytown;

	public Bitmap getIconURL() {
		return iconURL;
	}
	public void setIconURL(Bitmap iconURL) {
		this.iconURL = iconURL;
	}
	public String getImageURL() {
		return ImageURL;
	}
	public void setImageURL(String icon) {
		this.ImageURL = icon;
	}
	public String getTotlalID() {
		return totlalID;
	}
	public void setTotlalID(String totlalID) {
		this.totlalID = totlalID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBranches() {
		return branches;
	}
	public void setBranches(String branches) {
		this.branches = branches;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance2) {
		this.distance = distance2;
	}
	public String getCitytown() {
		return citytown;
	}
	public void setCitytown(String citytown) {
		this.citytown = citytown;
	}
}
