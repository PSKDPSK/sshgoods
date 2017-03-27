package com.biz;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dao.ICategoriesDao;
import com.entity.Category;
@Service
public class ICategories {
	@Autowired
	private ICategoriesDao cdao;
	public List<Category> listAllCategorier() {
		List<Category> category=null;
		try {
			category=cdao.listAllCategorier();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return category;
	}

}
