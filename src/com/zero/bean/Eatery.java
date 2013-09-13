package com.zero.bean;

import java.io.Serializable;
/*餐馆*/
public class Eatery implements Serializable {

	private static final long serialVersionUID = 1L;
	private int eateryId;//餐馆id
	private String eateryName;//餐馆名称
	private String eateryAdedress;//餐馆地址
	private String eateryPhone;//餐馆电话
	private String eateryDetails;//介绍
	private String eateryBusy;//是否忙碌
	
	public Eatery() {
		// TODO Auto-generated constructor stub
	}

	public Eatery(int eateryId, String eateryName, String eateryAdedress,
			String eateryPhone, String eateryDetails, String eateryBusy) {
		super();
		this.eateryId = eateryId;
		this.eateryName = eateryName;
		this.eateryAdedress = eateryAdedress;
		this.eateryPhone = eateryPhone;
		this.eateryDetails = eateryDetails;
		this.eateryBusy = eateryBusy;
	}

	public int getEateryId() {
		return eateryId;
	}

	public void setEateryId(int eateryId) {
		this.eateryId = eateryId;
	}

	public String getEateryName() {
		return eateryName;
	}

	public void setEateryName(String eateryName) {
		this.eateryName = eateryName;
	}

	public String getEateryAdedress() {
		return eateryAdedress;
	}

	public void setEateryAdedress(String eateryAdedress) {
		this.eateryAdedress = eateryAdedress;
	}

	public String getEateryPhone() {
		return eateryPhone;
	}

	public void setEateryPhone(String eateryPhone) {
		this.eateryPhone = eateryPhone;
	}

	public String getEateryDetails() {
		return eateryDetails;
	}

	public void setEateryDetails(String eateryDetails) {
		this.eateryDetails = eateryDetails;
	}

	public String getEateryBusy() {
		return eateryBusy;
	}

	public void setEateryBusy(String eateryBusy) {
		this.eateryBusy = eateryBusy;
	}
	
	
}
