package shu.edu.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import shu.edu.common.pojo.SearchItem;
import shu.edu.common.pojo.SearchResult;
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;
	// 根据查询条件查询索引库
	public SearchResult search(SolrQuery query) throws SolrServerException {
		//获取查询结果
		QueryResponse response = solrServer.query(query);
		SolrDocumentList solrDocumentList = response.getResults();
		//获取查询总记录数
		long numFound = solrDocumentList.getNumFound();
		SearchResult searchResult = new SearchResult();
		searchResult.setRecordCount(numFound);
		//创建商品列表
		List<SearchItem> itemList = new ArrayList<>();
		//获取商品列表，需要高亮显示
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		for(SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem();
			searchItem.setId((String)solrDocument.get("id"));
			searchItem.setCategory_name((String)solrDocument.get("item_category_name"));
			searchItem.setImage((String)solrDocument.get("item_image"));
			searchItem.setPrice((float)solrDocument.get("item_price"));
			searchItem.setSell_point((String)solrDocument.get("item_sell_point"));
			//获取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle="";
			if(list != null&& list.size() > 0) {
				itemTitle = list.get(0);
			}else {
				itemTitle = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(itemTitle);
			//添加到商品列表
			itemList.add(searchItem);
		}
		//把查询商品列表添加搜素结果中返回
		searchResult.setItemList(itemList);
		return searchResult;
	}
}
