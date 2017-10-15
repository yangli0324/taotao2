package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.easyui.TaotaoResult;
import com.taotao.search.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * @Title: SearchItemImpl.java
 * @Package com.taotao.search.service.impl
 * @Description: 全文检索商品数据导入到索引库
 */
@Service
public class SearchItemServiceImpl implements com.taotao.search.service.SearchItemService {

	@Autowired
	private SearchItemMapper SearchItemMapper;

	@Autowired
	private SolrServer solrServer;

	/**
	 * 商品数据导入到索引库
	 */
	@Override
	public TaotaoResult importAllItemIndex() throws Exception {

		// 查询所有商品信息
		List<SearchItem> list = SearchItemMapper.getItemList();

		for (SearchItem searchItem : list) {
			// 创建文本对象
			SolrInputDocument solrInputDocument = new SolrInputDocument();

			// 添加文本信息
			solrInputDocument.addField("id", searchItem.getId());
			solrInputDocument.addField("item_title", searchItem.getTitle());
			solrInputDocument.addField("item_sell_point", searchItem.getSell_point());
			solrInputDocument.addField("item_price", searchItem.getPrice());
			solrInputDocument.addField("item_image", searchItem.getImage());
			solrInputDocument.addField("item_category_name", searchItem.getCategory_name());
			solrInputDocument.addField("item_desc", searchItem.getItem_desc());

			// 添加到索引库
			solrServer.add(solrInputDocument);

		}
		// 提交
		solrServer.commit();
		return TaotaoResult.ok();
	}

}
