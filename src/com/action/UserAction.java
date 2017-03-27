package com.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson.JSONObject;
import com.biz.IUserBiz;
import com.commons.BeanKit;
import com.commons.StrKit;
import com.entity.User;
import com.opensymphony.xwork2.ActionSupport;
@SuppressWarnings("serial")
@Controller
public class UserAction extends ActionSupport {
	@Resource
	private IUserBiz ubiz ;
	private JSONObject result;
	public String regist() throws MessagingException, IOException{
		User user=BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), User.class);
		// 后台校验
		Map<String, String> errors=validateRegist(user, ServletActionContext.getContext().getSession());
		if (errors != null && errors.size() > 0) {
			ServletActionContext.getRequest().setAttribute("user", user);
			ServletActionContext.getRequest().setAttribute("errors", errors);
			return "error";
		}
		// 注册用户
		ubiz.regist(user);
		// 保存成功信息，转发到下一个页面
		ServletActionContext.getRequest().setAttribute("code", "success");
		ServletActionContext.getRequest().setAttribute("msg", "恭喜，注册成功！请马上到邮箱完成激活！");
		return "success";
	}

	private Map<String, String> validateRegist(User user, Map<String, Object> session) {
		Map<String, String> errors = new HashMap<String, String>();
		String loginname = user.getLoginname();
		if (StrKit.isBlank(loginname)) {
			errors.put("loginname", "用户名不能为空！");
		} else if (loginname.length() < 6 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在6~20之间！");
		} else if (ubiz.existUserName(loginname)) {
			errors.put("loginname", "用户名已被占用！");
		}

		String loginpass = user.getLoginpass();
		if (StrKit.isBlank(loginpass)) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 6 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在6~20之间！");
		}

		String reloginpass = user.getReloginpass();
		if (StrKit.isBlank(reloginpass)) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if (!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}

		String email = user.getEmail();
		if (StrKit.isBlank(email)) {
			errors.put("email", "Email不能为空！");
		} else if (!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if (ubiz.existEmail(email)) {
			errors.put("email", "Email已被注册！");
		}

		String verifyCode = user.getVerifyCode();
		String vCode=(String) ServletActionContext.getRequest().getSession().getAttribute("vCode");
		if (StrKit.isBlank(verifyCode)) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vCode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	
	public String existLoginname() throws Exception{
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		result= new JSONObject();
		String loginname =ServletActionContext.getRequest().getParameter("loginname");
		boolean bl = ubiz.existUserName(loginname);
		if(bl){
			result.put("status", "exist");
		}else{
			result.put("status", "noexist");
		}		
		out.print(result);
		out.flush();
		out.close();
		//返回值为字符串，如果字符串中包含"f:"，这就是转发，如果字符串中包含"r:"，这就是重定向，如果return null，既不转发也不重定向，
		return null;
	}
	
	public String validateEmail() throws Exception{
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		result= new JSONObject();
		String email =ServletActionContext.getRequest().getParameter("email");
		boolean bl = ubiz.existEmail(email);
		if(bl){
			result.put("status", "exist");
		}else{
			result.put("status", "noexist");
		}		
		out.print(result);
		out.flush();
		out.close();
		return null;
	}
	
	public String validateVerifyCode () throws Exception {
		//在session中获取
		String vCode=(String) ServletActionContext.getRequest().getSession().getAttribute("vCode");
		result= new JSONObject();
		String verifyCode=ServletActionContext.getRequest().getParameter("verifyCode");
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		if(verifyCode.equalsIgnoreCase(vCode))
		{
			result.put("status", "pass");
		}else{
			result.put("status", "nopass");
		}
		out.print(result);
		out.flush();
		out.close();
		return null;
	}
	
	public String login() throws UnsupportedEncodingException {
		// 将请求流中的数据封装到User对象中
		User formUser = BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), User.class);
		// 后台校验
		Map<String, String> errors = validateLogin(formUser, ServletActionContext.getRequest().getSession());
		if (errors != null && errors.size() > 0) {
			ServletActionContext.getRequest().setAttribute("user", formUser);
			ServletActionContext.getRequest().setAttribute("errors", errors);
			return "error";
		}
		User user = ubiz.login(formUser);
		if(user == null) {
			ServletActionContext.getRequest().setAttribute("msg", "用户名或密码错误！");
			ServletActionContext.getRequest().setAttribute("user", formUser);
			return "error";
		} else {
			if(!user.isStatus()) {
				ServletActionContext.getRequest().setAttribute("msg", "您还没有激活！");
				ServletActionContext.getRequest().setAttribute("user", formUser);
				return "error";				
			} else {
				ServletActionContext.getRequest().getSession().setAttribute("sessionUser", user);
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname, "UTF-8");
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);
				ServletActionContext.getResponse().addCookie(cookie);
				/*ActionContext.getContext().getSession().put("user","id");*/
				return "success";
			}
		}
	}
	private Map<String, String> validateLogin(User formUser, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		String loginname = formUser.getLoginname();
		if (StrKit.isBlank(loginname)) {
			errors.put("loginname", "用户名不能为空！");
		} else if (loginname.length() < 6 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在6~20之间！");
		}
		String loginpass = formUser.getLoginpass();
		if (StrKit.isBlank(loginpass)) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 6 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在6~20之间！");
		}
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (StrKit.isBlank(verifyCode)) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	public String validatePassword() throws Exception{
		PrintWriter out=ServletActionContext.getResponse().getWriter();
		JSONObject jo = new JSONObject();
		String loginpass=ServletActionContext.getRequest().getParameter("loginpass");
		
		User users=(User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		users.setLoginpass(loginpass);
		User str=ubiz.login(users);
		if(str==null){
			jo.put("status", "exist");
		}else{
			jo.put("status", "noexist");
		}		
		out.print(jo);
		out.flush();
		out.close();
		return null;
	}
	
	public String updatePassword(){
		User formUser = BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), User.class);
		Map<String, String> errors = validateNewpass(formUser, ServletActionContext.getRequest().getSession());
		if (errors != null && errors.size() > 0) {
			ServletActionContext.getRequest().setAttribute("user", formUser);
			ServletActionContext.getRequest().setAttribute("errors", errors);
			return "error";
		}
		User users=(User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		if(users==null){
			ServletActionContext.getRequest().setAttribute("msg", "你还没有登入");
			return "error";
		}
		formUser.setUid(users.getUid());
		ubiz.findpass(formUser);
		ServletActionContext.getRequest().setAttribute("code", "success");
		ServletActionContext.getRequest().setAttribute("msg", "密码修改成功！");
		return "success";
	}

	private Map<String, String> validateNewpass(User formUser, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		
		String loginpass = formUser.getLoginpass();
		if (StrKit.isBlank(loginpass)) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 6 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在6~20之间！");
		}
		
		String newpass = formUser.getNewpass();
		if (StrKit.isBlank(newpass)) {
			errors.put("newpass", "新密码不能为空！");
		} else if (newpass.length() < 6 || newpass.length() > 20) {
			errors.put("newpass", "新密码长度必须在6~20之间！");
		}
		
		String reloginpass=formUser.getReloginpass();
		if (StrKit.isBlank(reloginpass)) {
			errors.put("newpass", "确认密码不能为空！");
		} else if (reloginpass.length() < 6 || reloginpass.length() > 20) {
			errors.put("newpass", "确认密码长度必须在6~20之间！");
		}
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (StrKit.isBlank(verifyCode)) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	
	public String quit() {
		ServletActionContext.getRequest().getSession().invalidate();
		return "success";
	}
}
