package com.rvr.visitsassignments.model;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("SortedTrip")
public class SortedTrip implements Serializable
{
	private String id;
	private Integer rating;

	public SortedTrip() { }

	public SortedTrip(String id, Integer rating)
	{
		this.id = id;
		this.rating = rating;
	}

	public String getId()
	{
		return id;
	}

	public Integer getRating()
	{
		return rating;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setRating(Integer rating)
	{
		this.rating = rating;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SortedTrip that = (SortedTrip) o;
		return Objects.equals(id, that.id) && Objects.equals(rating, that.rating);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, rating);
	}

	@Override
	public String toString()
	{
		return "SortedTrip{" +
			"id='" + id + '\'' +
			", rating='" + rating + '\'' +
			'}';
	}
}
