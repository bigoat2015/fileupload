package cn.ylw.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class LogRegAction extends ActionSupport implements ServletResponseAware,ServletRequestAware {
	private static Logger logger = Logger.getLogger(LogRegAction.class);
	private HttpServletResponse response;
	private PrintWriter pw;
	private HttpSession session;
	
	private String username;
	private String password;
	private String rpassword;
	private MyFile file = new MyFile();

	
	private void myValidate() {
		logger.info("字段验证中......");
		
		try{
			if(username==null || username.trim().equals("")) {
				pw.write("用户名不能为空");
				pw.flush();
				pw.close();
				return;
			}
			
			if(password==null || password.trim().equals("")) {
				pw.write("密码不能为空");
				pw.flush();
				pw.close();
				return;
			}
			
		}catch (Exception e) {
			pw.write("服务器异常");
		} 
	}
	
	
	@Override
	public String execute() throws Exception {
		System.out.println("刷新吗");
		session.removeAttribute("username");
		return SUCCESS;
	}
	
	public void regist() {
		myValidate();
		logger.info("注册中......");
		
		try {
			
			if(rpassword==null || !rpassword.equals(password)) {
				pw.write("两次密码不相等");
				pw.flush();
				pw.close();
				return;
			}
			
			String value = file.getValueByKey(username);
			
			if(value == null) {
				file.writeProperties(username, password);
				file.createFile(username);
				pw.write("true");
				logger.info("注册成功");
				
			} else {
				pw.write("用户已存在");
			}
			
		} catch (Exception e) {
			pw.write("服务器异常");
		} finally {
			pw.flush();
			pw.close();
		}

	}
	
	public void login() {
		myValidate();
		logger.info("登录中......");
		
		try {
			 	
			String p = file.getValueByKey(username);
			
			if(p != null) {
				if(p.equals(password)) {
					session.setAttribute("username", username);
					String json = file.getFilesByUser(username);
					pw.write(json);
					logger.info("登录成功");
					
				} else {
					pw.write("false");
				}
			} else {
				pw.write("false");
			}
			
		} catch (Exception e) {
			pw.write("服务器异常");
		} finally {
			pw.flush();
			pw.close();
		}
		
	}

	public String logout() {
		
		Enumeration<String> e = session.getAttributeNames();
		while (e.hasMoreElements()) {
			String string = (String) e.nextElement();
			System.out.println(string);
			
		}
		session.removeAttribute("username");
		logger.info("退出：" + username);
		return "logout";
	}
	
	
	public void checkLogin(){
		logger.info("检查用户是否登录......");
		
		String u = (String) session.getAttribute("username");
		
		try{
			if(u==null) {
				pw.write("请先登录！");
			} else {
				pw.write("true");
			}
		
		} catch (Exception e) {
			pw.write("服务器异常");
		} finally {
			pw.flush();
			pw.close();
		}
		
	}
	
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		response.setContentType("text/html;charset=utf-8");  
	    //response.setCharacterEncoding("UTF-8"); 
		try {
			pw = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void setServletRequest(HttpServletRequest request) {
		this.session = request.getSession();	
		//String p = request.getContextPath();
		//this.path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+p+"/";
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRpassword() {
		return rpassword;
	}


	public void setRpassword(String rpassword) {
		this.rpassword = rpassword;
	} 
	
	
}
