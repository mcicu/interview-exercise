package com.hostfully.interview.controllers;

import com.hostfully.interview.dtos.IntervalRequest;
import com.hostfully.interview.dtos.PropertyActionRequest;
import com.hostfully.interview.dtos.PropertyDTO;
import com.hostfully.interview.services.PropertyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping(produces = "application/json")
    public List<PropertyDTO> getProperties() {
        return propertyService.getProperties();
    }

    @PostMapping(path = "/{propertyId}/block")
    public Boolean addBlock(@PathVariable Long propertyId, @RequestBody IntervalRequest intervalRequest) {
        PropertyActionRequest propertyActionRequest = new PropertyActionRequest(
                propertyId,
                intervalRequest.getStartDate(),
                intervalRequest.getEndDate()
        );
        return propertyService.blockDateInterval(propertyActionRequest);
    }

    @PostMapping(path = "/{propertyId}/remove-block")
    public Boolean removeBlock(@PathVariable Long propertyId, @RequestBody IntervalRequest intervalRequest) {
        PropertyActionRequest propertyActionRequest = new PropertyActionRequest(
                propertyId,
                intervalRequest.getStartDate(),
                intervalRequest.getEndDate()
        );
        return propertyService.unblockDateInterval(propertyActionRequest);
    }
}
