package shu.edu.sso.service;

import shu.edu.common.utils.EDUResult;
import shu.edu.pojo.TbUser;

public interface UserRegisterService {

	EDUResult checkData(String param, int type);
	EDUResult register(TbUser user);
}
