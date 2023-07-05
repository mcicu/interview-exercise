package com.hostfully.interview.dtos;

import java.time.LocalDate;
import java.util.Set;

public class PropertyDTO {
    private Long id;
    private String owner;
    private String address;
    private Set<LocalDate> bookedDays;
    private Set<LocalDate> blockedDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<LocalDate> getBookedDays() {
        return bookedDays;
    }

    public void setBookedDays(Set<LocalDate> bookedDays) {
        this.bookedDays = bookedDays;
    }

    public Set<LocalDate> getBlockedDays() {
        return blockedDays;
    }

    public void setBlockedDays(Set<LocalDate> blockedDays) {
        this.blockedDays = blockedDays;
    }
}
