package cn.ylw.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class User {

	private String path;
	private File user;
	
	public User(String path) {
		this.path = path;
		
		user = new File(path + "user.properties");    
		if(!user.exists()) {
		    try {    
		        user.createNewFile();    
		    } catch (IOException e) {     
		        e.printStackTrace();    
		    }    
		} 
		
		System.out.println(user.getPath());
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean regist(String username, String password) throws Exception {
		
		Properties prop = new Properties();
		InputStream in;
		OutputStream out;
		try{
			
			in = new BufferedInputStream(new FileInputStream(user));
			out = new FileOutputStream(user);
			prop.load(in);
			
			Map<String, String> toSaveMap = new HashMap<String ,String>();
			Set keys = prop.keySet();
			for (Iterator itr = keys.iterator(); itr.hasNext();) {
				String key = (String) itr.next();
				String value = prop.getProperty(key);
				toSaveMap.put(key, value);
			}
			
			if(prop.get(username) == null) {
				

				toSaveMap.put(username, password);  			
				prop.putAll(toSaveMap);
				prop.store(out, "");
				
				File file = new File(path + username);
				
				if(!file .exists()  && !file .isDirectory()) {       
				    System.out.println("//不存在 创建");  
				    file .mkdir();    
				    return true;
				} else {  
					System.out.println("//目录存在");  
					return false;
				} 
				
			} else {
				return false;
			}
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			
		}
		
	}
	
	public boolean login(String username, String password) throws Exception {
		
		Properties prop = new Properties();
		
		try{
			InputStream in = new BufferedInputStream(new FileInputStream(user));
			prop.load(in);
			String p = prop.getProperty(username);
			
			if(p != null) {
				if(p.equals(password)){
				
					return true;
				} else {
					return false;
				}
					
			} else {
				return false;
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}
