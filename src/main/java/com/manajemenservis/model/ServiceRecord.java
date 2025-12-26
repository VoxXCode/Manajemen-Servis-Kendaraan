package com.manajemenservis.model;

public class ServiceRecord {
    private String date;
    private String plateNumber;
    private String description;
    private double serviceCost;
    private double partCost;
    private double totalCost;

    public ServiceRecord() {
    }

    public ServiceRecord(String date, String plateNumber, String description, double serviceCost, double partCost) {
        this.date = date;
        this.plateNumber = plateNumber;
        this.description = description;
        this.serviceCost = serviceCost;
        this.partCost = partCost;
        this.totalCost = serviceCost + partCost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(double serviceCost) {
        this.serviceCost = serviceCost;
        updateTotal();
    }

    public double getPartCost() {
        return partCost;
    }

    public void setPartCost(double partCost) {
        this.partCost = partCost;
        updateTotal();
    }

    public double getTotalCost() {
        return totalCost;
    }

    private void updateTotal() {
        this.totalCost = this.serviceCost + this.partCost;
    }
}