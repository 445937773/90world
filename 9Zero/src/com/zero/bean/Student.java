package com.zero.bean;

import java.io.Serializable;

public class Student implements Serializable{
	private static final long serialVersionUID = -3779423788666399711L;
	public String stu_id;
	public String phone_num;
	public String stu_name;
	public String stu_pic = "";
	public String stu_sex;
	public String stu_school;
	public String stu_address;
	public String stu_pwd;
	public String stu_number;
	public String stu_xingyu;
	
	public Student() {}

	public Student(String stu_id, String phone_num, String stu_name,
			String stu_pic, String stu_sex, String stu_school,
			String stu_address, String stu_pwd, String stu_number,
			String stu_xingyu) {
		super();
		this.stu_id = stu_id;
		this.phone_num = phone_num;
		this.stu_name = stu_name;
		this.stu_pic = stu_pic;
		this.stu_sex = stu_sex;
		this.stu_school = stu_school;
		this.stu_address = stu_address;
		this.stu_pwd = stu_pwd;
		this.stu_number = stu_number;
		this.stu_xingyu = stu_xingyu;
	}

	public String getStu_id() {
		return stu_id;
	}

	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getStu_name() {
		return stu_name;
	}

	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}

	public String getStu_pic() {
		return stu_pic;
	}

	public void setStu_pic(String stu_pic) {
		this.stu_pic = stu_pic;
	}

	public String getStu_sex() {
		return stu_sex;
	}

	public void setStu_sex(String stu_sex) {
		this.stu_sex = stu_sex;
	}

	public String getStu_school() {
		return stu_school;
	}

	public void setStu_school(String stu_school) {
		this.stu_school = stu_school;
	}

	public String getStu_address() {
		return stu_address;
	}

	public void setStu_address(String stu_address) {
		this.stu_address = stu_address;
	}

	public String getStu_pwd() {
		return stu_pwd;
	}

	public void setStu_pwd(String stu_pwd) {
		this.stu_pwd = stu_pwd;
	}

	public String getStu_number() {
		return stu_number;
	}

	public void setStu_number(String stu_number) {
		this.stu_number = stu_number;
	}

	public String getStu_xingyu() {
		return stu_xingyu;
	}

	public void setStu_xingyu(String stu_xingyu) {
		this.stu_xingyu = stu_xingyu;
	}
	
	
}
