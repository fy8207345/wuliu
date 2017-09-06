package cn.itcast.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.base.Area;

//条件查询
public interface AreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area>{

}
