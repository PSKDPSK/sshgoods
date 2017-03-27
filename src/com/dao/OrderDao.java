package com.dao;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.commons.PageBean;
import com.entity.Book;
import com.entity.Order;
import com.entity.OrderItem;
import com.entity.User;
@Repository
public class OrderDao {
	@Resource
	private HibernateTemplate ht;
	public void saveOrder(Order order) {
		try {
			ht.save(order);
			for (int j = 0; j < order.getOrderItemList().size(); j++) {
				OrderItem item = order.getOrderItemList().get(j);
				ht.save(item);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public List<Order> listOrdersByUser(final User user, PageBean<Order> pb) {
		List<Order> olist = null;
		try {
			final int pageNumber = pb.getPageNumber();
			final int pageSize = pb.getPageSize();
			final String hql="from Order where uid=? order by ordertime desc ";
			olist= ht.executeFind(new HibernateCallback<List<Book>>() {
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql);
					query.setString(0, user.getUid());
					query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return olist;
	}
	
	@SuppressWarnings("unchecked")
	public int countOrdersByUser(User user) {
		int i = 0;
		try {
			List<Number> list=ht.find("select count(*) from Order where uid=?",user.getUid());
			Number num=list.get(0);
			i = num.intValue();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	@SuppressWarnings("unchecked")
	public Order getOrderByOid(Order order) {
		try {
			List<Order> list=ht.find("from Order where oid=?",order.getOid());
			if(list.size()>0){
				order=list.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return order;
	}
	
	@SuppressWarnings("unchecked")
	public int getStatusByOid(String oid) {
		int i = 0;
		try {
			List<Number> list=ht.find("select status from Order where oid=?",oid);
			if(list.size()>0){
				Number num=list.get(0);
				i = num.intValue();
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	
	public int updateStatusByOid(String oid, int i) {
		int j = 0;
		try {
			j=ht.bulkUpdate("update Order set status=? where oid=?",i,oid);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return j;
	}
}
