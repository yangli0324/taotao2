package com.taotao.content.service;

import java.util.List;

import com.taotao.easyui.EasyUIDataGridResult;
import com.taotao.easyui.EasyUITreeNode;
import com.taotao.easyui.TaotaoResult;
import com.taotao.pojo.TbContent;

/**
 * @Title: ContentService.java
 * @Package com.taotao.content.service
 * @Description:后台商品类容分页
 */
public interface ContentService {

	/**
	 * 查询网站内容管理 @throws
	 */
	List<EasyUITreeNode> getContenCategoryList(long id);

	/**
	 * 添加网站内容管理节点
	 */
	TaotaoResult saveContentCatetoryNode(long parentId, String name);

	/**
	 * 网站内容管理节点修改 @throws
	 */
	TaotaoResult updateContentCatetoryNode(long id, String name);

	/**
	 * 网站后台管理节点的删除
	 */
	TaotaoResult deleteContentCatetoryNode(long id);

	/**
	 * 商城首页轮播图显示
	 */
	List<TbContent> getContentList(long cid);

	/**
	 * 商城后台管理内容添加 @throws
	 */
	TaotaoResult addContent(TbContent content);

	/**
	 * 商城后台管理内容查询
	 */
	EasyUIDataGridResult getItemList(long categoryId, Integer page, Integer rows);
}
