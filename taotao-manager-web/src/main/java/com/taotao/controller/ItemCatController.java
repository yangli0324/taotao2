package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.easyui.EasyUITreeNode;
import com.taotao.manager.service.ItemCatService;

/**
 * 商品
 * 
 * @Title: ItemCatController.java
 * @Package com.taotao.controller
 * @Description: TODO(用一句话描述该文件做什么)
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService catService;

	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id", defaultValue = "0") long id) {
		List<EasyUITreeNode> list = catService.getItemList(id);

		return list;
	}

}
