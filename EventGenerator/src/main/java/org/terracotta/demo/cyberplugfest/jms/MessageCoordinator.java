package org.terracotta.demo.cyberplugfest.jms;

import java.util.Map;
import java.util.Map.Entry;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class MessageCoordinator {
	private static Logger log = LoggerFactory.getLogger(EventPublisher.class);
	
	private JmsTemplate jmsTemplate;
	private Destination destination;

	public MessageCoordinator() {
		super();
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	@Autowired
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public Destination getDestination() {
		return destination;
	}

	@Autowired
	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public void sendMessage(final String messageContent, final Map<String,String> properties){
		if(log.isDebugEnabled()){
			log.debug("Sending message:" + messageContent);
		}
		
		try {
			sendTextMessage(destination, messageContent, properties);
		} catch (JMSException e) {
			log.error("Could not send the event onto the bus...error.", e);
		}
	}
	
	public void sendTextMessage(Destination dest, final String message, final Map<String,String> properties) throws JMSException {
		jmsTemplate.send(dest, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message createMessage = session.createTextMessage(message);
				if(null != properties){
					for(Entry<String, String> entry : properties.entrySet()){
						createMessage.setStringProperty(entry.getKey(),entry.getValue());
					}
				}
				if(log.isDebugEnabled())
					log.debug("Sending Message Notification:" + createMessage);
				return createMessage;
			}
		});
	}
}
