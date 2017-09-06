package cn.itcast.bos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard,Integer>{
	
	//根据收派标准名称查询
	public List<Standard> findByName(String name);
	
	//模糊查询findBy+列名
	//public List<Standard> findByNameC_NAME(String name);
	//多个条件等值 查询
	
	//public List<Standard> findByC_IDAndC_NAME(Integer id,String name);
	
	@Query(value="from Standard where name=?",nativeQuery=false)    //JPQL
	public List<Standard> queryName(String name);
	
	//在实体类中配置
	@Query
	public List<Standard> queryName2(String name);
	
	
	//这得指定前后顺序
	@Query(value="update Standard set minLength=?2 where id=?1")
	@Modifying
	public void updateMinLength(Integer id,Integer minLength);
	
}
