package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Title: RegisterPageContriller.java
 * @Package com.taotao.sso.controller
 * @Description: 用户注册页面register页面
 */

@Controller
public class RegisterPageContriller {

	/**
	 * 注册页面 @throws
	 */
	@RequestMapping("/page/register")
	public String showRegister() {

		return "register";
	}

	/**
	 * 登录页面 @throws
	 */
	@RequestMapping("/page/login")
	public String showLogin() {

		return "login";
	}

}
