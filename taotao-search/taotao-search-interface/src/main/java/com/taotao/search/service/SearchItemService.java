package com.taotao.search.service;

import com.taotao.easyui.TaotaoResult;

/**
 * @Title: SearchItem.java
 * @Package com.taotao.search.service
 * @Description: 全文检索商品数据导入到索引库
 */
public interface SearchItemService {
	/**
	 * 商品数据导入索引库 @throws
	 */
	TaotaoResult importAllItemIndex() throws Exception;
}
