# 🛠️ Sistem Manajemen Servis Kendaraan (Bengkel-KU)

**Bengkel-KU** adalah aplikasi desktop berbasis Java (Swing) yang dirancang untuk membantu operasional bengkel kendaraan. Aplikasi ini menggunakan **Microsoft Excel (.xlsx)** sebagai basis data penyimpanan, menjadikannya solusi yang ringan, portabel, dan mudah dibackup tanpa memerlukan instalasi server database (seperti MySQL/PostgreSQL).

Aplikasi ini mengusung antarmuka **Dark Mode** yang modern dan *user-friendly*.

---

## 📋 Fitur Utama

### 1. 🔐 Keamanan & Autentikasi
* **Login Admin:** Sistem login sederhana dengan validasi username dan password.
* **Auto-Generated Admin:** Akun default akan dibuat otomatis jika file database belum ada.

### 2. 👥 Manajemen Pelanggan (Customers)
* **CRUD Data:** Tambah, Edit, dan Hapus data pelanggan beserta kendaraannya.
* **Pencarian & Pagination:** Fitur pencarian real-time dan navigasi halaman tabel.
* **Validasi:** Pengecekan input nama dan plat nomor.

### 3. 🛍️ Manajemen Stok (Inventory)
* **Stok Sparepart:** Input data barang, kategori, stok, dan harga jual.
* **Format Mata Uang:** Penanganan format Rupiah (Rp) secara otomatis.
* **Dropdown Kategori:** Pilihan kategori dan satuan barang yang terstruktur.

### 4. 🔧 Transaksi Servis (Service Entry)
* **Input Servis Mudah:** Form terintegrasi yang menghubungkan Pelanggan dan Sparepart.
* **Kalkulasi Otomatis:** Menghitung total biaya (Jasa Mekanik + Harga Sparepart x Qty).
* **Update Stok:** (Logika integrasi stok siap dikembangkan lebih lanjut).

### 5. 📜 Riwayat & Laporan (History)
* **Log Aktivitas:** Mencatat semua transaksi servis ke dalam file Excel terpisah.
* **Tabel Riwayat:** Melihat daftar servis terdahulu lengkap dengan tanggal dan total biaya.

---

## 📸 Struktur Folder Proyek

```text
Manajemen-Servis-Kendaraan/
├── data/                       # [PENTING] Database Excel tersimpan di sini
│   ├── customers.xlsx          # Data Pelanggan & Kendaraan
│   ├── inventory.xlsx          # Data Stok Barang
│   ├── services.xlsx           # Riwayat Transaksi Servis
│   └── users.xlsx              # Data Akun Login
├── src/main/java/com/manajemenservis/
│   ├── controller/             # Logika Bisnis & Apache POI Handler
│   ├── model/                  # Objek Data (POJO)
│   ├── util/                   # ExcelHandler & Utilities
│   ├── view/                   # Tampilan GUI (Swing & Custom Components)
│   └── main/                   # Main Class (Entry Point)
├── target/                     # Hasil Build Maven
├── pom.xml                     # Konfigurasi Dependensi Maven
└── README.md                   # Dokumentasi ini