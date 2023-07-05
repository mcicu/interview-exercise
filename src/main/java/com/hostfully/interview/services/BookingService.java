package com.hostfully.interview.services;

import com.hostfully.interview.dtos.BookingDTO;
import com.hostfully.interview.dtos.BookingRequest;
import com.hostfully.interview.entities.Booking;
import com.hostfully.interview.entities.Property;
import com.hostfully.interview.exceptions.ValidationException;
import com.hostfully.interview.mappers.BookingMapper;
import com.hostfully.interview.repositories.BookingRepository;
import com.hostfully.interview.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }

    public BookingDTO getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException(String.format("Booking %s not found", bookingId)));

        return BookingMapper.mapToBookingDTO(booking);
    }

    public List<BookingDTO> getBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::mapToBookingDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookings(List<Long> ids) {
        return bookingRepository.findAllById(ids)
                .stream()
                .map(BookingMapper::mapToBookingDTO)
                .collect(Collectors.toList());
    }

    public Long createBooking(BookingRequest bookingRequest) {
        Long propertyId = bookingRequest.getPropertyId();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ValidationException(String.format("Property %s not found", propertyId)));

        Map<LocalDate, Boolean> alreadyBookedDays = getAlreadyBookedDaysByPropertyId(propertyId);
        Map<LocalDate, Boolean> propertyBlockedDays = property.getBlockedDays();
        validateBookingRequest(bookingRequest, alreadyBookedDays, propertyBlockedDays);

        //validations passed, create new booking
        Booking booking = new Booking();
        booking.setPropertyId(propertyId);
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        bookingRepository.saveAndFlush(booking);

        return booking.getId();
    }

    public void updateBooking(Long bookingId, BookingRequest bookingRequest) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException(String.format("Booking %s not found", bookingId)));

        Property property = propertyRepository.findById(bookingRequest.getPropertyId())
                .orElseThrow(() -> new ValidationException(String.format("Property %s not found", bookingRequest.getPropertyId())));

        Map<LocalDate, Boolean> alreadyBookedDays = getAlreadyBookedDaysByPropertyId(property.getId(), bookingId);
        Map<LocalDate, Boolean> propertyBlockedDays = property.getBlockedDays();
        validateBookingRequest(bookingRequest, alreadyBookedDays, propertyBlockedDays);

        booking.setPropertyId(bookingRequest.getPropertyId()); //Assuming that we can change the property for a booking (maybe not a real scenario)
        booking.setStartDate(bookingRequest.getStartDate());
        booking.setEndDate(bookingRequest.getEndDate());
        bookingRepository.saveAndFlush(booking);
    }

    private void validateBookingRequest(BookingRequest bookingRequest, Map<LocalDate, Boolean> alreadyBookedDays, Map<LocalDate, Boolean> propertyBlockedDays) {
        LocalDate startDate = bookingRequest.getStartDate();
        LocalDate endDate = bookingRequest.getEndDate();

        if (startDate.compareTo(endDate) >= 0) {
            throw new ValidationException("Requested start date - end date interval is invalid");
        }

        if (startDate.compareTo(LocalDate.now()) <= 0) {
            throw new ValidationException("Booking start date cannot be in the past");
        }

        Set<LocalDate> daysToBook = startDate.datesUntil(endDate.plusDays(1)) //+1 to include final day
                .collect(Collectors.toSet());

        //prevent booking if property is already booked for the given interval
        Set<LocalDate> conflictingBookedDays = daysToBook.stream()
                .filter(alreadyBookedDays::containsKey)
                .collect(Collectors.toSet());

        if (!conflictingBookedDays.isEmpty()) {
            throw new ValidationException(String.format("Booking is not allowed, following dates are already booked %s", conflictingBookedDays));
        }

        //prevent booking if property is blocked for the given interval
        Set<LocalDate> conflictingBlockedDays = daysToBook.stream()
                .filter(propertyBlockedDays::containsKey)
                .collect(Collectors.toSet());

        if (!conflictingBlockedDays.isEmpty()) {
            throw new ValidationException(String.format("Booking is not allowed, following dates are blocked by owner %s", conflictingBlockedDays));
        }
    }

    public Map<LocalDate, Boolean> getAlreadyBookedDaysByPropertyId(Long propertyId) {
        return getAlreadyBookedDaysByPropertyId(propertyId, null);
    }

    public Map<LocalDate, Boolean> getAlreadyBookedDaysByPropertyId(Long propertyId, Long excludedBookingId) {
        Stream<Booking> bookingStream = bookingRepository.findByPropertyId(propertyId).stream();

        if (excludedBookingId != null) {
            bookingStream = bookingStream.filter(booking -> !booking.getId().equals(excludedBookingId));
        }
        return bookingStream
                .flatMap(booking -> booking.getStartDate()
                        .datesUntil(booking.getEndDate().plusDays(1)))
                .collect(Collectors.toMap(Function.identity(), (x) -> true, (x1, x2) -> x1, TreeMap::new));
    }

    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException(String.format("Booking %s not found", bookingId)));

        bookingRepository.delete(booking);
        bookingRepository.flush();
    }
}
