package com.hostfully.interview.dtos;

import java.time.LocalDate;

public class PropertyActionRequest {

    private final Long propertyId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public PropertyActionRequest(Long propertyId, LocalDate startDate, LocalDate endDate) {
        this.propertyId = propertyId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
