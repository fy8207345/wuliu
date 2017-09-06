package cn.itcast.bos.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.commons.BaseAction;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.Impl.TakeTimeService;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
@Actions
public class TakeTimeAction extends BaseAction<TakeTime>{
	
	@Autowired
	private TakeTimeService takeTimeService;
	
	@Action(value = "taketime_findAll", results = { @Result(name = "success", type = "json") })
	public String findAll(){
		List<TakeTime> lists=takeTimeService.findAll();
		//压入值栈的栈顶
		ActionContext.getContext().getValueStack().push(lists);
		
		System.out.println("排班时间为："+lists.size());
		return SUCCESS;
	}
}
