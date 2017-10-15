package com.taotao.mq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Title: testQueueConsumer.java
 * @Package com.taotao.mq
 * @Description: MQ消费者测试
 */
public class testQueueConsumer {

	@Test
	public void queueConsumer() throws Exception {
		// 初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");
		System.in.read();

	}
}
