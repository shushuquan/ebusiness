package shu.edu.content.service;

import java.util.List;

import shu.edu.common.pojo.EasyUITreeNode;
import shu.edu.common.utils.EDUResult;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(long parentId);
	EDUResult addContentCategory(long parentId,String name);
}
