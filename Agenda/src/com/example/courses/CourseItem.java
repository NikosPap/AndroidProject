package com.example.courses;

public class CourseItem {
	private String name = "";
	private String desc = "";
	private boolean checked = false;
	private boolean changed = false;

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public CourseItem() {
	}

	public CourseItem(String name) {
		this.name = name;
	}

	public CourseItem(String name, boolean checked, String desc) {
		this.name = name;
		this.checked = checked;
		this.desc = desc;
	}

	public String getDesc(){
		return this.desc;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toString() {
		return name;
	}

	public void toggleChecked() {
		checked = !checked;
	}
}
