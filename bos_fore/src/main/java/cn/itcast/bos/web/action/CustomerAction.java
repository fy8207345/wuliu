package cn.itcast.bos.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.utils.MailUtils;
import cn.itcast.bos.utils.SmsUtils;
import cn.itcast.bos.web.commons.BaseAction;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CustomerAction extends BaseAction<Customer>{
	//model里封装的是从页面获取来的数据，因为没有id，且电话号码是唯一的，所以把电话号码设置为key
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	
	@Action(value="customer_sendSms")
	public String sendMessage() throws UnsupportedEncodingException {
		//生成随机的四位数手机验证码
		String randomCode=RandomStringUtils.randomNumeric(4);
		//将验证码保存到session中,以key，value方式，key存的是电话号码
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
		System.out.println("生成的手机验证码为："+randomCode);
		
		//编辑短信内容
		final String msg="尊敬的用户，本次您获得的验证码为"+randomCode+"服务电话为：100100100";
		
		//使用sms发送短信
		//String results=SmsUtils.sendSmsByHTTP(model.getTelephone(), msg);
//		String results="000/xxxx";
//		if(results.startsWith("000")){
//			/*Action.NONE是struts中内置的一个返回字符串，和Action.SUCCESS一样。
//			 * 与SUCCESS不同的是，NONE并不进行页面"跳转"。一般用在Ajax请求中。
//			 * */
//			return NONE;
//		}else{
//			throw new RuntimeException("发送信息失败"+results);
//		}
		
		//调用MQ服务，发送一条短信
		jmsTemplate.send("bos_sms",new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				//因为存的是键值对的形式，所以用map
				MapMessage createMapMessage = session.createMapMessage();
				createMapMessage.setString("telephone", model.getTelephone());
				createMapMessage.setString("msg", msg);
				return createMapMessage;
			}
		});
		return NONE;
	}
	
	//获取从页面传过来的验证码,这个属性值要和页面的name属性值保持一致
	private String checkCode;
	
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
	//保存一段时间的激活码，然后失效
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	//注册，
	@Action(value="customer_regist",results={@Result(name="success",type="redirect",location="signup-success.html"),
			@Result(name="input",type="redirect",location="signup.html")
	})
	public String regist() {
		//获取保存在session中的验证码
		String checkCodeSession=(String) ServletActionContext.getRequest().getSession().getAttribute(model.getTelephone());
		//判断验证码是否正确,超过时间失效了
		if(checkCodeSession==null || !checkCodeSession.equals(checkCode)){
			System.out.println("短信验证码错误，请重新输入");
			return INPUT;
		}else{
			//System.out.println("注册成功");
			//调用webService，连接crm，保存客户信息
			WebClient.create("http://localhost:8083/crm_management/services/customerService/customer")
			.type(MediaType.APPLICATION_JSON).post(model);
			System.out.println("保存成功");
			System.out.println(model);
			
			
			//*****************************************************
			//发送一封邮件
			//随机生成激活码
			String activecode=RandomStringUtils.randomNumeric(32);
			
			//将激活码保存到redis中，设置24小时失效
			//以键值的方式存的，键是telephone
			redisTemplate.opsForValue().set(model.getTelephone(), activecode, 24,TimeUnit.HOURS);
			
			//用mailUtils发送邮件
			String content="尊敬的客户，请在24小时内，进行邮箱账户的绑定，点击下面地址完成绑定：<br/><a href='"
			+MailUtils.activeUrl+"?telephone="+model.getTelephone()
			+"&activecode="+activecode+"'>速运快递邮箱绑定地址</a>";
			
			MailUtils.sendMail("速运快递激活邮件", content, model.getEmail());
			return SUCCESS;
		}
	}
	
	//设置属性驱动，接受从页面传过来的激活码

	private String activecode;
	
	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}

	
	//判断用户的激活码是否有效，是否重复绑定
	@Action(value="customer_activeMail")
	public String activeMail() throws IOException {
		//解决乱码问题
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		//判断激活码是否有效
		//获取激活码，通过键 获取值
		String activecodeRedis=redisTemplate.opsForValue().get(model.getTelephone());
		System.out.println(activecodeRedis);
		
		if(activecodeRedis==null || !activecodeRedis.equals(activecode)){
			//激活码无效
			ServletActionContext.getResponse().getWriter().println("激活码无效");
		}else{
			//激活码有效，防止重复绑定,查询客户信息，判断是否已经绑定
			Customer customer=WebClient.create("http://localhost:8083/crm_management/services/customerService/telephone/"
			+model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
			System.out.println(customer);
		
			//type是激活还是没激活邮箱的一个状态码
			
			if(customer.getType()==null || customer.getType()!=1){
				//没有绑定
				WebClient.create("http://localhost:8083/crm_management/services/customerService/updatetype/"
						+model.getTelephone()).get();
				
				ServletActionContext.getResponse().getWriter().println("绑定邮箱成功");		
			}else{
				ServletActionContext.getResponse().getWriter().println("已经绑定过，无需再绑定");
				
			}
			
			//删除激活码
			redisTemplate.delete(model.getTelephone());
		}
		
		return NONE;
	}
	
}
