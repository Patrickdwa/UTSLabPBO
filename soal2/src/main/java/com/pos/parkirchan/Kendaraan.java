package com.pos.parkirchan;

import javafx.beans.property.*;

public class Kendaraan {
    private final StringProperty jenisKendaraan = new SimpleStringProperty();
    private final StringProperty durasi = new SimpleStringProperty();
    private final DoubleProperty harga = new SimpleDoubleProperty();

    public Kendaraan(String jenis, String durasi, double harga) {
        this.jenisKendaraan.set(jenis);
        this.durasi.set(durasi);
        this.harga.set(harga);
    }

    // Getter
    public String getJenisKendaraan() {
        return jenisKendaraan.get();
    }

    public StringProperty jenisKendaraanProperty() {
        return jenisKendaraan;
    }

    public String getDurasi() {
        return durasi.get();
    }

    public StringProperty durasiProperty() {
        return durasi;
    }

    public double getHarga() {
        return harga.get();
    }

    public DoubleProperty hargaProperty() {
        return harga;
    }

    // Method overloading untuk menghitung biaya
    public static double hitungBiaya(int durasi, double tarifPerJam) {
        return durasi * tarifPerJam;
    }

    public static double hitungBiaya(int jamMasuk, int jamKeluar, double tarifPerJam) {
        int durasi = Math.max(0, jamKeluar - jamMasuk);
        return hitungBiaya(durasi, tarifPerJam);
    }
}
