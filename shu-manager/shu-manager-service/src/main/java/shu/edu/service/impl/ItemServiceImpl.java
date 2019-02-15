package shu.edu.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import shu.edu.common.jedis.JedisClient;
import shu.edu.common.pojo.EasyUIDataGridResult;
import shu.edu.common.utils.EDUResult;
import shu.edu.common.utils.IDUtils;
import shu.edu.common.utils.JsonUtils;
import shu.edu.mapper.TbItemDescMapper;
import shu.edu.mapper.TbItemMapper;
import shu.edu.pojo.TbItem;
import shu.edu.pojo.TbItemDesc;
import shu.edu.pojo.TbItemExample;
import shu.edu.service.ItemService;
@Service
public class ItemServiceImpl implements ItemService {
	

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;

	public TbItem getItemById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if(!StringUtils.isEmpty(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		//缓存中没有数据然后查询数据库
		//根据主键查询
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//把结果添加到缓存
		try {
			//转换成json格式保存在缓存中
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(tbItem));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return tbItem;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//取结果
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
		
	}

	@Override
	public EDUResult addItem(TbItem item, String desc) {
		// 生成商品id
		final long itemId = IDUtils.genItemId();
		// 补全item的属性
		item.setId(itemId);
		// 1正常 2下架3删除
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(item);
		// 增加描述对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全描述对象属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 插入商品描述数据
		itemDescMapper.insert(itemDesc);
		//发送一个商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		return EDUResult.ok();
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if(!StringUtils.isEmpty(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		//缓存中没有数据然后查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);//把结果添加到缓存
		try {
			//转换成json格式保存在缓存中
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return itemDesc;

	}

}
