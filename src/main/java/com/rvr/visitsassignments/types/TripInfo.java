package com.rvr.visitsassignments.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"tripId",
	"status",
	"rating"
})
public class TripInfo
{
	@JsonProperty("tripId")
	private String tripId;
	@JsonProperty("status")
	private String status;
	@JsonProperty("rating")
	private Integer rating;

	@JsonProperty("tripId")
	public String getTripId() {
		return tripId;
	}

	@JsonProperty("tripId")
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public TripInfo withTripId(String tripId) {
		this.tripId = tripId;
		return this;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	public TripInfo withStatus(String status) {
		this.status = status;
		return this;
	}

	@JsonProperty("rating")
	public Integer getRating() {
		return rating;
	}

	@JsonProperty("rating")
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public TripInfo withRating(Integer rating) {
		this.rating = rating;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("tripId", tripId)
			.append("status", status)
			.append("rating", rating).toString();
	}

}
