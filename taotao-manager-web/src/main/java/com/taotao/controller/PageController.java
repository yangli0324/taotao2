package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	//访问工程路径跳转到index页面
	@RequestMapping("/")
	public String IndexShow(){
		
		
		return "index";
	}
	
	//根据请求跳转到对应请求的页面
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		
		return page;
	}
}
