package com.rvr.visitsassignments.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rvr.visitsassignments.model.SortedTrip;

@Repository
public interface SortedTripRepository extends CrudRepository<SortedTrip, String> {}
