package com.hostfully.interview.repositories;

import com.hostfully.interview.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
