package com.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.biz.ICategories;
import com.entity.Category;
import com.opensymphony.xwork2.ActionSupport;
@SuppressWarnings("serial")
@Controller
public class CategoryAction extends ActionSupport {
	@Resource
	private ICategories cbiz;
	
	public String listCats() throws Exception{
		List<Category> catlist=new ArrayList<Category>();
		List<Category> clist=cbiz.listAllCategorier();
		for (Category cat : clist) {
			if(cat.getPid() != null){
				cat.setUrl("/sshgoods/listBooksByCategory?cid="+cat.getCid());
				cat.setTarget("body");
			}
			catlist.add(cat);
		}
		ServletActionContext.getResponse().setCharacterEncoding("UTF-8");
		String str=JSON.toJSONString(catlist);
		PrintWriter out = ServletActionContext.getResponse().getWriter();
		out.print(str);
		out.flush();
		out.close();
		return null;
	}
}
