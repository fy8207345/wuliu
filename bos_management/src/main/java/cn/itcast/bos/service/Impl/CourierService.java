package cn.itcast.bos.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findByPage(Specification<Courier>specification,Pageable pageable);

	void delBatch(String[] str);

	void addBatch(String[] str);

	List<Courier> findNoAssociation();


}
