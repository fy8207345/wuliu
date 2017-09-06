package cn.itcast.bos.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;


import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.commons.BaseAction;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.Impl.FixedAreaService;
import cn.itcast.crm.domain.Customer;



@Controller
@Namespace("/")
@Scope("prototype")
@ParentPackage("json-default")
@Actions
public class FixedAreaAction extends BaseAction<FixedArea> {
	@Autowired
	private FixedAreaService fixedAreaService;
	
	@Action(value="fixedArea_save",results={@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String saveFixedArea() {
		
		fixedAreaService.saveFixedArea(model);
		return SUCCESS;
	}
	
	//属性驱动
	private String[] customerIds;
	
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	
	
	//有条件的分页查询
	@Action(value="fixeArea-pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery() {
		//创建分页对象,接受页面传过来的数据
		Pageable pageable=new PageRequest(page-1, rows);
		//创建条件查询对象
		Specification<FixedArea> specification=new Specification<FixedArea>() {

			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//创建集合Predicate
				List<Predicate> list=new ArrayList<Predicate>();
				//构造查询条件
				if(StringUtils.isNotBlank(model.getId())){
					//定区 编号等值查询
					Predicate p1=cb.equal(root.get("id").as(String.class),model.getId() );
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getCompany())){
					//
						Predicate p2=cb.like(root.get("company").as(String.class),"%"+model.getCompany()+"%");
						list.add(p2);
					
				}
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//
		Page<FixedArea> pageDate=fixedAreaService.pageQuery(specification,pageable);
		
		pushPageDataToValueStack(pageDate);
		return SUCCESS;
	}
	
	
	//查询没有关联的客户
	@Action(value="fixedArea_findNoConnectCustomer",results={@Result(name="success",type="json")})
	public String findNoConnectCustomer(){
		Collection<? extends Customer> collection = WebClient.create("http://localhost:8083/crm_management/services/customerService/noconnectcustomer").
		accept(MediaType.APPLICATION_XML).getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		
		System.out.println("未关联"+collection.size());
		return SUCCESS;
	}
	
	
	
	//查询已关联的客户
	@Action(value="fixedArea_findConnectFixedAreaCustomers",results={@Result(name="success",type="json")})
	public String findConnectFixedAreaCustomers(){
		Collection<? extends Customer> collection = WebClient
				.create("http://localhost:8083/crm_management/services/customerService/connectcustomer/"
						+ model.getId()).accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		
		ActionContext.getContext().getValueStack().push(collection);
		
		System.out.println("已关联"+collection.size());
		return SUCCESS;
	}
	
	//客户关联到定区
	@Action(value="fixedArea_associationCustomersToFixedArea",results={@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String associationCustomerToFixedArea(){
		//把id数据以，号分隔开
		String customerIdStr=StringUtils.join(customerIds,",");
		WebClient.create("http://localhost:8083/crm_management/services/customerService"+"/connectcustomertofixedarea?customerIdStr="+customerIdStr+"&fixedAreaId="+model.getId()).put(null);
		
		return SUCCESS;
	}
	
	private Integer courierId;
	private Integer takeTimeId;
	
	public void setCourierId(Integer courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(Integer takeTimeId) {
		this.takeTimeId = takeTimeId;
	}



	//关联快递员到定区
	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location="/pages/base/fixed_area.html")})
	public String associationCourierToFixedArea(){
		fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
		return SUCCESS;
	}

}
