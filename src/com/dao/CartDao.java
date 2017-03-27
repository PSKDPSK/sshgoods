package com.dao;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import com.entity.Cartitem;
@Repository
public class CartDao {
	@Resource
	private HibernateTemplate ht;
	
	@SuppressWarnings("unchecked")
	public List<Cartitem> findByUser(String uid) {
		List<Cartitem> list=null;
		try {
			list=ht.find("from Cartitem  where uid=? ",uid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public Cartitem findByUserAndBook(String uid, String bid) {
		Cartitem cartitem=null;
		try {
			List<Cartitem> list=ht.find("from Cartitem where uid=? and bid=?",uid,bid);
			if(list.size()>0){
				cartitem=list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return cartitem;
	}

	public void add(Cartitem cartItem) {
		try {
			ht.save(cartItem);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public void updateQuantity(String cartItemId, int quantity) {
		try {
			ht.bulkUpdate("update Cartitem set quantity=? where cartItemId=?",quantity, cartItemId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public int removeCartItemByCid(Cartitem cart) {
		int i = 0;
		try {
			i=ht.bulkUpdate("delete from Cartitem where cartItemId=?",cart.getCartItemId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public Cartitem load(String cartItemId) {
		Cartitem cartitem=null;
		try {
			List<Cartitem> list=ht.find("from Cartitem  where cartItemId=? ",cartItemId);
			if(list.size()>0){
				cartitem=list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return cartitem;
	}


	@SuppressWarnings("unchecked")
	public List<Cartitem> loadCartItems(String str) {
		List<Cartitem> list=new ArrayList<Cartitem>();
		try {
			list = ht.find("from Cartitem where bid=? and cartItemId=?",str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Cartitem> listCartItemsByCids(String cartItemIds) {
		List<Cartitem> clist = new ArrayList<Cartitem>();
		try {
			Object[] cids = cartItemIds.split(",");
			String insql = this.insql(cids.length);
			String hql = "from Cartitem where " + insql;
			clist = ht.find(hql,cids);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return clist;
	}

	private String insql(int length) {
		StringBuilder sb = new StringBuilder("cartItemId in (");
		for(int i = 0; i < length; i++) {
			sb.append("?");
			if(i < length - 1) {
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
