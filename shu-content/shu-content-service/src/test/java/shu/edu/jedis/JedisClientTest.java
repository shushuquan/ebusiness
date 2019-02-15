package shu.edu.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import shu.edu.common.jedis.JedisClient;

public class JedisClientTest {

	@Test
	public void jedisClientTest() throws Exception {
		//初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//只需要jedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		jedisClient.set("test1", "集群版redis");
		String string = jedisClient.get("test1");
		System.out.println(string);
	}
}
