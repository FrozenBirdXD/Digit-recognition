module com.openjfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.openjfx to javafx.fxml;
    exports com.openjfx;
}
