package shu.edu.service;

import java.util.List;

import shu.edu.common.pojo.EasyUITreeNode;

public interface ItemCatService {

	List<EasyUITreeNode> getItemCatList(long parentId);
}
