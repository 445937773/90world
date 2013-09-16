package com.zero.bean;

import java.io.Serializable;

public class GoodsInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private int goodsId;
	private String goodsName;
	private String image;
	private double price;
	private int brandId;
	private int goodsSmallId;
	private String goodsIntroduce;
	private String goodsStandard="";
	private String goodsColorSort;
	private int number;
	public GoodsInfo() {
		// TODO Auto-generated constructor stub
	}

	public GoodsInfo(int goodsId, String goodsName, String image, double price,
			int brandId, int goodsSmallId, String goodsIntroduce,
			String goodsStandard, String goodsColorSort) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.image = image;
		this.price = price;
		this.brandId = brandId;
		this.goodsSmallId = goodsSmallId;
		this.goodsIntroduce = goodsIntroduce;
		this.goodsStandard = goodsStandard;
		this.goodsColorSort = goodsColorSort;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public int getGoodsSmallId() {
		return goodsSmallId;
	}

	public void setGoodsSmallId(int goodsSmallId) {
		this.goodsSmallId = goodsSmallId;
	}

	public String getGoodsIntroduce() {
		return goodsIntroduce;
	}

	public void setGoodsIntroduce(String goodsIntroduce) {
		this.goodsIntroduce = goodsIntroduce;
	}

	public String getGoodsStandard() {
		return goodsStandard.trim();
	}

	public void setGoodsStandard(String goodsStandard) {
		this.goodsStandard = goodsStandard.trim();
	}

	public String getGoodsColorSort() {
		return goodsColorSort;
	}

	public void setGoodsColorSort(String goodsColorSort) {
		this.goodsColorSort = goodsColorSort;
	}
	
	
}
