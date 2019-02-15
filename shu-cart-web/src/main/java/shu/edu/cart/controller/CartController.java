package shu.edu.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import shu.edu.common.utils.CookieUtils;
import shu.edu.common.utils.JsonUtils;
import shu.edu.pojo.TbItem;
import shu.edu.service.ItemService;

@Controller
public class CartController {
	
	@Value("${TT_CART}")
	private String TT_CART;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 1、从cookie中查询商品列表。
		List<TbItem> cartList = getCartList(request);
		// 2、判断商品在商品列表中是否存在。
		boolean hasItem = false;
		for (TbItem tbItem : cartList) {
			//对象比较的是地址，应该是值的比较
			if (tbItem.getId() == itemId.longValue()) {
				// 3、如果存在，商品数量相加。
				tbItem.setNum(tbItem.getNum() + num);
				hasItem = true;
				break;
			}
		}
		if (!hasItem) {
			// 4、不存在，根据商品id查询商品信息。
			TbItem tbItem = itemService.getItemById(itemId);
			//取一张图片
			String image = tbItem.getImage();
			if (!StringUtils.isEmpty(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
			//设置购买商品数量
			tbItem.setNum(num);
			// 5、把商品添加到购车列表。
			cartList.add(tbItem);
		}
		// 6、把购车商品列表写入cookie。
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
		return "cartSuccess";
	}
	
	/**
	 * 从cookie中取购物车列表
	 * <p>Title: getCartList</p>
	 * <p>Description: </p>
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		//取购物车列表,并转码的得到json
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		//判断json是否为null
		if (!StringUtils.isEmpty(json)) {
			//把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
	
	
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, Model model) {
		//取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		//传递给页面
		model.addAttribute("cartList", cartList);
		return "cart";
	}

}
