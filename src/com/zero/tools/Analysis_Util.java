package com.zero.tools;


import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class Analysis_Util {
	private static final String NAMESPACE = "http://tempuri.org/";

	private static final String URL = "http://90world.51idctg.com/Service1.asmx";
	
	public static SoapObject getDetail(List<String> parameters,List<String> values, String METHOD_NAME){
		
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			if(parameters != null){
				for (int i = 0; i < parameters.size(); i++) {
					request.addProperty(parameters.get(i), values.get(i));
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
			        SoapEnvelope.VER10);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,20000);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(NAMESPACE + METHOD_NAME, envelope);
			
			SoapObject result = (SoapObject)envelope.bodyIn;
			SoapObject detail = (SoapObject)result.getProperty(METHOD_NAME+"Result");
			androidHttpTransport.reset();
			return detail;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
	}
	
	public static String getDetail4oneResult(List<String> parameters,List<String> values, String METHOD_NAME){
		try {
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
			if(parameters.size() > 0){
				for (int i = 0; i < parameters.size(); i++) {
					request.addProperty(parameters.get(i), values.get(i));
				}
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER10);
			envelope.bodyOut = request;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,20000);
			androidHttpTransport.debug = true;
			androidHttpTransport.call(NAMESPACE + METHOD_NAME, envelope);
			SoapObject result = (SoapObject)envelope.bodyIn;
			Object detail = (Object)result.getProperty(METHOD_NAME+"Result");
			return detail.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
