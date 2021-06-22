package com.rvr.visitsassignments.common;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;
import java.util.Map;

import com.rvr.visitsassignments.serde.JsonDeserializer;
import com.rvr.visitsassignments.serde.JsonSerializer;
import com.rvr.visitsassignments.types.RatingByTrips;
import com.rvr.visitsassignments.types.TripInfo;

public class AppSerdes extends Serdes {

	static final class TripInfoSerde extends WrapperSerde<TripInfo> {
		TripInfoSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>());
		}
	}

	public static Serde<TripInfo> TripInfo() {
		TripInfoSerde serde = new TripInfoSerde();

		Map<String, Object> serdeConfigs = new HashMap<>();
		serdeConfigs.put("specific.class.name", TripInfo.class);
		serde.configure(serdeConfigs, false);

		return serde;
	}

	static final class RatingByTripsSerde extends WrapperSerde<RatingByTrips> {
		RatingByTripsSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>());
		}
	}

	public static Serde<RatingByTrips> RatingByTrips() {
		RatingByTripsSerde serde = new RatingByTripsSerde();

		Map<String, Object> serdeConfigs = new HashMap<>();
		serdeConfigs.put("specific.class.name", RatingByTrips.class);
		serde.configure(serdeConfigs, false);

		return serde;
	}

	static final class Top10TripsSerde extends WrapperSerde<Top10Trips> {
		Top10TripsSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>());
		}
	}

	public static Serde<Top10Trips> Top10Trips() {
		Top10TripsSerde serde = new Top10TripsSerde();

		Map<String, Object> serdeConfigs = new HashMap<>();
		serdeConfigs.put("specific.class.name", Top10Trips.class);
		serde.configure(serdeConfigs, false);

		return serde;
	}

}
