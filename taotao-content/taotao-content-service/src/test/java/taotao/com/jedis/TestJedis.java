package taotao.com.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {
	
	@Test
	public void testJedis()throws Exception{
		Jedis jedis=new Jedis("192.168.25.175",6379);
		jedis.set("jedis_key", "jedis_value");
		System.out.println(jedis.get("jedis_key"));
		System.out.println(jedis.get("name"));
		jedis.close();
	}
	
	@Test
	public void testJedisPool()throws Exception{
		JedisPool jedisPool=new JedisPool("127.0.0.1",6379);
		Jedis jedis=jedisPool.getResource();
		System.out.println(jedis.get("jedis_key"));
		System.out.println(jedis.get("age"));
		jedis.close();
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster()throws Exception{
		Set<HostAndPort>nodes=new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.175",7001));
		nodes.add(new HostAndPort("192.168.25.175",7002));
		nodes.add(new HostAndPort("192.168.25.175",7003));
		nodes.add(new HostAndPort("192.168.25.175",7004));
		nodes.add(new HostAndPort("192.168.25.175",7005));
		nodes.add(new HostAndPort("192.168.25.175",7006));
		JedisCluster jedisCluster=new JedisCluster(nodes);
		jedisCluster.set("jedis_cluster","hello jedis cluster");
		String value=jedisCluster.get("jedis_cluster");
		System.out.println(value);
		jedisCluster.close();
	}
	
}
