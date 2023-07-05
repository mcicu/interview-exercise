package com.hostfully.interview.services;

import com.hostfully.interview.dtos.PropertyActionRequest;
import com.hostfully.interview.dtos.PropertyDTO;
import com.hostfully.interview.entities.Property;
import com.hostfully.interview.exceptions.ValidationException;
import com.hostfully.interview.mappers.PropertyMapper;
import com.hostfully.interview.repositories.PropertyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final BookingService bookingService;

    public PropertyService(PropertyRepository propertyRepository, BookingService bookingService) {
        this.propertyRepository = propertyRepository;
        this.bookingService = bookingService;
    }

    public List<PropertyDTO> getProperties() {
        return propertyRepository.findAll().stream()
                .map(property -> {
                    Set<LocalDate> propertyBookedDays = bookingService.getAlreadyBookedDaysByPropertyId(property.getId()).keySet();
                    return PropertyMapper.mapToPropertyDTO(property, propertyBookedDays);
                })
                .collect(Collectors.toList());
    }

    public Boolean blockDateInterval(PropertyActionRequest request) {
        Long propertyId = request.getPropertyId();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ValidationException(String.format("Property %s not found", propertyId)));

        Set<LocalDate> daysToBlock = request.getStartDate()
                .datesUntil(request.getEndDate().plusDays(1)) //+1 to include final day
                .collect(Collectors.toSet());

        Map<LocalDate, Boolean> propertyBookedDays = bookingService.getAlreadyBookedDaysByPropertyId(propertyId);
        Set<LocalDate> alreadyBookedDates = daysToBlock.stream()
                .filter(propertyBookedDays::containsKey)
                .collect(Collectors.toSet());

        if (!alreadyBookedDates.isEmpty()) {
            throw new ValidationException(String.format("Block not allowed, following dates are already booked %s", alreadyBookedDates));
        }

        property.addBlockedDays(daysToBlock);
        propertyRepository.saveAndFlush(property);
        return true;
    }

    public Boolean unblockDateInterval(PropertyActionRequest request) {
        Long propertyId = request.getPropertyId();
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ValidationException(String.format("Property %s not found", propertyId)));

        Set<LocalDate> daysToUnblock = request.getStartDate()
                .datesUntil(request.getEndDate().plusDays(1)) //+1 to include final day
                .collect(Collectors.toSet());

        property.removeBlockedDays(daysToUnblock);
        propertyRepository.saveAndFlush(property);
        return true;
    }
}
