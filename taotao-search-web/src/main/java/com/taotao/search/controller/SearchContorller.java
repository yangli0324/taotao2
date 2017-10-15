package com.taotao.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.search.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 
 * @Title: SearchContorller.java
 * @Package com.taotao.search.controller
 * @Description: 商品前台条件分页查询
 */
@Controller
public class SearchContorller {

	@Value("${ITEM_ROWS}")
	private Integer ITEM_ROWS;

	@Autowired
	private SearchService searchService;

	@RequestMapping("/search")
	public String search(@RequestParam("q") String queryString, @RequestParam(defaultValue = "1") Integer page,
			Model model) throws Exception {

		// 乱码处理
		queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
		// 调用服务查询
		SearchResult searchResult = searchService.search(queryString, page, ITEM_ROWS);

		// 设置页面参数传递
		model.addAttribute("query", queryString);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("itemList", searchResult.getItemList());

		return "search";
	}

}
