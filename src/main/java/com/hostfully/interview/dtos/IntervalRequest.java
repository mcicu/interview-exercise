package com.hostfully.interview.dtos;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class IntervalRequest {
    @NotNull(message = "Field startDate cannot be empty")
    private LocalDate startDate;

    @NotNull(message = "Field endDate cannot be empty")
    private LocalDate endDate;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
