package shu.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import shu.edu.common.pojo.EasyUIDataGridResult;
import shu.edu.common.utils.EDUResult;
import shu.edu.pojo.TbItem;
import shu.edu.service.ItemService;



/**
 * 商品管理Controller
 * @author asus
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemservice;

	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable long itemId) {
		TbItem tbItem = itemservice.getItemById(itemId);
		return tbItem;
	}
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page ,Integer rows) {
		EasyUIDataGridResult result = itemservice.getItemList(page, rows);
		return result;
	}
	/*
	 *  商品添加功能
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public EDUResult addItem(TbItem item,String desc) {
		System.out.println(desc);
		EDUResult result = itemservice.addItem(item, desc);
		return result;
	}
}

