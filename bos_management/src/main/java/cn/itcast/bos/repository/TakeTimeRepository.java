package cn.itcast.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.TakeTime;

public interface TakeTimeRepository extends JpaRepository<TakeTime, Integer>{

}
