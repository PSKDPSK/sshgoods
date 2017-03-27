package com.biz;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dao.CartDao;
import com.entity.Cartitem;
@Service
public class CartBiz {
	@Resource
	private CartDao idao;
	
	public List<Cartitem> myCart(String uid) {
		return idao.findByUser(uid);
	}
	@Transactional(rollbackFor=Exception.class)
	public void add(Cartitem cartItem) {
		Cartitem cat = idao.findByUserAndBook(cartItem.getOwner().getUid(), cartItem.getBook().getBid());
		if(cat == null) {
			idao.add(cartItem);
		} else {
			int quantity = cartItem.getQuantity() + cat.getQuantity();
			idao.updateQuantity(cat.getCartItemId(), quantity);
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public int removeCartItemByCid(Cartitem cart) {
		return idao.removeCartItemByCid(cart);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void removeCartItemsByCids(String[] cids) {
		for (String cid : cids) {
			Cartitem cart  = new Cartitem();
			cart.setCartItemId(cid);
			idao.removeCartItemByCid(cart);
		}
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void updateQuantity(String cartItemId, int quantity) {
		idao.updateQuantity(cartItemId,quantity);
	}
	
	
	public Cartitem load(String cartItemId) {
		return idao.load(cartItemId);
	}
	
	public List<Cartitem> listCartItemsByCids(String cartItemIds) {
		List<Cartitem> clist = null;
		try {
			clist = idao.listCartItemsByCids(cartItemIds);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return clist;
	}
}
