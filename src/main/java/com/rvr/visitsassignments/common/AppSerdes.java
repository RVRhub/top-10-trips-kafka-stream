package com.rvr.visitsassignments.common;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import java.util.HashMap;
import java.util.Map;

import com.rvr.visitsassignments.serde.JsonDeserializer;
import com.rvr.visitsassignments.serde.JsonSerializer;
import com.rvr.visitsassignments.types.Participant;
import com.rvr.visitsassignments.types.PlannedAssigment;
import com.rvr.visitsassignments.types.RatingByTrips;
import com.rvr.visitsassignments.types.TripInfo;

public class AppSerdes extends Serdes {

	static final class PlannedAssigmentSerde extends WrapperSerde<PlannedAssigment> {
		PlannedAssigmentSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>());
		}
	}

	public static Serde<PlannedAssigment> PlannedAssigment() {
		PlannedAssigmentSerde serde = new PlannedAssigmentSerde();

		Map<String, Object> serdeConfigs = new HashMap<>();
		serdeConfigs.put("specific.class.name", PlannedAssigment.class);
		serde.configure(serdeConfigs, false);

		return serde;
	}

	static final class ParticipantSerde extends WrapperSerde<Participant> {
		ParticipantSerde() {
			super(new JsonSerializer<>(), new JsonDeserializer<>());
		}
	}

	public static Serde<Participant> Participant() {
		ParticipantSerde serde = new ParticipantSerde();

		Map<String, Object> serdeConfigs = new HashMap<>();
		serdeConfigs.put("specific.class.name", Participant.class);
		serde.configure(serdeConfigs, false);

		return serde;
	}

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
