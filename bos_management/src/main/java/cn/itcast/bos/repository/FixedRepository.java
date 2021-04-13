package cn.itcast.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedRepository extends JpaRepository<FixedArea,String>,JpaSpecificationExecutor<FixedArea>{

}
