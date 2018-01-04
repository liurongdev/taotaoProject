package taotao.com.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;



public class MyMessageListener implements MessageListener{
	
	@Override
	public void onMessage(Message message) {
		TextMessage textMessage=(TextMessage)message;
		try {
			String info = textMessage.getText();
			System.out.println("###"+info);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
