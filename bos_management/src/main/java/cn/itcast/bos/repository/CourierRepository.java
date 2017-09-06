package cn.itcast.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer> ,
	JpaSpecificationExecutor<Courier>{
	//JpaSpecificationExecutor有条件的查询
	
	@Query(value="update Courier set deltag='1' where id=?")
	@Modifying
	void delBatch(Integer id);

	@Query(value="update Courier set deltag='0' where id=?")
	@Modifying
	void addBatch(Integer id);
	
}
