package com.zero.bean;

import java.io.Serializable;

public class GoodsSmall implements Serializable {

	private static final long serialVersionUID = 1L;
	private int goodsSmallId;
	private String goodsSmallName;
	private int bigGoodsId;
	
	public GoodsSmall() {
		// TODO Auto-generated constructor stub
	}

	public GoodsSmall(int goodsSmallId, String goodsSmallName, int bigGoodsId) {
		super();
		this.goodsSmallId = goodsSmallId;
		this.goodsSmallName = goodsSmallName;
		this.bigGoodsId = bigGoodsId;
	}

	public int getGoodsSmallId() {
		return goodsSmallId;
	}

	public void setGoodsSmallId(int goodsSmallId) {
		this.goodsSmallId = goodsSmallId;
	}

	public String getGoodsSmallName() {
		return goodsSmallName;
	}

	public void setGoodsSmallName(String goodsSmallName) {
		this.goodsSmallName = goodsSmallName;
	}

	public int getBigGoodsId() {
		return bigGoodsId;
	}

	public void setBigGoodsId(int bigGoodsId) {
		this.bigGoodsId = bigGoodsId;
	}
	
	
}
