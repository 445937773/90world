package com.zero.bean;

import java.io.Serializable;

public class School implements Serializable{
	private static final long serialVersionUID = 5596324892140194891L;
	public int school_id;
	public String school_name;
	public String school_pic;
	
	public School() {
		// TODO Auto-generated constructor stub
	}
	public School(int school_id, String school_name, String school_pic) {
		super();
		this.school_id = school_id;
		this.school_name = school_name;
		this.school_pic = school_pic;
	}

	public int getSchool_id() {
		return school_id;
	}

	public void setSchool_id(int school_id) {
		this.school_id = school_id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getSchool_pic() {
		return school_pic;
	}

	public void setSchool_pic(String school_pic) {
		this.school_pic = school_pic;
	}
	
	
}
