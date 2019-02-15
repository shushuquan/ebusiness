package shu.edu.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shu.edu.common.utils.EDUResult;
import shu.edu.common.utils.JsonUtils;
import shu.edu.sso.service.TokenService;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	@RequestMapping(value="/user/token/{token}",
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		EDUResult result = tokenService.getUserByToken(token);
		//响应之前判断是否为jsonp请求
		if(!StringUtils.isEmpty(callback)) {
			//把结果封装成一个js语句响应
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}

}
