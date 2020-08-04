package com.qys.training.biz.auth.service.impl;

import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qys.training.biz.auth.entity.User;
import com.qys.training.biz.auth.mapper.UserMapper;
import com.qys.training.biz.auth.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper userMapper;

	public Long create(User user) {
		this.checkParam(user);
		final String username = userMapper.queryUser(user.getUsername());
		if (!StringUtils.isEmpty(username)) {
			logger.info("user existed,username = {}", username);
			throw new QysException(BizCodeEnum.USER_EXISTED.getCode(), BizCodeEnum.USER_EXISTED.getDescription());
		}
		// 加密
		final String password = DigestUtils.md5Digest(user.getPassword().getBytes()).toString();
		user.setPassword(password);
		userMapper.insert(user);
		return user.getId();
	}

	public User checkUser(User user) {
		this.checkParam(user);
		final User user_db = userMapper.getOneUser(user.getUsername());
		if (!user_db.getPassword().equals(DigestUtils.md5Digest(user.getPassword().getBytes()).toString()))
			throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
		return user;
	}

	private void checkParam(User user) {
		// 账号密码不可为空
		if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword()))
			throw new QysException(BizCodeEnum.LOST_PARAM.getCode(), BizCodeEnum.LOST_PARAM.getDescription());
	}
	public void testQysException() {
		throw new QysException(BizCodeEnum.ERROR.getCode(),BizCodeEnum.ERROR.getDescription());
	}

	public void testOtherException() {
		int error = 10/0;
	}
}
