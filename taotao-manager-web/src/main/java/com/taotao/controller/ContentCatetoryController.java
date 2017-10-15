package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.content.service.ContentService;
import com.taotao.easyui.EasyUITreeNode;
import com.taotao.easyui.TaotaoResult;

/**
 * @Title: ContentCatetoryController.java
 * @Package com.taotao.controller
 * @Description: 内容节点查询
 */
@Controller
public class ContentCatetoryController {

	@Autowired
	private ContentService contentService;

	/**
	 * 网站内容管理节点查询 @throws
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatetoryList(@RequestParam(value = "id", defaultValue = "0") long id) {

		List<EasyUITreeNode> list = contentService.getContenCategoryList(id);

		return list;
	}

	/**
	 * 网站内容管理节点添加
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult saveContentGatetory(long parentId, String name) {

		TaotaoResult result = contentService.saveContentCatetoryNode(parentId, name);

		return result;
	}

	/**
	 * 网站内容管理节点修改
	 */

	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCatetoryNode(long id, String name) {
		TaotaoResult result = contentService.updateContentCatetoryNode(id, name);
		return result;
	}

	/**
	 * 网站内容管理节点删除
	 */
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public TaotaoResult deleteContentCatetoryNode(long id) {
		TaotaoResult result = contentService.deleteContentCatetoryNode(id);
		return result;
	}
}
