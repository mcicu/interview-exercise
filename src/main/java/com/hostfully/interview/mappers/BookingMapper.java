package com.hostfully.interview.mappers;

import com.hostfully.interview.dtos.BookingDTO;
import com.hostfully.interview.entities.Booking;

public class BookingMapper {

    public static BookingDTO mapToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setPropertyId(booking.getPropertyId());
        bookingDTO.setStartDate(booking.getStartDate());
        bookingDTO.setEndDate(booking.getEndDate());

        return bookingDTO;
    }
}
