package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.easyui.TaotaoResult;
import com.taotao.search.service.SearchItemService;

@Controller
public class IndexManagementContorller {

	@Autowired
	private SearchItemService searchItemService;

	/**
	 * 商品数据导入索引库 @throws
	 */
	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult indexImport() throws Exception {

		TaotaoResult taotaoResult = searchItemService.importAllItemIndex();

		return taotaoResult;
	}
}
