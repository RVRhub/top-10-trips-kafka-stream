package com.rvr.visitsassignments.types;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"tripId",
	"rating"
})
public class RatingByTrips {

	@JsonProperty("tripId")
	private String tripId;
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

	public RatingByTrips withTripId(String tripId) {
		this.tripId = tripId;
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

	public RatingByTrips withRating(Integer rating) {
		this.rating = rating;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("tripId", tripId).append("rating", rating).toString();
	}

}
