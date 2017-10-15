package com.taotao.sso.service;

import com.taotao.easyui.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserRegisterService {

	/**
	 * 数据校验
	 */
	TaotaoResult checkUserInfo(String param, Integer type);

	TaotaoResult UserRegister(TbUser user);
}
