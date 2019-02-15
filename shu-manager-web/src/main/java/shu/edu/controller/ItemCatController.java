package shu.edu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shu.edu.common.pojo.EasyUITreeNode;
import shu.edu.service.ItemCatService;

/**
 * 商品分类功能Controller
 * @author asus
 *
 */
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0")Long parentId){
		//调用服务查询节点列表
		List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
		return list;
	}
}
