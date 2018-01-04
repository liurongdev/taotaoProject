package taotao.com.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;




public class TestActiveMq {
	
	@Test
	public void testQueueProducer()throws Exception{
		//创建连接工厂
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.175:61616");
		//创建连接对象
		Connection connection= connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建Session对象，需要第一个参数为是否启用事物，这里不启用，
		//如果启动第二个参数可以忽略，第二个参数为响应消息的模式，这里为自动响应
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建Destination对象，这里有topic，queue
		Queue queue=session.createQueue("test-queue");
		//设置发送的队列,消息的生产者
		MessageProducer producer=session.createProducer(queue);
		//创建消息文本
		TextMessage message=session.createTextMessage("这是测试queue消息10");
		//发送消息
		producer.send(message);
		//关闭连接的资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws Exception{
		//创建连接工厂
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.175:61616");
		//创建连接对象
		Connection connection= connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建Session对象，需要第一个参数为是否启用事物，这里不启用，
		//如果启动第二个参数可以忽略，第二个参数为响应消息的模式，这里为自动响应
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建Destination对象，消息的消费者应该从队列中接受消息，地址应该和服务端一样
		Queue queue=session.createQueue("test-queue");
		//Session创建一个Consumer而对象
		MessageConsumer consumer=session.createConsumer(queue);
		//Consummer对象中设置ConsumerListener对象
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//去消息内容
				if(message instanceof TextMessage){
					TextMessage textMessage=(TextMessage)message;
					try {
						String text=textMessage.getText();
						//打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		//等待接受消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	
	
	@Test
	public void testTopicProducer()throws Exception{
		//创建连接工厂
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.175:61616");
		//创建连接对象
		Connection connection= connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建Session对象，需要第一个参数为是否启用事物，这里不启用，
		//如果启动第二个参数可以忽略，第二个参数为响应消息的模式，这里为自动响应
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建Destination对象，这里有topic，queue
		Topic topic=session.createTopic("test-topic");
		//设置发送的队列,消息的生产者
		MessageProducer producer=session.createProducer(topic);
		//创建消息文本
		TextMessage message=session.createTextMessage("这是测试topic消息01");
		//topic默认不持久化到服务器端的
		//发送消息
		producer.send(message);
		//关闭连接的资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception{
		//创建连接工厂
		ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://192.168.25.175:61616");
		//创建连接对象
		Connection connection= connectionFactory.createConnection();
		//开启连接
		connection.start();
		//创建Session对象，需要第一个参数为是否启用事物，这里不启用，
		//如果启动第二个参数可以忽略，第二个参数为响应消息的模式，这里为自动响应
		Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建Destination对象，消息的消费者应该从队列中接受消息，地址应该和服务端一样
		Topic topic=session.createTopic("test-topic");
		//Session创建一个Consumer而对象
		MessageConsumer consumer=session.createConsumer(topic);
		//Consummer对象中设置ConsumerListener对象
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				//去消息内容
				if(message instanceof TextMessage){
					TextMessage textMessage=(TextMessage)message;
					try {
						String text=textMessage.getText();
						//打印消息内容
						System.out.println(text);
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		System.out.println("topic消息consumer3...");
		//等待接受消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
