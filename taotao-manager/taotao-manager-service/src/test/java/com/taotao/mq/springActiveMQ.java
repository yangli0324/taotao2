package com.taotao.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @Title: springActiveMQ.java
 * @Package com.taotao.mq
 * @Description: springActiveMQ整合测试
 */
public class springActiveMQ {

	@Test
	public void testSpringActiveMQ() {

		// 1、初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/applicationContext-activemq.xml");

		// 2、获取JmsTemplate
		JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);

		// 3、从容器中获得一个Destination对象
		Queue queue = (Queue) applicationContext.getBean(Queue.class);

		// 4、JmsTemplate发送消息对象
		jmsTemplate.send(queue, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("spring整合activemq");
				return textMessage;
			}
		});

	}

}
