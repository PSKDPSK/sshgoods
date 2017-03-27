package com.action;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.biz.CartBiz;
import com.commons.BeanKit;
import com.commons.StrKit;
import com.entity.Book;
import com.entity.Cartitem;
import com.entity.User;
@Controller
public class CartItemAction {
	@Resource
	private CartBiz ibiz;
	
	public String add() {
		Book book=BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), Book.class);
		User owner = (User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		Cartitem cartItem = BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), Cartitem.class);
		cartItem.setCartItemId(StrKit.uuid());
		cartItem.setOwner(owner);
		cartItem.setBook(book);
		ibiz.add(cartItem);
		return "success";
	}
	
	public String myCart(){
		User user = (User)ServletActionContext.getRequest().getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		List<Cartitem> cartItemList = ibiz.myCart(uid);
		ServletActionContext.getRequest().setAttribute("cartItemList", cartItemList);
		return "success";
	}
	
	public String removeCartItemByCid(){
		Cartitem cart = BeanKit.toBean(ServletActionContext.getRequest().getParameterMap(), Cartitem.class);
		ibiz.removeCartItemByCid(cart);
		return "success";
	}
	
	public String removeCartItemsByCids() {
		String[] cids = ServletActionContext.getRequest().getParameterValues("a[]");
		if(cids != null & cids.length > 0){
			ibiz.removeCartItemsByCids(cids);
		}
		return null;
	}
	
	public String updateQuantity() throws Exception {
		JSONObject jo = new JSONObject();
		String cartItemId =  ServletActionContext.getRequest().getParameter("cartItemId");
		int quantity = Integer.parseInt( ServletActionContext.getRequest().getParameter("quantity"));
		ibiz.updateQuantity(cartItemId, quantity);
		Cartitem cartItem = ibiz.load(cartItemId);
		jo.put("quantity", cartItem.getQuantity());
		jo.put("subtotal", cartItem.getSubtotal());
		ServletActionContext.getResponse().getWriter().print(jo);
		return null;
	}
	
	public String listCartItemsByCids(){
		HttpServletRequest request=ServletActionContext.getRequest();
		String cartItemIds = request.getParameter("cartItemIds");
		double total = Double.parseDouble(request.getParameter("total"));
		List<Cartitem> clist = ibiz.listCartItemsByCids(cartItemIds);
		request.setAttribute("clist", clist);
		request.setAttribute("total", total);
		request.setAttribute("cartItemIds", cartItemIds);
		return "success";
	}
}
