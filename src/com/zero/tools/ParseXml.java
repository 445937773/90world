package com.zero.tools;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;



import com.zero.bean.Address;
import com.zero.bean.Dish;
import com.zero.bean.Eatery;
import com.zero.bean.FoodsFavorite;
import com.zero.bean.GoodsFavorite;
import com.zero.bean.GoodsInfo;
import com.zero.bean.Indent;
import com.zero.bean.OrderInfo;
import com.zero.bean.Poster;
import com.zero.bean.School;
import com.zero.bean.ShoppingCar;
import com.zero.bean.Student;

public class ParseXml {
	//获取学校列表
	public static List<School> getSchoolInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<School> schools = new ArrayList<School>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
				School school = new School();
				school.setSchool_id(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("SchoolId").toString()));
				school.setSchool_name((((SoapObject)detail.getProperty(i)).getProperty("SchoolName").toString()).trim());
				schools.add(school);
			}
		}else{
			return null;
		}
		
		return schools;
	}
	
	//通过超市大的分类获取小的分类
	public static List<String> getSmallGoodsSort(List<String> name, List<String> parameter, String METHOD_NAME){
		List<String> schools = new ArrayList<String>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
				schools.add(((SoapObject)detail.getProperty(i)).getProperty("SmallCategoryName").toString());
			}
		}else{
			return null;
		}
		
		return schools;
	}
	//通过超市小的分类获取品牌分类
		public static ArrayList<String> getbrandGoodsSort(List<String> name, List<String> parameter, String METHOD_NAME){
			ArrayList<String> schools = new ArrayList<String>();
			SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
			if(detail!=null){
					for (int i = 0; i < detail.getPropertyCount(); i++) {
					schools.add(((SoapObject)detail.getProperty(i)).getProperty("BrandName").toString());
				}
			}else{
				return null;
			}
			
			return schools;
		}
	//获取超市大的分类,
	public static List<String> getBigGoodsSort(List<String> name, List<String> parameter, String METHOD_NAME){
		List<String> schools = new ArrayList<String>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			schools.add(((SoapObject)detail.getProperty(i)).getProperty("CategoryName").toString());
		}
		}else{
			return null;
		}
		
		return schools;
	}
	//搜索菜品
	public static List<Dish> getDish(List<String> name, List<String> parameter, String METHOD_NAME){
		List<Dish> schools = new ArrayList<Dish>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			Dish school = new Dish();
			school.setDishId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("FoodId").toString()));
			school.setDishName(((SoapObject)detail.getProperty(i)).getProperty("FoodName").toString());
			school.setPrice(Double.parseDouble(((SoapObject)detail.getProperty(i)).getProperty("Price").toString()));
			if(((SoapObject)detail.getProperty(i)).hasProperty("RestaurantName")){
				school.setRestaurantName(((SoapObject)detail.getProperty(i)).getProperty("RestaurantName").toString());
				school.setIsBusy((((SoapObject)detail.getProperty(i)).getProperty("IsBusy").toString()));
			}
			schools.add(school);
		}
		}else{
			return null;
		}
		
		return schools;
	}
	public static Dish getDish1(List<String> name, List<String> parameter, String METHOD_NAME){
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		Dish school = new Dish();
		if(detail!=null){
			school.setDishId(Integer.parseInt(((SoapObject)detail.getProperty("MFoods")).getProperty("FoodId").toString()));
			school.setDishName(((SoapObject)detail.getProperty("MFoods")).getProperty("FoodName").toString());
			school.setRestaurantName(((SoapObject)detail.getProperty("MFoods")).getProperty("RestaurantName").toString());
			school.setImage(((SoapObject)detail.getProperty("MFoods")).getProperty("FoodPicture").toString());
			school.setPrice(Double.parseDouble(((SoapObject)detail.getProperty("MFoods")).getProperty("Price").toString()));
			school.setIsBusy(((SoapObject)detail.getProperty("MFoods")).getProperty("IsBusy").toString());
		}else{
			return null;
		}
		
		return school;
	}
	public static Dish getShoppingcarDish(List<String> name, List<String> parameter, String METHOD_NAME){
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		Dish school = new Dish();
		if(detail!=null){
			school.setDishId(Integer.parseInt((detail.getProperty("FoodId").toString())));
			school.setDishName((detail.getProperty("FoodName").toString()));
			school.setImage((detail.getProperty("FoodPicture").toString()));
			school.setPrice(Double.parseDouble((detail.getProperty("Price").toString())));
		}else{
			return null;
		}
		
		return school;
	}
	//根据餐馆获取菜品类别
	public static List<String> getDishSort(List<String> name, List<String> parameter, String METHOD_NAME){
		List<String> schools = new ArrayList<String>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			String sort = ((SoapObject)detail.getProperty(i)).getProperty("FoodsCatgoryName").toString();
			schools.add(sort);
		}
		}else{
			return null;
		}
		
		return schools;
	}
	public static List<String> getDishSort1(List<String> name, List<String> parameter, String METHOD_NAME){
		List<String> schools = new ArrayList<String>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			String sort = ((SoapObject)detail.getProperty(i)).getProperty("RestaurantName").toString();
			schools.add(sort);
		}
		}else{
			return null;
		}
		
		return schools;
	}
	//根据菜品类别获取菜的列表和相应餐馆
	public static List<Eatery> getEatery(List<String> name, List<String> parameter, String METHOD_NAME){
		List<Eatery> schools = new ArrayList<Eatery>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			Eatery school = new Eatery();
			school.setEateryId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("").toString()));
			school.setEateryName(((SoapObject)detail.getProperty(i)).getProperty("").toString());
			school.setEateryAdedress(((SoapObject)detail.getProperty(i)).getProperty("").toString());
			school.setEateryPhone(((SoapObject)detail.getProperty(i)).getProperty("").toString());
			school.setEateryDetails(((SoapObject)detail.getProperty(i)).getProperty("").toString());
			school.setEateryBusy(((SoapObject)detail.getProperty(i)).getProperty("").toString());
			schools.add(school);
		}
		}else{
			return null;
		}
		
		return schools;
	}
	//获取相应用户的订单
	public static List<Indent> getIndent(List<String> name, List<String> parameter, String METHOD_NAME){
		try {
			List<Indent> schools = new ArrayList<Indent>();
			Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
			if(detail != null){
				JSONArray array = new JSONArray(detail.toString());
				if(array.length() > 0){
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Indent indent = new Indent();
						List<String> urls = new ArrayList<String>();
						indent.setIndentId(obj.getInt("OrderFormId"));
						indent.setAddress(obj.getString("Address"));
						indent.setReserve(obj.getString("Comment"));
						indent.setSum(obj.getDouble("TotalAmount"));
						indent.setFavorableSum(obj.getDouble("PrivilegeAmount"));
						indent.setFukuangSum(obj.getDouble("PayableAmount"));
						indent.setIndentItate(obj.getString("Status"));
						indent.setOrderFormNumber(obj.getString("OrderFormNumber"));
						indent.setOrdersTime(obj.getString("CreateTime"));
						if(!obj.getString("GoodPictUrl").equals("[]")){
							JSONArray array1 = new JSONArray(obj.getString("GoodPictUrl"));
							for (int j = 0; j < array1.length(); j++) {
								JSONObject ob = array1.getJSONObject(j);
								urls.add(ob.getString("url"));
							}
							indent.setUrls(urls);
						}
						if(!obj.getString("FoodPictUrls").equals("[]")){
							JSONArray array1 = new JSONArray(obj.getString("FoodPictUrls"));
							for (int j = 0; j < array1.length(); j++) {
								JSONObject ob = array1.getJSONObject(j);
								urls.add(ob.getString("url"));
							}
							indent.setUrls(urls);
						}
						
						
						schools.add(indent);
					}
				}else{
					return schools;
				}
				
			}else{
				return null;
			}
			
			return schools;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static List<Indent> getIndentOrderStatus(List<String> name, List<String> parameter, String METHOD_NAME){
		try {
			List<Indent> schools = new ArrayList<Indent>();
			Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
			if(detail!=null){
				JSONArray array = new JSONArray(detail.toString());
				if(array.length() > 0){
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						Indent indent = new Indent();
						List<String> urls = new ArrayList<String>();
						indent.setIndentId(obj.getInt("OrderFormId"));
						indent.setAddress(obj.getString("Address"));
						indent.setReserve(obj.getString("Comment"));
						indent.setSum(obj.getInt("TotalAmount"));
						indent.setFavorableSum(obj.getInt("PrivilegeAmount"));
						indent.setFukuangSum(obj.getInt("PayableAmount"));
						indent.setIndentItate(obj.getString("Status"));
						indent.setOrderFormNumber(obj.getString("OrderFormNumber"));
						indent.setOrdersTime(obj.getString("CreateTime"));
						if(!obj.getString("GoodPictUrl").equals("[]")){
							JSONArray array1 = new JSONArray(obj.getString("GoodPictUrl"));
							for (int j = 0; j < array1.length(); j++) {
								JSONObject ob = array1.getJSONObject(j);
								urls.add(ob.getString("url"));
							}
							indent.setUrls(urls);
						}
						if(!obj.getString("FoodPictUrls").equals("[]")){
							JSONArray array1 = new JSONArray(obj.getString("FoodPictUrls"));
							for (int j = 0; j < array1.length(); j++) {
								JSONObject ob = array1.getJSONObject(j);
								urls.add(ob.getString("url"));
							}
							indent.setUrls(urls);
						}
						schools.add(indent);
					}
				}
			}
			return schools;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static List<OrderInfo> getIndentgoodsInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<OrderInfo> goodsinfo = new ArrayList<OrderInfo>();
		try {
			
			Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
			if(detail != null){
				JSONArray arr = new JSONArray(detail.toString());
				for (int i = 0; i < arr.length(); i++) {
					JSONObject objosn = arr.getJSONObject(i);
					if(!objosn.getString("GoodsList").equals("[]")){
						JSONArray array1 = new JSONArray(objosn.getString("GoodsList"));
						for (int j = 0; j < array1.length(); j++) {
							JSONObject ob = array1.getJSONObject(j);
							OrderInfo goods = new OrderInfo();
							goods.setId(ob.getInt("GoodsId"));
							goods.setNumber(ob.getInt("Amount"));
							goods.setName(ob.getString("GoodsName").trim());
							goods.setImage(ob.getString("GoodPicture"));
							goods.setPrice(ob.getDouble("Price"));
							goods.setGoodsStandard(ob.getString("Standard").trim());
							goodsinfo.add(goods);
						}
					}
					if(!objosn.getString("FoodsList").equals("[]")){
						JSONArray array = new JSONArray(objosn.getString("FoodsList"));
						for (int j = 0; j < array.length(); j++) {
							JSONObject ob = array.getJSONObject(j);
							OrderInfo goods = new OrderInfo();
							goods.setId(ob.getInt("FoodsId"));
							goods.setNumber(ob.getInt("Amount"));
							goods.setImage(ob.getString("FoodPicture"));
							goods.setPrice(ob.getDouble("Price"));
							goods.setName(ob.getString("Name").trim());
							goods.setRestaurantName(ob.getString("RestaurantName").trim());
							goodsinfo.add(goods);
						}
					}
				}
			}
			
			
			return goodsinfo;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return goodsinfo;
	}
	//搜商品
	public static List<GoodsInfo> getGoodsInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<GoodsInfo> schools = new ArrayList<GoodsInfo>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
				GoodsInfo school = new GoodsInfo();
				school.setGoodsId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("Id").toString()));
				school.setGoodsName(((SoapObject)detail.getProperty(i)).getProperty("GoodsName").toString());
				school.setImage(((SoapObject)detail.getProperty(i)).getProperty("GoodsPictureUrl").toString());
				school.setPrice(Double.parseDouble(((SoapObject)detail.getProperty(i)).getProperty("Price").toString()));
				if(((SoapObject)detail.getProperty(i)).hasProperty("GoodsStandard")){
					school.setGoodsStandard(((SoapObject)detail.getProperty(i)).getProperty("GoodsStandard").toString());
				}
				if(((SoapObject)detail.getProperty(i)).hasProperty("GoodsInfo")){
					school.setGoodsIntroduce(((SoapObject)detail.getProperty(i)).getProperty("GoodsInfo").toString());
				}
				schools.add(school);
			}
		}else{
			return null;
		}
		
		return schools;
	}
	/*获取当前的广告列表()*/
	public static List<Poster> getPosterInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<Poster> schools = new ArrayList<Poster>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail != null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
				Poster poster = new Poster();
				poster.setPosterId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("Id").toString()));
				poster.setImageUri(((SoapObject)detail.getProperty(i)).getProperty("ADImageUrl").toString());
				poster.setTitle(((SoapObject)detail.getProperty(i)).getProperty("Title").toString());
				poster.setPointer(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("Pointer").toString()));
				poster.setGoodsId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("GoodsId").toString()));
				if(((SoapObject)detail.getProperty(i)).hasProperty("ADUrl")){
					poster.setPosterChaining(((SoapObject)detail.getProperty(i)).getProperty("ADUrl").toString());
				}
				schools.add(poster);
			}
		}else{
			return  null;
		}
		return schools;
	}
	//通过学生账号获取学生信息
		public static List<Student> getlStudentInfo(List<String> name, List<String> parameter, String METHOD_NAME){
			List<Student> students = new ArrayList<Student>();
			SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
			if(detail!=null){
				Student student = new Student();
			student.setStu_id(detail.getProperty("StudentID").toString());
			student.setStu_name(detail.getProperty("Name").toString());
			if(detail.hasProperty("Address")){
				student.setStu_address(detail.getProperty("Address").toString());
			}
			student.setStu_sex(detail.getProperty("Sex").toString());
			student.setPhone_num(detail.getProperty("Phone").toString());
			student.setStu_school(detail.getProperty("SchoolName").toString());
			if(detail.hasProperty("PhotoUrl")){
				student.setStu_pic(detail.getProperty("PhotoUrl").toString());
			}
			student.setStu_pwd(detail.getProperty("Password").toString());
			students.add(student);
			}else{
				return null;
			}
			
			
			return students;
		}
	//获取注册返回的信息
	public static String GetRegisterReturnInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		String region;
		if(detail!=null){
			region = (String) detail.getProperty(0);
		}else{
			return null;
		}
		
		return region;
	}
	//获取找回密码返回的信息
	public static String GetReturnInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		String region;
		if(detail!=null){
			region = (String) detail.getProperty(0);
		}else{
			return null;
		}
		return region;
	}
	//根据快餐名称得到您快餐的详细信息
	public static Dish GetFoodsInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		Dish dish = new Dish();
		if(detail!=null){
			dish.setDishId(Integer.parseInt(detail.getProperty("FoodId").toString()));
		dish.setDishName(detail.getProperty("FoodName").toString());
		dish.setImage(detail.getProperty("FoodPicture").toString());
		dish.setPrice(Double.parseDouble(detail.getProperty("Price").toString()));
		dish.setIsBusy(detail.getProperty("IsBusy").toString());
		}else{
			return null;
		}
		
		return dish;
	}
	//获取全部菜的分类
	public static List<String> getAllDishSort(List<String> name, List<String> parameter, String METHOD_NAME){
		List<String> sorts = new ArrayList<String>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			sorts.add(((SoapObject)detail.getProperty(i)).getProperty("IsBusy").toString());
		}
		}else{
			return null;
		}
		
		return sorts;
	}
	
	public static List<Eatery> getAllEaterySort(List<String> name, List<String> parameter, String METHOD_NAME){
		List<Eatery> sorts = new ArrayList<Eatery>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			Eatery ea = new Eatery();
			ea.setEateryName(((SoapObject)detail.getProperty(i)).getProperty("RestaurantName").toString());
			ea.setEateryBusy(((SoapObject)detail.getProperty(i)).getProperty("IsBusy").toString());
			sorts.add(ea);
		}
		}else{
			return null;
		}
		
		
		return sorts;
	}
	//加入购物车
	public static boolean getAddShoopingCar(List<String> name, List<String> parameter, String METHOD_NAME){
		Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
		boolean rely;
		if(detail!=null){
			rely = Boolean.parseBoolean(detail.toString());
		}else{
			return false;
		}
		
		return rely;
	}
	//获取购物车信息
	public static List<ShoppingCar> getShoppingInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<ShoppingCar> sorts = new ArrayList<ShoppingCar>();
		Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
		try {
			JSONObject objosn = new JSONObject(detail.toString());
			JSONArray array = new JSONArray(objosn.getString("GoodCarList"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				ShoppingCar shoppingcar = new ShoppingCar();
				shoppingcar.setCategoryId(obj.getInt("Id"));
				shoppingcar.setGoodsNumber(obj.getInt("GoodsAmount"));
				
				JSONObject ob = new JSONObject(obj.getString("Good"));
				GoodsInfo goods = new GoodsInfo();
				goods.setGoodsId(ob.getInt("Id"));
				goods.setGoodsName(ob.getString("GoodsName"));
				goods.setImage(ob.getString("GoodsPictureUrl"));
				goods.setGoodsStandard(ob.getString("GoodsStandard"));
				goods.setPrice(ob.getDouble("Price"));
				
				shoppingcar.setGoods(goods);
				sorts.add(shoppingcar);
			}
			JSONArray arra = new JSONArray(objosn.getString("FoodsCarList"));
			for (int i = 0; i < arra.length(); i++) {
				JSONObject obj = arra.getJSONObject(i);
				ShoppingCar shoppingcar = new ShoppingCar();
				shoppingcar.setCategoryId(obj.getInt("Id"));
				shoppingcar.setGoodsNumber(obj.getInt("FoodsAmount"));
				JSONObject ob = new JSONObject(obj.getString("Food"));
				Dish goods = new Dish();
				goods.setDishId(ob.getInt("FoodId"));
				goods.setDishName(ob.getString("FoodName"));
				goods.setPrice(ob.getDouble("Price"));
				goods.setImage(ob.getString("FoodPicture"));
				goods.setIsBusy(ob.getString("IsBusy"));
				goods.setRestaurantName(ob.getString("RestaurantName"));
				
				shoppingcar.setDish(goods);
				sorts.add(shoppingcar);
			}
			return sorts;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sorts;
	}
	public static List<ShoppingCar> getFoodsShoppingInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<ShoppingCar> sorts = new ArrayList<ShoppingCar>();
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
				ShoppingCar shopping = new ShoppingCar();
				Dish goods = new Dish();
				shopping.setCategoryId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("Id").toString()));
				goods.setDishName(((SoapObject)detail.getProperty(i)).getProperty("FoodssName").toString());
				goods.setRestaurantName(((SoapObject)detail.getProperty(i)).getProperty("RestaurantName").toString());
				goods.setDishId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("FoodsId").toString()));
				shopping.setGoodsNumber(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("FoodsAmount").toString()));
				goods.setIsBusy(((SoapObject)detail.getProperty(i)).getProperty("IsBusy").toString());
				shopping.setDish(goods);
				sorts.add(shopping);
			}
			return sorts;
		}else{
			return null;
		}
		
		
	}
	//获取地址信息
		public static Address getDeAddressInfo(List<String> name, List<String> parameter, String METHOD_NAME){
			SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
			Address address = new Address();
			if(detail != null){
				address.setAddressId(Integer.parseInt((detail.getProperty("AddressId").toString())));
				address.setAddressInfo((detail.getProperty("AddressInfo").toString()));
				address.setDefault(toBoolean(detail.getProperty("IsDefault").toString()));
				address.setRecipient(detail.getProperty("Recipient").toString());
				address.setPhoneNum(detail.getProperty("PhoneNum").toString());
				return address;
			}else{
				return null;
			}
			
			
		}
		private static boolean toBoolean(String name) { 
			 return ((name != null) && name.equalsIgnoreCase("true"));
			    }

		public static List<Address> getAddressInfo(List<String> name, List<String> p, String METHOD_NAME) {
			List<Address> addresses = new ArrayList<Address>();
			SoapObject detail = Analysis_Util.getDetail(name, p, METHOD_NAME);
			if(detail!=null){
				for (int i = 0; i < detail.getPropertyCount(); i++) {
				Address address = new Address();
				address.setAddressId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("AddressId").toString()));
				address.setAddressInfo(((SoapObject)detail.getProperty(i)).getProperty("AddressInfo").toString());
				address.setDefault(toBoolean(((SoapObject)detail.getProperty(i)).getProperty("IsDefault").toString()));
				address.setRecipient(((SoapObject)detail.getProperty(i)).getProperty("Recipient").toString());
				address.setPhoneNum(((SoapObject)detail.getProperty(i)).getProperty("PhoneNum").toString());
				addresses.add(address);
			}
			}else{
				return null;
			}
			
			return addresses;
		}
		//加入收藏夹
		public static Object getAddFavorite(List<String> name, List<String> parameter, String METHOD_NAME){
			Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
			if(detail!=null){
				return detail;
			}else{
				return null;
			}
			
		}
		//根据商品id获取商品详细信息
		public static GoodsInfo getIdGoodsInfo(List<String> name, List<String> parameter, String METHOD_NAME){
			GoodsInfo goods = new GoodsInfo();
			SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
			if(detail!=null){
			goods.setGoodsName(detail.getProperty("GoodsName").toString());
			goods.setImage(detail.getProperty("GoodsPictureUrl").toString());
			goods.setPrice(Double.parseDouble(detail.getProperty("Price").toString()));
			if(detail.hasProperty("GoodsStandard")){
				goods.setGoodsStandard(detail.getProperty("GoodsStandard").toString());
			}
			return goods;
			}else{
				return null;
			}
			
			
		}
		//根据商品id获取商品详细信息
				public static OrderInfo getIdGoodsOrderInfo(List<String> name, List<String> parameter, String METHOD_NAME){
					OrderInfo goods = new OrderInfo();
					SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
					if(detail!=null){
					goods.setName(detail.getProperty("GoodsName").toString());
					goods.setImage(detail.getProperty("GoodsPictureUrl").toString());
					goods.setPrice(Double.parseDouble(detail.getProperty("Price").toString()));
					if(detail.hasProperty("GoodsStandard")){
						goods.setGoodsStandard(detail.getProperty("GoodsStandard").toString());
					}
					return goods;
					}else{
						return null;
					}
					
					
				}
		public static OrderInfo getIdFoodsInfo(List<String> name, List<String> parameter, String METHOD_NAME){
			OrderInfo goods = new OrderInfo();
			SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
			if(detail!=null){
				goods.setId(Integer.parseInt(detail.getProperty("FoodId").toString()));
				goods.setName(detail.getProperty("FoodName").toString());
				goods.setImage(detail.getProperty("FoodPicture").toString());
				goods.setPrice(Double.parseDouble(detail.getProperty("Price").toString()));
				goods.setRestaurantName(detail.getProperty("RestaurantName").toString());
			return goods;
			}else{
				return null;
			}
			
		}
	//根据学生id获取快餐收藏夹详细信息
		public static List<FoodsFavorite> getFoodsPavoriteInfo(List<String> name, List<String> parameter, String METHOD_NAME){
			List<FoodsFavorite> foods = new ArrayList<FoodsFavorite>();
			FoodsFavorite goods = null;
			SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
			if(detail!=null){
				for (int i = 0; i < detail.getPropertyCount(); i++) {
				goods = new FoodsFavorite();
				goods.setFoodId(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("FoodId").toString()));
				goods.setFoodName(((SoapObject)detail.getProperty(i)).getProperty("FoodName").toString());
				goods.setPrice(Double.parseDouble(((SoapObject)detail.getProperty(i)).getProperty("Price").toString()));
				if(((SoapObject)detail.getProperty(i)).hasProperty("FoodPicture")){
					goods.setFoodPicture(((SoapObject)detail.getProperty(i)).getProperty("FoodPicture").toString());
				}
				goods.setIsBusy(((SoapObject)detail.getProperty(i)).getProperty("IsBusy").toString());
				if(((SoapObject)detail.getProperty(i)).getProperty("RestaurantName")!=null){
					goods.setRestaurantName(((SoapObject)detail.getProperty(i)).getProperty("RestaurantName").toString());
				}
				foods.add(goods);
			}
			return foods;
			}else{
				return null;
			}
			
		}
		//删除快餐收藏夹
		public static boolean getDeleteFoodsFavorite(List<String> name, List<String> parameter, String METHOD_NAME){
			Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
			boolean rely;
			if(detail!=null){
				rely= Boolean.parseBoolean(detail.toString());
				return rely;
			}else{
				return false;
			}
			
		}
		//删除商品收藏夹
				public static boolean getDeleteGoodsFavorite(List<String> name, List<String> parameter, String METHOD_NAME){
					Object detail = Analysis_Util.getDetail4oneResult(name, parameter, METHOD_NAME);
					boolean rely;
					if(detail!=null){
						rely= Boolean.parseBoolean(detail.toString());
						return rely;
					}else{
						return false;
					}
				}

//根据学生id获取商品收藏夹详细信息
	public static List<GoodsFavorite> getGoodsPavoriteInfo(List<String> name, List<String> parameter, String METHOD_NAME){
		List<GoodsFavorite> foods = new ArrayList<GoodsFavorite>();
		GoodsFavorite goods = null;
		SoapObject detail = Analysis_Util.getDetail(name, parameter, METHOD_NAME);
		if(detail!=null){
			for (int i = 0; i < detail.getPropertyCount(); i++) {
			goods = new GoodsFavorite();
			goods.setGoods(Integer.parseInt(((SoapObject)detail.getProperty(i)).getProperty("GoodsId").toString()));
			goods.setGoodsName(((SoapObject)detail.getProperty(i)).getProperty("GoodsName").toString());
			goods.setPrice(Double.parseDouble(((SoapObject)detail.getProperty(i)).getProperty("Price").toString()));
			goods.setGoodsPictureUrl(((SoapObject)detail.getProperty(i)).getProperty("GoodsPictureUrl").toString());
			goods.setBrandName(((SoapObject)detail.getProperty(i)).getProperty("BrandName").toString());
			if(((SoapObject)detail.getProperty(i)).hasProperty("GoodsStandard")){
				goods.setGoodsStandard(((SoapObject)detail.getProperty(i)).getProperty("GoodsStandard").toString());
			}
			
			foods.add(goods);
		}
		return foods;
		}else{
			return null;
		}
		
	}
}
