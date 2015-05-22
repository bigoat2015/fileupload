package cn.ylw.file;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.struts2.ServletActionContext;


public class MyFile {
	
	private Properties pro;
	private InputStream in;
	private OutputStream os;
	private String path;
	private File file;
	private String upFile;
	
	public MyFile() {
		
		try {
			path = ServletActionContext.getServletContext().getRealPath("") + "\\";
			
			pro = new Properties();
			file = new File(path + "user.properties");
			if(!file.exists()) {    
				System.out.println("文件user.properties不存在创建：" + file.createNewFile());    
			}
	
			System.out.println("#######################  "+path);
			
			File f = new File(path + "uploadFile");
			if(!f.exists()  && !f.isDirectory()) {        
			   System.out.println("文件夹uploadFile不存在创建：" + f.mkdir());    
			}
			upFile = f.getPath() + "\\";
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getValueByKey(String key) throws Exception {
		
		try {
			
			in = new BufferedInputStream(new FileInputStream(file));
			pro.load(in);
			return pro.getProperty(key);
			
		} catch (Exception e) {
			throw new Exception("读入文件失败");
		} finally {
			in.close();
		}
	}
	
	public void writeProperties(String key, String value) throws Exception {
		try{
			
			in = new BufferedInputStream(new FileInputStream(file));
			pro.load(in);
			os = new FileOutputStream(file);
			pro.setProperty(key, value);
			pro.store(os, "");
		
		} catch (Exception e) {
			throw new Exception("写入文件失败");
		} finally {
			os.flush();
			os.close();
		}
	} 
	
	
	public void createFile(String filename) throws Exception {
		
		File file = new File(upFile + filename);
		
		if(!file .exists()  && !file .isDirectory()) { 
			 boolean b = file .mkdir();
			 System.out.println("文件夹"+filename+"不存在创建：" + b);
			 if(b==false) {
				 throw new Exception("创建文件夹失败");
			 }
			 
		} else {  
			throw new Exception("文件夹创建失败可能已经存在：" + filename);
		} 		
	}

	
	public String getFilesByUser(String username) {
		
		File f = new File(upFile + username);
		String json = "[";
		String tap = ",";
		
		if(f.exists() && f.isDirectory()) {
			File[] fs = f.listFiles();
			if(fs.length == 0){
				return "";
			}
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (int i=0; i<fs.length; i++) {
				              
		        long t = fs[i].lastModified();  
		        cal.setTimeInMillis(t);    
		        String time = formatter.format(cal.getTime()); 
		        String size = String.format("%.2f", (double) fs[i].length() / 1024 / 1024);
		        
				json += "{\"name\" : \"" + fs[i].getName() + "\", " +
						"\"size\" : \"" + size + " M\", " +
						"\"time\" : \"" + time + "\", " +
						"\"url\" : \"uploadFile/" + username +"/"+ fs[i].getName() + 		
						"\"}" + tap;
				if(i==fs.length-2) {
					tap = "";
				}
			}
			//[{"name" : "fileupload.war ", "size" : 4317314, "url" : uploadFile/ylw/fileupload.war"},{"name" : "可行性报告.doc ", "size" : 67072, "url" : uploadFile/ylw/可行性报告.doc"},{"name" : "需求分析.doc ", "size" : 83968, "url" : uploadFile/ylw/需求分析.doc"}

			
    	} else {
    		System.out.println("用户目录不存在");
    	}
		
		System.out.println(json);
		return json + "]";
		
	
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUpFile() {
		return upFile;
	}

	public void setUpFile(String upFile) {
		this.upFile = upFile;
	}


}


