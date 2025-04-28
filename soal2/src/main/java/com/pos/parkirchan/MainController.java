package com.pos.parkirchan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ComboBox<String> kendaraanComboBox;
    @FXML
    private ComboBox<String> durasiModeComboBox;
    @FXML
    private TextField manualDurasiField;
    @FXML
    private TextField jamMasukField;
    @FXML
    private TextField jamKeluarField;
    @FXML
    private Label jumlahKendaraanLabel;
    @FXML
    private Label pendapatanLabel;
    @FXML
    private TableView<ParkirData> tabelParkir;
    @FXML
    private TableColumn<ParkirData, String> jenisCol;
    @FXML
    private TableColumn<ParkirData, String> durasiCol;
    @FXML
    private TableColumn<ParkirData, Integer> hargaCol;
    @FXML
    private Button insertButton;
    @FXML
    private Button exitButton;
    private ObservableList<ParkirData> dataList = FXCollections.observableArrayList();

    private int totalPendapatan = 0;
    private int totalKendaraan = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Isi ComboBox
        kendaraanComboBox.setItems(FXCollections.observableArrayList("Motor", "Mobil", "Truk"));
        durasiModeComboBox.setItems(FXCollections.observableArrayList("Manual", "Time"));

        // Bind TableColumn ke atribut ParkirData
        jenisCol.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        durasiCol.setCellValueFactory(new PropertyValueFactory<>("durasi"));
        hargaCol.setCellValueFactory(new PropertyValueFactory<>("harga"));

        // Set data ke TableView
        tabelParkir.setItems(dataList);

        // Insert Button Action
        insertButton.setOnAction(e -> handleInsert());

        // Exit Button Action
        exitButton.setOnAction(e -> handleExit());
    }

    private void handleInsert() {
        String jenis = kendaraanComboBox.getValue();
        String mode = durasiModeComboBox.getValue();
        int durasi = 0;

        try {
            if ("Manual".equalsIgnoreCase(mode)) {
                durasi = Integer.parseInt(manualDurasiField.getText());
                if (durasi <= 0 || durasi > 24) {
                    showAlert("Input tidak valid", "Durasi manual harus antara 1 sampai 24 jam.");
                    return;
                }
            } else if ("Time".equalsIgnoreCase(mode)) {
                int masuk = Integer.parseInt(jamMasukField.getText());
                int keluar = Integer.parseInt(jamKeluarField.getText());

                if (masuk < 0 || masuk > 23 || keluar < 0 || keluar > 23) {
                    showAlert("Input tidak valid", "Jam masuk dan keluar harus antara 0 sampai 23.");
                    return;
                }

                durasi = keluar - masuk;
                if (durasi < 0) durasi += 24; // lewat tengah malam
                if (durasi == 0) durasi = 1; // minimal 1 jam kalau masuk dan keluar sama
            }

            int harga = hitungHarga(jenis, durasi);

            // Tambah ke TableView
            dataList.add(new ParkirData(jenis, durasi + " Jam", harga));

            // Update rekap
            totalPendapatan += harga;
            totalKendaraan++;
            updateRekap();

            // Clear field
            manualDurasiField.clear();
            jamMasukField.clear();
            jamKeluarField.clear();
            kendaraanComboBox.getSelectionModel().clearSelection();
            durasiModeComboBox.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            showAlert("Input tidak valid", "Pastikan semua field telah diisi angka yang benar.");
        } catch (Exception e) {
            showAlert("Error", "Terjadi kesalahan: " + e.getMessage());
        }
    }


    private int hitungHarga(String jenis, int durasi) {
        int hargaPerJam = switch (jenis) {
            case "Motor" -> 2000;
            case "Mobil" -> 5000;
            case "Truk" -> 8000;
            default -> 0;
        };
        return hargaPerJam * durasi;
    }

    private void updateRekap() {
        jumlahKendaraanLabel.setText(String.valueOf(totalKendaraan));
        pendapatanLabel.setText("Rp " + totalPendapatan);
    }

    private void handleExit() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rekapitulasi");
        alert.setHeaderText("Ringkasan Hari Ini");
        alert.setContentText("Total kendaraan: " + totalKendaraan + "\nPendapatan: Rp " + totalPendapatan);
        alert.showAndWait();
        System.exit(0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Inner class untuk isi tabel
    public static class ParkirData {
        private final String jenis;
        private final String durasi;
        private final int harga;

        public ParkirData(String jenis, String durasi, int harga) {
            this.jenis = jenis;
            this.durasi = durasi;
            this.harga = harga;
        }

        public String getJenis() {
            return jenis;
        }

        public String getDurasi() {
            return durasi;
        }

        public int getHarga() {
            return harga;
        }
    }
}
