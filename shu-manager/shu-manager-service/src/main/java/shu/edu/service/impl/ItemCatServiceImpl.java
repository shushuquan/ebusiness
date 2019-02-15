package shu.edu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shu.edu.common.pojo.EasyUITreeNode;
import shu.edu.mapper.TbItemCatMapper;
import shu.edu.pojo.TbItemCat;
import shu.edu.pojo.TbItemCatExample;
import shu.edu.pojo.TbItemCatExample.Criteria;
import shu.edu.service.ItemCatService;
/**
 * 商品分类管理
 * @author asus
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		// 根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置条件查询
		criteria.andParentIdEqualTo(parentId);
		//执行条件查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//创建返回结果的list
		List<EasyUITreeNode> listResult = new ArrayList<>();
		//把列表转换成EasyUITreeNode列表
		for(TbItemCat tbItemCat : list) {
			EasyUITreeNode treeNode = new EasyUITreeNode();
			//设置属性
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到结果列表
			listResult.add(treeNode);
		}
		//返回结果
		return listResult;
	}

}
