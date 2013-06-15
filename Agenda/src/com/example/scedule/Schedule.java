package com.example.scedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.agenda.R;
//import android.app.Activity;
import com.example.agendaMain.ITCutiesReaderAppActivity;


class Course {
    private ArrayList<DatabaseCourse> data;

    public Course(){
    	this.data = new ArrayList<DatabaseCourse>();
    }
    
    /**
     * @return the data
     */
    public ArrayList<DatabaseCourse> getData() {
        return data;
    }
}

    
    

@SuppressLint("NewApi")
public class Schedule extends ExpandableListActivity {
	C_DatabaseHandler db;
	String Semester;
	SharedPreferences sharedPref;
	SharedPreferences.Editor prefEditor;
	Set<String> set;
	
	 

    public void onCreate(Bundle savedInstanceState) {
Log.i("OnCREATE", "YOHOOOO");
        try{
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_schedule_courses);
             db = new C_DatabaseHandler(this);
            
            String[] selections = {"selection1","Selection2"};
    		Set<String> selectionSet = new HashSet<String>();
    		selectionSet.addAll(Arrays.asList(selections));
    		        		
    		String preFile = ITCutiesReaderAppActivity.PREFS_NAME;
    		//Retrieve shared preferences
            sharedPref = getSharedPreferences(preFile, 0);
            
            //What is the current semester
    		Semester = sharedPref.getString("Semester", null);
    		
    		//A variable in order to check if this activity has launched for first time
    		Boolean FirstTimeLaunched = sharedPref.getBoolean("FirstTime", true);
    		
    		if(FirstTimeLaunched){
    			set = new HashSet<String>();
    			SharedPreferences.Editor prefEditor = sharedPref.edit();
        		prefEditor.putStringSet("CoursesChecked", set);
        		prefEditor.putBoolean("FirstTime", false);
        		prefEditor.commit();
    		}

    		String html="";
    		html = findTimetableHTML();
    		if(html != null){			//Get new Semester Schedule if it is needed
Log.i("GETSCHEDULE",html);
    			GetSemesterSchedule getTask = new GetSemesterSchedule(db,this.getApplicationContext());
        		getTask.execute(html);
    		
        		//Debug the thread name
        		Log.d("GetSchedule", Thread.currentThread().getName());

    		}
    		
            SimpleExpandableListAdapter expListAdapter =
           		 new SimpleExpandableListAdapter(
           				 this,
           				 createGroupList(),              // Creating group List.
           				 R.layout.group_row,             // Group item layout XML.
           				 new String[] { "Days" },  // the key of group item.
           				 new int[] { R.id.row_name },    // ID of each group item.-Data under the key goes into this TextView.
           				 createChildList(),              // childData describes second-level entries.
           				 R.layout.child_row,             // Layout for sub-level entries(second level).
           				 new String[] {"Course"},      // Keys in childData maps to display.
           				 new int[] { R.id.grp_child}     // Data under the keys above go into these TextViews.
           		);
           setListAdapter( expListAdapter );       // setting the adapter in the list.
        }catch(Exception e){
            System.out.println("Errrr +++ " + e.getMessage());
        }
    }
 
    /* Creating the Hashmap for the row */
    private List<HashMap<String, String>> createGroupList() {
    	String Days[] = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        for( int i = 0 ; i < 5 ; ++i ) {
        	HashMap<String, String> m = new HashMap<String, String>();
        	m.put( "Days",Days[i] ); 
        	result.add( m );
        }
        return (List<HashMap<String, String>>)result;
    }

    /*
private List createChildListV() {
	ArrayList result = new ArrayList();
	for( int i = 0 ; i < 5 ; ++i ) { 
		ArrayList secList = new ArrayList();
        for( int n = 0 ; n < 3 ; n++ ) {
        	HashMap child = new HashMap();
        	child.put( "Sub Item", "Item " + n );
        	secList.add( child );
        }
        result.add( secList );
    }
	return result;
}
*/
    
    /* creating the HashMap for the children */
    private List<ArrayList<HashMap<String, String>>> createChildList() {
    	ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
    	
    	//Retrieve selected courses
    	set = sharedPref.getStringSet("CoursesChecked", null);
    	
    	if(set.isEmpty()){
Log.i("BULLSHITTING", "MALAKIES");
    		for( int i = 0 ; i < 5 ; ++i ) { 
        		ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> child = new HashMap<String, String>();
                child.put( "Course", "Add course to schedule.." );
                secList.add( child );
                result.add( secList );
            }	
    	}
    	else{
Log.i("BULLSHITTING__2", "MALAKIES");
    		ArrayList<HashMap<String, String>> MonL = new ArrayList<HashMap<String, String>>();
    		ArrayList<HashMap<String, String>> TueL = new ArrayList<HashMap<String, String>>();
    		ArrayList<HashMap<String, String>> WenL = new ArrayList<HashMap<String, String>>();
    		ArrayList<HashMap<String, String>> ThuL = new ArrayList<HashMap<String, String>>();
    		ArrayList<HashMap<String, String>> FriL = new ArrayList<HashMap<String, String>>();
    	
    		Iterator<String> it = set.iterator();
    	
    		while(it.hasNext()) {
    			String les = it.next();
    			String query = "SELECT " + db.getKeyDay() + ", " + db.getKeyHour() + ", " + db.getKeyClass() + " FROM " + db.getTableScourses()+ " WHERE " + db.getKeyName() + "=" + "\"" + les + "\"";
//db.printAll();
    			Cursor c = db.execQuery(query);
Log.i("SCHEDULE_CREATE_LIST",les + "\n" + query + "\n" + c.getCount());
    			if (c.moveToFirst()) {
Log.i("CURSOR", c.getString(0) + "   " + c.getString(1) + "   " + c.getString(2) + "   " + les);   				
    				do {
    					HashMap<String, String> courseMap = new HashMap<String, String>();
    					ArrayList<HashMap<String, String>> array = null;
    					if("�������".equals(c.getString(0)) )
    						array = MonL;
    					else if("�����".equals(c.getString(0)))
    						array = TueL;
    					else if("�������".equals(c.getString(0)))
    						array = WenL;
    					else if("������".equals(c.getString(0)))
    						array = ThuL;
    					else if("���������".equals(c.getString(0)))
    						array = FriL;
    					courseMap.put("Course", c.getString(1) + "   " +les + "   " + c.getString(2));
    					array.add(courseMap);
    				} while (c.moveToNext());
    			}
    			c.close();
    		}
    		if(MonL.isEmpty()){
    			HashMap<String, String> courseMap = new HashMap<String, String>();
    			courseMap.put("Course","No course has been added for this day");
    			MonL.add(courseMap);
    		}
    		if(TueL.isEmpty()){
    			HashMap<String, String> courseMap = new HashMap<String, String>();
    			courseMap.put("Course","No course has been added for this day");
    			TueL.add(courseMap);
    		}
    		if(WenL.isEmpty()){
    			HashMap<String, String> courseMap = new HashMap<String, String>();
    			courseMap.put("Course","No course has been added for this day");
    			WenL.add(courseMap);
    		}
    		if(ThuL.isEmpty()){
    			HashMap<String, String> courseMap = new HashMap<String, String>();
    			courseMap.put("Course","No course has been added for this day");
    			ThuL.add(courseMap);
    		}
    		if(FriL.isEmpty()){
    			HashMap<String, String> courseMap = new HashMap<String, String>();
    			courseMap.put("Course","No course has been added for this day");
    			FriL.add(courseMap);
    		}
    			
    		result.add(MonL);
    		result.add(TueL);
    		result.add(ThuL);
    		result.add(WenL);
    		result.add(FriL);
    	}

        return result;
    }
    
    /*
     public void  onContentChanged  () {
     
        System.out.println("onContentChanged");
        super.onContentChanged();
    }
    */
    
    /* This function is called on each child click */
    public boolean onChildClick( ExpandableListView parent, View v, int groupPosition,int childPosition,long id) {
        System.out.println("Inside onChildClick at groupPosition = " + groupPosition +" Child clicked at position " + childPosition);
        return true;
    }
 
    /* This function is called on expansion of the group */
    public void  onGroupExpand  (int groupPosition) {
        try{
             System.out.println("Group exapanding Listener => groupPosition = " + groupPosition);
        }catch(Exception e){
            System.out.println(" groupPosition Errrr +++ " + e.getMessage());
        }
    }
	
	
	private String findTimetableHTML(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int ye = year%1000;
		String html;
		int InitYear=ye;
System.out.println(month+ "  " + year + "  Semester:" + Semester);
		
		if(month>=10 || month<=3){		//Winter Semester
			if(Semester == null || Semester.equals("Spring") || db.emptyDB() ){
				Semester = "Winter";
				prefEditor = sharedPref.edit();
				prefEditor.putString("Semester", Semester);
				prefEditor.commit();
			}
			else
				return null;
			if(month<=3)
				InitYear--;
			else
				ye++;
			html="http://cgi.di.uoa.gr/~schedule/timetables/"+InitYear+"-"+ye+"/timetable_PPS_"+"winter"+InitYear+ye+".html";
		}
		else{						//Spring Semester
			if(Semester == null || Semester.equals("Winter") || db.emptyDB() ){
				Semester = "Spring";
				prefEditor = sharedPref.edit();
				prefEditor.putString("Semester", Semester);
				prefEditor.commit();
			}
			else
				return null;
			InitYear--;
			html="http://cgi.di.uoa.gr/~schedule/timetables/"+InitYear+"-"+ye+"/timetable_PPS_"+"spring"+InitYear+ye+".html";
		}
System.out.println(html);
		return html;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option_scedule,menu);
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:
			add();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}
	
	private void add() {
		ArrayList<ScheduleCourseItem> subjects = db.selectSubjects();
		final CharSequence[] subs = new CharSequence[subjects.size()];
		
		for(int i=0; i<subjects.size();i++){
			subs[i]= subjects.get(i).getName();
		}
		
		//Retrieve selected courses
		set = sharedPref.getStringSet("CoursesChecked", null);
		
		boolean[] selected = new boolean[subjects.size()];		//Get the already checked courses
		for(int i=0; i<subjects.size();i++){
			if(set.contains(subs[i].toString()))
				selected[i] = true;
			else
				selected[i] = false;
		}

		//SelectCourseAdapter_Listener adapter = new SelectCourseAdapter_Listener(this.getApplication(),subjects); 
		new AlertDialog.Builder(this).setTitle("Add subject to schedule")
						.setMultiChoiceItems(subs, selected, new DialogInterface.OnMultiChoiceClickListener() {
			                		@Override
			                		public void onClick(DialogInterface dialog, int which,boolean isChecked) {			            
			                			if(isChecked)
		                					set.add(subs[which].toString());
			                			else if(set.contains(subs[which].toString()))
			                				set.remove(subs[which].toString());
			                		}
								}
						)
					   //.setAdapter(adapter,null)
					   .setNeutralButton("OK", new DialogInterface.OnClickListener() {
				            @Override
				            public void onClick(DialogInterface d, int which) {
				            	SharedPreferences.Editor prefEditor = sharedPref.edit();
				        		prefEditor.putStringSet("CoursesChecked", set);
				        		prefEditor.commit();
				            }
				        })
					   .create()
					   .show();
	}
	

}
