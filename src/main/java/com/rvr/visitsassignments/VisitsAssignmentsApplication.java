package com.rvr.visitsassignments;

import java.util.Collection;
import java.util.Random;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rvr.visitsassignments.common.AppConfigs;
import com.rvr.visitsassignments.model.SortedTrip;
import com.rvr.visitsassignments.repository.SortedTripRepository;
import com.rvr.visitsassignments.topology.Top10AvailableTripsWithRedisTopology;

@SpringBootApplication
@EnableKafkaStreams
public class VisitsAssignmentsApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(VisitsAssignmentsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(VisitsAssignmentsApplication.class, args);
	}

	@Autowired
	private RedissonClient redissonClient;

	@Bean
	public Topology topology(StreamsBuilder builder)
	{
		// https://github.com/LearningJournal/Kafka-Streams-Real-time-Stream-Processing/blob/master/top3-inventories

        // Topology topology = new JoinTopology(builder).getTopology();

		Topology topology = new Top10AvailableTripsWithRedisTopology(builder, redissonClient).getTopology();

		LOGGER.info(String.valueOf(topology.describe()));

		return topology;
	}
}

//@Component
//class VisitView
//{
//	@Autowired
//	public void buildVisitView(StreamsBuilder builder)
//	{
//		builder.table("unassigned-visit", Consumed.with(Serdes.String(), Serdes.String()), Materialized.as("visit-store"));
//	}
//}

@Component
class Producer
{
	private final KafkaTemplate<String, String> kafkaTemplate;

	Producer(KafkaTemplate<String, String> kafkaTemplate)
	{
		this.kafkaTemplate = kafkaTemplate;
	}

	@EventListener(ApplicationStartedEvent.class)
	public void produce()
	{
		kafkaTemplate.send("trips", "1001", "{\"tripId\": \"1001\", \"status\": \"Open\", \"rating\": 2}");
	}
}

@RestController
class MyIqController{

	private final RedissonClient redissonClient;
	private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;
	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public MyIqController(StreamsBuilderFactoryBean streamsBuilderFactoryBean, KafkaTemplate<String, String> kafkaTemplate,
		RedissonClient redissonClient)
	{
		this.streamsBuilderFactoryBean = streamsBuilderFactoryBean;
		this.kafkaTemplate = kafkaTemplate;
		this.redissonClient = redissonClient;
	}

	@GetMapping("/iq/{id}")
	public String getVisit(@PathVariable final Integer id)
	{
		KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
		ReadOnlyKeyValueStore<Integer, String> store = kafkaStreams.
			store(StoreQueryParameters.fromNameAndType("visit-store", QueryableStoreTypes.keyValueStore()));

		return store.get(id);
	}

	@GetMapping("/iq/all")
	public KeyValueIterator<String, String> getAllStream()
	{
		KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
		ReadOnlyKeyValueStore<String, String> store = kafkaStreams.
			store(StoreQueryParameters.fromNameAndType("assigning-visit", QueryableStoreTypes.keyValueStore()));

		return store.all();
	}

	@GetMapping("/iq/gen")
	public void gen()
	{
		Random random = new Random();
		int id = random.nextInt(100)*100;
		String s = String.valueOf(id);
			kafkaTemplate.send(AppConfigs.tripInfoTopic, s, "{\"tripId\": \""+ s +"\", \"status\": \"Open\", \"rating\": "+ s +"}");
	}

	@GetMapping("/iq/genNewParticipant")
	public void addParticipant()
	{
		Random random = new Random();
		int id = random.nextInt(100);
		String s = String.valueOf(id);
		kafkaTemplate.send(AppConfigs.participantsTopic, s, "{\"participantId\": \""+ s +"\"}");
	}

	@GetMapping("/allTripsFromRedis")
	public Collection<SortedTrip> allTripsFromRedis()
	{
		RSortedSet<SortedTrip> sortedTrip = redissonClient.getSortedSet("sortedTrip");

		return sortedTrip.readAll();
	}
}



