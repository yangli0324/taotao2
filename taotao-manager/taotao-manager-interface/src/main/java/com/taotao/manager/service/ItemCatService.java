package com.taotao.manager.service;

import java.util.List;

import com.taotao.easyui.EasyUITreeNode;

public interface ItemCatService {

	// 商品分类添加
	List<EasyUITreeNode> getItemList(long id);
}
