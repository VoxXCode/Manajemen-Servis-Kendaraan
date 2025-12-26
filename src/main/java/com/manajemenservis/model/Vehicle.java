package com.manajemenservis.model;

public class Vehicle {
    private String plateNumber;
    private String brand;
    private String type;
    private String year; // String, bukan int

    public Vehicle(String plateNumber, String brand, String type, String year) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.type = type;
        this.year = year;
    }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
}