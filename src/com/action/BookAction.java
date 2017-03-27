package com.action;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import com.biz.IBookBiz;
import com.commons.BeanKit;
import com.commons.PageBean;
import com.commons.PropKit;
import com.commons.StrKit;
import com.entity.Book;
import com.entity.Category;

@Controller
public class BookAction {
	@Resource
	private IBookBiz bbiz;
	public String listBooksByCategory(){
		Category cat=BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), Category.class);
		PageBean<Book> pb = new PageBean<Book>();
		//第一步：设置pageNumber
		pb.setPageNumber(this.getPageNumber(ServletActionContext.getRequest()));
		//第二步：设置pageSize
		pb.setPageSize(PropKit.use("pagesize.properties").getInt("book_page_size"));
		//第三步：设置url
		pb.setUrl(this.getUrl(ServletActionContext.getRequest(), null));
		//第四步：设置list(list是指定页码显示内容的集合)
		pb.setList(bbiz.listBooksByCategory(cat,pb));
		//第五步：设置totalRecords
		pb.setTotalRecords(bbiz.countBooksByCategory(cat));
		//第六步：将pb存放到request中
		ServletActionContext.getRequest().setAttribute("pb", pb);
		return "success";
	}
	
	public int getPageNumber(HttpServletRequest request) {
		int i = 0;
		try {
			//当前台页面没有传递pageNumber,默认为1
			i = 1;
			String pageNumberStr = request.getParameter("pageNumber");
			if(StrKit.notBlank(pageNumberStr)){
				i = Integer.parseInt(pageNumberStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	
	public String getUrl(HttpServletRequest request,String flagString) {
		String str = "";
		try {
			String contextPath = request.getContextPath();//项目名http://localhost:8080/firstjdbc
			String servletPath = request.getServletPath();//servlet名称：/stuAction
			String queryString = request.getQueryString();//参数：？后面的所有内容
			if(queryString == null){
				queryString = flagString;
			}
			if(queryString.contains("&pageNumber=")){
				queryString = queryString.substring(0, queryString.lastIndexOf("&pageNumber="));
			}
			str = contextPath + servletPath + "?" + queryString;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return str;
	}
	
	public String getBookByBid() {
		try {
			String bid=ServletActionContext.getRequest().getParameter("bid");
			Book pb= (Book)bbiz.findAll(bid);
			ServletActionContext.getRequest().setAttribute("pb", pb);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return "success";
	}
	
	public String getBookByAuthor() {
		try {
			Book book = BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), Book.class);
			PageBean<Book> pb = new PageBean<Book>();
			pb.setPageNumber(this.getPageNumber(ServletActionContext.getRequest()));
			pb.setPageSize(PropKit.use("pagesize.properties").getInt("book_page_size"));
			pb.setUrl(this.getUrl(ServletActionContext.getRequest(), null));
			pb.setList(bbiz.listBooksByAuthor(book,pb));
			pb.setTotalRecords(bbiz.countBooksByAuthor(book));
			ServletActionContext.getRequest().setAttribute("pb", pb);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return "success";
	}
	
	public String getBookByPress() {
		try {
			Book book = BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), Book.class);
			PageBean<Book> pb = new PageBean<Book>();
			pb.setPageNumber(this.getPageNumber(ServletActionContext.getRequest()));
			pb.setPageSize(PropKit.use("pagesize.properties").getInt("book_page_size"));
			pb.setUrl(this.getUrl(ServletActionContext.getRequest(), null));
			pb.setList(bbiz.listBooksByPress(book,pb));
			pb.setTotalRecords(bbiz.countBooksByPress(book));
			ServletActionContext.getRequest().setAttribute("pb", pb);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return "success";
	}
	
	public String doFuzzyQuery(){
		try {
			String bname=ServletActionContext.getRequest().getParameter("bname");
			int pageNumber=this.getPageNumber(ServletActionContext.getRequest());
			int pageSize=12;
			if(bname.isEmpty()){
				PageBean<Book> pb=bbiz.showAll(pageNumber, pageSize);
				pb.setUrl(this.getUrl(ServletActionContext.getRequest(), null));
				ServletActionContext.getRequest().setAttribute("pb", pb);
			}else{
				PageBean<Book> pb=bbiz.doFuzzyQuery(bname,pageNumber, pageSize);
				pb.setUrl(this.getUrl(ServletActionContext.getRequest(), null));
				ServletActionContext.getRequest().setAttribute("pb", pb);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return "success";
	}
	
	public String appointQuery(){
		try {
			HttpServletRequest request=ServletActionContext.getRequest();
			Book book = BeanKit.toBean(request.getParameterMap(), Book.class);
			PageBean<Book> pb = new PageBean<Book>();
			pb.setPageNumber(this.getPageNumber(request));
			pb.setPageSize(PropKit.use("pagesize.properties").getInt("book_page_size"));
			pb.setUrl(this.getUrl(request, null));
			pb.setList(bbiz.listBooksByConditionsFuzzy(book,pb));
			pb.setTotalRecords(bbiz.listBooksByConditionsFuzzy(book));
			request.setAttribute("pb", pb);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return "success";
	}
}
