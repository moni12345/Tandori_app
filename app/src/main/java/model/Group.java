package model;

import java.util.ArrayList;

public class Group {

	String name;
	ArrayList<Child> child;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Child> getChild() {
		return child;
	}
	public void setChild(ArrayList<Child> child) {
		this.child = child;
	}
}
