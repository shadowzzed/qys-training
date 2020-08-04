package com.qys.training.biz.auth.mapper;

import com.qys.training.biz.auth.entity.User;
import org.springframework.stereotype.Repository;

public interface UserMapper{

	int insert(User user);

	String queryUser(String username);

	User getOneUser(String username);
}
