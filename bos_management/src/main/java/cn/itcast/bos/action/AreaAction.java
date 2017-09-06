package cn.itcast.bos.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.commons.BaseAction;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.Impl.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;

@Controller
@ParentPackage("json-default")
@Scope("prototype")
@Namespace("/")
@Actions
public class AreaAction extends BaseAction<Area>{

	@Autowired
	private AreaService areaService;
	
	//这块不应该加自动注入
	private File file;
	
	public void setFile(File file) {
		this.file = file;
	}

	
	@Action(value="area_batchImport",results={@Result(name="success",type="redirect",location="/pages/base/area.html")})
	public String batchImport() throws FileNotFoundException, IOException {
		//编写解析代码
		List<Area> areas=new ArrayList<Area>();
		//加载Excel文件对象.xls
		HSSFWorkbook hssfWorkbook=new HSSFWorkbook(new FileInputStream(file));
		
		//读取一个sheet,
		HSSFSheet hssfSheet=hssfWorkbook.getSheetAt(0);
		//读入sheet中的每一行
		for(Row row:hssfSheet){
			//第一行跳过
			if(row.getRowNum()==0){
				continue;
			}
			//跳过空行
			if(row.getCell(0)==null ||
				StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
			
			Area area=new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			//生成简码
			//基于pinyin4j生成城市编码he简码
			String province=area.getProvince();
			String city=area.getCity();
			String district=area.getDistrict();
			
			province=province.substring(0,province.length()-1);
			city=city.substring(0, city.length()-1);
			district=district.substring(0, district.length()-1);
			
			//简码
			String[] headArray=PinYin4jUtils.getHeadByString(province+city+district);
			StringBuffer buffer=new StringBuffer();
			//把数组加成一个字符串
			for(String headStr:headArray){
				buffer.append(headStr);
			}
			String shortcode=buffer.toString();
			area.setShortcode(shortcode);
			//生成城市编码
			String citycode=PinYin4jUtils.hanziToPinyin(city,"");
			area.setCitycode(citycode);
			
			System.out.println(citycode);
			
			areas.add(area);
		}
			
		//调用业务层
		areaService.saveBatch(areas);
		return NONE;
	}
	
	//
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery() {

		//创建条件查询对象
		Specification<Area> specification=new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//创建保存条件集合对象
				List<Predicate> list=new ArrayList<Predicate>();
				//添加条件
				if(StringUtils.isNotBlank(model.getProvince())){
					//根据省份进行模糊查询
					Predicate p1=cb.like(root.get("province").as(String.class),"%"+model.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p2=cb.like(root.get("district").as(String.class),"%"+model.getDistrict()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p3=cb.like(root.get("city").as(String.class),"%"+model.getCity()+"%");
					list.add(p3);
				}
				
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		
		Pageable pageable=new PageRequest(page-1, rows);
		pushPageDataToValueStack(areaService.pageQuery(specification,pageable));
		
		return SUCCESS;
	}
}
