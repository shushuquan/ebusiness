package shu.edu.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shu.edu.common.pojo.SearchItem;
import shu.edu.common.utils.EDUResult;
import shu.edu.search.mapper.ItemMapper;
import shu.edu.search.service.SearchItemService;
/**
 * 	索引库维护数据的Service
 * @author asus
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;//容器加载的是子类HttpSolrServeer
	
	@Override
	public EDUResult importAllItems() {
			
				try {
					// 查询商品列表
					List<SearchItem> itemList = itemMapper.getItemList();
					// 便利商品列表
					for(SearchItem searchItem : itemList) {
						// 创建文档对象
						SolrInputDocument document = new SolrInputDocument();
						// 向文档对象中添加域
						document.addField("id", searchItem.getId());
						document.addField("item_title",searchItem.getTitle() );
						document.addField("item_sell_point", searchItem.getSell_point());
						document.addField("item_price", searchItem.getPrice());
						document.addField("item_image", searchItem.getImage());
						document.addField("item_category_name", searchItem.getCategory_name());
						// 把文档对象写入索引库
						solrServer.add(document);
					} 
					// 提交
					solrServer.commit();
					// 返回导入成功
					return EDUResult.ok();
				} catch (SolrServerException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return EDUResult.build(500, "数据导入时发生异常");
				}
				
		
	}

}
