package shu.edu.jedis;

import java.util.HashSet;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	@Test
	public void jedisTest() {
		//创建一个jedis链接，参数host，port
		Jedis jedis = new Jedis("192.168.56.103",6379);
		//直接使用jedis操作redis
		jedis.set("张三", "上海宁");
		String string = jedis.get("张三");
		System.out.println(string);
		jedis.close();
	}
	@Test
	public void jedisPoolTest() {
		JedisPool pool =new JedisPool("192.168.56.103", 6379);
		Jedis jedis = pool.getResource();
		jedis.set("李四", "北京人");
		String string = jedis.get("李四");
		System.out.println(string);
		jedis.close();
	}
	@Test
	public void jedisClusterTest() {
		//第一步：使用jediscluster对象，就需要一个Set<HostAndPort>对象
		HashSet<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.56.103",7001));
		nodes.add(new HostAndPort("192.168.56.103",7002));
		nodes.add(new HostAndPort("192.168.56.103",7003));
		nodes.add(new HostAndPort("192.168.56.103",7004));
		nodes.add(new HostAndPort("192.168.56.103",7005));
		nodes.add(new HostAndPort("192.168.56.103",7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//第二步：使用jediscluster对象操作redis
		jedisCluster.append("test", "1231233");
		String string = jedisCluster.get("test");
		System.out.println(string);
		jedisCluster.close();
	}
}
