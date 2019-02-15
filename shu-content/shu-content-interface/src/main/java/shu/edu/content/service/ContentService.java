package shu.edu.content.service;

import java.util.List;

import shu.edu.common.utils.EDUResult;
import shu.edu.pojo.TbContent;

public interface ContentService {

	/**
	 * 内容管理的新增功能
	 * 
	 * @param contents
	 * @return
	 */
	EDUResult addContent(TbContent contents);
	/*
	 * 根据内容分类的cid查询列表
	 */
	List<TbContent> getContentListByCid(long cid);
}
