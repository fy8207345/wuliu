package cn.itcast.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.TakeTime;
import org.springframework.stereotype.Repository;

@Repository
public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer>{

}
