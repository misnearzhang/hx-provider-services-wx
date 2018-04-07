package com.hx.wx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

	private static final String BUNDLE_NAME = "/application.properties";

//	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
//
//	private PropertyUtil() {
//	}
//
	public static String getString(String key) {
		try {
			return prop.getProperty(key);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}
	
	private static  Properties prop=new Properties(); 
	static{
		try {  
	        InputStream is=PropertyUtil.class.getResourceAsStream(BUNDLE_NAME);  
	        prop.load(is);  
	        is.close();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } 
	} 
}
