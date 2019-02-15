package shu.edu.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import shu.edu.common.pojo.SearchItem;
import shu.edu.search.mapper.ItemMapper;
/**
 * 监听商品添加事件，把商品同步索引库
 * @author asus
 *
 */
public class ItemAddMessageListener implements MessageListener {

	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = null;
			Long itemId = null; 
			//取商品id
			if (message instanceof TextMessage) {
				textMessage = (TextMessage) message;
				itemId = Long.parseLong(textMessage.getText());
				//先等待一会，因为发送消息很快，数据库事务提交可能未完成，避免查不到新添加的商品
				Thread.sleep(100);
				// 1、根据商品id查询商品信息。
				SearchItem searchItem = itemMapper.getItemById(itemId);
				// 2、创建一SolrInputDocument对象。
				SolrInputDocument document = new SolrInputDocument();
				// 3、使用SolrServer对象写入索引库。
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//4、向索引库添加文档
				solrServer.add(document);
				solrServer.commit();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
