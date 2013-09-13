package com.zero.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Poster implements Serializable {

	private static final long serialVersionUID = 1L;
	private int posterId;
	private String posterChaining;
	private String imageUri;
	private String title;
	private int pointer;
	private int goodsId;
	private byte[] bytes;
	private double price;
	private Bitmap bitmap;
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Poster() {
		// TODO Auto-generated constructor stub
	}

	public Poster(int posterId, String posterChaining, String imageUri,
			String title, int pointer, int goodsId, Bitmap bitmap) {
		super();
		this.posterId = posterId;
		this.posterChaining = posterChaining;
		this.imageUri = imageUri;
		this.title = title;
		this.pointer = pointer;
		this.goodsId = goodsId;
		this.bitmap = bitmap;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public int getPosterId() {
		return posterId;
	}

	public void setPosterId(int posterId) {
		this.posterId = posterId;
	}

	public String getPosterChaining() {
		return posterChaining;
	}

	public void setPosterChaining(String posterChaining) {
		this.posterChaining = posterChaining;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPointer() {
		return pointer;
	}

	public void setPointer(int pointer) {
		this.pointer = pointer;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
}
