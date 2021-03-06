package cn.itcast.bos.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.repository.AreaRepository;

@Service
@Transactional
public class AreaSeriveImpl implements AreaService {
	@Autowired
	private AreaRepository areaRepository;
	
	@Override
	public void saveBatch(List<Area> areas) {
		areaRepository.save(areas);
		
	}

	@Override
	public Page<Area> pageQuery(Specification<Area> specification, Pageable pageable) {
		//areaRepository.pageQuery(specification,pageable);
		return areaRepository.findAll(specification,pageable);
	}

}
