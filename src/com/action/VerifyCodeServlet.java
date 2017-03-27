package com.action;

import java.awt.image.BufferedImage;
import org.apache.struts2.ServletActionContext;
import com.vcode.VerifyCode;

public class VerifyCodeServlet {
	
	public String verifyCodeServlet() throws Exception{
		VerifyCode vc = new VerifyCode();
		BufferedImage image = vc.getImage();// 获取一次性验证码图片
		// 该方法必须在getImage()方法之后来调用
		// System.out.println(vc.getText());//获取图片上的文本
		VerifyCode.output(image, ServletActionContext.getResponse().getOutputStream());// 把图片写到指定流中
		// 把文本保存到session中，为LoginServlet验证做准备
		ServletActionContext.getRequest().getSession().setAttribute("vCode", vc.getText());
		return null;
	}
}
