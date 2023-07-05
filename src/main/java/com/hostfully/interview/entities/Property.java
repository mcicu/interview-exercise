package com.hostfully.interview.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner")
    private String owner; //For simplicity, we reference just the name of the owner

    @Column(name = "address")
    private String address;

    @ElementCollection
    @CollectionTable(name = "property_blocked_days", joinColumns = @JoinColumn(name = "property_id"))
    private Map<LocalDate, Boolean> blockedDays = new HashMap<>();

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

    public Map<LocalDate, Boolean> getBlockedDays() {
        return blockedDays;
    }

    public void addBlockedDays(Set<LocalDate> daysToBlock) {
        daysToBlock.forEach(dayToBlock -> blockedDays.put(dayToBlock, true));
    }

    public void removeBlockedDays(Set<LocalDate> daysToUnblock) {
        daysToUnblock.forEach(dayToUnblock -> blockedDays.remove(dayToUnblock));
    }
}
