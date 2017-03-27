package com.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="t_cartitem")
public class Cartitem {
	@Id
	private String cartItemId;
	private int quantity;
	@Transient
	private String bid;
	@Transient
	private String uid;
	private int orderBy;
	@ManyToOne
	@JoinColumn(name="bid")
	private Book book;
	@ManyToOne
	@JoinColumn(name="uid")
	private User owner;
	
	public double getSubtotal() {
		BigDecimal v1 = new BigDecimal(Double.toString(book.getCurrPrice()));
		BigDecimal v2 = new BigDecimal(Double.toString(quantity));
		return v1.multiply(v2).doubleValue();
	}
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
}
