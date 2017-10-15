package com.taotao.sso.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.easyui.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;

/**
 * @Title: UserRegisterServiceImpl.java
 * @Package com.taotao.sso.service
 * @Description: 检查user信息
 */
@Service
public class UserRegisterServiceImpl implements UserRegisterService {

	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private TbUserMapper userMapper;

	/**
	 * 数据校验
	 */
	@Override
	public TaotaoResult checkUserInfo(String param, Integer type) {

		TbUserExample example = new TbUserExample();

		Criteria criteria = example.createCriteria();

		// 判断校验数据类型1、2、3分别代表用户名、电话、邮箱
		if (type == 1) {

			criteria.andUsernameEqualTo(param);

		} else if (type == 2) {

			criteria.andPhoneEqualTo(param);

		} else if (type == 3) {

			criteria.andEmailEqualTo(param);

		}

		// 执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);

		// 判断是否有查询结果
		if (list == null || list.size() == 0) {

			return TaotaoResult.ok(true);

		}

		return TaotaoResult.ok(false);
	}

	/**
	 * 用户注册登录
	 */
	@Override
	public TaotaoResult UserRegister(TbUser user) {

		// 校验数据的合法性
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return TaotaoResult.build(400, "用户名密码不能为空!");
		}
		// 校验用户名是否被注册
		TaotaoResult result = checkUserInfo(user.getUsername(), 1);
		Boolean flag = (Boolean) result.getData();
		if (!flag) {
			return TaotaoResult.build(400, "该用户名已被注册！");
		}
		// 校验手机号是否合法性

		// 定义正则表达式
		String regex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(user.getPhone());
		boolean b = m.matches();
		if (!b) {
			return TaotaoResult.build(400, "请输入正确的手机号码!");
		}

		// 校验邮箱是否注册
		// 校验email是否可用
		if (StringUtils.isNotBlank(user.getEmail())) {
			TaotaoResult result1 = checkUserInfo(user.getEmail(), 3);
			if (!(boolean) result1.getData()) {
				return TaotaoResult.build(400, "此邮件地址已经被使用");
			}
		}
		/*
		 * // 校验邮箱的格式是否正确 // 定义邮箱的正则表达式 String email =
		 * "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		 * p = Pattern.compile(email); m = p.matcher(user.getEmail()); boolean c
		 * = m.matches();
		 * 
		 * if (c) { // 校验邮箱是否被注册 TaotaoResult resultEmail =
		 * checkUserInfo(user.getEmail(), 3); Boolean Email = (Boolean)
		 * resultEmail.getData(); if (!Email) { return TaotaoResult.build(400,
		 * "该邮箱已经被注册!"); } } else {
		 * 
		 * return TaotaoResult.build(400, "请输入正确的邮箱!"); }
		 */

		// 补全注册时间
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 密码加密
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5DigestAsHex);

		// 插入数据库
		userMapper.insert(user);
		return TaotaoResult.ok();
	}

}
