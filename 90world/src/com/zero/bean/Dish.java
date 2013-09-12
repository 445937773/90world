package com.zero.bean;

import java.io.Serializable;

public class Dish implements Serializable {
	private static final long serialVersionUID = 1L;
	private int dishId;
	private String dishName;
	private int dishSortId;
	private int tasteId;
	private String image = "";
	private int eateryId;
	private double price;
	private String restaurantName;
	private int number;
	private String isBusy;
	public Dish() {
		// TODO Auto-generated constructor stub
	}

	public Dish(int dishId, String dishName, int dishSortId, int tasteId,
			String image, int eateryId, double price, String restaurantName) {
		super();
		this.dishId = dishId;
		this.dishName = dishName;
		this.dishSortId = dishSortId;
		this.tasteId = tasteId;
		this.image = image;
		this.eateryId = eateryId;
		this.price = price;
		this.restaurantName = restaurantName;
	}


	public String getIsBusy() {
		return isBusy;
	}

	public void setIsBusy(String isBusy) {
		this.isBusy = isBusy;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
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

	public int getDishSortId() {
		return dishSortId;
	}

	public void setDishSortId(int dishSortId) {
		this.dishSortId = dishSortId;
	}

	public int getTasteId() {
		return tasteId;
	}

	public void setTasteId(int tasteId) {
		this.tasteId = tasteId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getEateryId() {
		return eateryId;
	}

	public void setEateryId(int eateryId) {
		this.eateryId = eateryId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	
}
