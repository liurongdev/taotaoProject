package taotao.com.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.sun.tools.doclets.internal.toolkit.Content;

public class TestJedisClient {
	
	@Test
	public void testJedisClientPool() throws Exception{
		//初始化spring容器
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/ApplicationContext-redis.xml");
		//取得JedisClient对象
		JedisClient jedisClient=context.getBean(JedisClient.class);
		//利用JedisClient对象进行操作
		jedisClient.set("jedisClientCluster", "jedis_Client_Cluster_value");
		System.out.println(jedisClient.get("jedisClientCluster"));
		
	}
}
