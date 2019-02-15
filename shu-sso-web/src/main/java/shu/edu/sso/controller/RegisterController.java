package shu.edu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import shu.edu.common.utils.EDUResult;
import shu.edu.pojo.TbUser;
import shu.edu.sso.service.UserRegisterService;

@Controller
public class RegisterController {

	@Autowired
	private UserRegisterService userRegisterService;
	
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public EDUResult checkData(@PathVariable String param, @PathVariable Integer type) {
		EDUResult eduResult = userRegisterService.checkData(param, type);
		return eduResult;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public EDUResult register(TbUser user) {
		EDUResult result = userRegisterService.register(user);
		return result;
	}


}
