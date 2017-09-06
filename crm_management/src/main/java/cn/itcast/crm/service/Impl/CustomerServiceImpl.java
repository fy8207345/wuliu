package cn.itcast.crm.service.Impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	//注入dao
	@Autowired
	private CustomerRepository customerRepository;
	
	//无参数查询
	@Override
	public List<Customer> findNoAssociationCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findByFixedAreaIdIsNull() ;
	}

	//有参数查询
	@Override
	public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId) {
		// TODO Auto-generated method stub
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}
	
	//进行编辑
	@Override
	public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		//修改crm业务实现，可以实现已关联变成未关联
		//解除关联动作
		customerRepository.clearFixedAreaId(fixedAreaId);
		//切割字符串
		if(StringUtils.isBlank(customerIdStr)){
			return;
		}
		
		//切割字符串id
		String[] customerIdArray=customerIdStr.split(",");
		for(String idStr:customerIdArray){
			Integer id=Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId,id);
		}
	}

	@Override
	public void regist(Customer customer) {
		customerRepository.save(customer);
		
	}

	@Override
	public Customer findByTelephone(String telephone) {
		return customerRepository.findByTelephone(telephone);
	}

	@Override
	public void updateType(String telephone) {
		customerRepository.updateType(telephone);
	}

}
