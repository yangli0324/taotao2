package com.taotao.manager.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.easyui.EasyUIDataGridResult;
import com.taotao.easyui.TaotaoResult;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.manager.service.ItemService;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.utils.IDUtils;
import com.taotao.utils.JsonUtils;

/**
 * @Title: ItemServiceImpl.java
 * @Package com.taotao.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	/*
	 * @Autowired private JmsTemplate jmsTemplate;
	 * 
	 * @Resource(name = "topicDestination") private Topic topicDestination;
	 */

	// 注入jedisClient
	@Autowired
	private JedisClient jedisClient;

	// 获取Redis.properties中value值
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;

	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {

		// 设置分页
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();

		List<TbItem> list = tbItemMapper.selectByExample(example);

		// 创建EasyUIDataGridResult对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();

		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		result.setRows(list);

		result.setTotal(pageInfo.getTotal());

		return result;
	}

	// 添加商品
	@Override
	public TaotaoResult saveItem(TbItem item, String tbItemDesc) {

		// 设置商品ID
		final long itemId = IDUtils.genItemId();

		item.setId(itemId);
		// 设置商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);

		Date date = new Date();

		item.setCreated(date);
		item.setUpdated(date);

		tbItemMapper.insert(item);

		// 创建TbItemMapper
		TbItemDesc desc = new TbItemDesc();

		desc.setItemId(itemId);
		desc.setCreated(date);
		desc.setUpdated(date);
		desc.setItemDesc(tbItemDesc);

		itemDescMapper.insert(desc);

		// // 添加商品成功发送MQ消息
		// jmsTemplate.send(topicDestination, new MessageCreator() {
		//
		// @Override
		// public Message createMessage(Session session) throws JMSException {
		// // 发送消息的内容
		// TextMessage textMessage = session.createTextMessage(itemId + "");
		// return textMessage;
		// }
		// });

		return TaotaoResult.ok(itemId);
	}

	/**
	 * 根据商品id差商品信息
	 */
	@Override
	public TbItem getItemById(long itemId) {

		// 查询Redis缓存、查询无结果查询数据库
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + "BASE");

			if (StringUtils.isNotBlank(json)) {

				// 转换json数据为对象
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);

				return tbItem;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 查询数据库
		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);

		try {
			// 添加缓存
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + "BASE", JsonUtils.objectToJson(item));
			// 设置缓存时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + "BASE", REDIS_ITEM_EXPIRE);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	/**
	 * 根据商品id查商品详情描述
	 */
	@Override
	public TbItemDesc getItemDescById(long itemId) {

		// 先查询Redis、无查询结果查询数据库
		String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + "DESC");

		if (StringUtils.isNotBlank(json)) {

			// 转换json数据为对象
			TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);

			return itemDesc;
		}

		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

		try {
			// 添加缓存
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + "DESC", JsonUtils.objectToJson(itemDesc));
			// 设置缓存时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + "DESC", REDIS_ITEM_EXPIRE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemDesc;
	}

}
