package shu.edu.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class SolrTest {

	@Test
	public void addDocument() throws Exception{
		//创建一个SolrServer对象，使用HttpSolrServer创建对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.56.101:8080/solr/solrcore1");
		//创建一个文档对象SolrInputDocument对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域，必须有id域，域的名称必须在managed-schema中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品");
		document.addField("item_price", 1000);
		//把文档写入索引库
		solrServer.add(document);
		solrServer.commit();
	}
}
