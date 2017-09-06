package cn.itcast.bos.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

	protected T model;
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	//构造器 完成model实例化
	public BaseAction() {
		//构造子类Action对象，获取继承父类型的泛型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		//获取类型第一个泛型参数
		ParameterizedType parameterizedType=(ParameterizedType) genericSuperclass;
		Class<T> modelClass=(Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			model=modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("模型构造失败...");
		}
	}
	
	//接收页面传过来的page和rows数据
		protected int page;
		protected int rows;
		
		public void setPage(int page) {
			this.page = page;
		}

		public void setRows(int rows) {
			this.rows = rows;
		}
		
		//创建分页对象
		
		public void pushPageDataToValueStack(Page<T> pageDate) {
			//调用业务层，完成查询
			
			
			//根据查询结果，封装datagrid需要数据格式
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("total", pageDate.getTotalElements());
			map.put("rows", pageDate.getContent());
			//压入值栈的栈顶
			ActionContext.getContext().getValueStack().push(map);
		}

}
