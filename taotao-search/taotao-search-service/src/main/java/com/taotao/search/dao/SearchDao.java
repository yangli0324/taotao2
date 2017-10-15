package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.search.SearchItem;
import com.taotao.search.SearchResult;

@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception {

		// 根据条件查询
		QueryResponse response = solrServer.query(query);

		// 获取商品列表集
		SolrDocumentList documentList = response.getResults();

		// 创建商品列表集
		List<SearchItem> itemList = new ArrayList<>();

		for (SolrDocument solrDocument : documentList) {

			// 创建商品对象
			SearchItem searchItem = new SearchItem();

			searchItem.setId((String) solrDocument.get("id"));

			// 取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");

			String itemTitle = "";
			if (list != null && list.size() > 0) {

				itemTitle = list.get(0);

			} else {

				itemTitle = (String) solrDocument.get("item_title");
			}

			searchItem.setTitle(itemTitle);

			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));

			searchItem.setPrice((long) solrDocument.get("item_price"));

			searchItem.setImage((String) solrDocument.get("item_image"));

			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));

			// 存入列表集
			itemList.add(searchItem);
		}
		// 存入SearachResul
		SearchResult result = new SearchResult();
		// 商品列表
		result.setItemList(itemList);
		// 总记录数
		result.setPageCount(documentList.getNumFound());

		return result;
	}

}
