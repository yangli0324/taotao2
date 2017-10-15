package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.easyui.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.service.UserRegisterService;
import com.taotao.utils.CookieUtils;

/**
 * @Title: UserRegisterController.java
 * @Package com.taotao.sso.controller
 * @Description: 用户登录注册
 */
@Controller
public class UserRegisterController {

	@Autowired
	private UserRegisterService registerService;

	@Autowired
	private UserLoginService userLoginService;

	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;

	/**
	 * 数据校验 @throws
	 */
	@RequestMapping(value = "/user/check/{param}/{type}", method = RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkUser(@PathVariable String param, @PathVariable Integer type) {

		TaotaoResult result = registerService.checkUserInfo(param, type);

		return result;

	}

	/**
	 * 用户注册
	 */
	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult registerUser(TbUser user) {

		TaotaoResult result = registerService.UserRegister(user);

		return result;

	}

	/**
	 * 用户登录 @throws
	 */

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult UserLong(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		// 调能用服务
		TaotaoResult result = userLoginService.login(username, password);

		// 判断是否登录成功
		if (result.getStatus() != 400) {
			// 获取token
			String token = result.getData().toString();
			// 设置cookie
			// 跨域、cookie过期时间（cookieUtils设置过期时间）
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);

		}
		return result;
	}

	/**
	 * 获取redis缓存中token的信息 @throws 方式一
	 */
	// @RequestMapping(value = "/user/token/{token}", produces =
	// MediaType.APPLICATION_JSON_UTF8_VALUE)
	// @ResponseBody
	// public String getUserByredisToken(@PathVariable String token, String
	// callback) {
	//
	// TaotaoResult result = userLoginService.getUserByRdeisToken(token);
	//
	// if (StringUtils.isNotBlank(callback)) {
	// // 客服端请求为jsonp请求
	// String jsonpResult = callback + "(" + JsonUtils.objectToJson(result) +
	// ");";
	//
	// return jsonpResult;
	// }
	//
	// return JsonUtils.objectToJson(result);
	// }
	/**
	 * 获取redis缓存中token的信息 @throws 方式二：4.1版本以后
	 */
	@RequestMapping("/user/token/{token}")
	@ResponseBody
	public Object getUserByredisToken(@PathVariable String token, String callback) {

		TaotaoResult result = userLoginService.getUserByRdeisToken(token);

		if (StringUtils.isNotBlank(callback)) {
			// 设置包装的数据
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			// 设置回调方法
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}

		return result;
	}
}
