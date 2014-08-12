package main;

import com.example.igotit.R;

public class DrawerItem {
	String userName;
	String itemName;
	int imgResID;
	
	public DrawerItem(String itemName, int imgResID) {
		super();
		this.userName = null;
		this.itemName = itemName;
		this.imgResID = imgResID;
	}
	
	public DrawerItem() {
		this.userName = "Min Soyoung";
		this.itemName = null;
		this.imgResID = R.drawable.ic_user;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getImgResID() {
		return imgResID;
	}

	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
