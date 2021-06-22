package com.rvr.visitsassignments.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

public class JoinTopology
{
	private final StreamsBuilder builder;

	public JoinTopology(StreamsBuilder streamsBuilder)
	{
		this.builder = streamsBuilder;
	}

	public Topology getTopology()
	{
		GlobalKTable<String, String> unassignedVisits = builder.globalTable("unassigned-visit",  Consumed.with(Serdes.String(), Serdes.String()));
		KStream<String, String> participantForAssign = builder.stream("participant-for-assign",  Consumed.with(Serdes.String(), Serdes.String()));

		// we want to enrich that stream
		KStream<String, String> userPurchasesEnrichedJoin =
			participantForAssign.join(unassignedVisits,
				(key, value) -> key, /* map from the (key, value) of this stream to the key of the GlobalKTable */
				(userPurchase, userInfo) -> "Participant=" + userPurchase + ",Visit=[" + userInfo + "]"
			);

		userPurchasesEnrichedJoin.to("visit-store", Produced.with(Serdes.String(), Serdes.String()));

		return builder.build();
	}
}
