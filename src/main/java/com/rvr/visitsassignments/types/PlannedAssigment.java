package com.rvr.visitsassignments.types;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"participantId",
	"tripId"
})
public class PlannedAssigment
{
	@JsonProperty("participantId")
	private String participantId;
	@JsonProperty("tripId")
	private String tripId;

	@JsonProperty("participantId")
	public String getParticipantId()
	{
		return participantId;
	}

	@JsonProperty("participantId")
	public void setParticipantId(String participantId)
	{
		this.participantId = participantId;
	}

	public PlannedAssigment withParticipantId(String participantId)
	{
		this.participantId = participantId;
		return this;
	}

	@JsonProperty("tripId")
	public String geTripId()
	{
		return tripId;
	}

	@JsonProperty("tripId")
	public void setTripId(String tripId)
	{
		this.tripId = tripId;
	}

	public PlannedAssigment withTripId(String tripId)
	{
		this.tripId = tripId;
		return this;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("participantId", participantId)
			.append("tripId", tripId)
			.toString();
	}
}
