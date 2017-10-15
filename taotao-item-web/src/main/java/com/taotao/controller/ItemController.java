package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.manager.service.ItemService;
import com.taotao.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * @Title: Item.java
 * @Package com.taotao.controller
 * @Description: 商品详情显示
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {

		// 根据id查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);

		// 根据id查询商品详情
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);

		// 把Tbitem准换成item
		Item item = new Item(tbItem);

		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
	}

}
