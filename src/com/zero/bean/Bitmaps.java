package com.zero.bean;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bitmaps implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Bitmap pir;
	public Bitmaps() {
		// TODO Auto-generated constructor stub
	}
	public Bitmap getPir() {
		return pir;
	}
	public void setPir(Bitmap pir) {
		this.pir = pir;
	}

    public static byte[] getBytes(Bitmap bitmap){  
        //实例化字节数组输出流  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图  
        return baos.toByteArray();//创建分配字节数组  
    }    

    public static Bitmap getBitmap(byte[] data){  
          return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图  
    }  
}
