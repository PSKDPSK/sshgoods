package com.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import com.biz.CartBiz;
import com.biz.OrderBiz;
import com.commons.BeanKit;
import com.commons.PageBean;
import com.commons.PropKit;
import com.commons.StrKit;
import com.entity.Cartitem;
import com.entity.Order;
import com.entity.OrderItem;
import com.entity.User;

@Controller
public class OrderAction {
	@Resource
	private OrderBiz obiz;
	@Resource
	private CartBiz cbiz ;
	
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
				queryString = str;
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
	
	public String create() {
		HttpServletRequest request=ServletActionContext.getRequest();
		String cids = request.getParameter("cartItemIds");
		List<Cartitem> clist = cbiz.listCartItemsByCids(cids);
		// 创建订单
		Order order = new Order();
		order.setOid(StrKit.uuid());
		// 将Date转换为String
		order.setOrdertime(String.format("%tF %<tT", new Date()));
		// 设置状态，1表示未付款
		order.setStatus(1);
		order.setAddress(request.getParameter("address"));
		order.setUser((User) request.getSession().getAttribute("sessionUser"));
		// order表中的合计字段
		BigDecimal total = new BigDecimal(0 + "");
		for (Cartitem cat : clist) {
			total = total.add(new BigDecimal(cat.getSubtotal() + ""));
		}
		order.setTotal(total.doubleValue());
		// 创建订单详细条目对象
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for (Cartitem cat : clist) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(StrKit.uuid());
			orderItem.setQuantity(cat.getQuantity());
			orderItem.setSubtotal(cat.getSubtotal());
			orderItem.setBook(cat.getBook());
			// 注意这里的写法，这个是双向的一对多
			orderItem.setOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItemList(orderItemList);
		// 调用业务层保存订单对象
		obiz.saveOrder(order);
		// 调用业务层删除购物车条目
		cbiz.removeCartItemsByCids(cids.split(","));
		// 将订单对象存放到request中
		request.setAttribute("order", order);
		return "success";
	}
	
	public String listOrdersByUser(){
		HttpServletRequest request=ServletActionContext.getRequest();
		User user = (User)request.getSession().getAttribute("sessionUser");
		PageBean<Order> pb = new PageBean<Order>();
		pb.setPageNumber(this.getPageNumber(request));
		pb.setPageSize(PropKit.use("pagesize.properties").getInt("book_page_size"));
		pb.setUrl(this.getUrl(request, null));
		pb.setList(obiz.listOrdersByUser(user,pb));
		pb.setTotalRecords(obiz.countOrdersByUser(user));
		request.setAttribute("pb", pb);
		return "success";
	}
	
	public String getOrderByOid() {
		HttpServletRequest request=ServletActionContext.getRequest();
		Order order = BeanKit.toBean(request.getParameterMap(), Order.class);
		order = obiz.getOrderByOid(order);
		request.setAttribute("order", order);
		request.setAttribute("action", request.getParameter("action"));
		return "success";
	}
	
	public String cancelOrder(){
		HttpServletRequest request=ServletActionContext.getRequest();
		String oid = request.getParameter("oid");
		int status = obiz.getStatusByOid(oid);
		if(status != 1) {
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能取消订单！");
			return "success";
		}
		
		obiz.updateStatusByOid(oid, 5);//设置状态为取消！
		request.setAttribute("code", "success");
		request.setAttribute("msg", "您的订单已取消，欢迎再来！");
		return "success";		
	}
	
	public String confirmOrder() {
		HttpServletRequest request=ServletActionContext.getRequest();
		String oid = request.getParameter("oid");
		//校验订单状态
		int status = obiz.getStatusByOid(oid);
		if(status != 3) {
			request.setAttribute("code", "error");
			request.setAttribute("msg", "状态不对，不能确认收货！");
			return "success";
		}
		obiz.updateStatusByOid(oid, 4);//设置状态为交易成功！
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜，交易成功！");
		return "success";		
	}
}
