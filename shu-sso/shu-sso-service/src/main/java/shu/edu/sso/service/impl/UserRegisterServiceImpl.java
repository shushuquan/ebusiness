package shu.edu.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import shu.edu.common.utils.EDUResult;
import shu.edu.mapper.TbUserMapper;
import shu.edu.pojo.TbUser;
import shu.edu.pojo.TbUserExample;
import shu.edu.pojo.TbUserExample.Criteria;
import shu.edu.sso.service.UserRegisterService;
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public EDUResult checkData(String param, int type) {
		// 1、从tb_user表中查询数据
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 2、查询条件根据参数动态生成。
		//1、2、3分别代表username、phone、email
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {
			criteria.andEmailEqualTo(param);
		} else {
			return EDUResult.build(400, "非法的参数");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		// 3、判断查询结果，如果查询到数据返回false。
		if (list == null || list.size() == 0) {
			// 4、如果没有返回true。
			return EDUResult.ok(true);
		} 
		// 5、使用EDUResult包装，有数据存在并返回false。
		return EDUResult.ok(false);
	}

	@Override
	public EDUResult register(TbUser user) {
		
		// 1、使用TbUser接收提交的请求。

		if (StringUtils.isEmpty(user.getUsername())) {
			return EDUResult.build(400, "用户名不能为空");
		}
		if (StringUtils.isEmpty(user.getPassword())) {
			return EDUResult.build(400, "密码不能为空");
		}
		//校验数据是否可用
		EDUResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return EDUResult.build(400, "此用户名已经被使用");
		}
		//校验电话是否可以
		if (StringUtils.isEmpty(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return EDUResult.build(400, "此手机号已经被使用");
			}
		}
/*		//校验email是否可用
		if (StringUtils.isEmpty(user.getEmail())) {
			result = checkData(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return EDUResult.build(400, "此邮件地址已经被使用");
			}
		}*/
		// 2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 3、密码要进行MD5加密。
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		// 4、把用户信息插入到数据库中。
		userMapper.insert(user);
		// 5、返回e3Result。
		return EDUResult.ok();

	}


}
