package com.taotao.controller;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.easyui.EasyUIDataGridResult;
import com.taotao.easyui.TaotaoResult;
import com.taotao.manager.service.ItemService;
import com.taotao.pojo.TbItem;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Resource(name = "topicDestination")
	private Topic topicDestination;

	// 设置商品查询分页
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {

		EasyUIDataGridResult easyUIDataGridResult = itemService.getItemList(page, rows);

		return easyUIDataGridResult;
	}

	// 商品添加
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult saveItem(TbItem item, String desc) {

		TaotaoResult result = itemService.saveItem(item, desc);
		final Long itemId = (Long) result.getData();

		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// 发送消息的内容
				TextMessage textMessage = session.createTextMessage(itemId + "");
				System.out.println("发送mq");
				return textMessage;
			}
		});

		return result;

	}
}
