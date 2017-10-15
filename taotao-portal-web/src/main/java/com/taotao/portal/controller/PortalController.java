package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;
import com.taotao.utils.JsonUtils;

/**
 * @Title: PortalController.java
 * @Package com.taotao.portal.controller
 * @Description: 首页轮播图展示
 */
@Controller
public class PortalController {

	@Value("${AD1_CID}")
	private Long AD1_CID;

	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;

	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;

	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;

	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;

	@Autowired
	private ContentService contentService;

	@RequestMapping("/index")
	public String indexLong(Model model) {

		List<TbContent> list = contentService.getContentList(AD1_CID);

		List<Ad1Node> ad1List = new ArrayList<>();
		for (TbContent tbContent : list) {
			Ad1Node node = new Ad1Node();
			node.setAlt(tbContent.getTitle());
			node.setHref(tbContent.getUrl());
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			// 添加到列表
			ad1List.add(node);
		}
		// 把数据传递给页面。
		model.addAttribute("ad1", JsonUtils.objectToJson(ad1List));
		System.out.println(JsonUtils.objectToJson(ad1List));
		return "index";
	}
}
