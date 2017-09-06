package cn.itcast.bos.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.Impl.StandardService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@Actions
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
	private Standard standard=new Standard();
	@Autowired
	private StandardService standardService;
	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}
	
	//从页面获取两个请求数据，当前页，和每页记录数
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}


	@Action(value="standard_save",results={@Result(name="success",type="redirect",location="/pages/base/standard.html")})
	public String save() {
		standardService.save(standard);
		return SUCCESS;
	}
	
	
	//分页查询返回的是json数据
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String queryPage() {
		//服务器要向页面返回总的记录数，和当前页
		//调用业务层查询数据结果
		Pageable pageable=new PageRequest(page-1, rows);
		Page<Standard> pageDate=standardService.findPageDate(pageable);
		
		//返回客户端数据
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("total", pageDate.getNumberOfElements());
		map.put("rows", pageDate.getContent());
		
		//将map数据转为json返回页面
		ActionContext.getContext().getValueStack().push(map);
		
		return SUCCESS;
	}
	
	
	//这个是快递员管理中的取派标准，是一个下拉选框，其中是取派标准的名称
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findAll() {
		List<Standard> standards=standardService.findAll();
		//压入值栈的顶部
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
	
}
