package com.manajemenservis.model;

public class ServiceRecord {
    private String date;
    private String plateNumber;
    private String description;
    private double serviceCost;
    private double partCost;

    public ServiceRecord(String date, String plateNumber, String description, double serviceCost, double partCost) {
        this.date = date;
        this.plateNumber = plateNumber;
        this.description = description;
        this.serviceCost = serviceCost;
        this.partCost = partCost;
    }

    // Getters
    public String getDate() { return date; }
    public String getPlateNumber() { return plateNumber; }
    public String getDescription() { return description; }
    public double getServiceCost() { return serviceCost; }
    public double getPartCost() { return partCost; }
    public double getTotalCost() { return serviceCost + partCost; }
}