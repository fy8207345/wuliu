package cn.itcast.bos.redis.test;

import org.junit.Test;

import redis.clients.jedis.Jedis;

//激活码保存一段时间，会自动失效的测试，需要用到redis，但是他需要使用jedis这个工具类
public class JedisTest {
	//在执行这个测试的时候，必须把redis的服务器端和客户端开启
		@Test
		public void JedisTest() {
			//连接localhost 默认6379
			Jedis jedis=new Jedis("localhost");
			//jedis.set("company","hello，世界");
			//设置多长时间失效
			jedis.setex("company", 30, "你好，地球");
			System.out.println(jedis.get("company"));
		}
}
