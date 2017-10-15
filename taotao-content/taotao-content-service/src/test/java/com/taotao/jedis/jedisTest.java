package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.content.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @Title: jedisTest.java
 * @Package com.taotao.jedis
 * @Description: jedis测试
 */
public class jedisTest {

	/**
	 * 使用单机Jedis单机连接Redis @throws
	 */
	@Test
	public void testJedis() {
		// 创建一个Jedis对象----注：这里的jedis对象是对Redis数据库操作对象
		Jedis jedis = new Jedis("192.168.25.128", 6379);
		jedis.set("test1", "快结束了！");
		String key1 = jedis.get("test1");
		System.out.println(key1);
		jedis.close();

	}

	/**
	 * 使用连接池连接单机Redis @throws
	 */
	@Test
	public void testJedisPool() {
		// 创建jedisPoll对象
		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379);

		// 获取jedis对象
		Jedis resource = jedisPool.getResource();

		resource.set("test2", "jedisPool");

		String result = resource.get("test2");

		System.out.println(result);

		jedisPool.close();

	}

	/**
	 * 连接集群版
	 */
	@SuppressWarnings("resource")
	@Test
	public void jedisCluster() {

		Set<HostAndPort> nodes = new HashSet<>();

		nodes.add(new HostAndPort("192.168.25.128", 7001));
		nodes.add(new HostAndPort("192.168.25.128", 7002));
		nodes.add(new HostAndPort("192.168.25.128", 7003));
		nodes.add(new HostAndPort("192.168.25.128", 7004));
		nodes.add(new HostAndPort("192.168.25.128", 7005));
		nodes.add(new HostAndPort("192.168.25.128", 7006));

		JedisCluster jedisCluster = new JedisCluster(nodes);

		jedisCluster.set("test3", "JedisCluster");

		System.out.println(jedisCluster.get("test3"));

		jedisCluster.close();

	}

	/**
	 * spring整合jedis
	 */
	@Test
	public void jedisAndSpring() {

		// 初始化applicationContext.xml
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-redis.xml");
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);

		jedisClient.set("springJedis", "xixi");

		System.out.println(jedisClient.get("springJedis"));

	}
}
