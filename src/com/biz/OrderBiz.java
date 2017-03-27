package com.biz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commons.PageBean;
import com.dao.OrderDao;
import com.dao.OrderItemDao;
import com.entity.Order;
import com.entity.OrderItem;
import com.entity.User;
@Service
public class OrderBiz {
	@Resource
	private OrderDao odao ;
	@Resource
	private OrderItemDao oidao;
	public void saveOrder(Order order) {
		odao.saveOrder(order);
	}
	public List<Order> listOrdersByUser(User user, PageBean<Order> pb) {
		List<Order> olist = new ArrayList<Order>();
		try {
			List<Order> list = odao.listOrdersByUser(user,pb);
			if(list != null && !list.isEmpty()){
				for (Order order : list) {
					List<OrderItem> oilist = oidao.listOrderItemsByOrder(order);
					order.setOrderItemList(oilist);
					olist.add(order);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return olist;
	}
	public int countOrdersByUser(User user) {
		int i = 0;
		try {
			i = odao.countOrdersByUser(user);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	
	public Order getOrderByOid(Order order) {
		try {
			order = odao.getOrderByOid(order);
			List<OrderItem> oilist = oidao.listOrderItemsByOrder(order);
			order.setOrderItemList(oilist);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return order;
	}
	
	public int getStatusByOid(String oid) {
		int i = 0;
		try {
			i = odao.getStatusByOid(oid);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public int updateStatusByOid(String oid, int i) {
		int j = 0;
		j = odao.updateStatusByOid(oid,i);
		return j;
	}
}
