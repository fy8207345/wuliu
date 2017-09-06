package cn.itcast.bos.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.repository.CourierRepository;
import cn.itcast.bos.repository.FixedRepository;
import cn.itcast.bos.repository.TakeTimeRepository;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	@Autowired
	private FixedRepository fixedRepository;
	
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	@Override
	public void saveFixedArea(FixedArea model) {
		fixedRepository.save(model);
	}
	@Override
	public Page<FixedArea> pageQuery(Specification<FixedArea> specification, Pageable pageable) {
		// TODO Auto-generated method stub
		return fixedRepository.findAll(specification,pageable);
	}
	
	//关联快递员到定区
	@Override
	public void associationCourierToFixedArea(FixedArea fixedArea, Integer courierId, Integer takeTimeId) {
		FixedArea persistFixedArea=fixedRepository.findOne(fixedArea.getId());
		
		Courier courier=courierRepository.findOne(courierId);
		
		TakeTime takeTime=takeTimeRepository.findOne(takeTimeId);
		
		//快递员关联到定区上,谁维护外键谁去关联
		persistFixedArea.getCouriers().add(courier);
		
		//将收派标准关联到快递员上
		courier.setTakeTime(takeTime);
	}

}
