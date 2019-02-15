package shu.edu.service;

import shu.edu.common.pojo.EasyUIDataGridResult;
import shu.edu.common.utils.EDUResult;
import shu.edu.pojo.TbItem;
import shu.edu.pojo.TbItemDesc;

public interface ItemService {
	
	TbItem getItemById(long itemId);
	//查询商品列表
	EasyUIDataGridResult getItemList(int page,int rows);
	//添加商品
	EDUResult addItem(TbItem item,String desc);
	TbItemDesc getItemDescById(long itemId);
}
