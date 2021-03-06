package cn.itcast.bos.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.repository.StandardRepository;

@Transactional
@Service
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardRepository standardRepository;
	@Override
	public void save(Standard standard) {
		standardRepository.save(standard);
		
	}
	@Override
	public Page<Standard> findPageDate(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}
	@Override
	public List<Standard> findAll() {
		
		return standardRepository.findAll();
	}

}
