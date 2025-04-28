module com.pos.karyawan {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.pos.karyawan to javafx.fxml;
    exports com.pos.karyawan;
}

