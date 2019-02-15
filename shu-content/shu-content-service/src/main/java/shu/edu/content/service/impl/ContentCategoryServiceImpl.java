package shu.edu.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shu.edu.common.pojo.EasyUITreeNode;
import shu.edu.common.utils.EDUResult;
import shu.edu.content.service.ContentCategoryService;
import shu.edu.mapper.TbContentCategoryMapper;
import shu.edu.pojo.TbContentCategory;
import shu.edu.pojo.TbContentCategoryExample;
import shu.edu.pojo.TbContentCategoryExample.Criteria;
/**
 * 内容分类管理Service
 * @author asus
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		// 根据parentid查询字节节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		//转换成EasyUITreeNode
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for(TbContentCategory tbContentCategory :list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}
	@Override
	public EDUResult addContentCategory(long parentId, String name) {
		// 创建一个表对应的pojo
		TbContentCategory contentCategory = new TbContentCategory();
		// 设置pojo的属性，要满足插入后返回主键
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1 正常 2删除
		contentCategory.setStatus(1);
		// 默认1
		contentCategory.setSortOrder(1);
		// 新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 插入到数据库
		contentCategoryMapper.insert(contentCategory);
		// 判断父节点的isparent属性，如果不是true更改为true
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) {
			parent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		// 返回结果，返回EDUResult 包含pojo
		return EDUResult.ok(contentCategory);
	}

}
