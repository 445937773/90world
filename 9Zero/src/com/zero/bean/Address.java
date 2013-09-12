package com.zero.bean;

import java.io.Serializable;

public class Address implements Serializable {
	private static final long serialVersionUID = 1L;
	private int addressId;
	private String addressInfo;
	private boolean isDefault;
	private String recipient;
	private String phoneNum;
	
	
	public Address() {
		super();
	}


	public Address(int addressId, String addressInfo, boolean isDefault) {
		super();
		this.addressId = addressId;
		this.addressInfo = addressInfo;
		this.isDefault = isDefault;
	}

	
	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getPhoneNum() {
		return phoneNum;
	}


	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}


	public int getAddressId() {
		return addressId;
	}


	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}


	public String getAddressInfo() {
		return addressInfo;
	}


	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}


	public boolean isDefault() {
		return isDefault;
	}


	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	
	
}
