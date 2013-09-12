package com.zero.bean;

import java.io.Serializable;

public class DishSort implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dishId;
	private String dishName;
	
	public DishSort() {
		// TODO Auto-generated constructor stub
	}

	public DishSort(int dishId, String dishName) {
		super();
		this.dishId = dishId;
		this.dishName = dishName;
	}

	public int getDishId() {
		return dishId;
	}

	public void setDishId(int dishId) {
		this.dishId = dishId;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}
	
	
}
