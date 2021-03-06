package cn.itcast.bos.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {

	void saveFixedArea(FixedArea model);

	Page<FixedArea> pageQuery(Specification<FixedArea> specification, Pageable pageable);

	void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);

}
