package com.example.courses;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;

public class DialogListener implements OnClickListener{
	List<CourseItem> list;
	Activity activity;
	EditText grade;
	
	public DialogListener(List<CourseItem> aListItems, Activity anActivity, EditText gr){
		list = aListItems;
		activity = anActivity;
		grade = gr;
	}
	

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		String regex = "10||[0-9](\\.[0-9])?";
		if(grade != null){
			String gr = grade.getText().toString();
			if(gr.length()!=0){
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(gr);
				if(matcher.matches()){
				}
				else{
				
				}
			}
		}
	}

}
