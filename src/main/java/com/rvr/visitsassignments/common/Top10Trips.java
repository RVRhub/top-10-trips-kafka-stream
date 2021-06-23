package com.rvr.visitsassignments.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvr.visitsassignments.types.RatingByTrips;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"top10Sorted"
})
public class Top10Trips implements Iterable<RatingByTrips> {

	private ObjectMapper mapper = new ObjectMapper();
	private final Map<String, RatingByTrips> top10Searchable = new HashMap<>();

	private final TreeSet<RatingByTrips> top10Sorted = new TreeSet<>((o1, o2) -> {
		final int result = o2.getRating().compareTo(o1.getRating());
		if (result != 0) {
			return result;
		} else {
			return o1.getTripId().compareTo(o2.getTripId());
		}

	});

	public void add(final RatingByTrips newValue) {
		if (top10Searchable.containsKey(newValue.getTripId())) {
			top10Sorted.remove(top10Searchable.remove(newValue.getTripId()));
		}
		top10Searchable.put(newValue.getTripId(), newValue);
		top10Sorted.add(newValue);
		if (top10Sorted.size() > 10) {
			final RatingByTrips lastItem = top10Sorted.last();
			top10Searchable.remove(lastItem.getTripId());
			top10Sorted.remove(lastItem);
		}
	}

	public void remove(final RatingByTrips oldValue) {
		top10Searchable.remove(oldValue.getTripId());
		top10Sorted.remove(oldValue);
	}

	@Override
	public Iterator<RatingByTrips> iterator() {
		return top10Sorted.iterator();
	}

	//Add getter and setter for Json Serialization
	@JsonProperty("top10Sorted")
	public String getTop10Sorted() throws JsonProcessingException
	{
		return mapper.writeValueAsString(top10Sorted);
	}

	public TreeSet<RatingByTrips> getTop10()
	{
		return top10Sorted;
	}

	@JsonProperty("top10Sorted")
	public void setTop10Sorted(String top10Sorted) throws IOException
	{
		RatingByTrips[] top10 = mapper.readValue(top10Sorted, RatingByTrips[].class);
		for (RatingByTrips i : top10) {
			add(i);
		}
	}
}
