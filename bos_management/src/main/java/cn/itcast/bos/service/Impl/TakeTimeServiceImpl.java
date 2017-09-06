package cn.itcast.bos.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.repository.TakeTimeRepository;

@Transactional
@Service
public class TakeTimeServiceImpl implements TakeTimeService {
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	@Override
	public List<TakeTime> findAll() {
		return takeTimeRepository.findAll();
	}

}
