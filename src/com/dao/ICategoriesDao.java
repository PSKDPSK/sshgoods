package com.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.entity.Category;
@Repository
public class ICategoriesDao {
	@Autowired
	private  HibernateTemplate ht;
	@SuppressWarnings("unchecked")
	public List<Category> listAllCategorier() {
		List<Category> category=null;
		try {
			category = ht.find("from Category");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return category;
	}
}
