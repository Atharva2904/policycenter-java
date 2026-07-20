package com.wipfli.training.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public abstract class Entity {
    private final String uniqueID;
    private String taxID;
    private final LocalDate registrationDate;
    private EntityStatus activeEntityStatus;
    private final Instant createdAt;
    private Instant updatedAt;

    public Entity(){
        this.uniqueID = UUID.randomUUID().toString();
        this.registrationDate = LocalDate.now();
        this.createdAt = Instant.now();
    }

    public Entity(String taxID){
        this();
        this.taxID = taxID;
        this.updatedAt = null;
        this.activeEntityStatus = EntityStatus.PENDING;
    }
    public EntityStatus getActiveStatus() {
        return activeEntityStatus;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public String getTaxID() {
        return taxID;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    protected void updateActiveStatus(EntityStatus newEntityStatus){
        this.activeEntityStatus = newEntityStatus;
    }

    protected void setUpdatedAt(Instant newUpdatedAt){
        this.updatedAt = newUpdatedAt;
    }

    protected void setTaxID(String newTaxID){
        this.taxID = newTaxID;
    }

    public abstract String getDisplayName();


}
