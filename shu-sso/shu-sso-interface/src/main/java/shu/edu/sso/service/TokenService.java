package shu.edu.sso.service;

import shu.edu.common.utils.EDUResult;

public interface TokenService {

	EDUResult getUserByToken(String token);
}
