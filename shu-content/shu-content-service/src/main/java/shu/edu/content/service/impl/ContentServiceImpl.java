package shu.edu.content.service.impl;

import java.util.Date;
import java.util.List;

import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import shu.edu.common.jedis.JedisClient;
import shu.edu.common.utils.EDUResult;
import shu.edu.common.utils.JsonUtils;
import shu.edu.content.service.ContentService;
import shu.edu.mapper.TbContentMapper;
import shu.edu.pojo.TbContent;
import shu.edu.pojo.TbContentExample;
import shu.edu.pojo.TbContentExample.Criteria;

@Service
public class ContentServiceImpl implements ContentService {

	/*
	 * 内容管理，新增功能。
	 * @see shu.edu.content.service.ContentService#addContent(shu.edu.pojo.TbContent)
	 */
	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public EDUResult addContent(TbContent contents) {
		// 创建一个TbContent
		// 补全
		contents.setCreated(new Date());
		contents.setUpdated(new Date());
		//插入到数据库，新的数据来了，就要更新缓存，删除当前缓存就可以。
		contentMapper.insert(contents);
		//删除对应cid的缓存key
		jedisClient.hdel(CONTENT_LIST, contents.getCategoryId().toString());
		return EDUResult.ok();
	}
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		//查询缓存
		try {
			//如果有缓存直接响应结果
			String jsonhget = jedisClient.hget(CONTENT_LIST, cid+"");
			//判断是否为空
			if(StringUtils.isEmpty(cid+"")) {
				//把json转换为List
				List<TbContent> list = JsonUtils.jsonToList(jsonhget, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		//没有的话去查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//使用包含大文本的查询语句 
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//把查询结果放入缓存
		try {
			//缓存内容类型 hashset string string
			jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.getStackTrace();
		}
		return list;
	}

}
