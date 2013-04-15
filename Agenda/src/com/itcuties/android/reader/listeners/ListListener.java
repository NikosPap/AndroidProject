package com.itcuties.android.reader.listeners;


import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.agenda.R;
import com.example.agendaMain.ITCutiesReaderAppActivity;
import com.itcuties.android.reader.data.RssItem;

/**
 * Class implements a list listener
 * 
 * @author ITCuties
 *
 */
@SuppressLint("NewApi")
public class ListListener implements OnItemClickListener{

	public static final String PUB_DESCR_LINK_MES = null;
	// List item's reference
	List<RssItem> listItems;
	// Calling activity reference
	Activity activity;
	
	public ListListener(List<RssItem> aListItems, Activity anActivity) {
		listItems = aListItems;
		activity  = anActivity;
	}
	
	/**
	 * Start a browser with url from the rss item.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		SharedPreferences sharedPref = activity.getSharedPreferences("MyPrefsFile", 0);
		Set<String> set=sharedPref.getStringSet("Titles_Clicked", null);
		
		String dp="Update at:\n" + listItems.get(pos).getPabDate()+"\n\n\n" + listItems.get(pos).getDescription()+ " (πιέστε τον παρακάτω σύνδεσμο):\n";
		String l=listItems.get(pos).getLink()+ "\n";
		
		//create pop up window in order to display details of a title
		final PopupWindow popup = new PopupWindow(activity);
		
		//retrieve the layout (existed layout in xml, which is ScrollView)
		LinearLayout L = (LinearLayout)activity.findViewById(R.id.popupLinearLayout);
		LayoutInflater lf=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout=lf.inflate(R.layout.news_popup, L);

		//If user click a title for first time, we save it in the Set
		if(!set.contains(listItems.get(pos).getTitle())){
        	view.setBackgroundColor(Color.GRAY);
        	set.add(listItems.get(pos).getTitle());
        	SharedPreferences.Editor prefEditor = sharedPref.edit();
    		prefEditor.putStringSet("Titles_Clicked", set);
    		prefEditor.commit();
        }
        
		//create 2 textview to display information
		TextView textView=(TextView)layout.findViewById(R.id.textView1);
		textView.setTextSize(15);
		textView.setText(dp);
		
		TextView textView2=(TextView)layout.findViewById(R.id.textView2);
		textView2.setAutoLinkMask(Linkify.WEB_URLS);		//make link clickable (connect to Internet)
		textView2.setTextSize(15);
		textView2.setText(l);
		
		Button b=(Button)layout.findViewById(R.id.close);
		popup.setContentView(layout);

		//Set size of pop up according screen dimensions (pop up is scrollable)
		Point scrSize=((ITCutiesReaderAppActivity) activity).getScreenSize();
		popup.setWidth(scrSize.x-50);
		popup.setHeight(scrSize.y-250);
		//popup.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popup.setFocusable(true);
		
		 // Displaying the popup at the specified location.
		 popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
		 
		 b.setOnClickListener(new OnClickListener() {

		     @Override
		     public void onClick(View v) {
		    	 popup.dismiss();
		     }
		 });
	}
}
