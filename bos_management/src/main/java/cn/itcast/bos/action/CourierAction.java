package cn.itcast.bos.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.Impl.CourierService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@Actions
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{
	private Courier courier=new Courier();
	
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Autowired
	private CourierService courierService;

	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}
	
	//接收页面的拼接数据
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	
	@Action(value="courier_save",results={@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String save() {
		courierService.save(courier);
		//System.out.println(courier);
		return SUCCESS;
	}
	
	@Action(value="courier_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery() {
		//请求穿够来的两个参数
		Pageable pageable=new PageRequest(page-1, rows);
		
		//根据查询条件，构造Specification 条件查询对象（类似于hibernate的QBC查询）
		Specification<Courier> specification=new Specification<Courier>() {

			@Override
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 当前查询Root根对象Courier
				List<Predicate> list=new ArrayList<Predicate>();
				//单表查询（查询当前对象，对应数据表）
				if(StringUtils.isNoneBlank(courier.getCourierNum())){
					//进行快递员 工号查询
					Predicate p1=cb.equal(root.get("courierNum").as(String.class),
							courier.getCourierNum());
					list.add(p1);
				}
				
				if(StringUtils.isNoneBlank(courier.getCompany())){
					//进行公司查询，模糊查询
					Predicate p2=cb.like(root.get("company").as(String.class),
							"%"+courier.getCompany()+"%");
					list.add(p2);
				}
				
				if(StringUtils.isNoneBlank(courier.getType())){
					//进行快递类型查询，等值查询
					Predicate p3=cb.equal(root.get("type").as(String.class),
							courier.getType());
					list.add(p3);
				}
				
				//多表查询，（查询当前对象，关联 对象对应数据表）
				//使用Courier（Root），关联Standard
				Join<Object, Object> standardRoot=root.join("standard",
						JoinType.INNER);
				if(courier.getStandard()!=null && StringUtils.isNoneBlank(courier.getStandard().getName())){
					//进行收派标准，名称，模糊查询
					Predicate p4=cb.like(standardRoot.get("name").as(String.class), 
							"%"+courier.getStandard().getName()+"%");
					list.add(p4);
							
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		
		};
		
		//调用业务层
		Page<Courier> pageData=courierService.findByPage(specification,pageable);
		
		//返回客户端的数据
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		
		
		//把数据封装到栈顶
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	//快递员作废功能
	@Action(value="courier_delBatch",results={@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String delBatch() {
		//用分隔符分割id
		String[] str=ids.split(",");
		courierService.delBatch(str);
		return SUCCESS;
	}
	
	//快递员还原功能
	@Action(value="courier_addBatch",results={@Result(name="success",type="redirect",location="/pages/base/courier.html")})
	public String addBatch() {
		String[] str=ids.split(",");
		courierService.addBatch(str);
		return SUCCESS;
	}
	
	
	//找到没有关联定区的快递员
	@Action(value="courier_findnossociation",results={@Result(name="success",type="json")})
	public String findnoassociation(){
		//调用业务层，查询未关联定区的快递员
		List<Courier> couriers=courierService.findNoAssociation();
		
		//将查询快递员列表 压入值栈
		ActionContext.getContext().getValueStack().push(couriers);
		return SUCCESS;
	}
	
}
