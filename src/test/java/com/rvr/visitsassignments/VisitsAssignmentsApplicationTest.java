package com.rvr.visitsassignments;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.Test;

class VisitsAssignmentsApplicationTest
{

	@Test
	void shouldCreateMaterializedView()
	{
		final StreamsBuilder builder = new StreamsBuilder();

		new VisitView().buildVisitView(builder);

		Properties config = new Properties();
		config.putAll(Map.of(StreamsConfig.APPLICATION_ID_CONFIG, "test-app",
			StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:9092"));

		final TopologyTestDriver topologyTestDriver = new TopologyTestDriver(builder.build(), config);

		TestInputTopic<Integer, String> visits = topologyTestDriver.createInputTopic("visits", Serdes.Integer().serializer(), Serdes.String().serializer());

		visits.pipeInput(1, "iPhone");
		visits.pipeInput(2, "iPad");
		visits.pipeInput(1, "iPhone, AirPods");
		visits.pipeInput(2, "HomePad");

		KeyValueStore<Integer, String> keyValueStore = topologyTestDriver.getKeyValueStore("visit-store");

		assertThat(keyValueStore.get(1)).isEqualTo("iPhone, AirPods");
		assertThat(keyValueStore.get(2)).isEqualTo("HomePad");
	}

}
