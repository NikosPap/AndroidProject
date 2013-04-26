package com.example.agendaMyAcount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.agenda.R;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
//import android.app.Activity;
import android.util.Log;
import android.view.Menu;

class Data{
	private String Day;
	private String LClass;
	private String Hour;
	private String Course;
	
	public String getDay() {
		return Day;
	}
	public void setDay(String day) {
		Day = day;
	}
	public String getLClass() {
		return LClass;
	}
	public void setLClass(String class1) {
		LClass = class1;
	}
	public String getHour() {
		return Hour;
	}
	public void setHour(String hour) {
		Hour = hour;
	}
	public String getCourse() {
		return Course;
	}
	public void setCourse(String course) {
		Course = course;
	}
}

class Course {
    private ArrayList<Data> data;

    public Course(){
    	this.data = new ArrayList<Data>();
    }
    
    /**
     * @return the data
     */
    public ArrayList<Data> getData() {
        return data;
    }

    
    /**
     * @param data the data to set
     */
    public void setData(String day, String lclass, String hour, String course) {
    	Data data=new Data();
    	
    	data.setCourse(course);
    	data.setDay(day);
    	data.setHour(hour);
    	data.setLClass(lclass);
        this.data.add(data);
    }
}

@SuppressLint("NewApi")
public class ScheduleCourses extends PreferenceActivity{
	
	private static int findWholeWord(final String text, final String searchString) {
	    return (" " + text + " ").indexOf(" " + searchString + " ");
	}
	
	private String findTimetableHTML(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int ye = year%1000;
		String html;
		int InitYear=ye;
//System.out.println(month+ "  " + year);
		
		if(month>=10 || month<=3){		//Winter Semister
			if(month<=3)
				InitYear--;
			else
				ye++;
			html="http://cgi.di.uoa.gr/~schedule/timetables/"+InitYear+"-"+ye+"/timetable_PPS_"+"winter"+InitYear+ye+".html";
		}
		else{						//Spring Semister
			InitYear--;
			html="http://cgi.di.uoa.gr/~schedule/timetables/"+InitYear+"-"+ye+"/timetable_PPS_"+"spring"+InitYear+ye+".html";
		}
System.out.println(html);
		return html;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_courses);
		
		
		String[] selections = {"selection1","Selection2"};
		Set<String> selectionSet = new HashSet<String>();
		selectionSet.addAll(Arrays.asList(selections));
		
		MultiSelectListPreference multiSelectPref = new MultiSelectListPreference(this);
		        multiSelectPref.setKey("multi_pref");
		        multiSelectPref.setTitle("Multi Select List  Preference");
		        multiSelectPref.setEntries(selections);
		        multiSelectPref.setEntryValues(selections);
		        multiSelectPref.setDefaultValue(selectionSet);
		        //getPreferenceScreen().addPreference(multiSelectPref);
		        
		        
		
		
		
		String html="";
		
		html = findTimetableHTML();
		GetSemisterSchedule getTask = new GetSemisterSchedule();
		
		getTask.execute(html);
		
		// Debug the thread name
        Log.d("GetSchedule", Thread.currentThread().getName());
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
								c.setData(key, data1.trim(), hour, course.trim());
							}
						}
						else if(findWholeWord(text,"Αίθουσα")!=-1){
							res = text.split("/");
							res2 = res[1].split(":");
							data1 = res2[1] + ",";
						}
					}
				}
				for(int i=0; i<c.getData().size();i++){
					System.out.println(c.getData().get(i).getDay() + "----"+c.getData().get(i).getHour() + "----"+c.getData().get(i).getLClass()+ "-----"+c.getData().get(i).getCourse());
				}
			} catch (Exception e) {
				Log.e("GetSchedule", e.getMessage());
			}
			return c;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_courses, menu);
		return true;
	}

}
