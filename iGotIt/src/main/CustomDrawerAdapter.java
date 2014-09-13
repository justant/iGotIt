package main;

import java.util.List;

import singleton.Utility;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.igotit.R;
import com.squareup.picasso.Picasso;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem>{
	private static final String TAG = "CustomDrawerAdapter";
	private Utility utility = Utility.getInstance();
	private Context context;
	private List<DrawerItem> drawerItemList;
	private Typeface fontType;
	private int layoutResID;
	
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
		if (dItem.getUserName() != null) {
			drawerHolder.userLayout.setVisibility(LinearLayout.VISIBLE);
			drawerHolder.itemLayout.setVisibility(LinearLayout.GONE);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(180, 180);
			drawerHolder.userIcon.setLayoutParams(layoutParams);
			
			String url = "https://graph.facebook.com/" + utility.getFacebook_id() + "/picture?type=large";
			Picasso.with(context)
	        .load(url)
	        .resize(180, 180)
	        .centerCrop()
	        .into(drawerHolder.userIcon);
			
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