package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.content.service.ContentService;
import com.taotao.easyui.EasyUIDataGridResult;
import com.taotao.easyui.TaotaoResult;
import com.taotao.pojo.TbContent;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;

	/**
	 * 商城后台管理内容添加 @throws
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent content) {
		TaotaoResult result = contentService.addContent(content);
		return result;
	}

	/**
	 * 内容管理查询 @throws
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(long categoryId, Integer page, Integer rows) {

		EasyUIDataGridResult easyUIDataGridResult = contentService.getItemList(categoryId, page, rows);

		return easyUIDataGridResult;
	}
}
