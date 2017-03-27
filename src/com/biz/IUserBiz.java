package com.biz;

import java.io.IOException;
import java.text.MessageFormat;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.Session ;
import com.commons.PropKit;
import com.commons.StrKit;
import com.dao.IUserDao;
import com.entity.User;
import com.mail.Mail;
import com.mail.MailKit;
@Service
public class IUserBiz {
	@Resource
	private IUserDao udao ;
	public boolean existUserName(String loginname) {
		boolean bl = false;
		try {
			bl = udao.existUserName(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return bl;
	}

	public boolean existEmail(String email) {
		boolean bl = false;
		try {
			bl = udao.existEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return bl;
	}
	@Transactional(rollbackFor=Exception.class)
	public void regist(User user) throws MessagingException, IOException {
		user.setUid(StrKit.uuid());
		user.setStatus(false);
		user.setVerifyCode(StrKit.uuid()+StrKit.uuid());
		int i = udao.save(user);
		if(i > 0){
			PropKit.use("email_template.properties");
			String host = PropKit.get("host");
			String username = PropKit.get("username");
			String password = PropKit.get("password");
			Session  session=MailKit.createSession(host, username, password);
			
			String from = PropKit.get("from");
			String to = user.getEmail();
			String subject = PropKit.get("subject");
			
			String content = MessageFormat.format(PropKit.get("content"), user.getActivationCode());
			Mail mail = new Mail(from, to, subject, content);
			MailKit.send(session, mail);
		}
	}

	public User login(User formUser) {
		User user = null;
		try {
			user = udao.findByNamePwd(formUser);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return user;
	}

	public void findpass(User formUser) {
		try {
			udao.findpass(formUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
