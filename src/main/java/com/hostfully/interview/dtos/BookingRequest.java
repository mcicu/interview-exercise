package com.hostfully.interview.dtos;

import javax.validation.constraints.NotNull;

public class BookingRequest extends IntervalRequest {

    @NotNull(message = "Field propertyId cannot be empty")
    private Long propertyId;

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}
