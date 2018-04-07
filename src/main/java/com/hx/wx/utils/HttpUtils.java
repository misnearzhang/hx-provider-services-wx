package com.hx.wx.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @description HTTP连接工具类
 * @author denglitao
 *
 */
public class HttpUtils {
	private static final String ENCODING = "UTF-8";
	
	private static int connectTimeout = 15*1000;
	private static int readTimeout = 15*1000;

	public static String getRequest(URL url) throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
					String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
					String authType) {
			}
		} };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestMethod("GET");
		conn.connect();

		InputStream in = conn.getInputStream();
		return IOUtils.toString(in,"utf-8");
	}
	
	/**
	 * @description 发送HTTP请求
	 * @param url
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public static String postRequest(URL url, Map<String, String> parameters) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
			if(connectTimeout != -1) {
				conn.setConnectTimeout(connectTimeout);
			}
			if (readTimeout != -1) {
				conn.setReadTimeout(readTimeout);
			}
			conn.setRequestMethod("POST");
			//conn.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
			conn.setDoOutput(true);
			conn.connect();
			
			// 写入POST数据
			out = conn.getOutputStream();
			byte[] params = generatorParamString(parameters).getBytes();
			out.write(params, 0, params.length);
			out.flush();
			
			// 读取响应数据
			in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
			StringBuilder buffer = new StringBuilder();
			char[] buf = new char[1000];
			int len = 0;
			while (len >= 0) {
				buffer.append(buf, 0, len);
				len = reader.read(buf);
			}
			return buffer.toString();
		} catch(IOException e) {
			throw e;
		} finally {
			close(in);
			close(out);
			if(conn != null){
				conn.disconnect();
			}
		}
	}
	
	
	
	/**
	 * @description 发送HTTP请求
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String postRequestXML(URL url, String xml) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		InputStream in = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
			if(connectTimeout != -1) {
				conn.setConnectTimeout(connectTimeout);
			}
			if (readTimeout != -1) {
				conn.setReadTimeout(readTimeout);
			}
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");  
			 conn.setRequestProperty("Accept-Charset", "UTF-8");
			//conn.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
			conn.setDoOutput(true);
			conn.connect();
			
			// 写入POST数据
			out = conn.getOutputStream();
			byte[] params = xml.getBytes();
			out.write(params, 0, params.length);
			out.flush();
			
			// 读取响应数据
			in = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
			StringBuilder buffer = new StringBuilder();
			char[] buf = new char[1000];
			int len = 0;
			while (len >= 0) {
				buffer.append(buf, 0, len);
				len = reader.read(buf);
			}
			return buffer.toString();
		} catch(IOException e) {
			throw e;
		} finally {
			close(in);
			close(out);
			if(conn != null){
				conn.disconnect();
			}
		}
	}
	
	/**
	 * @description 生成请求参数字符串
	 * @param parameters
	 * @return
	 */
	public static String generatorParamString(Map<String, String> parameters) {
        StringBuffer params = new StringBuffer();
        if(parameters != null) {
        	for(Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext(); ) {
        		String name = iter.next();
        		String value = parameters.get(name);
        		params.append(name + "=");
                try {
                    params.append(URLEncoder.encode(value, ENCODING));
                } catch (UnsupportedEncodingException e) {
                	throw new RuntimeException(e.getMessage(), e);
                } catch (Exception e) {
                	throw new RuntimeException(String.format("'%s'='%s'", name, value), e);
                }
                if(iter.hasNext())
                	params.append("&");
            }
        }
        return params.toString();
    }
	
	/**
	 * @description 关闭对象
	 * @param f
	 */
	public static void close(Closeable f) {
		if(f != null){
			try{
				f.close();
			}catch(IOException e){
				Logger.getAnonymousLogger().info(e.toString());
			}
		}
	}
	
	public static String inputStream2String(InputStream is) {
		boolean i = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			int i1;
			while ((i1 = is.read()) != -1) {
				baos.write(i1);
			}

			String e = baos.toString();
			baos.flush();
			baos.close();
			return e;
		} catch (IOException arg3) {
			arg3.printStackTrace();
			return null;
		}
	}

}
