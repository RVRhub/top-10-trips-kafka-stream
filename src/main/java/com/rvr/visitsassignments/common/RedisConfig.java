//package com.rvr.visitsassignments.common;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.listener.ChannelTopic;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
//import org.springframework.data.redis.serializer.GenericToStringSerializer;
//
//import com.rvr.visitsassignments.queue.MessagePublisher;
//import com.rvr.visitsassignments.queue.MessagePublisherImpl;
//import com.rvr.visitsassignments.queue.MessageSubscriber;
//
//@Configuration
//public class RedisConfig {
//
//	@Bean
//	JedisConnectionFactory jedisConnectionFactory() {
//		return new JedisConnectionFactory();
//	}
//
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate() {
//		final RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(jedisConnectionFactory());
//		template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
//		return template;
//	}
//
//	@Bean
//	MessageListenerAdapter messageListener() {
//		return new MessageListenerAdapter(new MessageSubscriber());
//	}
//
//	@Bean
//	RedisMessageListenerContainer redisContainer() {
//		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//		container.setConnectionFactory(jedisConnectionFactory());
//		container.addMessageListener(messageListener(), topic());
//		return container;
//	}
//
//	@Bean
//	MessagePublisher redisPublisher() {
//		return new MessagePublisherImpl(redisTemplate(), topic());
//	}
//
//	@Bean
//	ChannelTopic topic() {
//		return new ChannelTopic("pubsub:queue");
//	}
//}
