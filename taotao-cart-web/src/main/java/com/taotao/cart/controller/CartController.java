package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.easyui.TaotaoResult;
import com.taotao.manager.service.ItemService;
import com.taotao.pojo.TbItem;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.JsonUtils;

/**
 * @Title: CartController.java
 * @Package com.taotao.cart.controller
 * @Description: 购物车商品信息操作
 */
@Controller
public class CartController {

	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;

	@Autowired
	private ItemService itemService;

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 1、从url中取商品id
		// 2、从cookie中取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		// 3、遍历列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 4、删除商品。
				cartList.remove(tbItem);
				break;
			}
		}
		// 5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE,
				true);
		// 6、返回逻辑视图：在逻辑视图中做redirect跳转。
		return "redirect:/cart/cart.html";
	}

	/**
	 * 修改购物车信息
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 1、接收两个参数
		// 2、从cookie中取商品列表
		List<TbItem> cartList = getCartList(request);
		// 3、遍历商品列表找到对应商品
		for (TbItem item : cartList) {
			if (item.getId() == itemId.longValue()) {
				// 4、更新商品数量
				item.setNum(num);
			}
		}
		// 5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE,
				true);
		// 6、响应TaoTaoResult。Json数据。
		return TaotaoResult.ok();
	}

	/**
	 * 查看购物车
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, Model model) {
		// 取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		// 传递给页面
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	/**
	 * 添加商品信息
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addItemCookie(@PathVariable Long itemId, Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取cookie信息
		List<TbItem> list = getCartList(request);

		// 判断该列表信息中是否存在该商品
		boolean flag = false;
		for (TbItem tbItem : list) {

			// 如果存在该商品信息、则添加该商品数量
			if (tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(tbItem.getNum() + num);
				flag = true;
				// 直接终止循环
				break;
			}
		}
		// 如果不存在、则把该商品信息添加到cookie信息中
		if (!flag) {
			// 根据商品ID查询该商品信息
			TbItem item = itemService.getItemById(itemId);
			// 取一张图片
			String image = item.getImage();
			if (StringUtils.isNoneBlank(image)) {
				String[] images = image.split(",");
				item.setImage(images[0]);
			}
			// 设置购买商品数量
			item.setNum(num);
			// 5、把商品添加到购车列表。
			list.add(item);
		}
		// 6、把购车商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(list), COOKIE_CART_EXPIRE,
				true);
		return "cartSuccess";

	}

	/**
	 * 提取cookie查询公共方法
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		// 取购物车列表
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_KEY, true);
		// 判断json是否为null
		// System.out.println("cookie json:"+json);
		if (StringUtils.isNotBlank(json)) {
			// 把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
}
