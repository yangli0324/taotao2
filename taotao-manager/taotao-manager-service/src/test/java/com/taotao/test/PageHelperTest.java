package com.taotao.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

/**
 * 项目名称：taotao-manager-service 类名称：PageHelperTest 类描述： 创建人：15378 创建时间：2017年9月15日
 * 下午11:22:46 修改人：15378 修改时间：2017年9月15日 下午11:22:46 修改备注：
 * 
 * @version
 *
 */
public class PageHelperTest {

	@Test
	public void pageHelper() {
		// 初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-dao.xml");
		// 从容器中获取Mapper代理对象
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		// 执行查询
		TbItemExample example = new TbItemExample();
		// 设置分页条件
		PageHelper.startPage(1, 30);

		List<TbItem> list = tbItemMapper.selectByExample(example);

		// 查询分页结果集
		System.out.println("分页查询记录数：" + list.size());

		PageInfo<TbItem> pageInfo = new PageInfo<>(list);

		System.out.println("总记录数：" + pageInfo.getTotal());
		System.out.println("总记录页数：" + pageInfo.getPages());

	}
}
