package com.sqs.copy.infra;

import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSAsync;
@Component
public class SqsConfig {

	@Bean
	public QueueMessagingTemplate QueueMessagingTemplate(AmazonSQSAsync amazonSqs) {
		return new QueueMessagingTemplate(amazonSqs);
	}

}
