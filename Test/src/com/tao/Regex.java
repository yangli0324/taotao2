package com.tao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: Regex.java
 * @Package com.tao
 * @Description: 正则测试
 */
public class Regex {
	public static void main(String[] args) {
		String regex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("153785939531");
		System.out.println(m.matches());
		System.out.println("----------------------------");

		String email = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		p = Pattern.compile(email);
		m = p.matcher("15378593953@163.com");
		System.out.println(m.matches());
	}
}
