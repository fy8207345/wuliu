package cn.itcast.bos.service.Impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.repository.CourierRepository;

@Transactional
@Service
public class CourierSeriveImpl implements CourierService {
	@Autowired
	private CourierRepository courierRepository;
	
	@Override
	public void save(Courier courier) {
		// TODO Auto-generated method stub
		courierRepository.save(courier);
	}

	@Override
	public Page<Courier> findByPage(Specification<Courier> specification, Pageable pageable) {
		// TODO Auto-generated method stub
		return courierRepository.findAll(specification,pageable);
	}

	@Override
	public void delBatch(String[] str) {
		for(String idStr:str){
			Integer id=Integer.parseInt(idStr);
			courierRepository.delBatch(id);
		}
		
	}

	@Override
	public void addBatch(String[] str) {
		for(String idaStr:str){
			Integer id=Integer.parseInt(idaStr);
			courierRepository.addBatch(id);
		}
	}

	//查询没有关联定区的管理员
	@Override
	public List<Courier> findNoAssociation() {
		//封装Specification   有条件的查询
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			public Predicate toPredicate(Root<Courier> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 查询条件，判定列表size为空,Set用的是util下的util
				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
				return p;
			}
		};
		
		return courierRepository.findAll(specification);
	}

	

	
}
