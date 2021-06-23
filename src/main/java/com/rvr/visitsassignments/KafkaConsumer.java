package com.rvr.visitsassignments;

import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.rvr.visitsassignments.common.AppConfigs;
import com.rvr.visitsassignments.common.Top10Trips;
import com.rvr.visitsassignments.types.RatingByTrips;

@Component
public class KafkaConsumer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private final KafkaTemplate<String, String> kafkaTemplate;
	private CountDownLatch latch = new CountDownLatch(1);
	private TreeSet<RatingByTrips> top10Rating = null;

	public KafkaConsumer(KafkaTemplate<String, String> kafkaTemplate) {this.kafkaTemplate = kafkaTemplate;}

	@KafkaListener(topics = AppConfigs.participantsTopic, groupId = AppConfigs.participantsTopic)
	public void receiveParticipantsTopic(ConsumerRecord<String, ?> consumerRecord)
	{
		LOGGER.info(consumerRecord.key());
		Optional.ofNullable(top10Rating.first()).ifPresent(t -> {
				LOGGER.info("First tripId: " + t.getTripId());
				this.kafkaTemplate.send(AppConfigs.joinParticipantToTrip, consumerRecord.key(), t.getTripId());
			}
		);

		latch.countDown();
	}

	@KafkaListener(topics = "visit-assignments-top10-ratings-changelog", groupId = "json", containerFactory = "kafkaListenerContainerFactory")
	public void receiveTop10Ratings(@Payload Top10Trips consumerRecord)
	{
		LOGGER.info(consumerRecord.toString());
		setCurrentListOfTop10Rating(consumerRecord.getTop10());
		latch.countDown();
	}

	private void setCurrentListOfTop10Rating(TreeSet<RatingByTrips> top10Rating)
	{
		this.top10Rating = top10Rating;
	}

	public CountDownLatch getLatch()
	{
		return latch;
	}

}
