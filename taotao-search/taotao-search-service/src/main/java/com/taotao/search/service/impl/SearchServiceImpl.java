package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;

/**
 * 
 * @Title: SearchServiceImpl.java
 * @Package com.taotao.search
 * @Description: 商品搜索条件查询
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult search(String queryString, Integer page, Integer rows) throws Exception {
		// 创建查询条件对象
		SolrQuery solrquery = new SolrQuery();

		// 设置查询条件
		solrquery.setQuery(queryString);

		// 设置分页条件
		solrquery.setStart((page - 1) * rows);
		solrquery.setRows(rows);

		// 设置默认搜索域
		solrquery.set("df", "item_title");

		// 设置高亮显示
		solrquery.setHighlight(true);
		solrquery.addHighlightField("item_title");
		solrquery.setHighlightSimplePre("<em style=\"color:red\">");
		solrquery.setHighlightSimplePost("</em>");

		// 调用dao服务、执行查询条件
		SearchResult searchResult = searchDao.search(solrquery);

		// 获取总记录数
		long recordCount = searchResult.getRecordCount();

		// 获取查询总页数
		long pageCount = searchResult.getPageCount();

		if (recordCount % rows > 0) {
			pageCount++;
		}
		// 设置总页数
		searchResult.setPageCount(pageCount);

		return searchResult;
	}

}
