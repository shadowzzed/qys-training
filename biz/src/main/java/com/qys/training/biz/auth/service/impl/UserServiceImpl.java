package com.qys.training.biz.auth.service.impl;

import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qys.training.biz.auth.entity.User;
import com.qys.training.biz.auth.mapper.UserMapper;
import com.qys.training.biz.auth.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	public Long create(User user) {
		userMapper.insert(user);
		return user.getId();
	}

	public void testQysException() {
		throw new QysException(BizCodeEnum.ERROR.getCode(),BizCodeEnum.ERROR.getDescription());
	}

	public void testOtherException() {
		int error = 10/0;
	}
}
