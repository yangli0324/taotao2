package com.taotao.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.manager.service.ItemService;
import com.taotao.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @Title: HtmlGenListener.java
 * @Package com.taotao.listener
 * @Description:添加商品时生成静态模块
 */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;

	@Autowired
	private FreeMarkerConfigurer freemarkerConfigurer;

	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;

	@Override
	public void onMessage(Message message) {

		try {
			System.out.println("接受mq1");
			// 获取消息信息
			TextMessage textMessage = (TextMessage) message;
			String strItemId = textMessage.getText();

			// 装换id类型
			Long itemId = new Long(strItemId);

			// 获取商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);

			// 获取商品详细信息
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);

			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);

			// 指定文件输出目录
			Configuration configuration = freemarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.htm");
			FileWriter out = new FileWriter(new File(HTML_OUT_PATH + itemId + ".htm"));

			// 生成静态文件
			template.process(data, out);
			System.out.println("接受mq消息，生成模板");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
