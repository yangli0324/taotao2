package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.search.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * @Title: ItemAddListener.java
 * @Package com.taotao.search.listener
 * @Description: 添加商品（item）qctivemq的监听器、同步到solr索引库
 */
public class ItemAddListener implements MessageListener {

	@Autowired
	private SearchItemMapper searchItemMapper;

	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		try {
			// 获取消息信息
			TextMessage textMessage = (TextMessage) message;
			String strItemId = textMessage.getText();
			// 转成long类型
			Long itemId = new Long(strItemId);

			// 查询商品
			SearchItem searchItem = searchItemMapper.getItemById(itemId);

			// 创建所有对象
			SolrInputDocument solrInputDocument = new SolrInputDocument();

			// 添加文本信息
			solrInputDocument.addField("id", searchItem.getId());
			solrInputDocument.addField("item_title", searchItem.getTitle());
			solrInputDocument.addField("item_sell_point", searchItem.getSell_point());
			solrInputDocument.addField("item_price", searchItem.getPrice());
			solrInputDocument.addField("item_image", searchItem.getImage());
			solrInputDocument.addField("item_category_name", searchItem.getCategory_name());
			solrInputDocument.addField("item_desc", searchItem.getItem_desc());

			solrServer.add(solrInputDocument);
			// 提交
			solrServer.commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
