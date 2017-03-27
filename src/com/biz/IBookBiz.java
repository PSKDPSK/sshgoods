package com.biz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.commons.PageBean;
import com.dao.IBookDao;
import com.entity.Book;
import com.entity.Category;


@Service
public class IBookBiz {
	@Resource
	private IBookDao ddao;

	public List<Book> listBooksByCategory(Category cat, PageBean<Book> pb) {
		return ddao.listBooksByCategory(cat,pb);
	}

	public int countBooksByCategory(Category cat) {
		return ddao.countBooksByCategory(cat);
	}

	public Book findAll(String bid) {
		return ddao.findAll(bid);
	}

	public PageBean<Book> showAll(int pageNumber, int pageSize) {
		return ddao.showAll(pageNumber, pageSize);
	}

	public PageBean<Book> doFuzzyQuery(String bname, int pageNumber, int pageSize) {
		return ddao.doFuzzyQuery(bname,pageNumber, pageSize);
	}

	public List<Book> listBooksByAuthor(Book book, PageBean<Book> pb) {
		return ddao.listBooksByAuthor(book,pb);
	}

	public int countBooksByAuthor(Book book) {
		return ddao.countBooksByAuthor(book);
	}

	public List<Book> listBooksByPress(Book book, PageBean<Book> pb) {
		return ddao.listBooksByPress(book,pb);
	}
	
	public int countBooksByPress(Book book) {
		return ddao.countBooksByPress(book);
	}

	public List<Book> listBooksByConditionsFuzzy(Book book, PageBean<Book> pb) {
		return ddao.listBooksByConditionsFuzzy(book,pb);
	}

	public int listBooksByConditionsFuzzy(Book book) {
		return ddao.listBooksByConditionsFuzzy(book);
	}
}
