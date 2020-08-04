package com.qys.training.biz.auth.service;

import com.qys.training.biz.auth.entity.User;

public interface UserService {
	
	/**
	 * 创建用户
	 * 
	 * @param user
	 * @return
	 */
	Long create(User user);

	/**
	 * 测试业务异常
	 */
	void testQysException();

	/**
	 * 测试其他异常
	 */
	void testOtherException();

}
