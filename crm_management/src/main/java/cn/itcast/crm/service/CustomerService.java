package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	//查询所有没有关联客户列表
	@Path("/noconnectcustomer")   //查询路径
	@GET    //查询
	@Produces({"application/xml","application/json"})   //接受参数方式
	public List<Customer> findNoAssociationCustomers();
	
	//查询所有关联客户列表,要有定区的id
	@Path("/connectcustomer/{fixedareaid}")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findHasAssociationFixedAreaCustomers(@PathParam("fixedareaid") String fixedAreaId);
	
	//将客户关联到定区上，将所有客户的id拼成字符串
	@Path("/connectcustomertofixedarea")
	@PUT       //进行修改操作
	public void associationCustomersToFixedArea(
			//http请求参数，吧查询到的所有id拼接成字符串返回
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId
	);
	
	//添加用户注册的方法
	@Path("/customer")
	@POST
	@Produces({"application/xml","application/json"})
	public void regist(Customer customer);
	
	
	//查找用户
	
	@Path("/telephone/{telephone}")
	@GET
	@Consumes({"application/xml","application/json"})    
	public Customer findByTelephone(@PathParam("telephone") String telephone);
	
	//修改状态码
	@Path("/updatetype/{telephone}")
	@GET
	public void updateType(@PathParam("telephone") String telephone);	
}
