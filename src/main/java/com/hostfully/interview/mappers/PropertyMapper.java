package com.hostfully.interview.mappers;

import com.hostfully.interview.dtos.PropertyDTO;
import com.hostfully.interview.entities.Property;

import java.time.LocalDate;
import java.util.Set;

public class PropertyMapper {

    public static PropertyDTO mapToPropertyDTO(Property property, Set<LocalDate> propertyBookedDays) {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setId(property.getId());
        propertyDTO.setOwner(property.getOwner());
        propertyDTO.setAddress(property.getAddress());
        propertyDTO.setBookedDays(propertyBookedDays);
        propertyDTO.setBlockedDays(property.getBlockedDays().keySet());
        return propertyDTO;
    }
}
