package shu.edu.sso.service;

import shu.edu.common.utils.EDUResult;

public interface LoginService {

	/*
	 * 1判断用户名和密码是否正确
	 * 2如果不正确返回登录
	 * 3如果正确要生成token
	 * 4把用户信息写入redis token：用户信息，设置过期时间
	 * 5把token返回
	 */
	EDUResult userLogin(String username, String password);
}
