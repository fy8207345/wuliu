package cn.itcast.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener{
	
	@Override
	public void onMessage(Message message) {
		MapMessage mapMessage=(MapMessage) message;
			try {
				//使用sms发送短信
				//String results=SmsUtils.sendSmsByHTTP(mapMessage.getString("telephone");
				String results="000/xxxx";
				if(results.startsWith("000")){
					/*Action.NONE是struts中内置的一个返回字符串，和Action.SUCCESS一样。
					 * 与SUCCESS不同的是，NONE并不进行页面"跳转"。一般用在Ajax请求中。
					 * */
					System.out.println("发送短信成功，手机号："+mapMessage.getString("telephone")+",验证码："+mapMessage.getString("msg"));
				}else{
					throw new RuntimeException("发送信息失败"+results);
				} 
			}catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
