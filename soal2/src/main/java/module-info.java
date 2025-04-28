module com.pos.parkirchan {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pos.parkirchan to javafx.fxml;
    exports com.pos.parkirchan;
}