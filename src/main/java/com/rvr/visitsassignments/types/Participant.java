package com.rvr.visitsassignments.types;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"participantId",
})
public class Participant
{
	@JsonProperty("participantId")
	private String participantId;

	@JsonProperty("participantId")
	public String getParticipantId() {
		return participantId;
	}

	@JsonProperty("participantId")
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	public Participant withParticipantId(String status) {
		this.participantId = status;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("participantId", participantId)
			.toString();
	}

}
