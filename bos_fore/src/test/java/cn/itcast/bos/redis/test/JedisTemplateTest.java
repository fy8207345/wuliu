package cn.itcast.bos.redis.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-cache.xml")
public class JedisTemplateTest {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	//Spring data Redis的使用
	@Test
	public void JedisTemplateTest() {
		//保存key和value
		//设置30秒失效
		redisTemplate.opsForValue().set("city","北京",50,TimeUnit.SECONDS );
		System.out.println(redisTemplate.opsForValue().get("city"));
	}
}
