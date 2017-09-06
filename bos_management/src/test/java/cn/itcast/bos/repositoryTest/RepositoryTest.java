package cn.itcast.bos.repositoryTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.repository.StandardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RepositoryTest {
	@Autowired
	private StandardRepository standardRepository;
	
	
	@Test
	public void findByName(){
		System.out.println(standardRepository.findByName("1"));
	}

	
	@Test
	public void findLikeName(){
		//System.out.println(standardRepository.findByC_NAME());
	}
	
	@Test
	public void findByAnd(){
		//System.out.println(standardRepository.findByC_IDAndC_NAME(1, "1"));
	}
	
	@Test
	public void queryByName() {
		System.out.println(standardRepository.queryName("1"));
	}
	
	@Test
	public void queryByName2() {
		System.out.println(standardRepository.queryName2("1"));
	}
	
	
	@Test
	@Transactional
	@Rollback(false)
	public void updateMinLength(){
		standardRepository.updateMinLength(1, 2);
	}
	
}
