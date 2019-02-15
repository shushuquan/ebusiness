package shu.edu.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import shu.edu.common.utils.CookieUtils;
import shu.edu.common.utils.EDUResult;
import shu.edu.sso.service.LoginService;

@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showLogin() {
		return "login";
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public EDUResult login(String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		// 1、接收两个参数。
		// 2、调用Service进行登录。
		EDUResult result = loginService.userLogin(username, password);
		// 3、从返回结果中取token，写入cookie。Cookie要跨域。
		if(result.getStatus()==200) {
			
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		// 4、响应数据。Json数据。eduResult，其中包含Token。
		return result;
		
	}

}
