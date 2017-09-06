package cn.itcast.bos.web.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.poi.ss.formula.functions.T;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{

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
	
	
	

}
