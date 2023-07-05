package com.hostfully.interview.dtos;

public class BookingResponse {

    private final Long bookingId;
    private final String message;

    public BookingResponse(Long bookingId, String message) {
        this.bookingId = bookingId;
        this.message = message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public String getMessage() {
        return message;
    }
}

