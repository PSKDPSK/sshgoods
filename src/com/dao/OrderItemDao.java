package com.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.entity.Order;
import com.entity.OrderItem;
@Repository
public class OrderItemDao {
	@Resource
	private HibernateTemplate ht;
	@SuppressWarnings("unchecked")
	public List<OrderItem> listOrderItemsByOrder(Order order) {
		List<OrderItem> oilist = new ArrayList<OrderItem>();
		try {
			oilist=ht.find("from OrderItem where oid=?",order.getOid());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return oilist;
	}

}
