package shu.edu.sso.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import shu.edu.common.jedis.JedisClient;
import shu.edu.common.utils.EDUResult;
import shu.edu.common.utils.JsonUtils;
import shu.edu.pojo.TbUser;
import shu.edu.sso.service.TokenService;
@Service
public class TokenServiceImpl implements TokenService {


	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public EDUResult getUserByToken(String token) {
		// 2根据token查询redis。
		String json = jedisClient.get("USER_SESSION_INFO:" + token);
		if (StringUtils.isEmpty(json)) {
		// 3、如果查询不到数据。返回用户已经过期。
			return EDUResult.build(400, "用户登录已经过期，请重新登录。");
		}
		// 4、如果查询到数据，说明用户已经登录。
		// 5、需要重置key的过期时间。
		jedisClient.expire("USER_SESSION_INFO:" + token, SESSION_EXPIRE);
		// 6、把json数据转换成TbUser对象，然后使用e3Result包装并返回。
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return EDUResult.ok(user);

	}

}
