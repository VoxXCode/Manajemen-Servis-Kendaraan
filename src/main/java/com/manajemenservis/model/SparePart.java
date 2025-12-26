package com.manajemenservis.model;

public class SparePart {
    private String kode;
    private String nama;
    private String kategori;
    private int stok;
    private double hargaJual;
    private String satuan;

    public SparePart(String kode, String nama, String kategori, int stok, double hargaJual, String satuan) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.stok = stok;
        this.hargaJual = hargaJual;
        this.satuan = satuan;
    }

    // Getters
    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public int getStok() { return stok; }
    public double getHargaJual() { return hargaJual; }
    public String getSatuan() { return satuan; }
}