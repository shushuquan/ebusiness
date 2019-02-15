package shu.edu.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shu.edu.common.pojo.SearchResult;
import shu.edu.search.dao.SearchDao;
import shu.edu.search.service.SearchService;


@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String keyword, int page, int rows) throws Exception  {
		// 创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		// 设置查询条件
		solrQuery.setQuery(keyword);
		// 设置分页条件，rows
		if(page <= 0)
			page = 1;
		solrQuery.setStart((page - 1)* rows);
		solrQuery.setRows(rows);
		// 设置默认搜素域
		solrQuery.set("df", "item_title");
		// 设置高亮显示
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<em style=\"color:red\">");
		solrQuery.setHighlightSimplePost("</em>");
		// 执行查询条件
		SearchResult searchResult = searchDao.search(solrQuery);
		// 计算总页数
		long recordCount = searchResult.getRecordCount();
		int totalPage = (int) (recordCount / rows);
		if(recordCount % rows > 0)
			totalPage++;
		// 设置返回结果
		searchResult.setTotalPage(totalPage);
		return searchResult;
	}

}
