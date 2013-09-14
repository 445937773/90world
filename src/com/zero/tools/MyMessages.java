package com.zero.tools;


public class MyMessages {
	public static final int LOGIN_OK=0;//登陆成功
	public static final int LOGIN_NOT_ACCOUNT=1;//登陆找不到该账户
	public static final int LOGIN_PWD_ERROR=2;//登陆密码错误
	public static final int LOGIN_GET_STUDENT_FAILD = 3;//获取用户信息失败
	public static final int FINDPWD_OK = 4;
	public static final int FINDPWD_FAILD = 5;
	public static final int GET_SCHOOL_COMPLETE = 6;
	
	public static final int REGISTER_OK = 7; //注册成功
	public static final int REGISTER_NOT_REAL_STUDENT = 8;//数据库中有找到这个学生的真实信息，但学生姓名和输入的姓名不匹配
	public static final int UNKNOW_EXCEPTION = 9;//未知错误
	public static final int REGISTER_EXIST_USERNAME = 10;//已经注册过
	public static final int REGISTER_NOT_STUDENT = 11;//数据库中没有找到这个学生的真实信息
	public static final int REGISTER_EXIST_PHONE = 12;//手机号已经被注册
	
	public static final int FOUND_PWD_FAILD = 13;
	public static final int FOUND_PWD_OK = 14;
	
	public static final int LOAD_ICON_OK = 15;
	public static final int LOAD_ICON_FAILD = 16;
	
	
	public static final int CHANGE_PWD_OK = 17;
	public static final int CHANGE_PWD_FAILD = 18;
	
	public static final int COLLECT_OK = 19;//收藏
	public static final int COLLECT_FAILD = 20;
	
	public static final int GOODS_BIG_OK = 21;//大的分类
	public static final int GOODS_BIG_FAILD = 22;
	public static final int GOODS_SMALL_OK = 23;//小的分类
	public static final int GOODS_SMALL_FAILD = 24;
	public static final int GOODS_BRAND_SMALL_OK = 25;//得到商品信息
	public static final int GOODS_BRAND_SMALL_FAILD = 26;
	public static final int GOODS_INFO_OK = 27;//得到商品信息
	public static final int GOODS_INFO_FAILD = 28;
	
	public static final int GET_DE_ADDRESS_OK = 29;
	public static final int GET_ADDRESSES_OK = 30;
	public static final int ADD_ADDRESS_OK = 31;
	public static final int ADD_ADDRESS_FAILD = 32;
	public static final int UPDATE_ADDRESS_OK = 33;
	public static final int UPDATE_ADDRESS_FAILD = 34;
	public static final int DELETE_ADDRESS_OK = 35;
	public static final int DELETE_ADDRESS_FAILD = 36;
	public static final int CHANGE_GOODS_OK = 37;
	public static final int CHANGE_GOODS_FAILD = 38;
	
	public static final int DELETE_FOODS_OK = 39;
	public static final int DELETE_FOODS_FAILD = 40;
	
	public static final int DELETE_FAVORITE_OK = 41;
	public static final int DELETE_FAVORITE_FAILD = 42;
	public static final int ADD_SHOPPINGCAR_OK = 43;
	public static final int ADD_SHOPPINGCAR_FAILD = 44;
	public static final int ADD_restaurant_OK = 47;
	public static final int ADD_restaurant_FAILD = 48;
	public static final int SEARCH_FOODS_OK = 49;
	public static final int SEARCH_RESTAURANT_OK = 50;
	
	public static final int DELETE_GOODSSHOPPING_OK = 51;
	public static final int DELETE_GOODSSHOPPING_FAILD = 52;
	
	public static final int DELETE_FOODSSHOPPING_OK = 53;
	public static final int DELETE_FOODSSHOPPING_FAILD = 54;
	public static final int TIME_OUT = 55;
	public static final int GET_RES_OK = 56;
	public static final int GET_CATEGORY_OK = 57;
	public static final int GET_ADDRESS_OK = 58;
	public static final int GET_ADDRESS_FAILD = 59;
	public static final int BTN_CANCEL_VIVIBLTY = 60;
	public static final int SEND_SMS_OK = 61;
	public static final int GET_ADD_PIC_OK = 62;
	
	
}
