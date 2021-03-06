package shu.edu.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import shu.edu.item.pojo.Item;
import shu.edu.pojo.TbItem;
import shu.edu.pojo.TbItemDesc;
import shu.edu.service.ItemService;
/**
 *  商品详情页面展示Controller 
 * @author asus
 *
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		//跟据商品id查询商品信息
		TbItem tbItem = itemService.getItemById(itemId);
		//把TbItem转换成Item对象
		Item item = new Item(tbItem);
		//根据商品id查询商品描述
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		//把数据传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}

}
