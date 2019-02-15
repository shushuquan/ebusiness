package shu.edu.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import shu.edu.common.jedis.JedisClient;
import shu.edu.common.utils.EDUResult;
import shu.edu.common.utils.JsonUtils;
import shu.edu.mapper.TbUserMapper;
import shu.edu.pojo.TbUser;
import shu.edu.pojo.TbUserExample;
import shu.edu.pojo.TbUserExample.Criteria;
import shu.edu.sso.service.LoginService;
@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	@Override
	public EDUResult userLogin(String username, String password) {
		
		// 1、判断用户名密码是否正确。
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//查询用户信息
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return EDUResult.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		//校验密码
		if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return EDUResult.build(400, "用户名或密码错误");
		}
		// 2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
		String token = UUID.randomUUID().toString();
		// 3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
		// 4、使用String类型保存Session信息。可以使用“前缀:token”为key
		user.setPassword(null);
		jedisClient.set("USER_SESSION_INFO:" + token, JsonUtils.objectToJson(user));
		// 5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
		jedisClient.expire("USER_SESSION_INFO:"+ token, SESSION_EXPIRE);
		// 6、返回e3Result包装token。
		return EDUResult.ok(token);

	}

}
