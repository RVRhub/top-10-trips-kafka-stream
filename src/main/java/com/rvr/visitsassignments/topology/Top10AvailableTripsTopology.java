package com.rvr.visitsassignments.topology;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rvr.visitsassignments.KafkaConsumer;
import com.rvr.visitsassignments.common.AppConfigs;
import com.rvr.visitsassignments.common.AppSerdes;
import com.rvr.visitsassignments.common.Top10Trips;
import com.rvr.visitsassignments.types.RatingByTrips;
import com.rvr.visitsassignments.types.TripInfo;

public class Top10AvailableTripsTopology
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Top10AvailableTripsTopology.class);

	private final StreamsBuilder streamsBuilder;

	public Top10AvailableTripsTopology(StreamsBuilder streamsBuilder)
	{
		this.streamsBuilder = streamsBuilder;
	}

	public Topology getTopology()
	{
		KTable<String, TripInfo> tripInfoGlobalKTable =
			streamsBuilder.table(
				AppConfigs.tripInfoTopic,
				Consumed.with(AppSerdes.String(),
					AppSerdes.TripInfo())
			);

		KTable<String, Top10Trips> top10TripsKTable =
			tripInfoGlobalKTable.groupBy(
							(tripId, ratingValue) -> {
								RatingByTrips value = new RatingByTrips();
								value.setTripId(tripId);
								value.setRating(ratingValue.getRating());
								return KeyValue.pair(AppConfigs.top10AggregateKey, value);
							},
							Grouped.with(AppSerdes.String(), AppSerdes.RatingByTrips())
						).aggregate(Top10Trips::new,
							(k, newRatingByTrips, aggTop10Trips) -> {
								aggTop10Trips.add(newRatingByTrips);
								return aggTop10Trips;
							},
							(k, oldRatingByTrips, aggTop10Trips) -> {
								aggTop10Trips.remove(oldRatingByTrips);
								return aggTop10Trips;
							},
							Materialized.<String, Top10Trips, KeyValueStore<Bytes, byte[]>>
								as("top10-ratings")
								.withKeySerde(AppSerdes.String())
								.withValueSerde(AppSerdes.Top10Trips()));

		top10TripsKTable.toStream().foreach((k, v) -> {
			try
			{
				LOGGER.info("k=" + k + " v= " + v.getTop10Sorted());
			}
			catch (JsonProcessingException e)
			{
				e.printStackTrace();
			}
		});

		return streamsBuilder.build();
	}
}
