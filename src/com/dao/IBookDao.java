package com.dao;

import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import com.commons.PageBean;
import com.commons.StrKit;
import com.entity.Book;
import com.entity.Category;
@Repository
public class IBookDao {
	@Autowired
	private  HibernateTemplate ht;

	@SuppressWarnings({ "unchecked" })
	public List<Book> listBooksByCategory(final Category cat, PageBean<Book> pb) {
		List<Book> blist = null;
		try {
			final int pageNumber = pb.getPageNumber();
			final int pageSize = pb.getPageSize();
			final String hql = "from Book  where cid=?";
			blist= ht.executeFind(new HibernateCallback<List<Book>>() {
				//这个方法是在hibernateTemplate内部会去调用
				//hibernateTemplate会先获取session, 然后把session传递到你重写的这个方法中
				//最后hibernateTemplate会把你重写的这个方法所返回的返回值传递给外面的方法
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql);
					query.setString(0, cat.getCid());
					query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return blist;
	}

	@SuppressWarnings("unchecked")
	public int countBooksByCategory(Category cat) {
		int i = 0;
		try {
			List<Number> list = ht.find("select count(*) from Book where category.cid=?", cat.getCid());
			Number num = list.get(0);
			i = num.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public Book findAll(String bid) {
		Book book =new Book();
		try {
			List<Book> list=ht.find("from Book where bid=? ",bid);
			book=list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return book;
	}

	@SuppressWarnings("unchecked")
	public PageBean<Book> showAll(final int pageNumber, final int pageSize) {
		PageBean<Book> blist = new PageBean<Book>();
		try {
			blist.setPageNumber(pageNumber);
			blist.setPageSize(pageSize);
			List<Number> list=ht.find("select count(*) from Book");
			Number num = list.get(0);
			blist.setTotalRecords(num.intValue());
			final String hql = "from Book";
			List<Book> books = ht.executeFind(new HibernateCallback<List<Book>>() {
				//这个方法是在hibernateTemplate内部会去调用
				//hibernateTemplate会先获取session, 然后把session传递到你重写的这个方法中
				//最后hibernateTemplate会把你重写的这个方法所返回的返回值传递给外面的方法
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql);
					query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});	
			blist.setList(books);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return blist;
	}

	@SuppressWarnings("unchecked")
	public PageBean<Book> doFuzzyQuery(final String bname, final int pageNumber, final int pageSize) {
		PageBean<Book> pb = new PageBean<Book>();
		try {
			pb.setPageNumber(pageNumber);
			pb.setPageSize(pageSize);
			final String hql="select count(*) from Book where bname like :bname";
			List<Number> list= ht.executeFind(new HibernateCallback<List<Book>>() {
				//这个方法是在hibernateTemplate内部会去调用
				//hibernateTemplate会先获取session, 然后把session传递到你重写的这个方法中
				//最后hibernateTemplate会把你重写的这个方法所返回的返回值传递给外面的方法
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql);
			        query.setString("bname", "%"+bname+"%");
			        query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});	
			Number num = list.get(0);
			pb.setTotalRecords(num.intValue());
			final String hql2="from Book where bname like :bname ";
			List<Book> books = ht.executeFind(new HibernateCallback<List<Book>>() {
				//这个方法是在hibernateTemplate内部会去调用
				//hibernateTemplate会先获取session, 然后把session传递到你重写的这个方法中
				//最后hibernateTemplate会把你重写的这个方法所返回的返回值传递给外面的方法
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql2);
			        query.setString("bname", "%"+bname+"%");
			        query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});	
			pb.setList(books);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return pb;
	}

	@SuppressWarnings({ "unchecked"})
	public List<Book> listBooksByAuthor(final Book book, PageBean<Book> pb) {
		List<Book> blist = null;
		try {
			final int pageNumber = pb.getPageNumber();
			final int pageSize = pb.getPageSize();
			final String hql="from Book where author=? ";
			blist = ht.executeFind(new HibernateCallback<List<Book>>() {
				//这个方法是在hibernateTemplate内部会去调用
				//hibernateTemplate会先获取session, 然后把session传递到你重写的这个方法中
				//最后hibernateTemplate会把你重写的这个方法所返回的返回值传递给外面的方法
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql);
					query.setString(0, book.getAuthor());
			        query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return blist;
	}

	@SuppressWarnings("unchecked")
	public int countBooksByAuthor(Book book) {
		int i = 0;
		try {
			List<Number> list = ht.find("select count(*) from Book where author=?", book.getAuthor());
			Number num = list.get(0);
			i = num.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public List<Book> listBooksByPress(final Book book, PageBean<Book> pb) {
		List<Book> blist = null;
		try {
			final int pageNumber = pb.getPageNumber();
			final int pageSize = pb.getPageSize();
			final String hql="from Book where press=? ";
			blist = ht.executeFind(new HibernateCallback<List<Book>>() {
				//这个方法是在hibernateTemplate内部会去调用
				//hibernateTemplate会先获取session, 然后把session传递到你重写的这个方法中
				//最后hibernateTemplate会把你重写的这个方法所返回的返回值传递给外面的方法
				@Override
				public List<Book> doInHibernate(Session arg0) throws HibernateException,
						SQLException {
					Query query = arg0.createQuery(hql);
					query.setString(0, book.getPress());
			        query.setFirstResult((pageNumber-1) * pageSize);
					query.setMaxResults(pageSize);
					return query.list();
				}
			});	
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return blist;
	}

	public int countBooksByPress(Book book) {
		int i = 0;
		try {
			@SuppressWarnings("unchecked")
			List<Number> list = ht.find("select count(*) from Book where press=?", book.getPress());
			Number num = list.get(0);
			i = num.intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}

	@SuppressWarnings("unchecked")
	public List<Book> listBooksByConditionsFuzzy(final Book book, PageBean<Book> pb) {
		List<Book> blist = null;
		try {
			final int pageNumber = pb.getPageNumber();
			final int pageSize = pb.getPageSize();
			if(StrKit.notBlank(book.getBname())){
				final String hql ="from Book where bname like :bname ";
				blist = ht.executeFind(new HibernateCallback<List<Book>>() {
					@Override
					public List<Book> doInHibernate(Session arg0) throws HibernateException,
							SQLException {
						Query query = arg0.createQuery(hql);
				        query.setString("bname", "%"+book.getBname()+"%");
				        query.setFirstResult((pageNumber-1) * pageSize);
						query.setMaxResults(pageSize);
						return query.list();
					}
				});	
			}
			if(StrKit.notBlank(book.getAuthor())){
				final String hql ="from Book where author like :author ";
				blist = ht.executeFind(new HibernateCallback<List<Book>>() {
					@Override
					public List<Book> doInHibernate(Session arg0) throws HibernateException,
							SQLException {
						Query query = arg0.createQuery(hql);
				        query.setString("author", "%"+book.getAuthor()+"%");
				        query.setFirstResult((pageNumber-1) * pageSize);
						query.setMaxResults(pageSize);
						return query.list();
					}
				});	
			}
			if(StrKit.notBlank(book.getPress())){
				final String hql ="from Book where press like :press ";
				blist= ht.executeFind(new HibernateCallback<List<Book>>() {
					@Override
					public List<Book> doInHibernate(Session arg0) throws HibernateException,
							SQLException {
						Query query = arg0.createQuery(hql);
				        query.setString("press", "%"+book.getPress()+"%");
				        query.setFirstResult((pageNumber-1) * pageSize);
						query.setMaxResults(pageSize);
						return query.list();
					}
				});	
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return blist;
	}

	@SuppressWarnings("unchecked")
	public int listBooksByConditionsFuzzy(final Book book) {
		int i = 0;
		try {
			if(StrKit.notBlank(book.getBname())){
				final String hql = " select count(*) from Book where bname like :bname";
				List<Number> books = ht.executeFind(new HibernateCallback<List<Book>>() {
					@Override
					public List<Book> doInHibernate(Session arg0) throws HibernateException,
							SQLException {
						Query query = arg0.createQuery(hql);
				        query.setString("bname", "%"+book.getBname()+"%");
						return query.list();
					}
				});	
				Number num = books.get(0);
				i = num.intValue();
			}
			if(StrKit.notBlank(book.getAuthor())){
				final String hql = " select count(*) from Book where author like :author";
				List<Number> books = ht.executeFind(new HibernateCallback<List<Book>>() {
					@Override
					public List<Book> doInHibernate(Session arg0) throws HibernateException,
							SQLException {
						Query query = arg0.createQuery(hql);
				        query.setString("author", "%"+book.getAuthor()+"%");
						return query.list();
					}
				});	
				Number num = books.get(0);
				i = num.intValue();
			}
			if(StrKit.notBlank(book.getPress())){
				final String hql = " select count(*) from Book where press like :press";
				List<Number> books = ht.executeFind(new HibernateCallback<List<Book>>() {
					@Override
					public List<Book> doInHibernate(Session arg0) throws HibernateException,
							SQLException {
						Query query = arg0.createQuery(hql);
				        query.setString("press", "%"+book.getPress()+"%");
						return query.list();
					}
				});	
				Number num = books.get(0);
				i = num.intValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
}
