package com.c3stones.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.c3stones.entity.User;
import com.c3stones.mapper.UserMapper;
import com.c3stones.service.UserService;

/**
 * 用户Service实现类
 * 
 * @author CL
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	@Override
	public List<User> findList() {
		return new User().selectAll();
	}

	/**
	 * 保存用户信息
	 * 
	 * @param user
	 * @return
	 */
	@Override
	public boolean save(User user) {
		return super.save(user);
	}


	public static void main(String[] args) {
		int a = 200;
		int e = 200;
		Integer b = 200;
		Integer f = 200;
		Integer c = new Integer(200);

		System.out.println(a == b);
		System.out.println(a == c);
		System.out.println(c == b);
		System.out.println(b == f);
		System.out.println("----------------");
		System.out.println(a);
		System.out.println(e == b);
		System.out.println(e == c);
		System.out.println(e == a);
	}

}
