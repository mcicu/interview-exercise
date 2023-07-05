package com.hostfully.interview.controllers;

import com.hostfully.interview.dtos.BookingDTO;
import com.hostfully.interview.dtos.BookingRequest;
import com.hostfully.interview.dtos.BookingResponse;
import com.hostfully.interview.services.BookingService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping(path = "/{bookingId}")
    public BookingDTO getBooking(@PathVariable Long bookingId) {
        return bookingService.getBooking(bookingId);
    }

    @GetMapping(produces = "application/json")
    public List<BookingDTO> getBookings(@RequestParam(required = false) List<Long> ids) {
        //response needs pagination for larger results
        if (CollectionUtils.isEmpty(ids))
            return bookingService.getBookings();

        return bookingService.getBookings(ids);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public BookingResponse createBooking(@RequestBody @Validated BookingRequest bookingRequest) {
        Long bookingId = bookingService.createBooking(bookingRequest);
        return new BookingResponse(bookingId, "Booking successfully created");
    }

    @DeleteMapping(path = "/{bookingId}")
    public BookingResponse deleteBooking(@PathVariable Long bookingId) {
        bookingService.deleteBooking(bookingId);
        return new BookingResponse(bookingId, "Booking successfully deleted");
    }

    @PostMapping(path = "/{bookingId}")
    public BookingResponse updateBooking(@PathVariable Long bookingId, @RequestBody @Validated BookingRequest bookingRequest) {
        bookingService.updateBooking(bookingId, bookingRequest);
        return new BookingResponse(bookingId, "Booking successfully updated");
    }
}
