package com.taotao.manager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.easyui.EasyUITreeNode;
import com.taotao.manager.service.ItemCatService;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNode> getItemList(long id) {

		// 根据ID查询节点列表
		TbItemCatExample example = new TbItemCatExample();

		Criteria criteria = example.createCriteria();

		criteria.andParentIdEqualTo(id);

		// 执行查询对象

		List<TbItemCat> selectByExample = itemCatMapper.selectByExample(example);

		// 创建List<EasyUITreeNode>集合
		List<EasyUITreeNode> list = new ArrayList<EasyUITreeNode>();

		for (TbItemCat tbItemCat : selectByExample) {

			// 创建EasyUITreeNode对象
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed" : "open");

			list.add(easyUITreeNode);
		}

		return list;
	}

}
