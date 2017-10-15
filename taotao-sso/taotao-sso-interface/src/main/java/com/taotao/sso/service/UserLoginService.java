package com.taotao.sso.service;

import com.taotao.easyui.TaotaoResult;

/**
 * @Title: UserLoginService.java
 * @Package com.taotao.sso.service
 * @Description: 用户登录模块
 */
public interface UserLoginService {

	TaotaoResult login(String username, String password);

	TaotaoResult getUserByRdeisToken(String token);
}
