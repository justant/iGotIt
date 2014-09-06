package main;

import com.example.igotit.R;

public class DrawerItem {
	String userName;
	String itemName;
	int imgResID;
	
	// 이 생성자는 test 입니다.
	public DrawerItem() {
		this.userName = "test";
		this.itemName = "test";
		this.imgResID = R.drawable.ic_user;
	}
	
	public DrawerItem(String userName, String itemName, int imgResId) {
		this.userName = "Min Soyoung";
		this.itemName = null;
		this.imgResID = R.drawable.ic_user;
	}
	
	public DrawerItem(String itemName, int imgResID) {
		this.userName = null;
		this.itemName = itemName;
		this.imgResID = imgResID;
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
