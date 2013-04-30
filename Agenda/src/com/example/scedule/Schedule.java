package com.example.scedule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.ExpandableListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.example.agenda.R;
//import android.app.Activity;


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
	
	 
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        try{
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_schedule_courses);
             db = new C_DatabaseHandler(this);
             
             SimpleExpandableListAdapter expListAdapter =
            		 new SimpleExpandableListAdapter(
            				 this,
            				 createGroupList(),              // Creating group List.
            				 R.layout.group_row,             // Group item layout XML.
            				 new String[] { "Group Item" },  // the key of group item.
            				 new int[] { R.id.row_name },    // ID of each group item.-Data under the key goes into this TextView.
            				 createChildList(),              // childData describes second-level entries.
            				 R.layout.child_row,             // Layout for sub-level entries(second level).
            				 new String[] {"Sub Item"},      // Keys in childData maps to display.
            				 new int[] { R.id.grp_child}     // Data under the keys above go into these TextViews.
            		);
            setListAdapter( expListAdapter );       // setting the adapter in the list.
            
            String[] selections = {"selection1","Selection2"};
    		Set<String> selectionSet = new HashSet<String>();
    		selectionSet.addAll(Arrays.asList(selections));
    		        		
    		
    		String html="";
    		
    		html = findTimetableHTML();
    		GetSemisterSchedule getTask = new GetSemisterSchedule();
    		
    		getTask.execute(html);
    		
    		// Debug the thread name
            Log.d("GetSchedule", Thread.currentThread().getName());
 
        }catch(Exception e){
            System.out.println("Errrr +++ " + e.getMessage());
        }
    }
 
    /* Creating the Hashmap for the row */
    @SuppressWarnings("unchecked")
    private List createGroupList() {
          ArrayList result = new ArrayList();
          for( int i = 0 ; i < 15 ; ++i ) { // 15 groups........
            HashMap m = new HashMap();
            m.put( "Group Item","Group Item " + i ); // the key and it's value.
            result.add( m );
          }
          return (List)result;
    }
 
    /* creatin the HashMap for the children */
    @SuppressWarnings("unchecked")
    private List createChildList() {
 
        ArrayList result = new ArrayList();
        for( int i = 0 ; i < 15 ; ++i ) { // this -15 is the number of groups(Here it's fifteen)
          /* each group need each HashMap-Here for each group we have 3 subgroups */
          ArrayList secList = new ArrayList();
          for( int n = 0 ; n < 3 ; n++ ) {
            HashMap child = new HashMap();
            child.put( "Sub Item", "Sub Item " + n );
            secList.add( child );
          }
         result.add( secList );
        }
        return result;
    }
    
    public void  onContentChanged  () {
        System.out.println("onContentChanged");
        super.onContentChanged();
    }
    
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
	
	private int findWholeWord(final String text, final String searchString) {
	    return (" " + text + " ").indexOf(" " + searchString + " ");
	}
	
	private String findTimetableHTML(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int ye = year%1000;
		String html;
		int InitYear=ye;
System.out.println(month+ "  " + year);
		
		if(month>=10 || month<=3){		//Winter Semester
			if(month<=3)
				InitYear--;
			else
				ye++;
			html="http://cgi.di.uoa.gr/~schedule/timetables/"+InitYear+"-"+ye+"/timetable_PPS_"+"winter"+InitYear+ye+".html";
		}
		else{						//Spring Semester
			InitYear--;
			html="http://cgi.di.uoa.gr/~schedule/timetables/"+InitYear+"-"+ye+"/timetable_PPS_"+"spring"+InitYear+ye+".html";
		}
System.out.println(html);
		return html;
	}


	
	private class GetSemisterSchedule extends AsyncTask<String, Void, Course >{

		@Override
		protected Course doInBackground(String... html) {
			String prog = "";
			Course c = new Course();
			
			// Debug the task thread name
            Log.d("GetSchedule", Thread.currentThread().getName());
            
			try {
				String[] Days = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"};
				HashMap<String, String> Prog = new HashMap<String, String>();
				Document doc = Jsoup.connect(html[0]).get();
				
        //"http://cgi.di.uoa.gr/~schedule/timetables/12-13/timetable_PPS_spring1213.html").get();
				for (int j = 0; j < 5; j++) { // 5 meres
					for (int i = 1; i < 10; i++) { // 9 ai8ouses
						for (Element table : doc.select("table[summary=" + Days[j] + "]")) {
							for (Element row : table.select("tr")) {
								Elements tds = row.select("td");
								if (tds.size() > 1) {
									prog += tds.get(0).text() + " : " + tds.get(i).text() + "\n";
								}
							}
						}
					}
					Prog.put(Days[j], prog);
					prog = "";
				}
				String data1="";
				String[] lines;
				String res[],res1[],res2[];
				String hour;
				String course;
				String text;
        
				Iterator<String> iter = Prog.keySet().iterator();

				while (iter.hasNext()) {
					String key = (String) iter.next();
					String val = (String) Prog.get(key);
					lines = val.split(System.getProperty("line.separator"));
					for(int j=0;j<lines.length;j++){
						text = lines[j];
        
						if(findWholeWord(text,"Αίθουσα")==-1){
							res = text.split(":");
							res1 = res[1].split("-");
							hour = res[0]+ "-" +res1[1];
							course = res[3];
							if(course.length()!=2){
//System.out.println(key+ "----" + data1.trim() + "----" + hour + "----" + course.trim() );
								//c.setData(key, data1.trim(), hour, course.trim());
								db.addSCourse(new DatabaseCourse(key, hour, course.trim(), data1.trim()));
							}
						}
						else if(findWholeWord(text,"Αίθουσα")!=-1){
							res = text.split("/");
							res2 = res[1].split(":");
							data1 = res2[1] + ",";
						}
					}
				}
db.printAll();
				//for(int i=0; i<c.getData().size();i++){
				//	System.out.println(c.getData().get(i).getDay() + "----"+c.getData().get(i).getHour() + "----"+c.getData().get(i).getClass()+ "-----"+c.getData().get(i).getName());
				//}
			} catch (Exception e) {
				Log.e("GetSchedule", e.getMessage());
			}
			return c;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_schedule, menu);
		return true;
	}

}
