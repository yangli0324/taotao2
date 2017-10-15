package com.taotao.manager.service;

import com.taotao.easyui.EasyUIDataGridResult;
import com.taotao.easyui.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

/**
 * @Title: ItemService.java
 * @Package com.taotao.service
 * @Description: TODO(用一句话描述该文件做什么)
 */
public interface ItemService {

	// 后台查询商品切实现分页
	EasyUIDataGridResult getItemList(Integer page, Integer rows);

	// 上传图片
	TaotaoResult saveItem(TbItem item, String tbItemDesc);

	// 根商品id查询商品信息
	TbItem getItemById(long itemId);

	// 根据商品id查询商品描述
	TbItemDesc getItemDescById(long itemId);
}
