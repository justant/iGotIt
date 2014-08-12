package main;

import java.util.List;

import com.example.igotit.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem>{
	Context context;
	List<DrawerItem> drawerItemList;
	int layoutResID;
	Typeface fontType;

	public CustomDrawerAdapter(Context context, int layoutResID,List<DrawerItem> listItems) {
		super(context, layoutResID, listItems);
		this.context = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResID;
		
		// 헬베티카 폰트로 변경
		fontType = Typeface.createFromAsset(context.getAssets(),"fonts/Helvetica.ttf"); 
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		DrawerItemHolder drawerHolder;
		View view = convertView;

		if (view == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);

			drawerHolder.userLayout = (LinearLayout) view.findViewById(R.id.userLayout);
			drawerHolder.userName = (TextView) view.findViewById(R.id.user_name);
			drawerHolder.userName.setTypeface(fontType);
			drawerHolder.userIcon = (ImageView) view.findViewById(R.id.user_icon);			
			
			drawerHolder.itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
			drawerHolder.itemName = (TextView) view.findViewById(R.id.item_name);
			drawerHolder.itemName.setTypeface(fontType);
			drawerHolder.itemIcon = (ImageView) view.findViewById(R.id.item_icon);
			
			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();
		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

		// user drawer
		if (dItem.getUserName() == "Min Soyoung") {
			drawerHolder.userLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.GONE);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(180, 180);
			drawerHolder.userIcon.setLayoutParams(layoutParams);
			drawerHolder.userIcon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
			
			drawerHolder.userName.setText(dItem.getUserName());
		} 
		
		// item drawer
		else {
			drawerHolder.userLayout.setVisibility(LinearLayout.GONE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.itemIcon.setImageDrawable(view.getResources().getDrawable(dItem.getImgResID()));
			drawerHolder.itemName.setText(dItem.getItemName());
		}
		
		return view;
	}

	private static class DrawerItemHolder {
		TextView itemName, userName;
		ImageView itemIcon, userIcon;
		LinearLayout itemLayout, userLayout;
	}
}