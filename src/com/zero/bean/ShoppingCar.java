package com.zero.bean;

import java.io.Serializable;

public class ShoppingCar implements Serializable {

	private static final long serialVersionUID = 1L;

	private int categoryId;
	private int goodsNumber;
	private GoodsInfo goods;
	private Dish dish;

	
	public ShoppingCar(int categoryId, int goodsNumber, GoodsInfo goods, Dish dish) {
		super();
		this.categoryId = categoryId;
		this.goodsNumber = goodsNumber;
		this.goods = goods;
		this.dish = dish;
	}
	public ShoppingCar() {
		super();
	}
	
	public Dish getDish() {
		return dish;
	}
	public void setDish(Dish dish) {
		this.dish = dish;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(int goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public GoodsInfo getGoods() {
		return goods;
	}
	public void setGoods(GoodsInfo goods) {
		this.goods = goods;
	}
	
	
}
