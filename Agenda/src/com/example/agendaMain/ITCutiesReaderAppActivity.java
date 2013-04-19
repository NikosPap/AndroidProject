package com.example.agendaMain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.agenda.R;
import com.itcuties.android.reader.data.RssItem;
import com.itcuties.android.reader.listeners.ListListener;
import com.itcuties.android.reader.util.RssReader;

/**
 * Main application activity.
 * 
 * @author ITCuties
 *
 */

@SuppressLint("NewApi")
public class ITCutiesReaderAppActivity extends Activity {
    
    // A reference to the local object
    private ITCutiesReaderAppActivity local;
    
    //Hold screens size
    private Point scrSize;
    
    ProgressDialog load;
    
    // Name of the preference file
    public static final String PREFS_NAME = "MyPrefsFile";
    
    //Name of the Set that will hold the already clicked titles
    public static final String CLICKED_TITLES="Titles_Clicked";
     
    /**
     * This method creates main application view
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set view
        setContentView(R.layout.activity_display_news);

        // Set reference to this activity
        local = this;
        
        scrSize=new Point();		//size of screen
        getWindowManager().getDefaultDisplay().getSize(scrSize);
         
        GetRSSDataTask task = new GetRSSDataTask();
         
        // Start download RSS task
        task.execute("http://www.di.uoa.gr/rss.xml");
         
        // Debug the thread name
        Log.d("ITCRssReader", Thread.currentThread().getName());
    }
    
    public Point getScreenSize(){
    	return this.scrSize;
    }
    
    
    //Create a custom adapter in order to change color to already clicked titles
    private class MyAdapter extends ArrayAdapter<RssItem> {

    	private Context c;
    	private int id;
    	private List<RssItem>items;
    	private Set<String> mySet;

    	public MyAdapter(Context context, int viewResourceId, List<RssItem> objects,Set<String> s){
    	    super(context,viewResourceId,objects);
    	    c=context;
    	    id=viewResourceId;
    	    items=objects;
    	    mySet=s;
    	}

    	@Override
    	public View getView(final int position, View convertView, ViewGroup parent) {
    	    View v = convertView;
    	    if (v == null) {
    	        LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	        v = vi.inflate(id, null);
    	    }

    	    final RssItem o = items.get(position);
    	    if (o != null) {
    	    	TextView tx=(TextView)v;
    	    	tx.setTextSize(12);
    	    	tx.setText(o.getTitle());
    	    	tx.setTextSize(15);
    	    	if(!mySet.isEmpty()){			//check if title exist in the Set
    	    		if (mySet.contains(o.getTitle())) {
    	    			v.setBackgroundColor(Color.GRAY);		//change color if exists
    	    		}
    	    	}		
    	    }
    	    return v;
    	}
    }
     
    @SuppressLint("NewApi")
	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem> > {
    	
        @Override
        protected List<RssItem> doInBackground(String... urls) {
             
            // Debug the task thread name
            Log.d("ITCRssReader", Thread.currentThread().getName());
             
            try {
                // Create RSS reader
                RssReader rssReader = new RssReader(urls[0]);
             
                // Parse RSS, get items
                return rssReader.getItems();
             
            } catch (Exception e) {
                Log.e("ITCRssReader", e.getMessage());
            }
             
            return null;
        }
        
        @Override
        protected void onPreExecute()		//display a loading message
        {
            super.onPreExecute();
            //load=ProgressDialog.show(ITCutiesReaderAppActivity.this, "Please wait", "Loading..", true);
            //load.setCancelable(true);
        }
         
        @Override
        protected void onPostExecute(List<RssItem> result) {
        	//once the load has finished
            //load.dismiss();
            
            Set<String> set;
            SharedPreferences.Editor prefEditor;
             
            // Get a ListView from main view
            ListView itcItems = (ListView) findViewById(R.id.list);
            
            //Retrieve shared preferences
            SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, 0);
            
            //A variable in order to check if this activity has launched for first time
    		Boolean FirstTimeLaunched = sharedPref.getBoolean("FirstTimeLaunched", true);
    		
    		//if is first time activity running, create a Set to keep which news has user already clicked
    		if(FirstTimeLaunched){
    			set = new HashSet<String>();
    			prefEditor = sharedPref.edit();
        		prefEditor.putBoolean("FirstTimeLaunched",false);
        		prefEditor.putStringSet(CLICKED_TITLES, set);
        		prefEditor.commit();
    		}
    		else{				//else retrieve the Set from shared preferences
    			set=sharedPref.getStringSet(CLICKED_TITLES, null);
    		}
    		// Create a list adapter
            MyAdapter adapter = new MyAdapter(local,android.R.layout.simple_list_item_1,result,set);
            
            // Set list adapter for the ListView
            itcItems.setAdapter(adapter);

            // Set list view item click listener
            itcItems.setOnItemClickListener(new ListListener(result, local));
        }
    }  
}