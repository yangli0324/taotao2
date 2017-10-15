package com.taotao.sso.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.easyui.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.jedis.JedisClient;
import com.taotao.utils.JsonUtils;

/**
 * @Title: UserLoginServicepl.java
 * @Package com.taotao.sso.service
 * @Description: 用户登录、service实现类模块
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${USER_SESSION}")
	private String USER_SESSION;

	@Value("${SESSION_EXPIE}")
	private Integer SESSION_EXPIE;

	@Override
	public TaotaoResult login(String username, String password) {
		// 根据用户名查询用户信息
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		// 执行查询条件
		List<TbUser> list = userMapper.selectByExample(example);

		// 判断是否有查询结果
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名和密码错误!");
		}

		// 校验登录密码是否正确
		TbUser user = list.get(0);

		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {

			return TaotaoResult.build(400, "用户名和密码错误!");
		}
		// 生成token
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		// 保存到Redis中
		jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
		// 设置Redis中过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIE);

		// 返回结果token
		return TaotaoResult.ok(token);
	}

	/**
	 * 登录取出token信息
	 */
	@Override
	public TaotaoResult getUserByRdeisToken(String token) {
		// 取出token信息
		String jsonToken = jedisClient.get(USER_SESSION + ":" + token);
		// 判断token信息是否存在
		if (StringUtils.isBlank(jsonToken)) {
			TaotaoResult.build(400, "该用户信息已过期!");
		}

		// 重置token的过期时间
		jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIE);
		// 转换对象
		TbUser user = JsonUtils.jsonToPojo(jsonToken, TbUser.class);
		return TaotaoResult.ok(user);
	}

}
