package cn.ylw.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class FileUploadAction extends ActionSupport{

	private String username;
	
    private List<File> file;  
    private List<String> fileFileName;  
    private List<String> fileContentType;
    
   
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public List<File> getFile() {
		return file;
	}


	public void setFile(List<File> file) {
		this.file = file;
	}


	public List<String> getFileFileName() {
		return fileFileName;
	}




	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}




	public List<String> getFileContentType() {
		return fileContentType;
	}




	public void setFileContentType(List<String> fileContentType) {
		this.fileContentType = fileContentType;
	}


	public void fileupload() {
		
		InputStream is = null;
		OutputStream os = null;
		PrintWriter pw = null; 
		
		try {
			String user = (String) ServletActionContext.getRequest().getSession().getAttribute("username");
			if(user==null) {
				System.out.println("上传文件失败 ： 未登录");
				throw new Exception("未登录");			
			}
			
			String path = ServletActionContext.getServletContext().getRealPath("/uploadFile/"+user); 	
			System.out.println("文件上传路径为：" + path + " 数量为："+ file.size());
			
			for (int i = 0; i < file.size(); i++) {	
				pw = ServletActionContext.getResponse().getWriter();
				File f = file.get(i); 
				is = new FileInputStream(f);
				byte[] buffer = new byte[1024];
				int length = 0;
				os = new FileOutputStream(new File(path, getFileFileName().get(i)));
				while((length = is.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
				System.out.println(f.length() / 1024 + " K  " + fileFileName + "---上传成功");
				pw.write("true");
			}
			} catch (Exception e) {
				pw.write("亲，你还没有登录");
				
			} finally {
				try {
					os.flush();
					if(os != null)
						os.close();
					if(is != null)
						is.close();
					pw.flush();
					pw.close();
				} catch (IOException e) {
					pw.write("服务器异常：" );
				}
				
			}
		}
	
	
	
	
}
