package com.rvr.visitsassignments.comparators;

import java.util.Comparator;

import com.rvr.visitsassignments.model.SortedTrip;

public class RatingComparator implements Comparator<SortedTrip>
{
	@Override
	public int compare(SortedTrip o1, SortedTrip o2)
	{
		final int result = o2.getRating().compareTo(o1.getRating());
		if (result != 0) {
			return result;
		} else {
			return o1.getId().compareTo(o2.getId());
		}

	}
}
