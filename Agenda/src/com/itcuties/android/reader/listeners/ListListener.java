package com.itcuties.android.reader.listeners;


import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.agenda.R;
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
		
		view.setAlpha(0.6f);
		SharedPreferences sharedPref = activity.getSharedPreferences("MyPrefsFile", 0);
		Set<String> set=sharedPref.getStringSet("Titles_Clicked", null);
		
		///////splitting string/////
		String[] sp;
		String pubdate;
		sp=listItems.get(pos).getPabDate().split(" ");
		Log.i("LISTLISTENER", sp[0]);
		Log.i("LISTLISTENER", sp[1]);
		pubdate = sp[0]+sp[1]+" "+sp[2]+" "+sp[3]+" "+sp[4];
		
		String dp=pubdate+"\n\n" + listItems.get(pos).getDescription()+":\n";
		String l=listItems.get(pos).getLink()+ "\n";
		

		
		//retrieve the layout (existed layout in xml, which is ScrollView)
		LinearLayout L = (LinearLayout)activity.findViewById(R.id.popupLinearLayout);
		LayoutInflater lf=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout=lf.inflate(R.layout.news_popup, L);

		//Keep preferences
		if(!set.contains(listItems.get(pos).getTitle())){
        	view.setAlpha(0.6f);
        	set.add(listItems.get(pos).getTitle());
        	SharedPreferences.Editor prefEditor = sharedPref.edit();
    		prefEditor.putStringSet("Titles_Clicked", set);
    		prefEditor.commit();
        }
        
		String yo;
		yo = "Ημερομηνία:\n" + dp + "\n" + l;
		
		//Hyperlinking
		final TextView ms = new TextView(activity);
		ms.setTextSize(18);
		final SpannableString s = new SpannableString(yo);
		Linkify.addLinks(s, Linkify.WEB_URLS);
		ms.setText(s);
		ms.setMovementMethod(LinkMovementMethod.getInstance());
		ms.setPadding(4, 2, 4, 2);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    	builder
    	.setTitle("Info")
    	.setView(ms)
    	.setNeutralButton("OK", null)
    	.show();

	}
	
}
