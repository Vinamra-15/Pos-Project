package com.increff.pos.service;

import java.util.List;

import com.increff.pos.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;

@Service
@Transactional(rollbackFor = ApiException.class)
public class UserService {

	@Autowired
	private UserDao dao;

	public UserPojo add(UserPojo p) throws ApiException {
		normalize(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		dao.insert(p);
        return p;
    }


	public UserPojo get(String email) throws ApiException {
		StringUtil.toLowerCase(email);
		return dao.select(email);
	}

	public List<UserPojo> getAll() {
		return dao.selectAll();
	}


	public void delete(Integer id) {
		dao.delete(id);
	}

	protected static void normalize(UserPojo p) {
		p.setEmail(p.getEmail().toLowerCase().trim());
	}
}
