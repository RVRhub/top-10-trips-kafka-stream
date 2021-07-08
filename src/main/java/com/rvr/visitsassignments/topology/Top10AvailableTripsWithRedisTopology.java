package com.rvr.visitsassignments.topology;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KTable;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rvr.visitsassignments.common.AppConfigs;
import com.rvr.visitsassignments.common.AppSerdes;
import com.rvr.visitsassignments.comparators.RatingComparator;
import com.rvr.visitsassignments.model.SortedTrip;
import com.rvr.visitsassignments.types.RatingByTrips;
import com.rvr.visitsassignments.types.TripInfo;

public class Top10AvailableTripsWithRedisTopology
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Top10AvailableTripsWithRedisTopology.class);

	static final String PROCESSOR_NAME = "rating-sort-processor";
	final static String sourceName = "rating-sort-source";


	private final RedissonClient redissonClient;
	private final StreamsBuilder streamsBuilder;

	public Top10AvailableTripsWithRedisTopology(StreamsBuilder streamsBuilder, RedissonClient redissonClient)
	{
		this.streamsBuilder = streamsBuilder;
		this.redissonClient = redissonClient;
	}

	public Topology getTopology()
	{
		KTable<String, TripInfo> tripInfoGlobalKTable =
			streamsBuilder.table(
				AppConfigs.tripInfoTopic, // unassigned visit
				Consumed.with(AppSerdes.String(),
					AppSerdes.TripInfo())
			);

		RSortedSet<SortedTrip> sortedTrip = redissonClient.getSortedSet("sortedTrip");
		sortedTrip.trySetComparator(new RatingComparator());

		KTable<String, RatingByTrips> top10TripsKTable = tripInfoGlobalKTable
			.groupBy((tripId, ratingValue) -> {
					RatingByTrips value = new RatingByTrips();
					value.setTripId(tripId); // visit id
					value.setRating(ratingValue.getRating()); // sort order
					return KeyValue.pair(AppConfigs.top10AggregateKey, value);
				},
				Grouped.with(AppSerdes.String(), AppSerdes.RatingByTrips()))
			.reduce((k, newRatingByTrips) -> {
					sortedTrip.add(new SortedTrip(newRatingByTrips.getTripId(), newRatingByTrips.getRating()));
					return newRatingByTrips;
				},
				(k, oldRatingByTrips) -> {
					sortedTrip.remove(new SortedTrip(oldRatingByTrips.getTripId(), oldRatingByTrips.getRating()));
					return oldRatingByTrips;
				});

		top10TripsKTable.toStream().foreach((k, v) -> {
			LOGGER.info("k=" + k + " v= " + v);
		});

		return streamsBuilder.build();
	}
}
