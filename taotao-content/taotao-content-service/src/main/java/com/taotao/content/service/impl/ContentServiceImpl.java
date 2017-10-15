package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.easyui.EasyUIDataGridResult;
import com.taotao.easyui.EasyUITreeNode;
import com.taotao.easyui.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.pojo.TbContentExample;
import com.taotao.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;

	@Autowired
	private TbContentMapper tbContentMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;

	@Override
	public List<EasyUITreeNode> getContenCategoryList(long id) {
		// 根据节点ID查询节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);

		// 执行查询对象

		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);

		// 创建List<EasyUITreeNode>集合
		List<EasyUITreeNode> easyList = new ArrayList<EasyUITreeNode>();

		for (TbContentCategory contentCategory : list) {
			// 创建EasyUITreeNode对象
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();

			easyUITreeNode.setId(contentCategory.getId());

			easyUITreeNode.setText(contentCategory.getName());

			easyUITreeNode.setState(contentCategory.getIsParent() ? "closed" : "open");

			easyList.add(easyUITreeNode);

		}

		return easyList;
	}

	/**
	 * 添加网站内容管理节点
	 */
	@Override
	public TaotaoResult saveContentCatetoryNode(long parentId, String name) {
		// TbContentCategory对象
		TbContentCategory category = new TbContentCategory();
		Date date = new Date();

		category.setIsParent(false);
		category.setParentId(parentId);
		category.setName(name);
		category.setCreated(date);
		category.setUpdated(date);
		// 排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		category.setSortOrder(1);
		// 状态。可选值:1(正常),2(删除)
		category.setStatus(1);

		// 添加节点
		tbContentCategoryMapper.insert(category);

		// 获取父节点是否为true
		TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if (!tbContentCategory.getIsParent()) {

			tbContentCategory.setIsParent(true);
			// 更新父节点
			tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		}

		// 该类目是否为父类目，1为true，0为false

		return TaotaoResult.ok(tbContentCategory);
	}

	/**
	 * 网站内容管理节点修改 @throws
	 */
	@Override
	public TaotaoResult updateContentCatetoryNode(long id, String name) {
		// 根据ID查询该节点对象
		TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);

		tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);

		return TaotaoResult.ok(tbContentCategory);
	}

	/**
	 * 网站内容管理节点删除
	 */
	@Override
	public TaotaoResult deleteContentCatetoryNode(long id) {

		// 跟该id查询节点对象
		TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);

		// 判断该节点是否为父节点
		if (contentCategory.getIsParent()) {

			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);

			// 通过父节点查询所有子节点集
			List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);

			// 删除所有的子节点
			for (TbContentCategory tbContentCategory : list) {
				if (tbContentCategory.getIsParent()) {
					// 如果是父节点，就调用递归
					deleteContentCatetoryNode(tbContentCategory.getId());
				}
				// 执行删除
				tbContentCategoryMapper.deleteByPrimaryKey(tbContentCategory.getId());
			}
		}
		// 如果是叶子节点,判断该叶子节点父节点是否还有同级几点

		// 获取该叶子节点的父节点对象
		TbContentCategory contentCategory2 = tbContentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());

		if (contentCategory2.getIsParent()) {

			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(contentCategory2.getId());

			// 通过父节点id查询所有子节点
			List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);

			// 如果子节点为1时，就将该节点修改为叶子节点
			if (list.size() <= 1) {
				// 修改父节点
				contentCategory2.setIsParent(false);
				tbContentCategoryMapper.updateByPrimaryKey(contentCategory2);
			}
		}
		// 删除自己
		tbContentCategoryMapper.deleteByPrimaryKey(id);

		return TaotaoResult.ok();

	}

	/**
	 * 商城首页轮播图显示
	 */

	@Override
	public List<TbContent> getContentList(long cid) {

		try {
			// 查询Redis缓存
			String json = jedisClient.hget(CONTENT_KEY, String.valueOf(cid));
			// 判断缓存中是否存在在查询数据
			if (StringUtils.isNoneBlank(json)) {
				// json转换成list
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);

				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		TbContentExample example = new TbContentExample();
		com.taotao.pojo.TbContentExample.Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);

		List<TbContent> list = tbContentMapper.selectByExample(example);

		// 存入Redis缓存
		try {

			jedisClient.hset(CONTENT_KEY, String.valueOf(cid), JsonUtils.objectToJson(list));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 商城后台管理内容添加 @throws
	 */
	@Override
	public TaotaoResult addContent(TbContent content) {

		// 补全属性
		content.setCreated(new Date());
		content.setUpdated(new Date());

		// 插入数据
		tbContentMapper.insert(content);
		jedisClient.hdel(CONTENT_KEY, content.getCategoryId().toString());

		return TaotaoResult.ok();
	}

	/**
	 * 商城后台内容查询
	 */
	@Override
	public EasyUIDataGridResult getItemList(long categoryId, Integer page, Integer rows) {
		// 设置分页
		PageHelper.startPage(page, rows);

		// 设置查询条件
		TbContentExample example = new TbContentExample();

		com.taotao.pojo.TbContentExample.Criteria criteria = example.createCriteria();

		criteria.andCategoryIdEqualTo(categoryId);

		List<TbContent> list = tbContentMapper.selectByExample(example);

		// 创建EasyUIDataGridResult对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();

		PageInfo<TbContent> pageInfo = new PageInfo<>(list);

		result.setRows(list);

		result.setTotal(pageInfo.getTotal());

		return result;

	}
}