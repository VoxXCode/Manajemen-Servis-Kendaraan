package com.manajemenservis.model;

public class Vehicle {
    private String plateNumber;
    private String brand;
    private String type;
    private int year;
    private String ownerName;

    public Vehicle() {
    }

    public Vehicle(String plateNumber, String brand, String type, int year, String ownerName) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.type = type;
        this.year = year;
        this.ownerName = ownerName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}