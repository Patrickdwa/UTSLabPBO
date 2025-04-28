module com.pos.lotre {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.pos.lotre to javafx.fxml;
    exports com.pos.lotre;
}