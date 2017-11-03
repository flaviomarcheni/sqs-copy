package com.sqs.copy;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Component
// @ConditionalOnProperty(value = {"from", "to"})
public class CopyQueue {
	Logger logger = LoggerFactory.getLogger(CopyQueue.class);
	private QueueMessagingTemplate messagingTemplate;
	private AmazonSQS amazonSQS;

	@Autowired
	public CopyQueue(QueueMessagingTemplate messagingTemplate, AmazonSQS amazonSQS) {
		this.messagingTemplate = messagingTemplate;
		this.amazonSQS = amazonSQS;

	}

//	@PostConstruct
	void sendMessage() {
		Person person = new Person("Flavio " + new Random().nextLong(), 29);
		// logger.info("Enviando msg: {}", person);
		// messagingTemplate.convertAndSend("base_unica_jms_persistir", person);

		teste();
	}

	private void teste() {

		int maxNumberOfMessages = 3;
		ReceiveMessageRequest receive_request = new ReceiveMessageRequest()
				.withQueueUrl("base_unica_jms_persistir_deadletter").withMessageAttributeNames("All")
				.withMaxNumberOfMessages(maxNumberOfMessages);
		receive_request.setMaxNumberOfMessages(maxNumberOfMessages);
		List<Message> messages2 = amazonSQS.receiveMessage(receive_request).getMessages();

		for (Message message : messages2) {

			logger.info("body: {}", message.getBody());

			Map<String, MessageAttributeValue> messageAttributes = message.getMessageAttributes();
			logger.info("messageAttributes: ");
			messageAttributes.forEach((k, v) -> {
				logger.info("{} : {}", k, v);
			});

			logger.info("Reenviando msg");
			SendMessageRequest messageRequest = new SendMessageRequest().withQueueUrl("base_unica_jms_persistir")
					.withMessageAttributes(message.getMessageAttributes()).withMessageBody(message.getBody());
			amazonSQS.sendMessage(messageRequest);

			amazonSQS.deleteMessage("base_unica_jms_persistir_deadletter", message.getReceiptHandle());
			logger.info("############################################################################################");
		}

	}

	static class Person {

		private String name;
		private Integer age;

		public Person(String name, Integer age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Person [name=" + name + ", age=" + age + "]";
		}

	}

	// @SqsListener(value = "${queue.from}", deletionPolicy =
	// SqsMessageDeletionPolicy.NEVER)
	// void ds(@Headers Map<Object, Object> headers, Acknowledgment acknowledgment,
	// MessageHeaders messagex) {
	//
	// headers.forEach((k, v) -> {
	// logger.info("{} : {}", k, v);
	// });
	//// logger.info("body: {}", message);
	// logger.info(
	// "#######################################################################################################");
	// logger.info("M: {}",messagex);
	// acknowledgment.acknowledge();
	// }

}
