package com.dao;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import com.entity.User;
@Repository
public class IUserDao {
	@Resource
	private HibernateTemplate ht;
	@SuppressWarnings("unchecked")
	public boolean existUserName(String loginname) {
		boolean bl = false;
		try {
			List<User> users=ht.find("from User where loginname=? ",loginname);
			if(users.size()>0){
				bl = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return bl;
	}
	public boolean existEmail(String email) {
		boolean bl = false;
		try {
			@SuppressWarnings("unchecked")
			List<User> users=ht.find("from User where email=? ",email);
			if(users.size()>0){
				bl = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return bl;
	}
	public int save(User user) {
		int i = 0;
		try {
			ht.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return i;
	}
	public User findByNamePwd(User formUser) {
		User user = null;
		try {
			@SuppressWarnings("unchecked")
			List<User> users=ht.find("from User where loginname=? and loginpass=?  ",formUser.getLoginname(),formUser.getLoginpass());
			if(users.size()>0){
				user=users.get(0);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return user;
	}
	public void findpass(User formUser) {
		try {
			ht.bulkUpdate("update User set loginpass=? where uid=?",formUser.getLoginpass(),formUser.getUid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
